package avrohugger
package format
package standard

import avrohugger.format.abstractions.ScalaTreehugger
import avrohugger.format.standard.avrohuggers.{ StandardProtocolhugger, StandardSchemahugger }
import avrohugger.format.standard.trees.Scala3EnumSourceCode
import avrohugger.matchers.TypeMatcher
import avrohugger.stores.ClassStore
import org.apache.avro.{ Protocol, Schema }
import treehugger.forest._
import treehuggerDSL._

object StandardScalaTreehugger extends ScalaTreehugger {

  val schemahugger = StandardSchemahugger
  val protocolhugger = StandardProtocolhugger
  val importer = StandardImporter

  private val header = "MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY"

  def asScalaCodeString(
    classStore: ClassStore,
    namespace: Option[String],
    schemaOrProtocol: Either[Schema, Protocol],
    typeMatcher: TypeMatcher,
    restrictedFields: Boolean,
    targetScalaPartialVersion: String,
  ): String = {

    val imports = importer.getImports(schemaOrProtocol, namespace, typeMatcher)

    val topLevelDefs: List[Tree] = schemaOrProtocol match {
      case Left(schema) => schemahugger.toTrees(classStore, namespace, schema, typeMatcher, None, None, restrictedFields, targetScalaPartialVersion)
      case Right(protocol) => protocolhugger.toTrees(classStore, namespace, protocol, typeMatcher, None, None, restrictedFields, targetScalaPartialVersion)
    }

    // wrap the imports and class definition in a block with comment and package
    topLevelDefs match {
      // if the only topLevelDef is a scala 3 enum, emit it as raw string
      case List(Scala3EnumSourceCode(content)) =>
        val pkg = namespace.map(ns => s"package $ns\n\n").getOrElse("")
        val importStrings = if (imports.nonEmpty) imports.mkString("\n") else ""
        s"/* $header */\n" + pkg + importStrings + content + "\n"
      case _ =>
        val tree = {
          val blockContent = imports ++ topLevelDefs
          if (namespace.isDefined) BLOCK(blockContent).inPackage(namespace.get)
          else BLOCK(blockContent: _*).withoutPackage
        }.withDoc(header)
        // SpecificCompiler can't return a tree for Java enums, so return
        // a string here for a consistent api vis a vis *ToFile and *ToStrings
        treeToString(tree)
    }
  }

}
