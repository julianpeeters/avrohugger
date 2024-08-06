package avrohugger
package input
package parsers

import stores.SchemaStore

import org.apache.avro.Protocol
import org.apache.avro.Schema
import org.apache.avro.Schema.Parser
import org.apache.avro.SchemaParseException
import org.apache.avro.idl.IdlReader
import org.apache.avro.compiler.idl.ParseException

import scala.jdk.CollectionConverters._
import java.nio.charset.Charset
import java.io.FileNotFoundException

// tries schema first, then protocol, then idl, then for case class defs
class StringInputParser {

  lazy val schemaParser = new Parser()

  def getSchemaOrProtocols(
    inputString: String,
    schemaStore: SchemaStore
  ): List[Either[Schema, Protocol]] = {

    def trySchema(str: String): List[Either[Schema, Protocol]] = {
      try {
        List(Left(schemaParser.parse(str)))
      } 
      catch {
        case notSchema: SchemaParseException => tryProtocol(str)
        case unknown: Throwable => sys.error("Unexpected exception: " + unknown)
      }
    }

    def tryProtocol(str: String): List[Either[Schema, Protocol]] = {
      try {
        List(Right(Protocol.parse(str)))
      }
      catch {
        case notProtocol: SchemaParseException => tryIDL(str)
        case unknown: Throwable => sys.error("Unexpected exception: " + unknown)
      }
    }

    def tryIDL(str: String): List[Either[Schema, Protocol]] = {
      try {
        val bytes = str.getBytes(Charset.forName("UTF-8"))
        val inStream = new java.io.ByteArrayInputStream(bytes)
        val idlParser = new IdlReader().parse(inStream)
        val protocol = idlParser.getProtocol()
        List(Right(protocol))
      }
      catch {
        case e: ParseException => sys.error(s"Unable to parse: ${e}")
        case e: SchemaParseException => sys.error("Imports not supported in String IDLs, only avdl files.")
        case unknown: Throwable => sys.error("Unexpected exception: " + unknown)
        }
      }
    
    // tries schema first, then protocol, then idl, then for case class defs
    val schemaOrProtocols = trySchema(inputString)
    schemaOrProtocols
  }
}

