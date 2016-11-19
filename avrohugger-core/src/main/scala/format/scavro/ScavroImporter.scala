package avrohugger
package format
package scavro

import format.abstractions.Importer
import avrohugger.input.DependencyInspector.{
  getReferredNamespace,
  getReferredTypeName
}
import avrohugger.matchers.TypeMatcher
import avrohugger.stores.SchemaStore

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Field
import org.apache.avro.Schema.Type.RECORD

import treehugger.forest._
import definitions._
import treehuggerDSL._

import scala.collection.JavaConverters._

object ScavroImporter extends Importer {
  
  def asRenamedImportTree(imported: Import) = {
    val packageSym = imported.expr
    val typeNames = imported.selectors.map(s => treeToString(s.name))
    val renames = typeNames.distinct.sorted.map(typeName => {
      val renamedType = "J" + typeName
      RENAME(typeName) ==> renamedType
    })
    IMPORT(packageSym, renames)
  }
  
  def getImports(
    schemaOrProtocol: Either[Schema, Protocol],
    currentNamespace: Option[String],
    schemaStore: SchemaStore,
    typeMatcher: TypeMatcher): List[Import] = {
    
    lazy val SchemaClass = RootClass.newClass("org.apache.avro.Schema")
    lazy val ScavroPackage = RootClass.newPackage("org.oedura.scavro")

    lazy val schemaImport = IMPORT(SchemaClass)
    lazy val scavroImport = IMPORT(
      ScavroPackage,
      "AvroMetadata",
      "AvroReader",
      "AvroSerializeable")
      
    lazy val baseImports = List(schemaImport, scavroImport)

    // gets all record schemas, including the root schema, which need renaming
    def getAllRecordSchemas(topLevelSchemas: List[Schema]): List[Schema] = {
      topLevelSchemas
        .filter(isRecord)
        .flatMap(schema => schema +: getFieldSchemas(schema))
        .distinct
    }
    // gets imported Scavro model classes, returning them as Import trees
    def getScalaRecordImports(
      recordSchemas: List[Schema],
      namespace: Option[String]): List[Import] = {
      recordSchemas
        .filter(schema => getReferredNamespace(schema).isDefined)
        .filter(schema => {
          val renamedNamespace = ScavroNamespaceRenamer.renameNamespace(
            getReferredNamespace(schema),
            Left(schema),
            typeMatcher)
          renamedNamespace != namespace
        })
        .groupBy(schema => {
          val renamedNamespace = ScavroNamespaceRenamer.renameNamespace(
            getReferredNamespace(schema),
            Left(schema),
            typeMatcher)
          renamedNamespace.get
        })
        .toList.map(group => group match {
          case(packageName, fields) => {
            val importedPackageSym = RootClass.newClass(packageName)
            val importedTypes = fields.map(field => getReferredTypeName(field))
            IMPORT(importedPackageSym, importedTypes)
          }
        })
    }
    val topLevelSchemas = getTopLevelSchemas(schemaOrProtocol, schemaStore)
    
    val allRecordSchemas = getAllRecordSchemas(topLevelSchemas)
    val allRecordImports =
      getRecordImports(allRecordSchemas, currentNamespace, typeMatcher)
    val renamedJavaImports = allRecordImports.map(asRenamedImportTree)
    
    val scalaRecords = getRecordSchemas(topLevelSchemas)
    val scalaImports = getScalaRecordImports(scalaRecords, currentNamespace)
    
    val recordImports = scalaImports ++ renamedJavaImports
    
    if (allRecordSchemas.isEmpty) List.empty
    else baseImports ++ recordImports
  }
  


  
}