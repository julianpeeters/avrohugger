package avrohugger
package format
package standard

import avrohugger.format.abstractions.Importer
import avrohugger.input.DependencyInspector._
import avrohugger.input.NestedSchemaExtractor._
import avrohugger.matchers.TypeMatcher
import avrohugger.stores.SchemaStore

import collection.JavaConverters._

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Type.RECORD

import treehugger.forest._
import definitions._
import treehuggerDSL._

object StandardImporter extends Importer {

  def getImports(
    schemaOrProtocol: Either[Schema, Protocol],
    currentNamespace: Option[String],
    schemaStore: SchemaStore,
    typeMatcher: TypeMatcher): List[Import] = {

    val shapelessCopSymbolsImport = RootClass.newClass("shapeless.{:+:, CNil}")
    val shapelessImport = IMPORT(shapelessCopSymbolsImport)

    val topLevelSchemas = getTopLevelSchemas(schemaOrProtocol, schemaStore)
    val recordSchemas = getRecordSchemas(topLevelSchemas)
    val deps = getRecordImports(recordSchemas, currentNamespace, typeMatcher)

    schemaOrProtocol match {
      case Left(schema) => {
        val includeShapeless =
          schema.getFields.asScala.exists(_.schema().getType == Schema.Type.UNION)
        if (schema.getType == RECORD && includeShapeless) shapelessImport :: deps
        else deps
      }
      case Right(protocol) => {
        val types = protocol.getTypes.asScala.toList
        val includeShapeless = types.exists(_.getFields.asScala.exists(_.schema().getType == Schema.Type.UNION))
        if (includeShapeless) shapelessImport :: deps
        else deps
      }
    }
  }

}