package avrohugger
package generators

import avrohugger.format.abstractions.SourceFormat
import avrohugger.input.parsers.{ FileInputParser, StringInputParser }
import avrohugger.input.{ DependencyInspector, NestedSchemaExtractor }
import avrohugger.matchers.TypeMatcher
import avrohugger.stores.ClassStore
import org.apache.avro.Schema.Parser
import org.apache.avro.{ Protocol, Schema }

import java.io.File
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }

// Unable to overload this class' methods because outDir uses a default value
private[avrohugger] class FileGenerator {

  def schemaToFile(
    schema: Schema,
    outDir: String,
    format: SourceFormat,
    classStore: ClassStore,
    typeMatcher: TypeMatcher,
    restrictedFields: Boolean,
    targetScalaPartialVersion: String): Unit = {
    val topNS: Option[String] = DependencyInspector.getReferredNamespace(schema)
    val topLevelSchemas: List[Schema] = NestedSchemaExtractor.getNestedSchemas(schema, typeMatcher)
    topLevelSchemas.distinct.foreach(schema => {
      // pass in the top-level schema's namespace if the nested schema has none
      val ns = DependencyInspector.getReferredNamespace(schema) orElse topNS
      format.compile(classStore, ns, Left(schema), outDir, typeMatcher, restrictedFields, targetScalaPartialVersion)
    })
  }

  def protocolToFile(
    protocol: Protocol,
    outDir: String,
    format: SourceFormat,
    classStore: ClassStore,
    typeMatcher: TypeMatcher,
    restrictedFields: Boolean,
    targetScalaPartialVersion: String): Unit = {
    val ns = Option(protocol.getNamespace)
    format.compile(classStore, ns, Right(protocol), outDir, typeMatcher, restrictedFields, targetScalaPartialVersion)
  }

  def stringToFile(
    str: String,
    outDir: String,
    format: SourceFormat,
    classStore: ClassStore,
    stringParser: StringInputParser,
    typeMatcher: TypeMatcher,
    restrictedFields: Boolean,
    targetScalaPartialVersion: String): Unit = {
    distinctSchemaOrProtocol(stringParser.getSchemaOrProtocols(str))
      .foreach {
        case Left(schema) =>
          schemaToFile(schema, outDir, format, classStore, typeMatcher, restrictedFields, targetScalaPartialVersion)
        case Right(protocol) =>
          protocolToFile(protocol, outDir, format, classStore, typeMatcher, restrictedFields, targetScalaPartialVersion)
      }
  }

  def fileToFile(
    inFile: File,
    outDir: String,
    format: SourceFormat,
    classStore: ClassStore,
    fileParser: FileInputParser,
    schemaParser: Parser,
    typeMatcher: TypeMatcher,
    classLoader: ClassLoader,
    restrictedFields: Boolean,
    targetScalaPartialVersion: String): Unit = {
    distinctSchemaOrProtocol(Await.result(fileParser.getSchemaOrProtocols(inFile, format, classStore, classLoader, schemaParser), Duration.Inf))
      .foreach {
        case Left(schema) =>
          schemaToFile(schema, outDir, format, classStore, typeMatcher, restrictedFields, targetScalaPartialVersion)
        case Right(protocol) =>
          protocolToFile(protocol, outDir, format, classStore, typeMatcher, restrictedFields, targetScalaPartialVersion)
      }
  }

  def filesToFiles(
    inFiles: List[File],
    outDir: String,
    format: SourceFormat,
    classStore: ClassStore,
    fileParser: FileInputParser,
    schemaParser: Parser,
    typeMatcher: TypeMatcher,
    classLoader: ClassLoader,
    restrictedFields: Boolean,
    targetScalaPartialVersion: String): Unit = {
    val f = inFiles.map(fileParser.getSchemaOrProtocols(_, format, classStore, classLoader, schemaParser))
    val res = Future.sequence(f).map(x => distinctSchemaOrProtocol(x.reduce(_ ::: _))).map(_.foreach {
      case Left(schema) =>
        schemaToFile(schema, outDir, format, classStore, typeMatcher, restrictedFields, targetScalaPartialVersion)
      case Right(protocol) =>
        protocolToFile(protocol, outDir, format, classStore, typeMatcher, restrictedFields, targetScalaPartialVersion)
    })
    Await.result(res, Duration.Inf)
  }

  private def distinctSchemaOrProtocol(schemaOrProtocols: List[Either[Schema, Protocol]]): List[Either[Schema, Protocol]] = {
    var processed = Set.empty[String]

    schemaOrProtocols.flatMap {
      case Left(schema) =>
        if (!processed.contains(schema.getFullName)) {
          processed += schema.getFullName
          Some(Left(schema))
        } else {
          None
        }
      case Right(protocol) =>
        val fullName = Option(protocol.getNamespace).map(ns => s"$ns.${protocol.getName}").getOrElse(protocol.getName)
        if (!processed.contains(fullName)) {
          processed += fullName
          Some(Right(protocol))
        } else {
          None
        }
    }
  }

}
