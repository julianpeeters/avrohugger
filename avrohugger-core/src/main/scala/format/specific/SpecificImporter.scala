package avrohugger
package format
package specific

import avrohugger.format.abstractions.Importer
import avrohugger.input.DependencyInspector._
import avrohugger.input.NestedSchemaExtractor._
import avrohugger.matchers.TypeMatcher
import avrohugger.stores.SchemaStore

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Type.RECORD

import treehugger.forest._
import definitions._
import treehuggerDSL._

import scala.collection.JavaConverters._

object SpecificImporter extends Importer {

  def getImports(
    schemaOrProtocol: Either[Schema, Protocol],
    currentNamespace: Option[String],
    schemaStore: SchemaStore,
    typeMatcher: TypeMatcher): List[Import] = {
      
    val switchAnnotSymbol = RootClass.newClass("scala.annotation.switch")
    val switchImport = IMPORT(switchAnnotSymbol)
    val shapelessTagImport = IMPORT(RootClass.newClass(s"shapeless.tag.@@"))
    val topLevelSchemas =
      getTopLevelSchemas(schemaOrProtocol, schemaStore, typeMatcher)
    val recordSchemas = getRecordSchemas(topLevelSchemas)
    val enumSchemas = getEnumSchemas(topLevelSchemas)
    val deps = getUserDefinedImports(recordSchemas ++ enumSchemas, currentNamespace, typeMatcher)

    def hasDecimalTypes(schema: Schema): Boolean =
      Option(schema.getType).exists {
        case Schema.Type.RECORD =>
          schema.getFields.asScala.toList.map(isDecimalType).exists(identity)
        case _ => false
      }

    def isDecimalType(field: Schema.Field): Boolean =
      Option(field.schema().getLogicalType).exists(_.getName == "decimal")
    
    schemaOrProtocol match {
      case Left(schema) =>
        val baseImports = if (hasDecimalTypes(schema)) shapelessTagImport :: deps else deps
        if (schema.getType == RECORD) switchImport :: baseImports
        else baseImports
      case Right(protocol) =>
        val types = protocol.getTypes.asScala.toList
        val baseImports = if (types.map(hasDecimalTypes).exists(identity)) shapelessTagImport :: deps else deps
        val messages = protocol.getMessages.asScala.toMap
        if (messages.isEmpty) switchImport :: baseImports // for ADT
        else List.empty // for RPC
    }
  }

}