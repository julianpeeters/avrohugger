package avrohugger
package format
package specific

import avrohugger.format.DependencyInspectionSupport._
import org.apache.avro.Schema.Field
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

    val imports =
      schema
        .getFields.toList
        .filter( getFieldReferredNamespace(_).isDefined )
        .filter { field => getFieldReferredNamespace(field) != namespace }
        .distinct
        .groupBy( getFieldReferredNamespace(_).get )
        .map { _ match { case(packageName, fields) =>
            IMPORT(packageName, fields.map( getReferredTypeName ) )
          }
        }

    // companion object definition
    val objectDef = SpecificObjectTree.toObjectDef(classStore, schema)

    // wrap the definitions in a block with a comment and a package
    val tree = {
      val blockContent = imports ++ List(classDef, objectDef)

      if (namespace.isDefined) BLOCK(blockContent:_*).inPackage(namespace.get)
      else BLOCK(blockContent:_*).withoutPackage
    }.withDoc("MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY")



    val codeString = treeToString(tree)
    codeString
  }


}


