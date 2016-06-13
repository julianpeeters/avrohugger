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

  def getImports(
    schemaOrProtocol: Either[Schema, Protocol],
    currentNamespace: Option[String],
    schemaStore: SchemaStore): List[Import] = {
    val topLevelSchemas = getTopLevelSchemas(schemaOrProtocol, schemaStore)
    val recordSchemas = getRecordSchemas(topLevelSchemas)
    getRecordImports(recordSchemas, currentNamespace)
  }

}