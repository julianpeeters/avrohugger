package avrohugger
package format

import format.abstractions.SourceFormat
import format.standard._
import models.CompilationUnit
import stores.{ ClassStore, SchemaStore }
import matchers.{ CustomNamespaceMatcher, TypeMatcher }

import treehugger.forest._
import definitions.RootClass

import org.apache.avro.{ Protocol, Schema }

import scala.collection.JavaConversions._

object Standard extends SourceFormat {

  val toolName = "generate";
  val toolShortDescription = "Generates Scala code for the given schema.";
  def fileExt(schemaOrProtocol: Either[Schema, Protocol]) = ".scala"

  val typeMatcher = new TypeMatcher
  val scalaTreehugger = StandardScalaTreehugger

  def asCompilationUnits(
    classStore: ClassStore, 
    namespace: Option[String], 
    schemaOrProtocol: Either[Schema, Protocol],
    schemaStore: SchemaStore,
    maybeOutDir: Option[String]): List[CompilationUnit] = {
      
    registerTypes(schemaOrProtocol, classStore)

    val scalaCompilationUnit = getScalaCompilationUnit(
      classStore,
      namespace,
      schemaOrProtocol,
      typeMatcher,
      schemaStore,
      maybeOutDir)
    List(scalaCompilationUnit)
  }

  def compile(
    classStore: ClassStore, 
    ns: Option[String], 
    schemaOrProtocol: Either[Schema, Protocol],
    outDir: String,
    schemaStore: SchemaStore): Unit = {
    val maybeCustomNS = Option(typeMatcher.customNamespaceMap.get(ns))
    val scalaNS = CustomNamespaceMatcher.checkCustomNamespace(maybeCustomNS, ns)
    val compilationUnits: List[CompilationUnit] = asCompilationUnits(
      classStore, 
      scalaNS, 
      schemaOrProtocol,
      schemaStore,
      Some(outDir))
    compilationUnits.foreach(writeToFile)
  }
  
  def getName(schemaOrProtocol: Either[Schema, Protocol]): String = {
    schemaOrProtocol match {
      case Left(schema) => schema.getName
      case Right(protocol) => {
        val localSubTypes = getLocalSubtypes(protocol)
        if (localSubTypes.length > 1) protocol.getName // for ADT
        else localSubTypes.headOption match {
          case Some(schema) => schema.getName // for single class defintion
          case None => protocol.getName  // default to protocol name
        }
      }
    }
  }

  def registerTypes(
    schemaOrProtocol: Either[Schema, Protocol],
    classStore: ClassStore): Unit = {
    schemaOrProtocol match {
      case Left(schema) => {
        val classSymbol = RootClass.newClass(renameEnum(schema))
        classStore.accept(schema, classSymbol)
      }
      case Right(protocol) => protocol.getTypes.toList.foreach(schema => {
        val classSymbol = RootClass.newClass(renameEnum(schema))
        classStore.accept(schema, classSymbol)
      })
    }
  }

}
