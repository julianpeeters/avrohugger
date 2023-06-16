package avrohugger
package input
package parsers

import reflectivecompilation.{ PackageSplitter, Toolbox }
import stores.{ SchemaStore, TypecheckDependencyStore }

import org.apache.avro.Protocol
import org.apache.avro.Schema
import org.apache.avro.Schema.Parser
import org.apache.avro.SchemaParseException
import org.apache.avro.compiler.idl.Idl
import org.apache.avro.compiler.idl.ParseException

import scala.jdk.CollectionConverters._
import java.nio.charset.Charset
import java.io.FileNotFoundException

// tries schema first, then protocol, then idl, then for case class defs
class StringInputParser {

  lazy val schemaParser = new Parser()
  lazy val typecheckDependencyStore = new TypecheckDependencyStore

  def getSchemaOrProtocols(
    inputString: String,
    schemaStore: SchemaStore): List[Either[Schema, Protocol]] = {

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
        val idlParser = new Idl(inStream)
        val protocol = idlParser.CompilationUnit()
        List(Right(protocol))
      }
      catch {
        case e: ParseException => {
          if (e.getMessage.contains("FileNotFoundException")) {
            sys.error("Imports not supported in String IDLs, only avdl files.")
          }
          else tryCaseClass(str, schemaStore)
        }
        case npe: NullPointerException => sys.error("Imports not supported in String IDLs, only avdl files.")
        case unknown: Throwable => sys.error("Unexpected exception: " + unknown)
        }
      }

    @deprecated("Reflective compilation will no longer be supported", "avrohugger 1.5.0")
    def tryCaseClass(
      str: String,
      schemaStore: SchemaStore): List[Either[Schema, Protocol]] = {
      val compilationUnits = PackageSplitter.getCompilationUnits(str)
      val scalaDocs = ScalaDocParser.getScalaDocs(compilationUnits)
      val trees = compilationUnits.map(src => Toolbox.toolBox.parse(src))
      val treesZippedWithDocs = trees.zip(scalaDocs)
      val schemas = treesZippedWithDocs.flatMap(treeAndDocs => {
        val tree = treeAndDocs._1
        val docs = treeAndDocs._2
        TreeInputParser.parse(tree, docs, schemaStore, typecheckDependencyStore)
      })
      schemas.map(schema => Left(schema))
    }
    
    // tries schema first, then protocol, then idl, then for case class defs
    val schemaOrProtocols = trySchema(inputString)
    schemaOrProtocols
  }
}

