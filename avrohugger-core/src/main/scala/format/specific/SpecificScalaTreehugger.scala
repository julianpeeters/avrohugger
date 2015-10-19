package avrohugger
package format
package specific

import avrohugger.input.DependencyInspector._
import avrohugger.input.NestedSchemaExtractor._

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
    schema: Schema,
    typeMatcher: TypeMatcher): String = {

    // register new type
    val classSymbol = RootClass.newClass(schema.getName)
    classStore.accept(schema, classSymbol)

    def isRecord(schema: Schema): Boolean = ( schema.getType == Schema.Type.RECORD )

    // imports in case a field type is from a different namespace
    val imports = {
      val topLevelSchemas: List[Schema] = schema::(getNestedSchemas(schema)) 
      topLevelSchemas.filter(isRecord).flatMap(s => s.getFields)
        .filter(field => getReferredNamespace(field.schema).isDefined )
        .filter(field => getReferredNamespace(field.schema) != namespace)
        .distinct
        .groupBy(field => getReferredNamespace(field.schema).get )
        .map { _ match { case(packageName, fields) =>
            IMPORT(packageName, fields.map( getReferredTypeName ).distinct )
          }
        }
    }

    // class definition
    val classDef = SpecificCaseClassTree.toCaseClassDef(classStore, namespace, schema, typeMatcher)

    // companion object definition
    val objectDef = SpecificObjectTree.toObjectDef(classStore, schema)

    // wrap the definitions in a block with a comment and a package
    val tree = {
      val blockContent = (imports ++ List(classDef, objectDef)).toSeq

      if (namespace.isDefined) BLOCK(blockContent:_*).inPackage(namespace.get)
      else BLOCK(blockContent:_*).withoutPackage
    }.withDoc("MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY")


    val codeString = treeToString(tree)
    codeString
  }


}


