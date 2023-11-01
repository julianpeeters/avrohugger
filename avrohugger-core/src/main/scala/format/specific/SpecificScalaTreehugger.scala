package avrohugger
package format
package specific

import format.abstractions.ScalaTreehugger
import avrohuggers.{ SpecificProtocolhugger, SpecificSchemahugger }
import matchers.TypeMatcher
import stores.{ ClassStore, SchemaStore }

import org.apache.avro.{ Protocol, Schema }
import treehugger.forest._
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
    restrictedFields: Boolean,
    targetScalaPartialVersion: String): String = {

    // imports in case a field type is from a different namespace
    val imports: List[Import] = importer.getImports(
      schemaOrProtocol,
      namespace,
      schemaStore,
      typeMatcher)

    val topLevelDefs: List[Tree] = schemaOrProtocol match {
      case Left(schema) => schemahugger.toTrees(
        schemaStore,
        classStore,
        namespace,
        schema,
        typeMatcher,
        None,
        None,
        restrictedFields,
        targetScalaPartialVersion
      )
      case Right(protocol) => protocolhugger.toTrees(
        schemaStore,
        classStore,
        namespace,
        protocol,
        typeMatcher,
        None,
        None,
        restrictedFields,
        targetScalaPartialVersion
      )
    }

    // wrap the definitions in a block with a comment and a package
    val tree = {
      val blockContent = imports ++ topLevelDefs
      if (namespace.isDefined) BLOCK(blockContent).inPackage(namespace.get)
      else BLOCK(blockContent).withoutPackage
    }.withDoc("MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY")

    val codeString = treeToString(tree)
    codeString
  }

}


