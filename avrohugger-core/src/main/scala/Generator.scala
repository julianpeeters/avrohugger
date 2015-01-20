package avrohugger

import org.apache.avro.Schema
import java.io.{File, FileNotFoundException, IOException}

class Generator {

  private val defaultOutputDir = "target/generated-sources/"
  
  val parser = new FileParser

  def fromSchema(schema: Schema, outDir: String = defaultOutputDir): Unit = {
    val classStore = new ClassStore
    val namespace: Option[String] = parser.getNamespace(schema)
    val recordSchemas: List[Schema] = schema::(parser.getNestedSchemas(schema))
    // if a field's type is a record, the nested Schema has no namespace, but we need one for each file so we pass one
    recordSchemas.reverse.map(schema => AvroHugger.defineCaseClass(classStore, namespace, schema, outDir))
  }

  def fromFile(inFile: File, outDir: String = defaultOutputDir): Unit = {
  	try {
      val schema: Schema = parser.getSchema(inFile)
      fromSchema(schema, outDir)
  	} 
  	catch {
      case ex: FileNotFoundException => sys.error("File not found:" + ex)
      case ex: IOException => sys.error("There was a problem using the file: " + ex)
    }
  }

}
