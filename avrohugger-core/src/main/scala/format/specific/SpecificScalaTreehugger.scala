package avrohugger
package format
package specific

import format.abstractions.ScalaTreehugger
import avrohuggers.{ SpecificProtocolhugger, SpecificSchemahugger }
import matchers.TypeMatcher
import stores.{ ClassStore, SchemaStore }

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Field
import org.apache.avro.Schema.Type.RECORD

import treehugger.forest._
import definitions._
import treehuggerDSL._


object SpecificScalaTreehugger extends ScalaTreehugger {

  val schemahugger = SpecificSchemahugger
  val protocolhugger = SpecificProtocolhugger
  val importer = SpecificImporter

  // SpecificCompiler can't return a tree for Java enums, so return
  // a String here for a consistent api vis a vis *ToFile and *ToStrings
  def asScalaCodeString(
    classStore: ClassStore,
    namespace: Option[String],
    schemaOrProtocol: Either[Schema, Protocol],
    typeMatcher: TypeMatcher,
    schemaStore: SchemaStore,
    restrictedFields: Boolean): String = {

    // imports in case a field type is from a different namespace
    val imports: List[Import] = importer.getImports(
      schemaOrProtocol,
      namespace,
      schemaStore,
      typeMatcher)

    val topLevelDefs: List[Tree] = schemaOrProtocol match {
      case Left(schema) => schemahugger.toTrees(
        classStore,
        namespace,
        schema,
        typeMatcher,
        None,
        None,
        restrictedFields
      )
      case Right(protocol) => protocolhugger.toTrees(
        classStore,
        namespace,
        protocol,
        typeMatcher,
        None,
        None,
        restrictedFields
      )
    }

    // wrap the definitions in a block with a comment and a package
    val tree = {
      val blockContent = imports ++ topLevelDefs
      if (namespace.isDefined) BLOCK(blockContent:_*).inPackage(namespace.get)
      else BLOCK(blockContent:_*).withoutPackage
    }.withDoc("MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY")

    val codeString = treeToString(tree)
    codeString
  }

}


