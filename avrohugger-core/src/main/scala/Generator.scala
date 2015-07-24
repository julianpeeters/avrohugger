package avrohugger

import format._

import org.apache.avro.Schema
import org.apache.avro.Schema.Type.{ARRAY, RECORD, UNION, ENUM}

import scala.collection.JavaConverters._
import java.io.{File, FileNotFoundException, IOException}

class Generator(format: SourceFormat) {
  val sourceFormat = format
  val defaultOutputDir = "target/generated-sources"
  val fileParser = new FileParser
  val schemaParser = new Schema.Parser
  val classStore = new ClassStore


  // methods for writing definitions out to file 
  def schemaToFile(schema: Schema, outDir: String = defaultOutputDir): Unit = {
    val namespace: Option[String] = getNamespace(schema)
    val topLevelSchemas: List[Schema] = schema::(getNestedSchemas(schema))
    // for nested records, the nested Schema has no namespace, so we pass one in
    val _ = topLevelSchemas.reverse.map(schema => {
      sourceFormat.writeToFile(classStore, namespace, schema, outDir)
    })
  }

  def stringToFile(schemaStr: String, outDir: String = defaultOutputDir): Unit = {
    val schema = schemaParser.parse(schemaStr)
    schemaToFile(schema, outDir)
  }

  def fileToFile(inFile: File, outDir: String = defaultOutputDir): Unit = {
      val schemas: List[Schema] = fileParser.getSchemas(inFile)
      schemas.foreach(schema => schemaToFile(schema, outDir))
  }


  // methods for writing definitions to a list of definitions in String format
  def schemaToStrings(schema: Schema): List[String] = {
    val namespace: Option[String] = getNamespace(schema)
    val topLevelSchemas: List[Schema] = schema::getNestedSchemas(schema)
    // for nested records, the nested Schema has no namespace, so we pass one in
    topLevelSchemas.reverse.map(schema => {
      val codeString = sourceFormat.asDefinitionString(classStore, namespace, schema)
      // drop the comment because it's not applicable outside of file writing/overwriting
      codeString.replace("/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */\n", "")
    })
  }

  def stringToStrings(schemaStr: String): List[String] = {
    val schema = schemaParser.parse(schemaStr)
    schemaToStrings(schema)
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


def getNamespace(schema: Schema): Option[String] = {
    schema.getNamespace match {
      case null => None
      case namespace => Some(namespace)
    }
  }

}

