package avrohugger
package format
package standard

import format.abstractions.ScalaTreehugger
import avrohuggers.{ StandardProtocolhugger, StandardSchemahugger }
import matchers.TypeMatcher
import stores.{ ClassStore, SchemaStore }

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Field
import org.apache.avro.Schema.Type.{ RECORD }

import treehugger.forest._
import definitions._
import treehuggerDSL._

import scala.collection.JavaConversions._

object StandardScalaTreehugger extends ScalaTreehugger {

  val schemahugger = StandardSchemahugger
  val protocolhugger = StandardProtocolhugger
  val importer = StandardImporter

  def asScalaCodeString(
		classStore: ClassStore,
    namespace: Option[String],
    schemaOrProtocol: Either[Schema, Protocol],
    typeMatcher: TypeMatcher,
    schemaStore: SchemaStore,
    restrictedFields: Boolean): String = {

    val imports = importer.getImports(
      schemaOrProtocol, namespace, schemaStore, typeMatcher)

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

    // wrap the imports and class definition in a block with comment and package
    val tree = {
      val blockContent = imports ++ topLevelDefs
      if (namespace.isDefined) BLOCK(blockContent).inPackage(namespace.get)
      else BLOCK(blockContent:_*).withoutPackage
    }.withDoc("MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY")
    // SpecificCompiler can't return a tree for Java enums, so return
    // a string here for a consistent api vis a vis *ToFile and *ToStrings
    treeToString(tree)
  }

}
