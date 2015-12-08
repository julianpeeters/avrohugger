package avrohugger
package format
package standard

import avrohugger.input.DependencyInspector._
import avrohugger.input.NestedSchemaExtractor._

import org.apache.avro.Schema
import org.apache.avro.Schema.Field
import org.apache.avro.Schema.Type.{ENUM, RECORD}
import treehugger.forest._
import treehuggerDSL._

import scala.collection.JavaConversions._

object StandardTreehugger {

  def asScalaCodeString(
		classStore: ClassStore,
		schema: Schema, 
		namespace: Option[String],
    typeMatcher: TypeMatcher): String = {

    val imports = if( isRecord(schema) ) getImports(schema, namespace) else List.empty

    val topLevelDef = schema.getType match {
      case RECORD => StandardCaseClassTree.toCaseClassDef(classStore, namespace, schema, typeMatcher)
      case ENUM => StandardObjectTree.toObjectDef(classStore, schema)
      case _ => sys.error("Only RECORD and ENUM can be top-level definitions")
    }

    // wrap the imports and class definition in a block with a comment and a package
    val tree = {
      val blockContent = imports ++ List(topLevelDef)
      if (namespace.isDefined) BLOCK(blockContent).inPackage(namespace.get)
      else topLevelDef
    }.withDoc("MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY")

    // SpecificCompiler can't return a tree for Java enums, so return
    // a string here for a consistent api vis a vis *ToFile and *ToStrings
    treeToString(tree)
  }

  def isRecord(schema: Schema): Boolean = ( schema.getType == RECORD )

  def getImports(schema: Schema, currentNamespace: Option[String]): Iterable[Import] = {
    val topLevelSchemas: List[Schema] = schema::(getNestedSchemas(schema)) 
      topLevelSchemas.filter(isRecord).flatMap(s => s.getFields)
        .filter( field => getReferredNamespace(field.schema).isDefined )
        .filter( field => getReferredNamespace(field.schema) != currentNamespace )
        .distinct
        .groupBy( field => getReferredNamespace(field.schema).get )
        .map { _ match { case(packageName, fields) =>
          IMPORT(packageName, fields.map( getReferredTypeName ).distinct )
        }
    }
  }
}
