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

import scala.collection.JavaConversions._

object Scavro extends SourceFormat {

  val toolName = "generate-scavro"
  val toolShortDescription = "Generates Scala wrapper code for the given schema."
  def fileExt(schemaOrProtocol: Either[Schema, Protocol]) = ".scala"

  
  val scalaTreehugger = ScavroScalaTreehugger

  def asCompilationUnits(
    classStore: ClassStore, 
    namespace: Option[String], 
    schemaOrProtocol: Either[Schema, Protocol],
    schemaStore: SchemaStore,
    maybeOutDir: Option[String],
    typeMatcher: TypeMatcher): List[CompilationUnit] = {
      
    registerTypes(schemaOrProtocol, classStore, typeMatcher)
        
    // By default, Scavro generates Scala classes in packages that are the same 
    // as the Java package with `model` appended. 
    val scavroModelNamespace = ScavroNamespaceRenamer.renameNamespace(
      namespace,
      schemaOrProtocol,
      typeMatcher)
    
    val scalaCompilationUnit = getScalaCompilationUnit(
      classStore,
      scavroModelNamespace,
      schemaOrProtocol,
      typeMatcher,
      schemaStore,
      maybeOutDir)
    List(scalaCompilationUnit)
  }
  
  def getName(schemaOrProtocol: Either[Schema, Protocol]): String = {
    schemaOrProtocol match {
      case Left(schema) => schema.getName
      case Right(protocol) => {
        val localSubtypes = getLocalSubtypes(protocol)
        if (localSubtypes.length > 1) protocol.getName
        else localSubtypes.headOption match {
          case Some(schema) => schema.getName
          case None => protocol.getName
        }
      }
    }
  }

  def compile(
    classStore: ClassStore, 
    namespace: Option[String],
    schemaOrProtocol: Either[Schema, Protocol],
    outDir: String,
    schemaStore: SchemaStore,
    typeMatcher: TypeMatcher): Unit = {
    val compilationUnits: List[CompilationUnit] = asCompilationUnits(
      classStore,
      namespace,
      schemaOrProtocol,
      schemaStore,
      Some(outDir),
      typeMatcher)  
    compilationUnits.foreach(writeToFile)
  }

}
