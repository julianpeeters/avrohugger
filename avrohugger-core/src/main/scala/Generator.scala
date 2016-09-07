package avrohugger

import avrohugger.format.abstractions.SourceFormat
import avrohugger.format.{ Scavro, SpecificRecord }
import avrohugger.input.parsers.{ FileInputParser, StringInputParser}
import avrohugger.matchers.TypeMatcher
import avrohugger.stores.{ ClassStore, SchemaStore }

import org.apache.avro.{ Protocol, Schema }

import java.io.File

// Unable to overload this class' methods because outDir uses a default value
class Generator(format: SourceFormat,
                avroScalaCustomTypes: Map[String, Class[_]] = Map.empty,
                avroScalaCustomNamespace: Map[String, String] = Map.empty,
                avroScalaCustomEnumStyle: Map[String, String] = Map.empty,
                restrictedFieldNumber: Boolean = false) {

  val sourceFormat = format
  val defaultOutputDir = "target/generated-sources"
  lazy val fileParser = new FileInputParser
  lazy val stringParser = new StringInputParser
  lazy val schemaParser = new Schema.Parser
  val classStore = new ClassStore
  val schemaStore = new SchemaStore
  val typeMatcher = new TypeMatcher

  // update format defaults
  format match {
    case Scavro =>
      typeMatcher.updateCustomTypeMap("array" -> classOf[Array[_]])
    case SpecificRecord =>
      typeMatcher.updateCustomEnumStyleMap("enum" -> "java enum")
    case _ => ()
  }
  //update potential custom type mapping and/or custom namespace
  avroScalaCustomTypes.foreach(typeMatcher.updateCustomTypeMap)
  avroScalaCustomNamespace.foreach(typeMatcher.updateCustomNamespaceMap)
  avroScalaCustomEnumStyle.foreach(typeMatcher.updateCustomEnumStyleMap)

  //////////////// methods for writing definitions out to file /////////////////
  def schemaToFile(
    schema: Schema,
    outDir: String = defaultOutputDir,
    restrictedFields: Boolean = false): Unit = {
    FileGenerator.schemaToFile(
      schema, outDir, format, classStore, schemaStore, typeMatcher, restrictedFields)
  }

  def protocolToFile(
    protocol: Protocol,
    outDir: String = defaultOutputDir/*,
    restrictedFields: Boolean = false*/): Unit = {
    FileGenerator.protocolToFile(
      protocol,
      outDir,
      format,
      classStore,
      schemaStore,
      typeMatcher,
      restrictedFieldNumber)
  }

  def stringToFile(
    schemaStr: String,
    outDir: String = defaultOutputDir/*,
    restrictedFields: Boolean = false*/): Unit = {
    FileGenerator.stringToFile(
      schemaStr,
      outDir,
      format,
      classStore,
      schemaStore,
      stringParser,
      typeMatcher,
      restrictedFieldNumber)
  }

  def fileToFile(
    inFile: File,
    outDir: String = defaultOutputDir/*,
    restrictedFields: Boolean = false*/): Unit = {
    FileGenerator.fileToFile(
      inFile,
      outDir,
      format,
      classStore,
      schemaStore,
      fileParser,
      typeMatcher,
      restrictedFieldNumber)
  }

  //////// methods for writing to a list of definitions in String format ///////
  def schemaToStrings(schema: Schema): List[String] = {
    StringGenerator.schemaToStrings(
      schema, format, classStore, schemaStore, typeMatcher, restrictedFieldNumber)
  }

  def protocolToStrings(protocol: Protocol): List[String] = {
    StringGenerator.protocolToStrings(
      protocol, format, classStore, schemaStore, typeMatcher, restrictedFieldNumber)
  }

  def stringToStrings(schemaStr: String): List[String] = {
    StringGenerator.stringToStrings(
      schemaStr,
      format,
      classStore,
      schemaStore,
      stringParser,
      typeMatcher,
      restrictedFieldNumber)
  }

  def fileToStrings(inFile: File): List[String] = {
    StringGenerator.fileToStrings(
      inFile,
      format,
      classStore,
      schemaStore,
      fileParser,
      typeMatcher,
      restrictedFieldNumber)
  }

}
