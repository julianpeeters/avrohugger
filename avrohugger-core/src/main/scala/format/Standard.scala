package avrohugger
package format

import avrohugger.format.standard._
import avrohugger.input.reflectivecompilation.schemagen.SchemaStore

import treehugger.forest._

import org.apache.avro.{ Protocol, Schema }

import java.nio.file.{Path, Paths, Files, StandardOpenOption}
import java.io.{File, FileNotFoundException, IOException}

import scala.collection.JavaConversions._

object Standard extends SourceFormat {

  override val toolName = "generate";
  override val toolShortDescription = "Generates Scala code for the given schema.";
  override def fileExt(schemaOrProtocol: Either[Schema, Protocol]) = ".scala"

  val typeMatcher = new TypeMatcher

  override def asDefinitionString(
    classStore: ClassStore, 
    namespace: Option[String], 
    schemaOrProtocol: Either[Schema, Protocol],
    schemaStore: SchemaStore): String = {
    // SpecificCompiler can't return a tree for Java enums, so return
    // a string here for a consistent api vis a vis *ToFile and *ToStrings
    StandardTreehugger.asScalaCodeString(
      classStore,
      schemaOrProtocol,
      namespace,
      typeMatcher,
      schemaStore)
  }

  override def writeToFile(
    classStore: ClassStore, 
    namespace: Option[String], 
    schemaOrProtocol: Either[Schema, Protocol],
    outDir: String,
    schemaStore: SchemaStore): Unit = {

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
