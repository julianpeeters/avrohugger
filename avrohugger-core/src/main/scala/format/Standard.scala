package avrohugger
package format

import format.abstractions.SourceFormat
import format.standard._
import models.CompilationUnit
import stores.{ ClassStore, SchemaStore }
import matchers.{ CustomNamespaceMatcher, CustomTypeMatcher, TypeMatcher }

import treehugger.forest._
import definitions.RootClass

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Type.{ ENUM, RECORD }

import scala.collection.JavaConversions._

object Standard extends SourceFormat {

  val toolName = "generate"
  
  val toolShortDescription = "Generates Scala code for the given schema."

  val scalaTreehugger = StandardScalaTreehugger
  
  def asCompilationUnits(
    classStore: ClassStore, 
    ns: Option[String], 
    schemaOrProtocol: Either[Schema, Protocol],
    schemaStore: SchemaStore,
    maybeOutDir: Option[String],
    typeMatcher: TypeMatcher): List[CompilationUnit] = {
      
    registerTypes(schemaOrProtocol, classStore, typeMatcher)

    val maybeCustom = ns match {
      case Some(schemaNS) => typeMatcher.customNamespaceMap.get(schemaNS)
      case None => ns
    }
    val namespace = CustomNamespaceMatcher.checkCustomNamespace(maybeCustom, ns)
    
    def maybeCustomEnumStyle = typeMatcher.customEnumStyleMap.get("enum")

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
              maybeOutDir)
            List(scalaCompilationUnit)
          }
          case ENUM => {
            maybeCustomEnumStyle match {
              case Some("java enum") => {
                val javaCompilationUnit = getJavaEnumCompilationUnit(
                  classStore,
                  namespace,
                  schema,
                  maybeOutDir,
                  typeMatcher)
                List(javaCompilationUnit)
              }
              case _ => {
                val scalaCompilationUnit = getScalaCompilationUnit(
                  classStore,
                  namespace,
                  schemaOrProtocol,
                  typeMatcher,
                  schemaStore,
                  maybeOutDir)
                List(scalaCompilationUnit)
              }
            }
            
          }
          case _ => sys.error("Only RECORD or ENUM can be toplevel definitions")
        }
      }
      case Right(protocol) => {
        val scalaADTorSoloClassCompilationUnit = getScalaCompilationUnit(
          classStore,
          namespace,
          Right(protocol),
          typeMatcher,
          schemaStore,
          maybeOutDir)
        maybeCustomEnumStyle match {
          case Some("java enum") => {
            val localSubtypes = getLocalSubtypes(protocol)
            val localEnums = localSubtypes.filter(isEnum)
            val javaCompilationUnits = localEnums.map(schema => {
              getJavaEnumCompilationUnit(
                classStore,
                namespace,
                schema,
                maybeOutDir,
                typeMatcher)
            })            
            scalaADTorSoloClassCompilationUnit +: javaCompilationUnits
          }
          case _ => List(scalaADTorSoloClassCompilationUnit)
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
    typeMatcher: TypeMatcher): Unit = {
    val compilationUnits: List[CompilationUnit] = asCompilationUnits(
      classStore, 
      ns, 
      schemaOrProtocol,
      schemaStore,
      Some(outDir),
      typeMatcher)
    compilationUnits.foreach(writeToFile)
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
        if (localSubTypes.length > 1) protocol.getName // for ADT
        else localSubTypes.headOption match {
          case Some(schema) => schema.getName // for single class defintion
          case None => protocol.getName  // default to protocol name
        }
      }
    }
  }

}
