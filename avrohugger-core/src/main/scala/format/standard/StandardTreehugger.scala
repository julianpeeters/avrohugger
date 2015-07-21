package avrohugger
package format
package standard

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.Schema
import org.apache.avro.Schema.Type.{RECORD, ENUM}

import scala.collection.JavaConversions._

object StandardTreehugger {

	def asScalaCodeString(
		classStore: ClassStore,
		schema: Schema, 
		namespace: Option[String]): String = {
    val topLevelDef = schema.getType match {
      case RECORD => StandardCaseClassTree.toCaseClassDef(classStore, namespace, schema)
      case ENUM => StandardObjectTree.toObjectDef(classStore, schema)
      case _ => sys.error("Only RECORD and ENUM can be top-level definitions")
    }
    // wrap the class definition in a block with a comment and a package
    val tree = {
      if (namespace.isDefined) BLOCK(topLevelDef).inPackage(namespace.get)
      else topLevelDef
    }.withDoc("MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY")

    // SpecificCompiler can't return a tree for Java enums, so return
    // a string here for a consistent api vis a vis *ToFile and *ToStrings
    treeToString(tree)
  }

}
