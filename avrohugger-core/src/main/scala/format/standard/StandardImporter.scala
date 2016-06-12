package avrohugger
package format
package standard

import avrohugger.format.abstractions.Importer
import avrohugger.input.DependencyInspector._
import avrohugger.input.NestedSchemaExtractor._
import avrohugger.stores.SchemaStore

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Type.RECORD

import treehugger.forest._
import definitions._
import treehuggerDSL._

import scala.collection.JavaConversions._

object StandardImporter extends Importer {
  
  def asImportTree(packageSym: Symbol, typeNames: List[String]) = {
    IMPORT(packageSym, typeNames)
  }
    
  def getImports(
    schemaOrProtocol: Either[Schema, Protocol],
    currentNamespace: Option[String],
    schemaStore: SchemaStore): List[Import] = {
    val topLevelSchemas = schemaOrProtocol match {
      case Left(schema) => getTopLevelSchemas(schema, schemaStore)
      case Right(protocol) => protocol.getTypes.toList.flatMap(schema => {
        getTopLevelSchemas(schema, schemaStore)
      })
    }
    val recordSchemas = getRecordSchemas(topLevelSchemas)
    getRecordImports(recordSchemas, currentNamespace)
  }

  // gets record schemas, excluding the root schema, which may have dependencies
  def getRecordSchemas(topLevelSchemas: List[Schema]): List[Schema] = {
    topLevelSchemas
      .filter(isRecord)
      .flatMap(schema => getFieldSchemas(schema))
      .distinct
  }

  
}