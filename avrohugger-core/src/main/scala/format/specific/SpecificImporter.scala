package avrohugger
package format
package specific

import avrohugger.format.abstractions.Importer
import avrohugger.matchers.TypeMatcher
import avrohugger.types._
import avrohugger.stores.SchemaStore
import org.apache.avro.Schema.Type.RECORD
import org.apache.avro.{Protocol, Schema}
import treehugger.forest._
import definitions._
import treehuggerDSL._

import scala.jdk.CollectionConverters._

object SpecificImporter extends Importer {

  def getImports(
    schemaOrProtocol: Either[Schema, Protocol],
    currentNamespace: Option[String],
    schemaStore: SchemaStore,
    typeMatcher: TypeMatcher): List[Import] = {
      
    val switchAnnotSymbol = RootClass.newClass("scala.annotation.switch")
    val switchImport = IMPORT(switchAnnotSymbol)
    val topLevelSchemas =
      getTopLevelSchemas(schemaOrProtocol, schemaStore, typeMatcher)
    val recordSchemas = getRecordSchemas(topLevelSchemas)
    val enumSchemas = getEnumSchemas(topLevelSchemas)
    val userDefinedDeps = getUserDefinedImports(recordSchemas ++ enumSchemas, currentNamespace, typeMatcher)
    val shapelessDeps = typeMatcher.avroScalaTypes.union match {
      case unionType: ShapelessUnionType => getShapelessImports(recordSchemas, typeMatcher, unionType)
      case OptionScala3UnionType => List()
    }
    val libraryDeps = shapelessDeps
    
    schemaOrProtocol match {
      case Left(schema) => {
        if (schema.getType == RECORD) switchImport :: libraryDeps ::: userDefinedDeps
        else libraryDeps ++ userDefinedDeps
      }
      case Right(protocol) => {
        val types = protocol.getTypes().asScala.toList
        val messages = protocol.getMessages.asScala.toMap
        if (messages.isEmpty) switchImport :: libraryDeps ::: userDefinedDeps // for ADT
        else List.empty // for RPC
      }
    }
  }

  /**
    * Shapeless representations of Coproduct are by default only active when `union`
    * is defined with more than two types or three types where one of them is nullable.
    * Otherwise the default values require no special imports
    * since they are codegen in terms of [[Option]] and [[Either]]
    */
  private[this] def getShapelessImports(
    topLevelRecordSchemas: List[Schema],
    typeMatcher: TypeMatcher,
    unionType: ShapelessUnionType): List[Import] = {

    def determineShapelessCoproductImports(
      field: Schema.Field,
      schema: Schema,
      potentialRecursives: List[Schema]): List[String] = schema.getType match {
      case Schema.Type.UNION  =>
        coproductImportsForUnionType(schema) ++
          schema.getTypes().asScala.toList.flatMap(s =>
            determineShapelessCoproductImports(field, s, potentialRecursives))
      case Schema.Type.ARRAY  =>
        determineShapelessCoproductImports(field, schema.getElementType(), potentialRecursives)
      case Schema.Type.MAP    =>
        determineShapelessCoproductImports(field, schema.getValueType(), potentialRecursives)
      case Schema.Type.RECORD =>
        schema.getFields().asScala.toList.flatMap(f => {
          if (potentialRecursives.map(_.getFullName).contains(schema.getFullName)) List.empty
          else determineShapelessCoproductImports(field, f.schema(), potentialRecursives:+schema)
        })
      case _ =>
        List.empty[String]
    }

    def determineShapelessTagImport(
      schema: Schema,
      potentialRecursives: List[Schema]): List[String] = schema.getType match {
      case Schema.Type.UNION  => schema.getTypes().asScala.toList.flatMap(s =>
                                   determineShapelessTagImport(s, potentialRecursives))
      case Schema.Type.ARRAY  => determineShapelessTagImport(schema.getElementType(), potentialRecursives)
      case Schema.Type.MAP    => determineShapelessTagImport(schema.getValueType(), potentialRecursives)
      case Schema.Type.RECORD => schema.getFields().asScala.toList.flatMap(f => {
                                   if (potentialRecursives.map(_.getFullName).contains(schema.getFullName)) List.empty
                                   else determineShapelessTagImport(f.schema, potentialRecursives:+schema)
                                 })
      case Schema.Type.BYTES  => importsForBigDecimalTagged(schema)
      case _ => List.empty[String]
    }

    def importsForBigDecimalTagged(schemas: Schema*): List[String] =
      schemas.find { schema =>
        schema.getType == Schema.Type.BYTES && LogicalType.foldLogicalTypes(
          schema = schema,
          default = false) {
            case Decimal(_, _) => typeMatcher.avroScalaTypes.decimal match {
              case ScalaBigDecimal(_) => false
              case ScalaBigDecimalWithPrecision(_) => true
            }
        }
      }.map(_ => List("tag.@@")).getOrElse(Nil)

    def coproductImportsForUnionType(unionSchema: Schema): List[String] = {
      val thresholdNonNullTypes = unionType match {
        case OptionalShapelessCoproduct => 0 // if a union contains at least one type, then it will need :+:
        case OptionShapelessCoproduct => 1
        case OptionEitherShapelessCoproduct => 2 // unions of one nullable type become Option, two become Either
      }
      val unionTypes = unionSchema.getTypes().asScala.toList
      val unionNonNullTypes = unionTypes.filterNot(_.getType == Schema.Type.NULL)
      val unionContainsNull: Boolean = unionNonNullTypes.length < unionTypes.length
      val isShapelessCoproduct: Boolean = unionNonNullTypes.length > thresholdNonNullTypes
      val hasDefaultValue: Boolean = !unionContainsNull
      val unionImports = if (isShapelessCoproduct && hasDefaultValue)
        List(":+:", "CNil", "Coproduct")
      else if (isShapelessCoproduct)
        List(":+:", "CNil")
      else
        List.empty[String]

      unionImports
    }
    val shapelessImport: List[String] => List[Import] = {
      case Nil          => Nil
      case head :: Nil  => List(IMPORT(RootClass.newClass(s"shapeless.$head")))
      case list         => List(IMPORT(RootClass.newClass(s"shapeless.{${list.mkString(", ")}}")))
    }
    val shapelessCopSymbols: List[String] =
      for {
        topLevelRecordSchema <- topLevelRecordSchemas
        field <- topLevelRecordSchema.getFields().asScala
        symbol <- determineShapelessCoproductImports(field, field.schema(), List.empty[Schema])
      } yield symbol
    val shapelessTag: List[String] =
      for {
        topLevelRecordSchema <- topLevelRecordSchemas
        field <- topLevelRecordSchema.getFields().asScala
        symbol <- determineShapelessTagImport(field.schema(), List.empty[Schema])
      } yield symbol
      
    shapelessImport(shapelessCopSymbols.distinct) ++
      shapelessImport(shapelessTag.distinct)
  }

}