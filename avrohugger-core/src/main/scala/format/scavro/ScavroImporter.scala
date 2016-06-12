package avrohugger
package format
package scavro

import format.abstractions.Importer
import avrohugger.stores.SchemaStore

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Field
import org.apache.avro.Schema.Type.RECORD

import treehugger.forest._
import definitions._
import treehuggerDSL._

import scala.collection.JavaConversions._

object ScavroImporter extends Importer {
  
  def asImportTree(packageSym: Symbol, typeNames: List[String]) = {
    val renames = typeNames.distinct.sorted.map(typeName => {
      val renamedType = "J" + typeName
      RENAME(typeName) ==> renamedType
    })
    IMPORT(packageSym, renames)
  }
  
  def getImports(
    schemaOrProtocol: Either[Schema, Protocol],
    currentNamespace: Option[String],
    schemaStore: SchemaStore): List[Import] = {
    
    def checkJavaConversions(schemaOrProtocol: Either[Schema, Protocol]) = {
      def getFieldTypes(schema: Schema): List[Schema.Type] = {
        if (isRecord(schema)) {
          val fieldSchemas = getFieldSchemas(schema)
          fieldSchemas.map(fieldSchema => fieldSchema.getType)
        }
        else List.empty
      }
      val fieldTypes: List[Schema.Type] = schemaOrProtocol match {
        case Left(schema) => getFieldTypes(schema)
        case Right(protocol) => {
          protocol.getTypes.toList.filter(isRecord).flatMap(schema => {
            getFieldTypes(schema)
          })
        }
      }
      val hasArrayField = fieldTypes.contains(Schema.Type.ARRAY)
      val hasMapField = fieldTypes.contains(Schema.Type.MAP)
      val hasUnionField = fieldTypes.contains(Schema.Type.UNION)
      val convPackage = RootClass.newClass("scala.collection.JavaConversions")
      val javaConversionsImport = IMPORT(convPackage, "_")
      if( hasArrayField || hasMapField || hasUnionField) {
        Some(javaConversionsImport)
      }
      else None
    }
      
    lazy val SchemaClass = RootClass.newClass("org.apache.avro.Schema")
    lazy val ScavroPackage = RootClass.newPackage("org.oedura.scavro")

    lazy val schemaImport = IMPORT(SchemaClass)
    lazy val scavroImport = IMPORT(
      ScavroPackage,
      "AvroMetadata",
      "AvroReader",
      "AvroSerializeable")
      
    lazy val baseImports = List(schemaImport, scavroImport)
    lazy val maybeJavaConversionsImport = checkJavaConversions(schemaOrProtocol)
    
    val topLevelSchemas = schemaOrProtocol match {
      case Left(schema) => getTopLevelSchemas(schema, schemaStore)
      case Right(protocol) => protocol.getTypes.toList.flatMap(schema => {
        getTopLevelSchemas(schema, schemaStore)
      })
    }
    val allRecordSchemas = getRecordSchemas(topLevelSchemas)
    val recordImports = getRecordImports(allRecordSchemas, currentNamespace)

    if (recordImports.isEmpty) List.empty
    else baseImports ++ recordImports ++ maybeJavaConversionsImport
  }
  
  // gets all record schemas, including the root schema, which need renaming
  def getRecordSchemas(topLevelSchemas: List[Schema]): List[Schema] = {
    topLevelSchemas
      .filter(isRecord)
      .flatMap(schema => schema +: getFieldSchemas(schema))
      .distinct
  }
  
}