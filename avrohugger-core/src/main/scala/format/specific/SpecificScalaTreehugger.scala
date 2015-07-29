package avrohugger
package format
package specific
import trees._

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.Schema

import scala.collection.JavaConversions._

object SpecificScalaTreehugger {

  def asScalaCodeString(
    classStore: ClassStore, 
    namespace: Option[String], 
    schema: Schema): String = {

    // register new type
    val classSymbol = RootClass.newClass(schema.getName)
    classStore.accept(schema, classSymbol)

    // class definition
    val classDef = SpecificCaseClassTree.toCaseClassDef(classStore, namespace, schema)
  
    // companion object definition
    val objectDef = SpecificObjectTree.toObjectDef(classStore, schema)

    // wrap the definitions in a block with a comment and a package
    val tree = {
      if (namespace.isDefined) BLOCK(classDef, objectDef).inPackage(namespace.get)
      else BLOCK(classDef, objectDef).withoutPackage
    }.withDoc("MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY")

    val codeString = treeToString(tree)
    codeString
  }

}
