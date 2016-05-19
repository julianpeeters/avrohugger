package avrohugger
package format
package specific

import avrohugger.input.reflectivecompilation.schemagen.SchemaStore
import avrohugger.input.DependencyInspector._
import avrohugger.input.NestedSchemaExtractor._

import org.apache.avro.Schema
import org.apache.avro.Schema.Type.RECORD

import treehugger.forest._
import definitions._
import treehuggerDSL._

import scala.collection.JavaConversions._

object SpecificImports {
  
  def isRecord(schema: Schema): Boolean = ( schema.getType == RECORD )
  
  def getImports(
    schema: Schema,
    currentNamespace: Option[String],
    schemaStore: SchemaStore): List[Import] = {
      
    if (isRecord(schema)) {
      val topLevelSchemas = schema::(getNestedSchemas(schema, schemaStore)) 
      topLevelSchemas.filter(isRecord).flatMap(s => s.getFields)
        .filter(field => getReferredNamespace(field.schema).isDefined)
        .filter(field => getReferredNamespace(field.schema) != currentNamespace)
        .distinct
        .groupBy(field => getReferredNamespace(field.schema).get )
        .toList
        .map { _ match {
          case(packageName, fields) =>
            IMPORT(packageName, fields.map( getReferredTypeName ).distinct )
          }
        }
    }
    else List.empty
  }
  
}