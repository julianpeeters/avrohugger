package avrohugger
package format

import abstractions.SourceFormat
import format.specific.{SpecificJavaTreehugger, SpecificScalaTreehugger}
import matchers.TypeMatcher
import matchers.custom.CustomNamespaceMatcher
import models.CompilationUnit
import stores.{ ClassStore, SchemaStore }
import types._


import treehugger.forest._
import definitions.RootClass

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Type.{ ENUM, FIXED, RECORD }

import java.nio.file.Path

object SpecificRecord extends SourceFormat{

  val toolName = "generate-specific"

  val toolShortDescription = "Generates Scala code extending SpecificRecordBase."
  
  val javaTreehugger = SpecificJavaTreehugger
  val scalaTreehugger = SpecificScalaTreehugger
  
  val defaultTypes: AvroScalaTypes = AvroScalaTypes.defaults.copy(`enum` = JavaEnum)

  def asCompilationUnits(
    classStore: ClassStore,
    ns: Option[String],
    schemaOrProtocol: Either[Schema, Protocol],
    schemaStore: SchemaStore,
    maybeOutDir: Option[String],
    typeMatcher: TypeMatcher,
    restrictedFields: Boolean,
    targetScalaPartialVersion: String): List[CompilationUnit] = {

    registerTypes(schemaOrProtocol, classStore, typeMatcher)
    val enumType = typeMatcher.avroScalaTypes.`enum`

    val namespace = 
      CustomNamespaceMatcher.checkCustomNamespace(
        ns,
        typeMatcher,
        maybeDefaultNamespace = ns)

    // generate as RPC trait and separate class/enum strings
    def protocolToRPC(protocol: Protocol): List[CompilationUnit] = {
      val localSubtypes = getLocalSubtypes(protocol)
      val localEnums = localSubtypes.filter(isEnum)
      val localNonEnums = localSubtypes.filterNot(isEnum)
      val maybePath = getFilePath(
        namespace,
        Right(protocol),
        maybeOutDir,
        typeMatcher)
      val rpcTraitString = scalaTreehugger.asScalaCodeString(
        classStore,
        namespace,
        Right(protocol),
        typeMatcher,
        schemaStore,
        restrictedFields,
        targetScalaPartialVersion)
      val rpcTraitCompUnit = CompilationUnit(maybePath, rpcTraitString)
      val scalaCompUnits = localNonEnums.map(schema => {
        val scalaCompilationUnit = getScalaCompilationUnit(
          classStore,
          namespace,
          Left(schema),
          typeMatcher,
          schemaStore,
          maybeOutDir,
          restrictedFields,
          targetScalaPartialVersion)
        scalaCompilationUnit
      })
      val javaCompUnits = localEnums.map(schema => {
        getJavaEnumCompilationUnit(
        classStore,
        namespace,
        schema,
        maybeOutDir,
        typeMatcher)
      })
      val rpcTypeCompUnits = scalaCompUnits ::: javaCompUnits
      rpcTraitCompUnit +: rpcTypeCompUnits
    }

    schemaOrProtocol match {
      case Left(schema) => {
        schema.getType match {
          case RECORD => {
            val scalaCompilationUnit = getScalaCompilationUnit(
              classStore,
              namespace,
              schemaOrProtocol,
              typeMatcher,
              schemaStore,
              maybeOutDir,
              restrictedFields,
              targetScalaPartialVersion)
            List(scalaCompilationUnit)
          }
          case ENUM => {
            enumType match {
              case JavaEnum =>
                // SpecificRecord requires java enums as of Avro 1.7.7; hard-coded
                val javaCompilationUnit = getJavaEnumCompilationUnit(
                  classStore,
                  namespace,
                  schema,
                  maybeOutDir,
                  typeMatcher)
                List(javaCompilationUnit)
              case EnumAsScalaString =>
                List.empty
              case _ =>
                sys.error("Only JavaEnum and EnumAsScalaString are supported for SpecificRecord format")
            }
          }
          case FIXED => {
            val scalaCompilationUnit = getScalaCompilationUnit(
              classStore,
              namespace,
              schemaOrProtocol,
              typeMatcher,
              schemaStore,
              maybeOutDir,
              restrictedFields,
              targetScalaPartialVersion)
            List(scalaCompilationUnit)
          }
          case _ => sys.error("Only FIXED, RECORD, or ENUM can be toplevel definitions")
        }
      }
      case Right(protocol) => {
        // SpecificRecord requires java enums as of Avro 1.7.7, thus hard-coded
        val messages = protocol.getMessages
        if (messages.isEmpty) {
          val localSubtypes = enumType match {
            case JavaEnum => getLocalSubtypes(protocol).filterNot(isEnum)
            case ScalaCaseObjectEnum => List.empty
            case ScalaEnumeration => List.empty
            case EnumAsScalaString => getLocalSubtypes(protocol).filterNot(isEnum)
          }
          val localRecords = localSubtypes.filterNot(isEnum)
          val localEnums = enumType match {
            case JavaEnum => getLocalSubtypes(protocol).filter(isEnum)
            case ScalaCaseObjectEnum => List.empty
            case ScalaEnumeration => List.empty
            case EnumAsScalaString => List.empty
          }
          val javaCompilationUnits = localEnums.map(schema => {
            getJavaEnumCompilationUnit(
              classStore,
              namespace,
              schema,
              maybeOutDir,
              typeMatcher)
          })
          val scalaCompilationUnit = getScalaCompilationUnit(
            classStore,
            namespace,
            Right(protocol),
            typeMatcher,
            schemaStore,
            maybeOutDir,
            restrictedFields,
            targetScalaPartialVersion)
          if (localRecords.length >= 1) scalaCompilationUnit +: javaCompilationUnits
          else javaCompilationUnits
        }
        else protocolToRPC(protocol)
      }
    }
  }

  def getName(
    schemaOrProtocol: Either[Schema, Protocol],
    typeMatcher: TypeMatcher): String = {
    schemaOrProtocol match {
      case Left(schema) => schema.getName
      case Right(protocol) => {
        def isEnum(schema: Schema) = schema.getType == Schema.Type.ENUM
        val messages = protocol.getMessages
        if (!messages.isEmpty) protocol.getName // for RPC trait
        else {
          val localRecords = getLocalSubtypes(protocol).filterNot(isEnum)
          if (localRecords.length > 1) protocol.getName // for ADT
          else localRecords.headOption match {
            case Some(schema) => schema.getName // for solo records make a class
            case None => protocol.getName       // default to protocol name
          }
        }
      }
    }
  }

  def compile(
    classStore: ClassStore,
    ns: Option[String],
    schemaOrProtocol: Either[Schema, Protocol],
    outDir: String,
    schemaStore: SchemaStore,
    typeMatcher: TypeMatcher,
    restrictedFields: Boolean,
    targetScalaPartialVersion: String): Unit = {
    val compilationUnits: List[CompilationUnit] = asCompilationUnits(
      classStore,
      ns,
      schemaOrProtocol,
      schemaStore,
      Some(outDir),
      typeMatcher,
      restrictedFields,
      targetScalaPartialVersion)
    compilationUnits.foreach(writeToFile)
  }

}
