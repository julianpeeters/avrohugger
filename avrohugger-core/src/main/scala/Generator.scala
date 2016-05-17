package avrohugger

import avrohugger.format.SourceFormat
import avrohugger.input.DependencyInspector
import avrohugger.input.NestedSchemaExtractor
import avrohugger.input.reflectivecompilation.schemagen._
import avrohugger.input.parsers.{ FileInputParser, StringInputParser}

import java.io.{File, FileNotFoundException, IOException}

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Type.ENUM

import scala.collection.JavaConversions._

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
  
  avroScalaCustomTypes.foreach(typeMatcher.updateTypeMap)
  avroScalaCustomNamespace.foreach(typeMatcher.updateNamespaceMap)


  // #### methods for writing definitions out to file ####
  def schemaToFile(
    schema: Schema,
    outDir: String = defaultOutputDir): Unit = {

    val topNS: Option[String] = DependencyInspector.getReferredNamespace(schema)
    val topLevelSchemas: List[Schema] = 
      NestedSchemaExtractor.getNestedSchemas(schema, schemaStore)
    // most-nested classes processed first
    topLevelSchemas.reverse.distinct.foreach { schema =>
      // pass in the top-level schema's namespace if the nested schema has none
      val ns = DependencyInspector.getReferredNamespace(schema) orElse topNS
    //  val schemas: List[Either[Schema, Protocol]] = List(Left(schema))
      format.writeToFile(classStore, ns, Left(schema), outDir, schemaStore)
    }
  }
  
  def protocolToFile(
    protocol: Protocol,
    outDir: String = defaultOutputDir): Unit = {
    val ns = Option(protocol.getNamespace)
    format.writeToFile(classStore, ns, Right(protocol), outDir, schemaStore)
  }

  def stringToFile(
    schemaStr: String,
    outDir: String = defaultOutputDir): Unit = {
    val schemas = stringParser.getSchemas(schemaStr, schemaStore)
    schemas.foreach(schema => schemaToFile(schema, outDir))
  }

  def fileToFile(
    inFile: File,
    outDir: String = defaultOutputDir): Unit = {
    val schemaOrProtocols: List[Either[Schema, Protocol]] =
      fileParser.getSchemaOrProtocols(inFile)
    schemaOrProtocols.foreach(schemaOrProtocol => schemaOrProtocol match {
      case Left(schema) => schemaToFile(schema, outDir)
      case Right(protocol) => protocolToFile(protocol, outDir)
    })
  }


  // #### methods for writing to a list of definitions in String format ####
  def schemaToStrings(schema: Schema): List[String] = {
    val namespace: Option[String] =
      DependencyInspector.getReferredNamespace(schema)
    val topLevelSchemas: List[Schema] = 
      NestedSchemaExtractor.getNestedSchemas(schema, schemaStore)
    //reversed to process nested classes first
    val strings: List[String] = topLevelSchemas.reverse.distinct.map(schema => {
      // pass in the top-level schema's namespace if the nested schema has none
      val ns = DependencyInspector.getReferredNamespace(schema) orElse namespace
    //  val schemas: List[Either[Schema, Protocol]] = List(Left(schema))
      val codeString =
        format.asDefinitionString(classStore, ns, Left(schema), schemaStore)
      // drop the comments because it's not applicable outside of file writing/overwriting
      filterWarningComment(codeString)
    })
    strings
  }
  
  def protocolToStrings(protocol: Protocol): List[String] = {
    val ns: Option[String] = Option(protocol.getNamespace)
    //val protocols: List[Either[Schema, Protocol]] = List(Right(protocol))
    val scalaCodeString =
      format.asDefinitionString(classStore, ns, Right(protocol), schemaStore)
    val javaCodeStrings =
      protocol.getTypes.toList.filterNot(schema => schema.getType == ENUM).flatMap(schemaToStrings)
    val codeStrings = scalaCodeString +: javaCodeStrings
    codeStrings.map(codeString => filterWarningComment(codeString))

    /*
    val namespace: Option[String] = getReferredNamespace(schema)
    val topLevelSchemas: List[Schema] = getNestedSchemas(schema, schemaStore)
    //reversed to process nested classes first
    topLevelSchemas.reverse.distinct.map(schema => {
      // pass in the top-level schema's namespace if the nested schema has none
      val ns = getReferredNamespace(schema) orElse namespace
      val codeString = format.asDefinitionString(classStore, ns, schema, schemaStore)
      // drop the comments because it's not applicable outside of file writing/overwriting
      filterWarningComment(codeString)
    })
    */
  }

  def stringToStrings(schemaStr: String): List[String] = {
    val schemas = stringParser.getSchemas(schemaStr, schemaStore)
    // reverse to restore printing order, top-level classes first
    val codeStrings = schemas.flatMap(schemaToStrings).reverse.distinct
    //reset the schema store after processing the whole submission
    schemaStore.schemas.clear
    codeStrings
  }

  def fileToStrings(inFile: File): List[String] = {
    try {
      val schemaOrProtocols = fileParser.getSchemaOrProtocols(inFile)
      schemaOrProtocols.flatMap(schemaOrProtocol => schemaOrProtocol match {
        case Left(schema) => schemaToStrings(schema)
        case Right(protocol) => protocolToStrings(protocol)
      })
      //val schemas: List[Schema] = fileParser.getSchemas(inFile)
      //schemas.flatMap(schema => schemaToStrings(schema))
    }
    catch {
      case ex: FileNotFoundException => sys.error("File not found:" + ex)
      case ex: IOException => sys.error("There was a problem using the file: " + ex)
    }
  }


  // #### utililty methods ####
  def filterWarningComment(codeStr: String): String = {
    codeStr
      .replace("/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */\n", "")
      .replace("""/**
        | * Autogenerated by Avro
        | *
        | * DO NOT EDIT DIRECTLY
        | */
        |""".stripMargin, "")
  }


}
