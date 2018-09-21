package avrohugger
package format
package standard

import avrohugger.format.abstractions.Importer
import avrohugger.matchers.TypeMatcher
import avrohugger.stores.SchemaStore
import avrohugger.types._
import org.apache.avro.Schema.Type.RECORD
import org.apache.avro.{Protocol, Schema}
import org.codehaus.jackson.JsonNode
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
  private[this] def getShapelessImports(
    topLevelRecordSchemas: List[Schema],
    typeMatcher: TypeMatcher): List[Import] = {
    
    def determineShapelessImports(
      field: Schema.Field,
      schema: Schema,
      typeMatcher: TypeMatcher): List[String] = schema.getType match {
      case Schema.Type.UNION  => importsForUnionType(field, schema, typeMatcher)
      case Schema.Type.ARRAY  => determineShapelessImports(field, schema.getElementType(), typeMatcher)
      case Schema.Type.MAP    => determineShapelessImports(field, schema.getValueType(), typeMatcher)
      case Schema.Type.RECORD => schema.getFields.asScala.toList.flatMap(f =>
                                   determineShapelessImports(field, f.schema(), typeMatcher))
      case Schema.Type.BYTES  => LogicalType.foldLogicalTypes(
        schema = schema,
        default = List.empty[String]) {
        case Decimal(_, _)  => List("@@")
        case _              => List.empty[String]
      }
      case _ => List.empty[String]
    }
    
    def importsForUnionType(
      field: Schema.Field,
      unionSchema: Schema,
      typeMatcher: TypeMatcher): List[String] = {
      val thresholdNonNullTypes = typeMatcher.avroScalaTypes.union match {
        case OptionalShapelessCoproduct => 0 // if a union contains at least one type, then it will need :+:
        case OptionShapelessCoproduct => 1
        case OptionEitherShapelessCoproduct => 2 // unions of one nullable type become Option, two become Either
      }
      val unionContainsNull: Schema => Boolean = _.getType == Schema.Type.NULL
      def shapelessCoproductTest(unionTypes: List[Schema], maxNonNullTypes: Int): Boolean =
        (unionTypes.length > maxNonNullTypes     && !unionTypes.exists(unionContainsNull)) ||
        (unionTypes.length > maxNonNullTypes + 1 &&  unionTypes.exists(unionContainsNull))
      def defaultValueTest(field: Schema.Field, unionTypes: List[Schema], maxNonNullTypes: Int) =
        (unionTypes.length > maxNonNullTypes     && !unionTypes.exists(unionContainsNull) && field.defaultValue != null) ||
        (unionTypes.length > maxNonNullTypes + 1 &&  unionTypes.exists(unionContainsNull) && field.defaultValue != null)
      val unionTypes = unionSchema.getTypes.asScala.toList
      val isShapelessCoproduct: Boolean = shapelessCoproductTest(unionTypes, thresholdNonNullTypes)
      val hasDefaultValue: Boolean = defaultValueTest(field, unionTypes, thresholdNonNullTypes)
      if (isShapelessCoproduct && hasDefaultValue)
        List(":+:", "CNil", "Coproduct")
      else if (isShapelessCoproduct)
        List(":+:", "CNil")
      else
        List.empty[String]
    }
    val shapelessImport: List[String] => List[Import] = {
      case Nil          => Nil
      case head :: Nil  => List(IMPORT(RootClass.newClass(s"shapeless.$head")))
      case list         => List(IMPORT(RootClass.newClass(s"shapeless.{${list.mkString(", ")}}")))
    }
    val shapelessCopSymbols: List[String] =
      for {
        topLevelRecordSchema <- topLevelRecordSchemas
        field <- topLevelRecordSchema.getFields.asScala
        symbol <- determineShapelessImports(field, field.schema(), typeMatcher)
      } yield symbol
    shapelessImport(shapelessCopSymbols.distinct)
  }

  def getImports(
    schemaOrProtocol: Either[Schema, Protocol],
    currentNamespace: Option[String],
    schemaStore: SchemaStore,
    typeMatcher: TypeMatcher): List[Import] = {
    val topLevelSchemas = getTopLevelSchemas(schemaOrProtocol, schemaStore, typeMatcher)
    val recordSchemas = getRecordSchemas(topLevelSchemas)
    val recordDeps = getUserDefinedImports(recordSchemas, currentNamespace, typeMatcher)
    val enumSchemas = getEnumSchemas(topLevelSchemas)
    val userDefinedDeps = getUserDefinedImports(recordSchemas ++ enumSchemas, currentNamespace, typeMatcher)
    val shapelessDeps = getShapelessImports(recordSchemas, typeMatcher)
    val libraryDeps = shapelessDeps
    libraryDeps ++ userDefinedDeps
  }

}