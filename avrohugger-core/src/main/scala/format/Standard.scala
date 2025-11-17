package avrohugger
package format

import avrohugger.format.abstractions.SourceFormat
import avrohugger.format.standard._
import avrohugger.matchers.TypeMatcher
import avrohugger.matchers.custom.CustomNamespaceMatcher
import avrohugger.models.CompilationUnit
import avrohugger.stores.ClassStore
import avrohugger.types._
import org.apache.avro.Schema.Type.{ ENUM, FIXED, RECORD }
import org.apache.avro.{ Protocol, Schema }

object Standard extends SourceFormat {

  val toolName = "generate"

  val toolShortDescription = "Generates Scala code for the given schema."

  val javaTreehugger = StandardJavaTreehugger
  val scalaTreehugger = StandardScalaTreehugger

  def asCompilationUnits(
    classStore: ClassStore,
    ns: Option[String],
    schemaOrProtocol: Either[Schema, Protocol],
    maybeOutDir: Option[String],
    typeMatcher: TypeMatcher,
    restrictedFields: Boolean,
    targetScalaPartialVersion: String): List[CompilationUnit] = {
    registerTypes(schemaOrProtocol, classStore, typeMatcher)
    val namespace =
      CustomNamespaceMatcher.checkCustomNamespace(
        ns,
        typeMatcher,
        maybeDefaultNamespace = ns)

    val enumType = typeMatcher.avroScalaTypes.`enum`

    schemaOrProtocol match {
      case Left(schema) => {
        schema.getType match {
          case RECORD => {
            val scalaCompilationUnit = getScalaCompilationUnit(
              classStore,
              namespace,
              schemaOrProtocol,
              typeMatcher,
              maybeOutDir,
              restrictedFields,
              targetScalaPartialVersion)
            List(scalaCompilationUnit)
          }
          case ENUM => {
            enumType match {
              // java enums can't be represented as trees so they can't be
              // handled by treehugger. Their compilation unit must be generated
              // separately, and they will be excluded from scala compilation
              // units.
              case JavaEnum => {
                val javaCompilationUnit = getJavaEnumCompilationUnit(
                  classStore,
                  namespace,
                  schema,
                  maybeOutDir,
                  typeMatcher)
                List(javaCompilationUnit)
              }
              case ScalaCaseObjectEnum => {
                val scalaCompilationUnit = getScalaCompilationUnit(
                  classStore,
                  namespace,
                  schemaOrProtocol,
                  typeMatcher,
                  maybeOutDir,
                  restrictedFields,
                  targetScalaPartialVersion)
                List(scalaCompilationUnit)
              }
              case Scala3Enum => {
                val scalaCompilationUnit = getScalaCompilationUnit(
                  classStore,
                  namespace,
                  schemaOrProtocol,
                  typeMatcher,
                  maybeOutDir,
                  restrictedFields,
                  targetScalaPartialVersion)
                List(scalaCompilationUnit)
              }
              case ScalaEnumeration => {
                val scalaCompilationUnit = getScalaCompilationUnit(
                  classStore,
                  namespace,
                  schemaOrProtocol,
                  typeMatcher,
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
          case FIXED =>
            val scalaCompilationUnit = getScalaCompilationUnit(
              classStore,
              namespace,
              schemaOrProtocol,
              typeMatcher,
              maybeOutDir,
              restrictedFields,
              targetScalaPartialVersion)
            List(scalaCompilationUnit)
          case _ => sys.error("Only FIXED, RECORD, or ENUM can be toplevel definitions")
        }
      }
      case Right(protocol) => {
        val scalaCompilationUnit = getScalaCompilationUnit(
          classStore,
          namespace,
          Right(protocol),
          typeMatcher,
          maybeOutDir,
          restrictedFields,
          targetScalaPartialVersion)
        enumType match {
          // java enums can't be represented as trees so they can't be handled
          // by treehugger. Their compilation unit must be generated
          // separately, and they will be excluded from scala compilation units.
          case JavaEnum => {
            val localSubtypes = getLocalSubtypes(protocol)
            val localRecords = localSubtypes.filterNot(isEnum)
            val localEnums = localSubtypes.filter(isEnum)
            val javaCompilationUnits = localEnums.map(schema => {
              getJavaEnumCompilationUnit(
                classStore,
                namespace,
                schema,
                maybeOutDir,
                typeMatcher)
            })
            if (localRecords.nonEmpty) scalaCompilationUnit +: javaCompilationUnits
            else javaCompilationUnits
          }
          case ScalaCaseObjectEnum => List(scalaCompilationUnit)
          case Scala3Enum => List(scalaCompilationUnit)
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
    typeMatcher: TypeMatcher,
    restrictedFields: Boolean,
    targetScalaPartialVersion: String): Unit = {
    val compilationUnits: List[CompilationUnit] = asCompilationUnits(
      classStore,
      ns,
      schemaOrProtocol,
      Some(outDir),
      typeMatcher,
      restrictedFields,
      targetScalaPartialVersion)
    compilationUnits.foreach(writeToFile)
  }

  val defaultTypes: AvroScalaTypes = AvroScalaTypes.defaults

  def getName(
    schemaOrProtocol: Either[Schema, Protocol],
    typeMatcher: TypeMatcher): String = {
    schemaOrProtocol match {
      case Left(schema) => schema.getName
      case Right(protocol) => {
        val localSubTypes = typeMatcher.avroScalaTypes.`enum` match {
          case JavaEnum => getLocalSubtypes(protocol).filterNot(isEnum)
          case ScalaCaseObjectEnum => getLocalSubtypes(protocol)
          case Scala3Enum => getLocalSubtypes(protocol)
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
