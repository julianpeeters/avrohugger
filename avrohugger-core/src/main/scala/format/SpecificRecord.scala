package avrohugger
package format

import abstractions.SourceFormat
import format.specific.{ SpecificJavaTreehugger, SpecificScalaTreehugger}
import models.CompilationUnit
import stores.{ ClassStore, SchemaStore }
import matchers.TypeMatcher

import treehugger.forest._
import definitions.RootClass

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Type.{ ENUM, RECORD }

import java.nio.file.Path
import scala.collection.JavaConversions._

object SpecificRecord extends SourceFormat{
  
  // SpecificRecord API can only handle Java enums
  def isEnum(schema: Schema) = schema.getType == Schema.Type.ENUM

  override val toolName = "generate-specific"

  override val toolShortDescription = "Generates Scala code extending SpecificRecordBase."

  override def fileExt(schemaOrProtocol: Either[Schema, Protocol]) = {
    schemaOrProtocol match {
      case Left(schema) => schema.getType match {
        case RECORD => ".scala"
        case ENUM => ".java" // Avro's SpecificData requires enums be Java Enum
        case _ => sys.error("Only RECORD and ENUM can be top-level definitions")
      }
      case Right(protocol) => ".scala"
    }
  }

  val typeMatcher = new TypeMatcher
  val scalaTreehugger = SpecificScalaTreehugger

  override def asCompilationUnits(
    classStore: ClassStore, 
    namespace: Option[String], 
    schemaOrProtocol: Either[Schema, Protocol],
    schemaStore: SchemaStore,
    maybeOutDir: Option[String]): List[CompilationUnit] = {
      
    registerTypes(schemaOrProtocol, classStore)

    // generate as single string ADT or solo class
    def protocolToADT(protocol: Protocol): List[CompilationUnit] = {
      val localSubtypes = getLocalSubtypes(protocol)
      val localEnums = localSubtypes.filter(isEnum)
      val scalaCompilationUnit = getScalaCompilationUnit(
        classStore,
        namespace,
        Right(protocol),
        typeMatcher,
        schemaStore,
        maybeOutDir)
      val javaCompilationUnits = localEnums.map(schema => {
        protocolEnumSchemaToEnum(namespace, schema, maybeOutDir)
      })
      scalaCompilationUnit +: javaCompilationUnits
    }
    
    // generate as RPC trait and separate class/enum strings
    def protocolToRPC(protocol: Protocol): List[CompilationUnit] = {
      val localSubtypes = getLocalSubtypes(protocol)
      val localEnums = localSubtypes.filter(isEnum)
      val localNonEnums = localSubtypes.filterNot(isEnum)
      val maybePath = getFilePath(namespace, Right(protocol), maybeOutDir)
      val rpcTraitString = scalaTreehugger.asScalaCodeString(
        classStore,
        namespace,
        Right(protocol),
        typeMatcher,
        schemaStore)
      val rpcTraitCompUnit = CompilationUnit(maybePath, rpcTraitString)
      val scalaCompUnits = localNonEnums.map(schema => {
        val scalaCompilationUnit = getScalaCompilationUnit(
          classStore,
          namespace,
          Left(schema),
          typeMatcher,
          schemaStore,
          maybeOutDir)
        scalaCompilationUnit
      })
      val javaCompUnits = localEnums.map(schema => {
        protocolEnumSchemaToEnum(namespace, schema, maybeOutDir)
      })
      val rpcTypeCompUnits = scalaCompUnits ::: javaCompUnits
      rpcTraitCompUnit +: rpcTypeCompUnits
    }
    
    def protocolEnumSchemaToEnum(
      namespace: Option[String],
      schema: Schema,
      maybeOutDir: Option[String]): CompilationUnit = {
      val maybePath = getFilePath(namespace, Left(schema), maybeOutDir)
      val javaCompilationUnit = getJavaCompilationUnit(
        classStore,
        namespace,
        schema,
        maybeOutDir)
      javaCompilationUnit
    }
     
    schemaOrProtocol match {
      case Left(schema) => {
        schema.getType match {
          case RECORD => {
            val scalaCompilationUnit = getScalaCompilationUnit(
              classStore,
              namespace,
              schemaOrProtocol,
              typeMatcher,
              schemaStore,
              maybeOutDir)
            List(scalaCompilationUnit)
          }
          case ENUM => {
            val javaCompilationUnit = getJavaCompilationUnit(
              classStore,
              namespace,
              schema,
              maybeOutDir)
            List(javaCompilationUnit)
          }
          case _ => sys.error("Only RECORD or ENUM can be toplevel definitions")
        }
      }
      case Right(protocol) => {
        val messages = protocol.getMessages.toMap
        if (messages.isEmpty) protocolToADT(protocol)
        else protocolToRPC(protocol)
      }
    }
  }
  
