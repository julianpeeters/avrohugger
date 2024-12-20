package avrohugger
package generators

import avrohugger.format.abstractions.SourceFormat
import avrohugger.input.{ DependencyInspector, NestedSchemaExtractor }
import avrohugger.input.parsers.{ FileInputParser, StringInputParser }
import avrohugger.matchers.TypeMatcher
import avrohugger.stores.{ ClassStore, SchemaStore }
import org.apache.avro.{ Protocol, Schema }

import java.io.File

// Unable to overload this class' methods because outDir uses a default value
private[avrohugger] class FileGenerator {

  def schemaToFile(
    schema: Schema,
    outDir: String,
    format: SourceFormat,
    classStore: ClassStore,
    schemaStore: SchemaStore,
    typeMatcher: TypeMatcher,
    restrictedFields: Boolean,
    targetScalaPartialVersion: String): Unit = {
    val topNS: Option[String] = DependencyInspector.getReferredNamespace(schema)
    val topLevelSchemas: List[Schema] =
      NestedSchemaExtractor.getNestedSchemas(schema, schemaStore, typeMatcher)
    // most-nested classes processed first
    topLevelSchemas.reverse.distinct.foreach(schema => {
      // pass in the top-level schema's namespace if the nested schema has none
      val ns = DependencyInspector.getReferredNamespace(schema) orElse topNS
      format.compile(classStore, ns, Left(schema), outDir, schemaStore, typeMatcher, restrictedFields, targetScalaPartialVersion)
    })
  }

  def protocolToFile(
    protocol: Protocol,
    outDir: String,
    format: SourceFormat,
    classStore: ClassStore,
    schemaStore: SchemaStore,
    typeMatcher: TypeMatcher,
    restrictedFields: Boolean,
    targetScalaPartialVersion: String): Unit = {
    val ns = Option(protocol.getNamespace)
    format.compile(classStore, ns, Right(protocol), outDir, schemaStore, typeMatcher, restrictedFields, targetScalaPartialVersion)
  }

  def stringToFile(
    str: String,
    outDir: String,
    format: SourceFormat,
    classStore: ClassStore,
    schemaStore: SchemaStore,
    stringParser: StringInputParser,
    typeMatcher: TypeMatcher,
    restrictedFields: Boolean,
    targetScalaPartialVersion: String): Unit = {
    val schemaOrProtocols = stringParser.getSchemaOrProtocols(str, schemaStore).distinct
    schemaOrProtocols.foreach {
      case Left(schema) =>
        schemaToFile(schema, outDir, format, classStore, schemaStore, typeMatcher, restrictedFields, targetScalaPartialVersion)
      case Right(protocol) =>
        protocolToFile(protocol, outDir, format, classStore, schemaStore, typeMatcher, restrictedFields, targetScalaPartialVersion)
    }
  }

  def fileToFile(
    inFile: File,
    outDir: String,
    format: SourceFormat,
    classStore: ClassStore,
    schemaStore: SchemaStore,
    fileParser: FileInputParser,
    typeMatcher: TypeMatcher,
    classLoader: ClassLoader,
    restrictedFields: Boolean,
    targetScalaPartialVersion: String): Unit = {
    fileParser.getSchemaOrProtocols(inFile, format, classStore, classLoader)
      .distinct
      .foreach {
        case Left(schema) =>
          schemaToFile(schema, outDir, format, classStore, schemaStore, typeMatcher, restrictedFields, targetScalaPartialVersion)
        case Right(protocol) =>
          protocolToFile(protocol, outDir, format, classStore, schemaStore, typeMatcher, restrictedFields, targetScalaPartialVersion)
      }
  }

  def filesToFiles(
    inFiles: List[File],
    outDir: String,
    format: SourceFormat,
    classStore: ClassStore,
    schemaStore: SchemaStore,
    fileParser: FileInputParser,
    typeMatcher: TypeMatcher,
    classLoader: ClassLoader,
    restrictedFields: Boolean,
    targetScalaPartialVersion: String): Unit = {
    inFiles.flatMap(fileParser.getSchemaOrProtocols(_, format, classStore, classLoader))
      .distinct
      .foreach {
        case Left(schema) =>
          schemaToFile(schema, outDir, format, classStore, schemaStore, typeMatcher, restrictedFields, targetScalaPartialVersion)
        case Right(protocol) =>
          protocolToFile(protocol, outDir, format, classStore, schemaStore, typeMatcher, restrictedFields, targetScalaPartialVersion)
      }
  }

}
