package avrohugger
package format

import scavro._
import scavro.ScavroTreehugger

import treehugger.forest._

import org.apache.avro.Schema

import java.nio.file.{Path, Paths, Files, StandardOpenOption}
import java.io.{File, FileNotFoundException, IOException}

import scala.collection.JavaConversions._

object Scavro extends SourceFormat {

  override val toolName = "generate-scavro";
  override val toolShortDescription = "Generates Scala wrapper code for the given schema.";
  override def fileExt(schema: Schema) = ".scala"

  val typeMatcher = new TypeMatcher
  typeMatcher.updateTypeMap("array"->"Array")

  override def asDefinitionString(
    classStore: ClassStore, 
    namespace: Option[String], 
    schema: Schema): String = {
    
    val tree = ScavroTreehugger.asScalaCodeString(classStore, 
      schema, 
      namespace, 
      typeMatcher)
    // SpecificCompiler can't return a tree for Java enums, so return
    // a string here for a consistent api vis a vis *ToFile and *ToStrings
    treeToString(tree)
  }

  override def writeToFile(
    classStore: ClassStore, 
    namespace: Option[String], 
    schema: Schema, 
    outDir: String): Unit = {

    // By default, Scavro generates Scala classes in packages that are the same as the 
    // Java package with `model` appended. 
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
    
    val scavroModelNamespace = schema.getNamespace match {
      case null => scavroModelDefaultNamespace
      case schemaNamespace => getCustomNamespace(schemaNamespace) 
    }

    val codeAsString = asDefinitionString(classStore, scavroModelNamespace, schema)

    val folderPath: Path = Paths.get{
      outDir + "/" + scavroModelNamespace.get.toString.replace('.','/')
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