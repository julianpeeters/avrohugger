package avrohugger
package input
package parsers

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Parser
import org.apache.avro.Schema.Type.{ RECORD, UNION, ENUM }
import org.apache.avro.compiler.idl.Idl
import org.apache.avro.generic.{ GenericDatumReader, GenericRecord }
import org.apache.avro.file.DataFileReader

import java.io.File

import scala.collection.JavaConverters._

class FileInputParser {
  lazy val parser = new Parser()

  def getSchemaOrProtocols(infile: File): List[Either[Schema, Protocol]] = {
    
    def unUnion(schema: Schema) = {
      schema.getType match {
        //if top-level record is wrapped in a union with no other types
        case UNION => {
          val types = schema.getTypes.asScala.toList
          if (types.length == 1) types.head
          else sys.error("""Unions, beyond nullable fields, are not supported. 
            |Found a union of more than one type: """.trim.stripMargin + types)
        }
        case RECORD => schema
        case ENUM => schema
        case _ => sys.error("""Neither a record, enum nor a union of either. 
          |Nothing to map to a definition.""".trim.stripMargin)
      }
    }
    
    /**
     * Avro files may contain imported types from other namespaces. For .avdl
     * files, it is possible to see where the imported types come from, and if 
     * they come from a protocol or idl, then they should be generated as part 
     * of an ADT. For .avpr and .avsc files, it is NOT possible to determine if 
     * imported types are part of an external idl or protocol, and are generated
     * as simple stand-alone classes or enums.  
     */
    val schemaOrProtocols: List[Either[Schema, Protocol]] = {
      infile.getName.split("\\.").last match {
        case "avro" =>
          val gdr = new GenericDatumReader[GenericRecord]
          val dfr = new DataFileReader(infile, gdr)
          val schema = unUnion(dfr.getSchema)
          List(Left(schema))
        case "avsc" =>
          val schema = unUnion(parser.parse(infile))
          List(Left(schema))
        case "avpr" =>
          val protocol = Protocol.parse(infile)
          val schemas = protocol.getTypes.asScala.toList
          val otherNamespaceSchemaOrProtocols = schemas.filterNot(schema => {
            schema.getNamespace == protocol.getNamespace
          }).map(schema => Left(schema))
          Right(protocol) +: otherNamespaceSchemaOrProtocols
        case "avdl" =>
          val idlParser = new Idl(infile)
          val protocol = idlParser.CompilationUnit()
          val importedFiles = IdlImportParser.getImportedFiles(infile)
          val importedSchemaOrProtocols = importedFiles.flatMap(imported => {
            getSchemaOrProtocols(imported)
          }).toList
          Right(protocol) +: importedSchemaOrProtocols
        case _ =>
          throw new Exception("""File must end in ".avpr" for protocol files, 
            |".avsc" for plain text json files, ".avdl" for IDL files, or .avro 
            |for binary.""".trim.stripMargin)
      }
    }
    
    schemaOrProtocols
  }
}
