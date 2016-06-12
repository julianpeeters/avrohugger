package avrohugger
package format
package specific

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

object SpecificImporter extends Importer {
  
  def asImportTree(packageSym: Symbol, typeNames: List[String]) = {
    IMPORT(packageSym, typeNames)
  }

  def getImports(
    schemaOrProtocol: Either[Schema, Protocol],
    currentNamespace: Option[String],
    schemaStore: SchemaStore): List[Import] = {
      
    val switchAnnotSymbol = RootClass.newClass("scala.annotation.switch")
    val switchImport = IMPORT(switchAnnotSymbol)
    
    val topLevelSchemas = schemaOrProtocol match {
      case Left(schema) => getTopLevelSchemas(schema, schemaStore)
      case Right(protocol) => protocol.getTypes.toList.flatMap(schema => {
        getTopLevelSchemas(schema, schemaStore)
      })
    }
    val recordSchemas = getRecordSchemas(topLevelSchemas)
    val deps = getRecordImports(recordSchemas, currentNamespace)
    
    schemaOrProtocol match {
      case Left(schema) => {
        if (schema.getType == RECORD) switchImport :: deps
        else deps
      }
      case Right(protocol) => {
        val types = protocol.getTypes.toList
        val messages = protocol.getMessages.toMap
        if (messages.isEmpty) switchImport :: deps // for ADT
        else List.empty // for RPC
      }
    }
  }
  
  // gets record schemas, excluding the root schema, which may be dependencies
  def getRecordSchemas(topLevelSchemas: List[Schema]): List[Schema] = {
    topLevelSchemas
      .filter(isRecord)
      .flatMap(schema => getFieldSchemas(schema))
      .distinct
  }
  
}