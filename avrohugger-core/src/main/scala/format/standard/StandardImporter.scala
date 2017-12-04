package avrohugger
package format
package standard

import avrohugger.format.abstractions.Importer
import avrohugger.matchers.TypeMatcher
import avrohugger.stores.SchemaStore
import org.apache.avro.Schema.Type.RECORD
import org.apache.avro.{Protocol, Schema}
import treehugger.forest._
import definitions._
import treehuggerDSL._

import scala.collection.JavaConverters._

object StandardImporter extends Importer {

  /**
    * Shapeless representations of Coproduct are by default only active when `union`
    * is defined with more than two types or three types where one of them is nullable.
    * Otherwise the default values require no special imports
    * since they are codegen in terms of [[Option]] and [[Either]]
    */
  private[this] def requiresShapelessImports(schema: Schema): Boolean = {
    val fields = schema.getFields.asScala
    val isUnion: Schema.Field => Boolean = _.schema().getType == Schema.Type.UNION
    val unionContainsNull: Schema.Field => Boolean = _.schema().getTypes.asScala.exists(_.getType == Schema.Type.NULL)
    val containsUnion = fields.exists(isUnion)
    val containsNull = fields.filter(isUnion).exists(unionContainsNull)
    val innerFieldsSize = fields.find(isUnion).map(_.schema().getTypes.size()).getOrElse(0)
    val requiresImports =
      containsUnion &&
        ((innerFieldsSize > 2 && !containsNull) || (innerFieldsSize > 3 && containsNull))
    requiresImports
  }

  def getImports(
    schemaOrProtocol: Either[Schema, Protocol],
    currentNamespace: Option[String],
    schemaStore: SchemaStore,
    typeMatcher: TypeMatcher): List[Import] = {

    val shapelessCopSymbolsImport = RootClass.newClass("shapeless.{:+:, CNil, Coproduct}")
    val shapelessImport = IMPORT(shapelessCopSymbolsImport)

    val topLevelSchemas = getTopLevelSchemas(schemaOrProtocol, schemaStore)
    val recordSchemas = getRecordSchemas(topLevelSchemas)
    val deps = getRecordImports(recordSchemas, currentNamespace, typeMatcher)

    schemaOrProtocol match {
      case Left(schema) => {
        if (schema.getType == RECORD && requiresShapelessImports(schema)) shapelessImport :: deps
        else deps
      }
      case Right(protocol) => {
        val types = protocol.getTypes.asScala.toList
        if (types.exists(s => s.getType == RECORD && requiresShapelessImports(s))) shapelessImport :: deps
        else deps
      }
    }
  }

}