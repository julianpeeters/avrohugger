package avrohugger

import org.apache.avro.Protocol
import org.apache.avro.Schema
import org.apache.avro.Schema.Parser
import org.apache.avro.compiler.idl.Idl
import org.apache.avro.SchemaParseException
import scala.collection.JavaConverters._
import java.nio.charset.Charset


// tries schema first, then protocol, then idl
class StringInputParser {

  lazy val parser = new Parser()

  def getSchemas(inputString: String): List[Schema] = {

    def trySchema(str: String) = {
      try {
        List(parser.parse(inputString))
      } 
      catch {
        case notSchema: SchemaParseException => tryProtocol(inputString)
        case unknown: Throwable => sys.error("Unexpected exception: " + unknown)
      }

    }

    def tryProtocol(str: String) = {
      try {
        Protocol.parse(inputString).getTypes().asScala.toList
      }
      catch {
        case notSchema: SchemaParseException => tryIDL(inputString)
        case unknown: Throwable => sys.error("Unexpected exception: " + unknown)
      }   
    }

    def tryIDL(str: String) = {
      try {
        val bytes = inputString.getBytes(Charset.forName("UTF-8"))
        val inStream = new java.io.ByteArrayInputStream(bytes)
        val idlParser = new Idl(inStream)
        val protocol = idlParser.CompilationUnit()
        val types = protocol.getTypes
        types.asScala.toList
      }
      catch {
        case unknown: Throwable => sys.error("Unexpected exception: " + unknown)
      }
    }

    // tries schema first, then protocol, then idl
    val schemas: List[Schema] = trySchema(inputString)
    schemas

  }
}

