package avrohugger
package format
package scavro

import scavro.trees.ScavroCaseClassTree.toCaseClassDef
import scavro.trees.ScavroObjectTree.{ toCompanionDef, toObjectDef }

import avrohugger.input.DependencyInspector._
import avrohugger.input.NestedSchemaExtractor._

import org.apache.avro.Schema
import org.apache.avro.Schema.Field
import org.apache.avro.Schema.Type.{ENUM, RECORD}
import treehugger.forest._
import definitions._
import treehuggerDSL._

import scala.collection.JavaConversions._

object ScavroTreehugger {

	def asScalaCodeString(
		classStore: ClassStore,
		schema: Schema,
		namespace: Option[String],
    typeMatcher: TypeMatcher): String = {

    // register new type
    val ScalaClass = RootClass.newClass(schema.getName)

    val javaClassRename = "J" + schema.getName
    val JavaClass = RootClass.newClass(javaClassRename)

    val schemaImport = IMPORT("org.apache.avro.Schema")
    val scavroImport = IMPORT("com.oedura.scavro", "AvroMetadata", "AvroReader", "AvroSerializeable")
    val renameImport = schema.getNamespace match {
      case null => sys.error("Scavro does not currently support schemas without namespaces.")
      case _ => IMPORT(schema.getNamespace, RENAME(schema.getName) ==> javaClassRename)
    }
    val javaConversionsImport = IMPORT("scala.collection.JavaConversions._")

    val imports: List[Import] = {

      if( isRecord(schema) ) {
        val fieldTypes = schema.getFields.map(f => f.schema.getType)
        val hasArrayField = fieldTypes.contains(Schema.Type.ARRAY)
        val hasMapField = fieldTypes.contains(Schema.Type.MAP)
        val hasUnionField = fieldTypes.contains(Schema.Type.UNION)

        // if the record has an array, or a map or union that may have an array value type,
        // we need an extra javaConversions import
        if( hasArrayField || hasMapField || hasUnionField) {
          List(schemaImport,
            scavroImport,
            renameImport,
            javaConversionsImport) ++ getImports(schema, namespace)
        }
        else {
          List(schemaImport,
            scavroImport,
            renameImport) ++ getImports(schema, namespace)
        }
      }
      else List(schemaImport, scavroImport)
    }

    val topLevelDef: List[Tree] = schema.getType match {
      case RECORD => {
        List(
          toCaseClassDef(classStore, namespace, schema, ScalaClass, JavaClass, typeMatcher),
          toCompanionDef(classStore, schema, ScalaClass, JavaClass, typeMatcher))
      }
      case ENUM => List(toObjectDef(classStore, schema))
      case _ => sys.error("Only RECORD and ENUM can be top-level definitions")
    }

    // wrap the imports and class definition in a block with a comment and a package
    val tree = {
      val blockContent: List[Tree] = imports ++ topLevelDef
      if (namespace.isDefined) {
        BLOCK(blockContent).inPackage(namespace.get)
        .withDoc("MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY")
      }
      else blockContent
    }

    // SpecificCompiler can't return a tree for Java enums, so return
    // a String here for a consistent api vis a vis *ToFile and *ToStrings
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
        IMPORT(packageName, fields.map( field => {
          RENAME(getReferredTypeName(field)) ==> "J" + getReferredTypeName(field)
        } ).distinct )
      }
    }
  }
}
