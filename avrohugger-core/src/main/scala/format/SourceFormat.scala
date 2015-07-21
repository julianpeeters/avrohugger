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

    val codeAsString = asDefinitionString(classStore, namespace, schema)

    val folderPath: Path = Paths.get{
      if (namespace.isDefined) outDir + "/" + namespace.get.toString.replace('.','/')
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