package avrohugger
package format

import format.abstractions.SourceFormat
import format.scavro.{ ScavroNamespaceRenamer, ScavroScalaTreehugger }
import matchers.TypeMatcher
import models.CompilationUnit
import stores.{ ClassStore, SchemaStore }

import treehugger.forest._
import definitions.RootClass

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Type.{ ENUM, RECORD }

object Scavro extends SourceFormat {

  val toolName = "generate-scavro"
  val toolShortDescription = "Generates Scala wrapper code for the given schema."

  val scalaTreehugger = ScavroScalaTreehugger

  def asCompilationUnits(
    classStore: ClassStore,
    maybeNamespace: Option[String],
    schemaOrProtocol: Either[Schema, Protocol],
    schemaStore: SchemaStore,
    maybeOutDir: Option[String],
    typeMatcher: TypeMatcher,
    restrictedFields: Boolean): List[CompilationUnit] = {

    registerTypes(schemaOrProtocol, classStore, typeMatcher)

    // By default, Scavro format generates Scala classes in packages that are 
    // the same as the Java package with `model` appended.
    val maybeScavroModelNamespace: Option[String] =
      ScavroNamespaceRenamer.renameNamespace(
        maybeNamespace,
        schemaOrProtocol,
        typeMatcher)

    def maybeCustomEnumStyle = typeMatcher.customEnumStyleMap.get("enum")

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
              restrictedFields)
            List(scalaCompilationUnit)
          }
          case ENUM => {
            maybeCustomEnumStyle match {
              // java enums can't be represented as trees so they can't be
              // handled by treehugger. Their compilation unit must de generated
              // separately, and they will be excluded from scala compilation
              // units.
              case Some("java enum") => {
                val javaCompilationUnit = getJavaEnumCompilationUnit(
                  classStore,
                  maybeScavroModelNamespace,
                  schema,
                  maybeOutDir,
                  typeMatcher)
                List(javaCompilationUnit)
              }
              case _ => {
                val scalaCompilationUnit = getScalaCompilationUnit(
                  classStore,
                  maybeScavroModelNamespace,
                  schemaOrProtocol,
                  typeMatcher,
                  schemaStore,
                  maybeOutDir,
                  restrictedFields)
                List(scalaCompilationUnit)
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
          restrictedFields)
        maybeCustomEnumStyle match {
          // java enums can't be represented as trees so they can't be
          // handled by treehugger. Their compilation unit must de generated
          // separately, and they will be excluded from scala compilation
          // units.
          case Some("java enum") => {
            val localSubtypes = getLocalSubtypes(protocol)
            val localEnums = localSubtypes.filter(isEnum)
            val javaCompilationUnits = localEnums.map(schema => {
              getJavaEnumCompilationUnit(
                classStore,
                maybeScavroModelNamespace,
                schema,
                maybeOutDir,
                typeMatcher)
            })
            scalaCompilationUnit +: javaCompilationUnits
          }
          case _ => List(scalaCompilationUnit)
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
        val maybeCustomEnumStyle = typeMatcher.customEnumStyleMap.get("enum")
        val localSubTypes = maybeCustomEnumStyle match {
          case Some("java enum") => getLocalSubtypes(protocol).filterNot(isEnum)
          case _ => getLocalSubtypes(protocol)
        }
        if (localSubTypes.length > 1) protocol.getName
        else localSubTypes.headOption match {
          case Some(schema) => schema.getName
          case None => protocol.getName
        }
      }
    }
  }

  def compile(
    classStore: ClassStore,
    maybeNamespace: Option[String],
    schemaOrProtocol: Either[Schema, Protocol],
    outDir: String,
    schemaStore: SchemaStore,
    typeMatcher: TypeMatcher,
    restrictedFields: Boolean): Unit = {
    val compilationUnits: List[CompilationUnit] = asCompilationUnits(
      classStore,
      maybeNamespace,
      schemaOrProtocol,
      schemaStore,
      Some(outDir),
      typeMatcher,
      restrictedFields)
    compilationUnits.foreach(writeToFile)
  }

}
