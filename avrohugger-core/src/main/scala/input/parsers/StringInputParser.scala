package avrohugger
package input
package parsers


import org.apache.avro.Schema.Parser
import org.apache.avro.{ Protocol, Schema, SchemaParseException }
import org.apache.avro.compiler.idl.{ Idl, ParseException }

import java.nio.charset.Charset

// tries schema first, then protocol, then idl, then for case class defs
class StringInputParser {

  lazy val schemaParser = new Parser()

  private def trySchema(str: String): List[Either[Schema, Protocol]] = {
    try {
      List(Left(schemaParser.parse(str)))
    }
    catch {
      case notSchema: SchemaParseException => tryProtocol(str)
      case unknown: Throwable => sys.error("Unexpected exception: " + unknown)
    }
  }

  private def tryProtocol(str: String): List[Either[Schema, Protocol]] = {
    try {
      List(Right(Protocol.parse(str)))
    }
    catch {
      case notProtocol: SchemaParseException => tryIDL(str)
      case unknown: Throwable => sys.error("Unexpected exception: " + unknown)
    }
  }

  private def tryIDL(str: String): List[Either[Schema, Protocol]] = {
    try {
      val bytes = str.getBytes(Charset.forName("UTF-8"))
      val inStream = new java.io.ByteArrayInputStream(bytes)
      val idlParser = new Idl(inStream)
      val protocol = idlParser.CompilationUnit()
      List(Right(protocol))
    }
    catch {
      case e: ParseException => sys.error(s"Unable to parse: ${e}")
      case npe: NullPointerException => sys.error("Imports not supported in String IDLs, only avdl files.")
      case unknown: Throwable => sys.error("Unexpected exception: " + unknown)
    }
  }

  def getSchemaOrProtocols(inputString: String): List[Either[Schema, Protocol]] =
    trySchema(inputString)
}

