package avrohugger
package format

import abstractions.SourceFormat
import format.specific.SpecificScalaTreehugger
import models.CompilationUnit
import stores.{ ClassStore, SchemaStore }
import matchers.{ CustomNamespaceMatcher, TypeMatcher }

import treehugger.forest._
import definitions.RootClass

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Type.{ ENUM, RECORD }

import java.nio.file.Path
import scala.collection.JavaConversions._

object SpecificRecord extends SourceFormat{
  
  val toolName = "generate-specific"

  val toolShortDescription = "Generates Scala code extending SpecificRecordBase."

  def fileExt(schemaOrProtocol: Either[Schema, Protocol]) = {
    schemaOrProtocol match {
      case Left(schema) => schema.getType match {
        case RECORD => ".scala"
        case ENUM => ".java" // Avro's SpecificData requires enums be Java Enum
        case _ => sys.error("Only RECORD and ENUM can be top-level definitions")
      }
      case Right(protocol) => ".scala"
    }
  }

  val scalaTreehugger = SpecificScalaTreehugger

  def asCompilationUnits(
    classStore: ClassStore, 
    ns: Option[String], 
    schemaOrProtocol: Either[Schema, Protocol],
    schemaStore: SchemaStore,
    maybeOutDir: Option[String],
    typeMatcher: TypeMatcher): List[CompilationUnit] = {
    
    registerTypes(schemaOrProtocol, classStore, typeMatcher)

    val maybeCustom = ns match {
      case Some(schemaNS) => typeMatcher.customNamespaceMap.get(schemaNS)
      case None => ns
    }
    val namespace = CustomNamespaceMatcher.checkCustomNamespace(maybeCustom, ns)

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
        getJavaEnumCompilationUnit(classStore, namespace, schema, maybeOutDir)
      })
      val rpcTypeCompUnits = scalaCompUnits ::: javaCompUnits
      rpcTraitCompUnit +: rpcTypeCompUnits
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
            val javaCompilationUnit = getJavaEnumCompilationUnit(
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
        if (messages.isEmpty) {
          val localSubtypes = getLocalSubtypes(protocol)
          val localEnums = localSubtypes.filter(isEnum)
          val javaCompilationUnits = localEnums.map(schema => {
            getJavaEnumCompilationUnit(classStore, namespace, schema, maybeOutDir)
          })
          val scalaADTorSoloClassCompilationUnit = getScalaCompilationUnit(
            classStore,
            namespace,
            Right(protocol),
            typeMatcher,
            schemaStore,
            maybeOutDir)
          scalaADTorSoloClassCompilationUnit +: javaCompilationUnits
        }
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
    ns: Option[String], 
    schemaOrProtocol: Either[Schema, Protocol],
    outDir: String,
    schemaStore: SchemaStore,
    typeMatcher: TypeMatcher): Unit = {
    val compilationUnits: List[CompilationUnit] = asCompilationUnits(
      classStore, 
      ns, 
      schemaOrProtocol,
      schemaStore,
      Some(outDir),
      typeMatcher)
    compilationUnits.foreach(writeToFile)
  }

}
