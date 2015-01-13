package avrohugger

import org.apache.avro.Schema
import java.io.{File, FileNotFoundException, IOException}

object Generator {
  
  private val defaultOutputDir = "target/generated-sources/"

  def fromSchema(schema: Schema, outDir: String = defaultOutputDir): Unit = {
    val classStore = new ClassStore
    val namespace: Option[String] = FileParser.getNamespace(schema)
    val recordSchemas: List[Schema] = schema::(FileParser.getNestedSchemas(schema))
    // if a field's type is a record, the nested Schema has no namespace, but we need one for each file so we pass one
    recordSchemas.reverse.map(schema => AvroHugger.defineCaseClass(classStore, namespace, schema, outDir))
  }

  def fromFile(inFile: File, outDir: String = defaultOutputDir): Unit = {
  	try {
      val schema: Schema = FileParser.getSchema(inFile)
      fromSchema(schema, outDir)
  	} 
  	catch {
      case ex: FileNotFoundException => sys.error("File not found:" + ex)
      case ex: IOException => sys.error("There was a problem using the file: " + ex)
    }
  }

}
