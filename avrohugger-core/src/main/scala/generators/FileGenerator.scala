package avrohugger
package generators

import avrohugger.format.abstractions.SourceFormat
import avrohugger.input.DependencyInspector
import avrohugger.input.NestedSchemaExtractor
// import avrohugger.input.reflectivecompilation.schemagen._
import avrohugger.input.parsers.{ FileInputParser, StringInputParser}
import avrohugger.matchers.TypeMatcher
import avrohugger.stores.{ ClassStore, SchemaStore }

import java.io.{File, FileNotFoundException, IOException}

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Type.ENUM

// Unable to overload this class' methods because outDir uses a default value
private[avrohugger] class FileGenerator() {


  def schemaToFile(
    schema: Schema,
    outDir: String,
    format: SourceFormat,
    classStore: ClassStore,
    schemaStore: SchemaStore,
    typeMatcher: TypeMatcher,
    restrictedFields: Boolean,
    targetScalaPartialVersion: String): Unit = {
    println(s"schemaToFile ${schema.getNamespace}.${schema.getName}")
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
    println(s"protocolToFile ${protocol.getNamespace}.${protocol.getName}")
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
    println(s"stringToFile ${str}")
    val schemaOrProtocols = stringParser.getSchemaOrProtocols(str, schemaStore)
    schemaOrProtocols.foreach{
      case Left(schema) =>
        schemaToFile(schema, outDir, format, classStore, schemaStore, typeMatcher, restrictedFields, targetScalaPartialVersion)
      case Right(protocol) =>
        protocolToFile(protocol, outDir, format, classStore, schemaStore, typeMatcher, restrictedFields, targetScalaPartialVersion)
    }
  }

  var processedSchemas: Set[String] = Set.empty
  var processedProtocols: Set[String] = Set.empty

  def clear() = {
    processedSchemas = Set.empty
    processedProtocols = Set.empty
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
    println(s"fileToFile3 ${inFile.getAbsolutePath}")
    fileParser.getSchemaOrProtocols(inFile, format, classStore, classLoader) // this is slow
      .foreach{
        case Left(schema) =>
          val name = schema.getNamespace + "." + schema.getName
          if (!processedSchemas.contains(name)) {
            schemaToFile(schema, outDir, format, classStore, schemaStore, typeMatcher, restrictedFields, targetScalaPartialVersion)
            processedSchemas += name
          }
        case Right(protocol) =>
          val name = protocol.getNamespace + "." + protocol.getName
          if (!processedProtocols.contains(name)) {
            protocolToFile(protocol, outDir, format, classStore, schemaStore, typeMatcher, restrictedFields, targetScalaPartialVersion)
            processedProtocols += name
          }
      }
  }

}
