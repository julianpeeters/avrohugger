package avrohugger

import avrohugger.format.abstractions.SourceFormat
import avrohugger.input.parsers.{ FileInputParser, StringInputParser}
import avrohugger.matchers.TypeMatcher
import avrohugger.stores.{ ClassStore, SchemaStore }

import org.apache.avro.{ Protocol, Schema }

import java.io.File

// Unable to overload this class' methods because outDir uses a default value
class Generator(format: SourceFormat,
  avroScalaCustomTypes: Map[String, Class[_]] = Map.empty,
  avroScalaCustomNamespace: Map[String, String] = Map.empty) {
  
  val sourceFormat = format
  val defaultOutputDir = "target/generated-sources"
  lazy val fileParser = new FileInputParser
  lazy val stringParser = new StringInputParser
  lazy val schemaParser = new Schema.Parser
  val classStore = new ClassStore
  val schemaStore = new SchemaStore
  val typeMatcher = format.typeMatcher
  
  //update potential custom type mapping and/or custom namespace
  avroScalaCustomTypes.foreach(typeMatcher.updateTypeMap)
  avroScalaCustomNamespace.foreach(typeMatcher.updateNamespaceMap)

  //////////////// methods for writing definitions out to file /////////////////
  def schemaToFile(
    schema: Schema,
    outDir: String = defaultOutputDir): Unit = {
    FileGenerator.schemaToFile(schema, outDir, format, classStore, schemaStore)
  }
  
  def protocolToFile(
    protocol: Protocol,
    outDir: String = defaultOutputDir): Unit = {
    FileGenerator.protocolToFile(
      protocol,
      outDir,
      format,
      classStore,
      schemaStore)
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
      stringParser)
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
      fileParser)
  }

  //////// methods for writing to a list of definitions in String format ///////
  def schemaToStrings(schema: Schema): List[String] = {
    StringGenerator.schemaToStrings(schema, format, classStore, schemaStore)
  }
  
  def protocolToStrings(protocol: Protocol): List[String] = {
    StringGenerator.protocolToStrings(protocol, format, classStore, schemaStore)
  }

  def stringToStrings(schemaStr: String): List[String] = {
    StringGenerator.stringToStrings(
      schemaStr,
      format,
      classStore,
      schemaStore,
      stringParser)
  }

  def fileToStrings(inFile: File): List[String] = {
    StringGenerator.fileToStrings(
      inFile,
      format,
      classStore,
      schemaStore,
      fileParser)
  }

}