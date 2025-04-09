package avrohugger
package format
package specific

import avrohugger.format.abstractions.ScalaTreehugger
import avrohugger.format.specific.avrohuggers.{ SpecificProtocolhugger, SpecificSchemahugger }
import avrohugger.matchers.TypeMatcher
import avrohugger.stores.ClassStore
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
    restrictedFields: Boolean,
    targetScalaPartialVersion: String): String = {

    // imports in case a field type is from a different namespace
    val imports: List[Import] = importer.getImports(schemaOrProtocol, namespace, typeMatcher)

    val topLevelDefs: List[Tree] = schemaOrProtocol match {
      case Left(schema) => schemahugger.toTrees(classStore, namespace, schema, typeMatcher, None, None, restrictedFields, targetScalaPartialVersion)
      case Right(protocol) => protocolhugger.toTrees(classStore, namespace, protocol, typeMatcher, None, None, restrictedFields, targetScalaPartialVersion)
    }

    // wrap the definitions in a block with a comment and a package
    val tree = {
      val blockContent = imports ++ topLevelDefs
      if (namespace.isDefined) BLOCK(blockContent).inPackage(namespace.get)
      else BLOCK(blockContent).withoutPackage
    }.withDoc("MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY")

    treeToString(tree)
  }

}


