package avrohugger
package format

import format.abstractions.SourceFormat
import format.scavro.{
  ScavroNamespaceRenamer,
  ScavroJavaTreehugger,
  ScavroScalaTreehugger
}
import matchers.TypeMatcher
import models.CompilationUnit
import stores.{ ClassStore, SchemaStore }
import types._

import treehugger.forest._
import definitions.RootClass

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Type.{ ENUM, RECORD }

object Scavro extends SourceFormat {

  val toolName = "generate-scavro"
  val toolShortDescription = "Generates Scala wrapper code for the given schema."
  
  val javaTreehugger = ScavroJavaTreehugger

  val scalaTreehugger = ScavroScalaTreehugger
  
  val defaultTypes: AvroScalaTypes = AvroScalaTypes.defaults.copy(array = ScalaArray)

  @deprecated("Scavro will no longer be supported", "avrohugger 1.5.0")
  def asCompilationUnits(
    classStore: ClassStore,
    maybeNamespace: Option[String],
    schemaOrProtocol: Either[Schema, Protocol],
    schemaStore: SchemaStore,
    maybeOutDir: Option[String],
    typeMatcher: TypeMatcher,
    restrictedFields: Boolean,
    targetScalaPartialVersion: String): List[CompilationUnit] = {

    registerTypes(schemaOrProtocol, classStore, typeMatcher)

    // By default, Scavro format generates Scala classes in packages that are 
    // the same as the Java package with `model` appended.
    val maybeScavroModelNamespace: Option[String] =
      ScavroNamespaceRenamer.renameNamespace(
        maybeNamespace,
        schemaOrProtocol,
        typeMatcher)

    val enumType = typeMatcher.avroScalaTypes.enum

    schemaOrProtocol match {
      case Left(schema) => {
        schema.getType match {
          case RECORD => {
            val scalaCompilationUnit = getScalaCompilationUnit(
              classStore,
              maybeScavroModelNamespace,
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
              // java enums can't be represented as trees so they can't be
              // handled by treehugger. Their compilation unit must de generated
              // separately, and they will be excluded from scala compilation
              // units.
              case JavaEnum => {
                val javaCompilationUnit = getJavaEnumCompilationUnit(
                  classStore,
                  maybeScavroModelNamespace,
                  schema,
                  maybeOutDir,
                  typeMatcher)
                List(javaCompilationUnit)
              }
              case ScalaEnumeration => {
                val scalaCompilationUnit = getScalaCompilationUnit(
                  classStore,
                  maybeScavroModelNamespace,
                  schemaOrProtocol,
                  typeMatcher,
                  schemaStore,
                  maybeOutDir,
                  restrictedFields,
                  targetScalaPartialVersion)
                List(scalaCompilationUnit)
              }
              case ScalaCaseObjectEnum => {
                val scalaCompilationUnit = getScalaCompilationUnit(
                  classStore,
                  maybeScavroModelNamespace,
                  schemaOrProtocol,
                  typeMatcher,
                  schemaStore,
                  maybeOutDir,
                  restrictedFields,
                  targetScalaPartialVersion)
                List(scalaCompilationUnit)
              }
              case EnumAsScalaString => {
                List.empty
              }
            }

          }
          case _ => sys.error("Only RECORD or ENUM can be toplevel definitions")
        }
      }
      case Right(protocol) => {
        val scalaCompilationUnit = getScalaCompilationUnit(
          classStore,
          maybeScavroModelNamespace,
          Right(protocol),
          typeMatcher,
          schemaStore,
          maybeOutDir,
          restrictedFields,
          targetScalaPartialVersion)
        enumType match {
          // java enums can't be represented as trees so they can't be
          // handled by treehugger. Their compilation unit must de generated
          // separately, and they will be excluded from scala compilation
          // units.
          case JavaEnum => {
            val localSubtypes = getLocalSubtypes(protocol)
            val localRecords = localSubtypes.filterNot(isEnum)
            val localEnums = localSubtypes.filter(isEnum)
            val javaCompilationUnits = localEnums.map(schema => {
              getJavaEnumCompilationUnit(
                classStore,
                maybeScavroModelNamespace,
                schema,
                maybeOutDir,
                typeMatcher)
            })
            if (localRecords.length >= 1) scalaCompilationUnit +: javaCompilationUnits
            else javaCompilationUnits
          }
          case ScalaCaseObjectEnum => List(scalaCompilationUnit)
          case ScalaEnumeration    => List(scalaCompilationUnit)
          case EnumAsScalaString   => List(scalaCompilationUnit)
        }
      }
    }
  }

  def getName(
    schemaOrProtocol: Either[Schema, Protocol],
    typeMatcher: TypeMatcher): String = {
    schemaOrProtocol match {
      case Left(schema) => schema.getName
      case Right(protocol) => {
        val enumType = typeMatcher.avroScalaTypes.enum
        val localSubTypes = enumType match {
          case JavaEnum => getLocalSubtypes(protocol).filterNot(isEnum)
          case ScalaCaseObjectEnum => getLocalSubtypes(protocol)
          case ScalaEnumeration => getLocalSubtypes(protocol)
          case EnumAsScalaString => getLocalSubtypes(protocol).filterNot(isEnum)
        }
        if (localSubTypes.length > 1) protocol.getName
        else localSubTypes.headOption match {
          case Some(schema) => schema.getName
          case None => protocol.getName
        }
      }
    }
  }

  @deprecated("Scavro will no longer be supported", "avrohugger 1.5.0")
  def compile(
    classStore: ClassStore,
    maybeNamespace: Option[String],
    schemaOrProtocol: Either[Schema, Protocol],
    outDir: String,
    schemaStore: SchemaStore,
    typeMatcher: TypeMatcher,
    restrictedFields: Boolean,
    targetScalaPartialVersion: String): Unit = {
    val compilationUnits: List[CompilationUnit] = asCompilationUnits(
      classStore,
      maybeNamespace,
      schemaOrProtocol,
      schemaStore,
      Some(outDir),
      typeMatcher,
      restrictedFields,
      targetScalaPartialVersion)
    compilationUnits.foreach(writeToFile)
  }

}
