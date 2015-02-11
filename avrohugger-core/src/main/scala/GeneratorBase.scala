package avrohugger

import org.apache.avro.Schema
import java.io.{File, FileNotFoundException, IOException}

trait GeneratorBase {
  val defaultOutputDir = "target/generated-sources/"
  val parser = new FileParser
  val classStore = new ClassStore
  def fromSchema(schema: Schema, outDir: String = defaultOutputDir): Unit 

  def fromFile(inFile: File, outDir: String = defaultOutputDir): Unit = {
    try {
      val schemas: List[Schema] = parser.getSchemas(inFile)
      schemas.foreach(s => fromSchema(s, outDir))
    } 
    catch {
      case ex: FileNotFoundException => sys.error("File not found:" + ex)
      case ex: IOException => sys.error("There was a problem using the file: " + ex)
    }
  }
}

