package avrohugger
package format
package scavro

import avrohugger.input.reflectivecompilation.schemagen.SchemaStore
import avrohugger.input.DependencyInspector._
import avrohugger.input.NestedSchemaExtractor._

import org.apache.avro.Schema
import org.apache.avro.Schema.Field
import org.apache.avro.Schema.Type.RECORD

import treehugger.forest._
import definitions._
import treehuggerDSL._

import scala.collection.JavaConversions._

object ScavroImports {
  
  def isRecord(schema: Schema): Boolean = ( schema.getType == RECORD )
  
  def getImports(
    schema: Schema,
    currentNamespace: Option[String],
    schemaStore: SchemaStore): List[Import] = {
      
    val SchemaClass = RootClass.newClass("org.apache.avro.Schema")
    val ScavroPackage = RootClass.newPackage("org.oedura.scavro")

    val schemaImport = IMPORT(SchemaClass)
    val scavroImport = IMPORT(
      ScavroPackage,
      "AvroMetadata",
      "AvroReader",
      "AvroSerializeable")
            
    if (isRecord(schema)) {
      val javaClassRename = "J" + schema.getName
      val renameImport = schema.getNamespace match {
        case null =>
          sys.error("Scavro does not support schemas without namespaces.")
        case _ =>
          val importedNamespace = RootClass.newClass(schema.getNamespace)
          IMPORT(importedNamespace, RENAME(schema.getName) ==> javaClassRename)
      }
      val topLevelSchemas = schema::(getNestedSchemas(schema, schemaStore))
      val recordImports: List[Import] = topLevelSchemas.filter(isRecord)
        .flatMap(s => s.getFields)
        .filter(field => getReferredNamespace(field.schema).isDefined)
        .filter(field => getReferredNamespace(field.schema) != currentNamespace)
        .distinct
        .groupBy(field => getReferredNamespace(field.schema).get)
        .toList
        .map { _ match { case(packageName, fields) =>
          val importedPackage = RootClass.newClass(packageName)
          IMPORT(importedPackage, fields.map( field => {
            val renamedType = "J" + getReferredTypeName(field)
            RENAME(getReferredTypeName(field)) ==> renamedType
          } ).distinct )
        }}
        
      // if the record has an array, or a map or union that may have an array
      // value type, we need an extra javaConversions import
      val fieldTypes = schema.getFields.map(f => f.schema.getType)
      val hasArrayField = fieldTypes.contains(Schema.Type.ARRAY)
      val hasMapField = fieldTypes.contains(Schema.Type.MAP)
      val hasUnionField = fieldTypes.contains(Schema.Type.UNION)
      val convPackage = RootClass.newClass("scala.collection.JavaConversions")
      val javaConversionsImport = IMPORT(convPackage, "_")
      if( hasArrayField || hasMapField || hasUnionField) {
        List(schemaImport, scavroImport, renameImport, javaConversionsImport) ++
          recordImports
      }
      else List(schemaImport, scavroImport, renameImport) ++ recordImports

    }
    else List(schemaImport, scavroImport)
  }
}