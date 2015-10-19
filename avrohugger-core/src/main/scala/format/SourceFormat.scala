package avrohugger
package format

import org.apache.avro.Schema

import java.nio.file.{Path, Paths, Files, StandardOpenOption}
import java.io.{File, FileNotFoundException, IOException}
import scala.collection.JavaConversions._

trait SourceFormat {

  // abstract members to be implemented by a subclass
  val toolName: String

  val toolShortDescription: String

  def fileExt(schema: Schema): String

  val typeMatcher: TypeMatcher

  def asDefinitionString(
  	classStore: ClassStore, 
    namespace: Option[String], 
    schema: Schema): String

  // concrete member takes a schema and writes the definition to a file
  def writeToFile(
    classStore: ClassStore, 
    namespace: Option[String], 
    schema: Schema, 
    outDir: String): Unit = {


    // Custom namespaces work for simple types, but seem to fail for records within unions, 
    // see http://apache-avro.679487.n3.nabble.com/Deserialize-with-different-schema-td4032782.html
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

    val codeAsString = asDefinitionString(classStore, 
      scalaNamespace, 
      schema)

    val folderPath: Path = Paths.get{
      if (scalaNamespace.isDefined) outDir + "/" + scalaNamespace.get.toString.replace('.','/')
      else outDir
    }

    if (!Files.exists(folderPath)) Files.createDirectories(folderPath)

    val filePath = { 
      Paths.get(folderPath.toString + "/" + schema.getName + fileExt(schema))
    }
    try { // delete old and/or create new
      Files.deleteIfExists(filePath)
      Files.write(filePath, codeAsString.getBytes(), StandardOpenOption.CREATE) 
      () 
    } 
    catch {
      case ex: FileNotFoundException => sys.error("File not found:" + ex)
      case ex: IOException => sys.error("There was a problem using the file: " + ex)
    }
  }

}