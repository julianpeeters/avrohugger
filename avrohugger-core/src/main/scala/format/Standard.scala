package avrohugger
package format

import format.abstractions.SourceFormat
import format.standard._
import matchers.TypeMatcher
import matchers.custom.{CustomNamespaceMatcher, CustomTypeMatcher}
import models.CompilationUnit
import stores.{ClassStore, SchemaStore}
import types._
import treehugger.forest._
import definitions.RootClass
import org.apache.avro.{Protocol, Schema}
import org.apache.avro.Schema.Type.{ENUM, FIXED, RECORD}

object Standard extends SourceFormat {

  val toolName = "generate"

  val toolShortDescription = "Generates Scala code for the given schema."

  val javaTreehugger: StandardJavaTreehugger.type = StandardJavaTreehugger
  val scalaTreehugger: StandardScalaTreehugger.type = StandardScalaTreehugger

  def asCompilationUnits(
      classStore: ClassStore,
      ns: Option[String],
      schemaOrProtocol: Either[Schema, Protocol],
      schemaStore: SchemaStore,
      maybeOutDir: Option[String],
      typeMatcher: TypeMatcher,
      restrictedFields: Boolean
  ): List[CompilationUnit] = {
    registerTypes(schemaOrProtocol, classStore, typeMatcher)
    val namespace =
      CustomNamespaceMatcher.checkCustomNamespace(
        ns,
        typeMatcher,
        maybeDefaultNamespace = ns)

    val enumType = typeMatcher.avroScalaTypes.enum

    schemaOrProtocol match {
      case Left(schema) =>
        schema.getType match {
          case RECORD =>
            val scalaCompilationUnit = getScalaCompilationUnit(
              classStore,
              namespace,
              schemaOrProtocol,
              typeMatcher,
              schemaStore,
              maybeOutDir,
              restrictedFields)
            List(scalaCompilationUnit)
          case ENUM =>
            enumType match {
              // java enums can't be represented as trees so they can't be
              // handled by treehugger. Their compilation unit must de generated
              // separately, and they will be excluded from scala compilation
              // units.
              case JavaEnum =>
                val javaCompilationUnit = getJavaEnumCompilationUnit(
                  classStore,
                  namespace,
                  schema,
                  maybeOutDir,
                  typeMatcher)
                List(javaCompilationUnit)
              case ScalaCaseObjectEnum =>
                val scalaCompilationUnit = getScalaCompilationUnit(
                  classStore,
                  namespace,
                  schemaOrProtocol,
                  typeMatcher,
                  schemaStore,
                  maybeOutDir,
                  restrictedFields)
                List(scalaCompilationUnit)
              case ScalaEnumeration =>
                val scalaCompilationUnit = getScalaCompilationUnit(
                  classStore,
                  namespace,
                  schemaOrProtocol,
                  typeMatcher,
                  schemaStore,
                  maybeOutDir,
                  restrictedFields)
                List(scalaCompilationUnit)
              case EnumAsScalaString => List.empty
            }
          case FIXED => List.empty

          case _ => sys.error("Only RECORD, ENUM or FIXED can be toplevel definitions")
        }
      case Right(protocol) => {
        val scalaCompilationUnit = getScalaCompilationUnit(
          classStore,
          namespace,
          Right(protocol),
          typeMatcher,
          schemaStore,
          maybeOutDir,
          restrictedFields)
        enumType match {
          // java enums can't be represented as trees so they can't be handled
          // by treehugger. Their compilation unit must de generated
          // separately, and they will be excluded from scala compilation units.
          case JavaEnum =>
            val localSubtypes = getLocalSubtypes(protocol)
            val localRecords = localSubtypes.filterNot(isEnum)
            val localEnums = localSubtypes.filter(isEnum)
            val javaCompilationUnits = localEnums.map(schema =>
              getJavaEnumCompilationUnit(
                classStore,
                namespace,
                schema,
                maybeOutDir,
                typeMatcher)
            )
            if (localRecords.length >= 1) scalaCompilationUnit +: javaCompilationUnits
            else javaCompilationUnits
          case ScalaCaseObjectEnum => List(scalaCompilationUnit)
          case ScalaEnumeration => List(scalaCompilationUnit)
          case EnumAsScalaString => List(scalaCompilationUnit)
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
      restrictedFields: Boolean
  ): Unit = {
    val compilationUnits: List[CompilationUnit] = asCompilationUnits(
      classStore,
      ns,
      schemaOrProtocol,
      schemaStore,
      Some(outDir),
      typeMatcher,
      restrictedFields)
    compilationUnits.foreach(writeToFile)
  }

  val defaultTypes: AvroScalaTypes = AvroScalaTypes.defaults

  def getName(
      schemaOrProtocol: Either[Schema, Protocol],
      typeMatcher: TypeMatcher): String = {
    schemaOrProtocol match {
      case Left(schema) => schema.getName
      case Right(protocol) => {
        val localSubTypes = typeMatcher.avroScalaTypes.enum match {
          case JavaEnum => getLocalSubtypes(protocol).filterNot(isEnum)
          case ScalaCaseObjectEnum => getLocalSubtypes(protocol)
          case ScalaEnumeration => getLocalSubtypes(protocol)
          case EnumAsScalaString => getLocalSubtypes(protocol).filterNot(isEnum)
        }
        if (localSubTypes.length > 1) protocol.getName // for ADT
        else localSubTypes.headOption match {
          case Some(schema) => schema.getName // for single class defintion
          case None => protocol.getName // default to protocol name
        }
      }
    }
  }

}