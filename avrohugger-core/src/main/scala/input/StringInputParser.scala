package avrohugger
package input

import schemagen._

import org.apache.avro.Protocol
import org.apache.avro.Schema
import org.apache.avro.Schema.Parser
import org.apache.avro.SchemaParseException
import org.apache.avro.compiler.idl.Idl
import org.apache.avro.compiler.idl.ParseException
import scala.collection.JavaConverters._
import java.nio.charset.Charset

import scala.reflect.runtime.universe._
import scala.reflect.runtime.currentMirror
import scala.tools.reflect.ToolBox


// tries schema first, then protocol, then idl, then for case class defs
class StringInputParser {

  lazy val parser = new Parser()

  def getSchemas(inputString: String): List[Schema] = {

    def trySchema(str: String) = {
      try {
        List(parser.parse(inputString))} 
      catch {
        case notSchema: SchemaParseException => tryProtocol(inputString)
        case unknown: Throwable => sys.error("Unexpected exception: " + unknown)}}

    def tryProtocol(protocolStr: String): List[Schema] = {
      try {
        Protocol.parse(protocolStr).getTypes().asScala.toList}
      catch {
        case notProtocol: SchemaParseException => tryIDL(inputString)
        case unknown: Throwable => sys.error("Unexpected exception: " + unknown)}}

    def tryIDL(idlString: String): List[Schema] = {
      try {
        val bytes = idlString.getBytes(Charset.forName("UTF-8"))
        val inStream = new java.io.ByteArrayInputStream(bytes)
        val idlParser = new Idl(inStream)
        val protocol = idlParser.CompilationUnit()
        val types = protocol.getTypes
        types.asScala.toList}
      catch {
        case notIDL: ParseException => tryCaseClass(inputString)
        case unknown: Throwable => sys.error("Unexpected exception: " + unknown)}}

    def tryCaseClass(codeStr: String): List[Schema] = {
      // the parser can only parse packages if their contents are within blocks
      def wrapPackages(code: String): String = {
        // match package definitions that don't already wrap their contents in { }
        val nonWrappedRegEx = "(?!(package .*? \\{))(package ([a-zA-Z_$][a-zA-Z\\d_$]*\\.)*[a-zA-Z_$][a-zA-Z\\d_$]*)".r
        nonWrappedRegEx.findFirstIn(code) match {
          case Some(nonWrappedPackage) => {
            val openPackage = nonWrappedPackage + " {"
            val closePackage = "}"
            val wrappedPackage = nonWrappedRegEx.replaceFirstIn(code, openPackage) + closePackage
            wrapPackages(wrappedPackage)}
          case None => code}}
      val packaged = wrapPackages(codeStr)
      val tree = Toolbox.toolBox.parse(packaged)
      TreeInputParser.parse(tree)}

    // tries schema first, then protocol, then idl, then for case class defs
    val schemas: List[Schema] = trySchema(inputString)
    schemas
  }
}