  def getName(schemaOrProtocol: Either[Schema, Protocol]): String = {
    schemaOrProtocol match {
      case Left(schema) => schema.getName
      case Right(protocol) => {
        def isEnum(schema: Schema) = schema.getType == Schema.Type.ENUM
        val messages = protocol.getMessages.toMap
        val localRecords = getLocalSubtypes(protocol).filterNot(isEnum)
        if (!messages.isEmpty) protocol.getName // for RPC trait
        else {
          if (localRecords.length > 1) protocol.getName // for ADT
          else localRecords.headOption match {
            case Some(schema) => schema.getName // for solo records make a class
            case None => protocol.getName       // default to protocol name
          }
        }
      }
    }
  }
  
  def compile(
    classStore: ClassStore, 
    namespace: Option[String], 
    schemaOrProtocol: Either[Schema, Protocol],
    outDir: String,
    schemaStore: SchemaStore): Unit = {
      
    def writeProtocolSubTypes(protocol: Protocol) = {
      // protocol could be destined to be an ADT def, so write Java separately
      def writeJavaTypesFirst(types: List[Schema]): Unit = {
        def isEnum(schema: Schema) = schema.getType == ENUM
        val enums = types.filter(isEnum)
        enums.foreach(schema => {
          compile(classStore, namespace, Left(schema), outDir, schemaStore)
        })
      }
      def writeAllTypes(types: List[Schema]): Unit = types.foreach(schema => {
        compile(classStore, namespace, Left(schema), outDir, schemaStore)
      })
      val localSubTypes = getLocalSubtypes(protocol)
      val messages = protocol.getMessages.toMap
      if (messages.isEmpty) writeJavaTypesFirst(localSubTypes) // for ADTs
      else writeAllTypes(localSubTypes) // for RPC trait
    }
    // Custom namespaces work for simple types, but seem to fail for records 
    // within unions, see http://apache-avro.679487.n3.nabble.com/Deserialize-with-different-schema-td4032782.html
    def checkCustomNamespace(namespace: Option[String]) = {
      def queryNamespaceMap(schemaNamespace: String): Option[String] = {
        typeMatcher.namespaceMap.get(schemaNamespace) match {
          case Some(customNamespace) => Some(customNamespace)
          case None => Some(schemaNamespace)
        }
      }
      namespace match {
        case Some(ns) => queryNamespaceMap(ns)
        case None => None
      }
    }
    if (schemaOrProtocol.isRight) {
      val Right(protocol) = schemaOrProtocol
      writeProtocolSubTypes(protocol)
    }
    val scalaNamespace = checkCustomNamespace(namespace)
    val compilationUnits = asCompilationUnits(
      classStore, 
      scalaNamespace, 
      schemaOrProtocol,
      schemaStore,
      Some(outDir))
    compilationUnits.foreach(writeToFile)
  }

  def registerTypes(
    schemaOrProtocol: Either[Schema, Protocol],
    classStore: ClassStore): Unit = {
    schemaOrProtocol match {
      case Left(schema) => {
        val classSymbol = RootClass.newClass(schema.getName)
        classStore.accept(schema, classSymbol)
      }
      case Right(protocol) => {
        protocol.getTypes.toList.foreach(schema => {
          val classSymbol = RootClass.newClass(schema.getName)
          classStore.accept(schema, classSymbol)
        })
      }
    }
  }

}
