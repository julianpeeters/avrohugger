package avrohugger

import avrohugger.format.abstractions.SourceFormat
import avrohugger.format._
import avrohugger.generators.{FileGenerator, StringGenerator}
import avrohugger.input.parsers.{FileInputParser, StringInputParser}
import avrohugger.matchers.TypeMatcher
import avrohugger.types.AvroScalaTypes
import avrohugger.stores.{ClassStore, SchemaStore}
import org.apache.avro.{Protocol, Schema}
import java.io.File

// Unable to overload this class' methods because outDir uses a default value
case class Generator(format: SourceFormat,
                     avroScalaCustomTypes: Option[AvroScalaTypes] = None,
                     avroScalaCustomNamespace: Map[String, String] = Map.empty,
                     restrictedFieldNumber: Boolean = false,
                     classLoader: ClassLoader = Thread.currentThread.getContextClassLoader) {

  val avroScalaTypes = avroScalaCustomTypes.getOrElse(format.defaultTypes)
  val defaultOutputDir = "target/generated-sources"
  lazy val fileParser = new FileInputParser
  lazy val stringParser = new StringInputParser
  lazy val schemaParser = new Schema.Parser
  val classStore = new ClassStore
  val schemaStore = new SchemaStore
  val typeMatcher = new TypeMatcher(avroScalaTypes, avroScalaCustomNamespace)

  //////////////// methods for writing definitions out to file /////////////////
  def schemaToFile(
    schema: Schema,
    outDir: String = defaultOutputDir): Unit = {
    FileGenerator.schemaToFile(
      schema, outDir, format, classStore, schemaStore, typeMatcher, restrictedFieldNumber)
  }

  def protocolToFile(
    protocol: Protocol,
    outDir: String = defaultOutputDir): Unit = {
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
    outDir: String = defaultOutputDir): Unit = {
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
    outDir: String = defaultOutputDir): Unit = {
    FileGenerator.fileToFile(
      inFile,
      outDir,
      format,
      classStore,
      schemaStore,
      fileParser,
      typeMatcher,
      classLoader,
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
      classLoader,
      restrictedFieldNumber)
  }

}
