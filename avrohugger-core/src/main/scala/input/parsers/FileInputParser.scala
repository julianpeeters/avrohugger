package avrohugger
package input
package parsers

import org.apache.avro.Protocol
import org.apache.avro.Schema
import org.apache.avro.Schema.Parser
import org.apache.avro.Schema.Type.{ARRAY, RECORD, UNION, ENUM}
import org.apache.avro.compiler.idl.Idl
import org.apache.avro.generic.{GenericDatumReader, GenericRecord}
import org.apache.avro.file.DataFileReader
import scala.collection.JavaConverters._

class FileInputParser {
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
        val types = protocol.getTypes
        types.asScala.toList
      case _ => throw new Exception("File must end in .avsc for plain text json files, .avdl for IDL files, or .avro for binary.")
    }
    schemas.map(s => s.getType match {
      //if top-level record is wrapped in a union with no other types
      case UNION => {
        val types = s.getTypes.asScala.toList
        if (types.length == 1) types.head
        else sys.error("Unions, beyond nullable fields, are not supported. Found a union of more than one type: " + types)
      }
      case RECORD => s
      case ENUM => s
      case _ => sys.error("Neither a record, enum nor a union of either. Nothing to map to a definition.")
    })
  }
}
