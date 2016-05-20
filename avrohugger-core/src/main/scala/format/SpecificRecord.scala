package avrohugger
package format

import avrohugger.format.specific._
import avrohugger.input.reflectivecompilation.schemagen.SchemaStore

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Type.{ ENUM, RECORD }

import java.nio.file.{Path, Paths, Files, StandardOpenOption}
import java.io.{File, FileNotFoundException, IOException}

import scala.collection.JavaConversions._

object SpecificRecord extends SourceFormat{

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

  override def asDefinitionString(
    classStore: ClassStore, 
    namespace: Option[String], 
    schemaOrProtocol: Either[Schema, Protocol],
    schemaStore: SchemaStore): String = {
      schemaOrProtocol match {
        case Left(schema) => schema.getType match {
          case RECORD => {
            SpecificScalaTreehugger.asScalaCodeString(
              classStore,
              namespace,
              schemaOrProtocol,
              typeMatcher,
              schemaStore)
          }
          case ENUM => {
            SpecificJavaTreehugger.asJavaCodeString(
              classStore,
              namespace,
              schema)
          }
          case _ => sys.error("Only RECORD or ENUM can be toplevel definitions")
        }
        case Right(protocol) => {
          SpecificScalaTreehugger.asScalaCodeString(
            classStore,
            namespace,
            schemaOrProtocol,
            typeMatcher,
            schemaStore)
        }
      }
  }
  
  override def writeToFile(
    classStore: ClassStore, 
    namespace: Option[String], 
    schemaOrProtocol: Either[Schema, Protocol],
    outDir: String,
    schemaStore: SchemaStore): Unit = {
      
    def writeProtocolSubTypes(protocol: Protocol) = {
      def writeJavaTypesFirst(types: List[Schema]): Unit = {
        // protocol could be destined to be an ADT def, so write Java separately
        def isEnum(schema: Schema) = schema.getType == ENUM
        val enums = types.filter(isEnum)
        enums.foreach(schema => {
          writeToFile(classStore, namespace, Left(schema), outDir, schemaStore)
        })
      }
      def writeAllTypes(types: List[Schema]): Unit = types.foreach(schema => {
        writeToFile(classStore, namespace, Left(schema), outDir, schemaStore)
      })
      val types = protocol.getTypes.toList
      val messages = protocol.getMessages.toMap
      if (messages.isEmpty) writeJavaTypesFirst(types) // for ADTs
      else writeAllTypes(types) // for RPC trait
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

    val codeAsString = asDefinitionString(
      classStore, 
      scalaNamespace, 
      schemaOrProtocol,
      schemaStore)

    val folderPath: Path = Paths.get{
      if (scalaNamespace.isDefined) {
        s"$outDir/${scalaNamespace.get.toString.replace('.','/')}"
      }
      else outDir
    }

    if (!Files.exists(folderPath)) Files.createDirectories(folderPath)

    val filePath = {
      val fileName = getName(schemaOrProtocol) + fileExt(schemaOrProtocol)
      Paths.get(s"$folderPath/$fileName")
    }
    try { // delete old and/or create new
      Files.deleteIfExists(filePath)
      Files.write(filePath, codeAsString.getBytes(), StandardOpenOption.CREATE) 
      () 
    } 
    catch {
      case ex: FileNotFoundException => sys.error("File not found:" + ex)
      case ex: IOException => sys.error("Problem using the file: " + ex)
    }
  }


}
