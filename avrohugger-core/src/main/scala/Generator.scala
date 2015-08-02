package avrohugger

import java.io.{File, FileNotFoundException, IOException}

import avrohugger.format.DependencyInspectionSupport._
import avrohugger.format._
import org.apache.avro.Schema
import org.apache.avro.Schema.Type.{ARRAY, ENUM, RECORD, UNION}

import scala.collection.JavaConverters._
import DependencyInspectionSupport._


// Unable to overload the methods of this class because outDir uses a default value
class Generator(format: SourceFormat) {
  val sourceFormat = format
  val defaultOutputDir = "target/generated-sources"
  lazy val fileParser = new FileInputParser
  lazy val stringParser = new StringInputParser
  lazy val schemaParser = new Schema.Parser
  val classStore = new ClassStore


  // methods for writing definitions out to file 
  def schemaToFile(schema: Schema, outDir: String = defaultOutputDir): Unit = {
    val namespace: Option[String] = getReferredNamespace(schema)
    val topLevelSchemas: List[Schema] = schema::(getNestedSchemas(schema))

    // for nested records, when the nested Schema has no namespace, we pass one in otherwise we use its one
    topLevelSchemas.reverse.foreach { schema =>
      val ns = getReferredNamespace(schema) orElse namespace
      sourceFormat.writeToFile(classStore, ns, schema, outDir)
    }
  }

  def stringToFile(schemaStr: String, outDir: String = defaultOutputDir): Unit = {
    val schemas = stringParser.getSchemas(schemaStr)
    schemas.foreach(schema => schemaToFile(schema, outDir))
  }

  def fileToFile(inFile: File, outDir: String = defaultOutputDir): Unit = {
    val schemas: List[Schema] = fileParser.getSchemas(inFile)
    schemas.foreach(schema => schemaToFile(schema, outDir))
  }


  // methods for writing definitions to a list of definitions in String format
  def schemaToStrings(schema: Schema): List[String] = {
    val namespace: Option[String] = getReferredNamespace(schema)
    val topLevelSchemas: List[Schema] = schema::getNestedSchemas(schema)
    // for nested records, the nested Schema has no namespace, so we pass one in
    topLevelSchemas.reverse.map(schema => {
      val ns = getReferredNamespace(schema) orElse namespace
      val codeString = sourceFormat.asDefinitionString(classStore, ns, schema)
      // drop the comment because it's not applicable outside of file writing/overwriting
      codeString.replace("/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */\n", "")
    })
  }

  def stringToStrings(schemaStr: String): List[String] = {
    val schemas = stringParser.getSchemas(schemaStr)
    schemas.flatMap(schema => schemaToStrings(schema))
  }

  def fileToStrings(inFile: File): List[String] = {
    try {
      val schemas: List[Schema] = fileParser.getSchemas(inFile)
      schemas.flatMap(schema => schemaToStrings(schema))
    } 
    catch {
      case ex: FileNotFoundException => sys.error("File not found:" + ex)
      case ex: IOException => sys.error("There was a problem using the file: " + ex)
    }
  }


  // called by sublasses that use it in their implementations of 
  // `writeToFile(schema: Schema): Unit` and `toStrings(schema: Schema): List[String]`
  def getNestedSchemas(schema: Schema): List[Schema] = schema.getType match {
    // if a record is found, check for and extract nested RECORDs and ENUMS (i.e. top-level types) 
    case RECORD =>
      val fields: List[org.apache.avro.Schema.Field] = schema.getFields.asScala.toList
      val fieldSchemas: List[org.apache.avro.Schema] = fields.map(field => field.schema())

      def flattenSchema(fieldSchema: Schema): List[Schema] = {
        fieldSchema.getType match {
          case ARRAY => flattenSchema(fieldSchema.getElementType)
          case RECORD => fieldSchema :: getNestedSchemas(fieldSchema)
          case UNION => fieldSchema.getTypes.asScala.toList.flatMap(x => flattenSchema(x))
          case _ => List(fieldSchema)
        }
      }
      val flatSchemas = fieldSchemas.flatMap(fieldSchema => flattenSchema(fieldSchema))
      def topLevelTypes(schema: Schema) = (schema.getType == RECORD | schema.getType == ENUM)
      val nestedTopLevelSchemas = flatSchemas.filter(topLevelTypes)

      nestedTopLevelSchemas
    case _ => Nil
  }

}

