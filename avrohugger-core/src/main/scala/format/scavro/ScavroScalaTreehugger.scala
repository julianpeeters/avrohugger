package avrohugger
package format
package scavro

import format.abstractions.ScalaTreehugger
import avrohuggers.{ ScavroProtocolhugger, ScavroSchemahugger }
import input.reflectivecompilation.schemagen._
import matchers.TypeMatcher
import stores.{ ClassStore, SchemaStore }

import org.apache.avro.{ Protocol, Schema }

import treehugger.forest._
import definitions._
import treehuggerDSL._

object ScavroScalaTreehugger extends ScalaTreehugger {

  val schemahugger = ScavroSchemahugger
  val protocolhugger = ScavroProtocolhugger
	val importer = ScavroImporter

  // SpecificCompiler can't return a tree for Java enums, so return
  // a String here for a consistent api vis a vis *ToFile and *ToStrings
  def asScalaCodeString(
    classStore: ClassStore,
    namespace: Option[String],
    schemaOrProtocol: Either[Schema, Protocol],
    typeMatcher: TypeMatcher,
    schemaStore: SchemaStore,
    restrictedFields: Boolean): String = {

    val imports: List[Import] = importer.getImports(
      schemaOrProtocol, namespace, schemaStore, typeMatcher)

    val topLevelDefs: List[Tree] = schemaOrProtocol match {
      case Left(schema) => schemahugger.toTrees(
        schemaStore,
        classStore,
        namespace,
        schema,
        typeMatcher,
        None,
        None,
        restrictedFields
      )
      case Right(protocol) => protocolhugger.toTrees(
        schemaStore,
        classStore,
        namespace,
        protocol,
        typeMatcher,
        None,
        None,
        restrictedFields
      )
    }
    // wrap the imports and classdef in a block with a comment and a package
    val tree = {
      val blockContent = imports ++ topLevelDefs
      if (namespace.isDefined) BLOCK(blockContent).inPackage(namespace.get)
      else BLOCK(blockContent:_*).withoutPackage
    }.withDoc("MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY")

    treeToString(tree)
  }

}
