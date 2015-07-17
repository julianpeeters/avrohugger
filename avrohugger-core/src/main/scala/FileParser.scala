package avrohugger

import org.apache.avro.Protocol
import org.apache.avro.Schema
import org.apache.avro.Schema.Parser
import org.apache.avro.Schema.Type.{ARRAY, RECORD, UNION, ENUM}
import org.apache.avro.compiler.idl.Idl
import org.apache.avro.generic.{GenericDatumReader, GenericRecord}
import org.apache.avro.file.DataFileReader
import scala.collection.JavaConverters._

class FileParser {
  lazy val parser = new Parser()

  def getSchemas(infile: java.io.File): List[Schema] = {
    val schemas: List[Schema] = infile.getName.split("\\.").last match {
      case "avro" =>
        val gdr = new GenericDatumReader[GenericRecord]
        val dfr = new DataFileReader(infile, gdr)
        List(dfr.getSchema)
      case "avsc" => List(parser.parse(infile))
      case "avpr" => Protocol.parse(infile).getTypes().asScala.toList
      case "avdl" =>
        val idlParser = new Idl(infile)
        val protocol = idlParser.CompilationUnit()
        protocol.getTypes.asScala.toList
      case _ => throw new Exception("File must end in .avsc for plain text json files, .avdl for IDL files, or .avro for binary.")
    }
    schemas.map(s => s.getType match {
      //if top-level record is wrapped in a union with no other types
      case UNION => {
        val types = s.getTypes.asScala.toList
        if (types.length == 1) types.head
        else sys.error("Expected a single top-level record, found a union of more than one type: " + types)
      }
      case RECORD => s
      case ENUM => s
      case _ => sys.error("Neither a record nor a union of records, nothing to map to case class.")
    })
  }

  def getNamespace(schema: Schema): Option[String] = {
    schema.getNamespace match {
      case null => None
      case namespace => Some(namespace)
    }
  }

  def getNestedSchemas(schema: Schema): List[Schema] = schema.getType match {
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

