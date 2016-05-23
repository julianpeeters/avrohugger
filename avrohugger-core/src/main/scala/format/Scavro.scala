package avrohugger
package format

import scavro._
import scavro.ScavroTreehugger
import avrohugger.input.reflectivecompilation.schemagen.SchemaStore

import treehugger.forest._

import org.apache.avro.{ Protocol, Schema }

import java.nio.file.{ Path, Paths, Files, StandardOpenOption }
import java.io.{ File, FileNotFoundException, IOException }

import scala.collection.JavaConversions._

object Scavro extends SourceFormat {

  override val toolName = "generate-scavro"
  override val toolShortDescription = "Generates Scala wrapper code for the given schema."
  override def fileExt(schemaOrProtocol: Either[Schema, Protocol]) = ".scala"

  val typeMatcher = new TypeMatcher
  typeMatcher.updateTypeMap("array"-> classOf[Array[_]])

  override def asDefinitionString(
    classStore: ClassStore, 
    namespace: Option[String], 
    schemaOrProtocol: Either[Schema, Protocol],
    schemaStore: SchemaStore): String = {
    // SpecificCompiler can't return a tree for Java enums, so return
    // a string here for a consistent api vis a vis *ToFile and *ToStrings
    ScavroTreehugger.asScalaCodeString(
      classStore, 
      schemaOrProtocol, 
      namespace, 
      typeMatcher,
      schemaStore)
  }
  
  override def getName(schemaOrProtocol: Either[Schema, Protocol]): String = {
    schemaOrProtocol match {
      case Left(schema) => schema.getName
      case Right(protocol) => {
        val localSubtypes = Scavro.getLocalSubtypes(protocol)
        if (localSubtypes.length > 1) protocol.getName
        else localSubtypes.headOption match {
          case Some(schema) => schema.getName
          case None => protocol.getName
        }
      }
    }
  }

  override def writeToFile(
    classStore: ClassStore, 
    namespace: Option[String],
    schemaOrProtocol: Either[Schema, Protocol],
    outDir: String,
    schemaStore: SchemaStore): Unit = {

    // By default, Scavro generates Scala classes in packages that are the same 
    // as the Java package with `model` appended. 
    val scavroModelDefaultPackage = "model"
    val scavroModelDefaultNamespace = namespace match {
      case Some(ns) => Some(ns + "." + scavroModelDefaultPackage)
      case None => Some(scavroModelDefaultPackage)
    }

    def getCustomNamespace(schemaNamespace: String): Option[String] = {
      typeMatcher.namespaceMap.get(schemaNamespace) match {
        case None => scavroModelDefaultNamespace
        case customNamespace => customNamespace
      }
    }
    
    val scavroModelNamespace = schemaOrProtocol match {
      case Left(schema) => {
        schema.getNamespace match {
          case null => scavroModelDefaultNamespace
          case schemaNamespace => getCustomNamespace(schemaNamespace)
        }
      }
      case Right(protocol) => {
        protocol.getNamespace match {
          case null => scavroModelDefaultNamespace
          case schemaNamespace => getCustomNamespace(schemaNamespace)
        }
      }
    }

    val codeAsString = asDefinitionString(
      classStore,
      scavroModelNamespace,
      schemaOrProtocol,
      schemaStore)

    val folderPath: Path = Paths.get{
      s"$outDir/${scavroModelNamespace.get.toString.replace('.','/')}"
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
