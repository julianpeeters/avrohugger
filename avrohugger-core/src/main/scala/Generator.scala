package avrohugger

import avrohugger.format.abstractions.SourceFormat
import avrohugger.format._
import avrohugger.generators.{ FileGenerator, StringGenerator }
import avrohugger.input.parsers.StringInputParser
import avrohugger.matchers.TypeMatcher
import avrohugger.types.AvroScalaTypes
import avrohugger.stores.{ ClassStore, SchemaStore }
import org.apache.avro.{ Protocol, Schema }
import java.io.File

// Unable to overload this class' methods because outDir uses a default value
case class Generator(format: SourceFormat,
  avroScalaCustomTypes: Option[AvroScalaTypes] = None,
  avroScalaCustomNamespace: Map[String, String] = Map.empty,
  restrictedFieldNumber: Boolean = false,
  classLoader: ClassLoader = Thread.currentThread.getContextClassLoader,
  targetScalaPartialVersion: String = avrohugger.internal.ScalaVersion.version) {

  val avroScalaTypes = avroScalaCustomTypes.getOrElse(format.defaultTypes)
  val defaultOutputDir = "target/generated-sources"
  lazy val stringParser = new StringInputParser
  lazy val schemaParser = new Schema.Parser
  val classStore = new ClassStore
  val schemaStore = new SchemaStore
  val fileGenerator = new FileGenerator
  val stringGenerator = new StringGenerator
  val typeMatcher = new TypeMatcher(avroScalaTypes, avroScalaCustomNamespace)

  //////////////// methods for writing definitions out to file /////////////////
  def schemaToFile(
    schema: Schema,
    outDir: String = defaultOutputDir): Unit = {
    fileGenerator.schemaToFile(
      schema, outDir, format, classStore, schemaStore, typeMatcher, restrictedFieldNumber, targetScalaPartialVersion)
  }

  def protocolToFile(
    protocol: Protocol,
    outDir: String = defaultOutputDir): Unit = {
    fileGenerator.protocolToFile(
      protocol,
      outDir,
      format,
      classStore,
      schemaStore,
      typeMatcher,
      restrictedFieldNumber,
      targetScalaPartialVersion)
  }

  def stringToFile(
    schemaStr: String,
    outDir: String = defaultOutputDir): Unit = {
    fileGenerator.stringToFile(
      schemaStr,
      outDir,
      format,
      classStore,
      schemaStore,
      stringParser,
      typeMatcher,
      restrictedFieldNumber,
      targetScalaPartialVersion)
  }

  def fileToFile(
    inFile: File,
    outDir: String = defaultOutputDir): Unit = {
    fileGenerator.fileToFile(
      inFile,
      outDir,
      format,
      classStore,
      schemaStore,
      typeMatcher,
      classLoader,
      restrictedFieldNumber,
      targetScalaPartialVersion)
  }

  def filesToFiles(
    inFiles: List[File],
    outDir: String = defaultOutputDir): Unit = {
    fileGenerator.filesToFiles(
      inFiles,
      outDir,
      format,
      classStore,
      schemaStore,
      typeMatcher,
      classLoader,
      restrictedFieldNumber,
      targetScalaPartialVersion)
  }

  //////// methods for writing to a list of definitions in String format ///////
  def schemaToStrings(schema: Schema): List[String] = {
    stringGenerator.schemaToStrings(
      schema, format, classStore, schemaStore, typeMatcher, restrictedFieldNumber, targetScalaPartialVersion)
  }

  def protocolToStrings(protocol: Protocol): List[String] = {
    stringGenerator.protocolToStrings(
      protocol, format, classStore, schemaStore, typeMatcher, restrictedFieldNumber, targetScalaPartialVersion)
  }

  def stringToStrings(schemaStr: String): List[String] = {
    stringGenerator.stringToStrings(
      schemaStr,
      format,
      classStore,
      schemaStore,
      stringParser,
      typeMatcher,
      restrictedFieldNumber,
      targetScalaPartialVersion)
  }

  def fileToStrings(inFile: File): List[String] = {
    stringGenerator.fileToStrings(
      inFile,
      format,
      classStore,
      schemaStore,
      typeMatcher,
      classLoader,
      restrictedFieldNumber,
      targetScalaPartialVersion)
  }

}
