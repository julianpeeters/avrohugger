package avrohugger
package format
package specific
package converters

import SchemaAccessors._

import matchers.TypeMatcher
import types._

import treehugger.forest._
import definitions._
import org.apache.avro.{LogicalTypes, Schema}
import treehuggerDSL._

import scala.jdk.CollectionConverters._
import scala.language.postfixOps

object JavaConverter {

  def bufferAsJavaListConverter(partialVersion: String): String =
    partialVersion match {
      case "2.11" => "scala.collection.JavaConverters.bufferAsJavaListConverter"
      case "2.12" => "scala.collection.JavaConverters.bufferAsJavaListConverter"
      case "2.13" => "scala.jdk.CollectionConverters.BufferHasAsJava"
      case _ => "scala.jdk.CollectionConverters.BufferHasAsJava"
    }

  // Recursive definition takes a field's schema, and a tree that represents the source code to be written.
  // The initial tree that is passed in is a REF("fieldName"), which is wrapped in a pattern match tree (e.g.,
  // to sort None and Some(x) if the field is a union). A Schema is passed in order to get access to the field's type
  // parameters while the tree is built up.
  def convertToJava(
    schema: Schema,
    schemaAccessor: Tree,
    isUnionMember: Boolean,
    tree: Tree,
    classSymbol: ClassSymbol,
    typeMatcher: TypeMatcher,
    targetScalaPartialVersion: String): Tree = schema.getType match {
    case Schema.Type.UNION => {
      val types = schema.getTypes().asScala
      val hasNull = types.exists(_.getType == Schema.Type.NULL)
      val typeParamSchemas = types.filterNot(_.getType == Schema.Type.NULL)
      if (hasNull) {
        val conversionCases = List(
          CASE(SOME(ID("x"))) ==> {
            convertToJava(
              typeParamSchemas.head,
              schemaAccessor,
              true,
              REF("x"),
              classSymbol,
              typeMatcher,
              targetScalaPartialVersion
            )
          },
          CASE(NONE) ==> {
            NULL
          }
        )
        tree MATCH (conversionCases)
      } else {
        tree
      }
    }
    case Schema.Type.ARRAY => {
      val applyParam = {
        BLOCK(tree MAP(LAMBDA(PARAM("x")) ==> BLOCK(
          convertToJava(
            schema.getElementType,
            if (isUnionMember) arrayAccessor(unionAccessor(schemaAccessor, schema.getFullName, ScalaConverter.asScalaBufferConverter(targetScalaPartialVersion))) else arrayAccessor(schemaAccessor),
            false,
            REF("x"),
            classSymbol,
            typeMatcher,
            targetScalaPartialVersion)
        )))
      }
      REF(bufferAsJavaListConverter(targetScalaPartialVersion)).APPLY(applyParam DOT "toBuffer").DOT("asJava")
    }
    case Schema.Type.MAP      => {
      val HashMapClass = RootClass.newClass("java.util.HashMap[String, Any]")
      BLOCK(
        VAL("map", HashMapClass) := NEW(HashMapClass),
        tree FOREACH( LAMBDA(PARAM("kvp")) ==>
          BLOCK(
            VAL("key") := REF("kvp._1"),
            VAL("value") := REF("kvp._2"),
            REF("map").DOT("put").APPLY(
              REF("key"),
              convertToJava(
                schema.getValueType,
                if (isUnionMember) mapAccessor(unionAccessor(schemaAccessor, schema.getFullName, ScalaConverter.asScalaBufferConverter(targetScalaPartialVersion))) else mapAccessor(schemaAccessor),
                false,
                REF("value"),
                classSymbol,
                typeMatcher,
                targetScalaPartialVersion))
          )
        ),
        REF("map")
      )
    }
    case Schema.Type.FIXED => schema.getLogicalType match {
      case decimal: LogicalTypes.Decimal => {
        val Decimal = RootClass.newClass("org.apache.avro.LogicalTypes.Decimal")
        val scale = tree.DOT("bigDecimal").DOT("setScale").APPLY(REF("scale"))
        def scaleAndRound(roundingMode: BigDecimal.RoundingMode.Value) =
          tree.DOT("bigDecimal").DOT("setScale").APPLY(REF("scale"), REF("BigDecimal.RoundingMode."+ roundingMode.toString))
        Block(
          VAL("schema") := {if (isUnionMember) unionAccessor(schemaAccessor, schema.getFullName, ScalaConverter.asScalaBufferConverter(targetScalaPartialVersion)) else schemaAccessor},
          VAL("decimalType") := REF("schema").DOT("getLogicalType").APPLY().AS(Decimal),
          VAL("scale") := REF("decimalType").DOT("getScale").APPLY(),
          VAL("scaledValue") := {
            typeMatcher.avroScalaTypes.decimal match {
              case ScalaBigDecimal(None) => scale
              case ScalaBigDecimal(Some(roundingMode)) => scaleAndRound(roundingMode)
              case ScalaBigDecimalWithPrecision(None) => scale
              case ScalaBigDecimalWithPrecision(Some(roundingMode)) => scaleAndRound(roundingMode)
            }
          },
          VAL("bigDecimal") := REF("scaledValue").DOT("bigDecimal"),
          classSymbol.DOT("decimalConversion").DOT("toFixed").APPLY(REF("bigDecimal"),REF("schema"),REF("decimalType"))
        )
      }
      case _ => tree
    }
    case Schema.Type.BYTES => schema.getLogicalType match {
      case decimal: LogicalTypes.Decimal => {
        val Decimal = RootClass.newClass("org.apache.avro.LogicalTypes.Decimal")
        val scale = tree.DOT("setScale").APPLY(REF("scale"))
        def scaleAndRound(roundingMode: BigDecimal.RoundingMode.Value) =
          tree.DOT("setScale").APPLY(REF("scale"), REF("BigDecimal.RoundingMode."+ roundingMode.toString))
        Block(
          VAL("schema") := {if (isUnionMember) unionAccessor(schemaAccessor, schema.getFullName, ScalaConverter.asScalaBufferConverter(targetScalaPartialVersion)) else schemaAccessor},
          VAL("decimalType") := REF("schema").DOT("getLogicalType").APPLY().AS(Decimal),
          VAL("scale") := REF("decimalType").DOT("getScale").APPLY(),
          VAL("scaledValue") := {
            typeMatcher.avroScalaTypes.decimal match {
              case ScalaBigDecimal(None) => scale
              case ScalaBigDecimal(Some(roundingMode)) => scaleAndRound(roundingMode)
              case ScalaBigDecimalWithPrecision(None) => scale
              case ScalaBigDecimalWithPrecision(Some(roundingMode)) => scaleAndRound(roundingMode)
            }
          },
          VAL("bigDecimal") := REF("scaledValue").DOT("bigDecimal"),
          classSymbol.DOT("decimalConversion").DOT("toBytes").APPLY(REF("bigDecimal"),REF("schema"),REF("decimalType"))
        )
      }
      case _ => REF("java.nio.ByteBuffer") DOT "wrap" APPLY tree
    }
    case Schema.Type.LONG => schema.getLogicalType match {
      case timestamp: LogicalTypes.TimestampMillis => typeMatcher.avroScalaTypes.timestampMillis match {
        case JavaSqlTimestamp => BLOCK(tree.DOT("getTime").APPLY())
        case JavaTimeInstant  => BLOCK(tree.DOT("toEpochMilli"))
      }
      case _: LogicalTypes.TimestampMicros => (typeMatcher.avroScalaTypes.timestampMicros match {
        case JavaTimeZonedDateTime => BLOCK(tree.DOT("toEpochSecond").INFIX("*", LIT(1000000L)).INFIX("+", tree.DOT("getNano").INFIX("/", LIT(1000L))))
      }) withComment "avro timestamp-micros long stores the number of microseconds from the unix epoch, 1 January 1970 00:00:00.000000 UTC"
      case _: LogicalTypes.LocalTimestampMillis => (typeMatcher.avroScalaTypes.localTimestampMillis match {
        case JavaTimeLocalDateTime => BLOCK(tree.DOT("toEpochSecond").APPLY(RootClass.newClass("java.time.ZoneOffset").DOT("UTC")).INFIX("*", LIT(1000L)).INFIX("+", tree.DOT("getNano").INFIX("/", LIT(1000000L))))
      }) withComment "avro local-timestamp-millis long stores the number of millis, from 1 January 1970 00:00:00.000000"
      case _: LogicalTypes.LocalTimestampMicros => (typeMatcher.avroScalaTypes.localTimestampMicros match {
        case JavaTimeLocalDateTime => BLOCK(tree.DOT("toEpochSecond").APPLY(RootClass.newClass("java.time.ZoneOffset").DOT("UTC")).INFIX("*", LIT(1000000L)).INFIX("+", tree.DOT("getNano").INFIX("/", LIT(1000L))))
      }) withComment "avro local-timestamp-micros long stores the number of microseconds, from 1 January 1970 00:00:00.000000"
      case _: LogicalTypes.TimeMicros => (typeMatcher.avroScalaTypes.timeMicros match {
        case JavaTimeLocalTime => BLOCK(tree.DOT("toNanoOfDay").INFIX("/", LIT(1000L)))
      }) withComment "avro time-micros long stores the number of microseconds after midnight, 00:00:00.000000"
      case _ => tree
    }
    case Schema.Type.INT => schema.getLogicalType match {
      case date: LogicalTypes.Date => typeMatcher.avroScalaTypes.date match {
        case JavaSqlDate       => tree.DOT("getTime").APPLY().DOT("/").APPLY(LIT(86400000))
        case JavaTimeLocalDate => tree.DOT("toEpochDay").DOT("toInt")
      }
      case timeMillis: LogicalTypes.TimeMillis => typeMatcher.avroScalaTypes.timeMillis match {
        case JavaSqlTime       => tree.DOT("getTime").APPLY()
        case JavaTimeLocalTime => tree.DOT("get").APPLY(REF("java.time.temporal.ChronoField").DOT("MILLI_OF_DAY"))
      }
      case _ => tree
    }
    case Schema.Type.STRING =>
      if (schema.getLogicalType == LogicalTypes.uuid()) {
        tree TOSTRING
      } else {
        tree
      }
    case Schema.Type.ENUM => {
      typeMatcher.avroScalaTypes.`enum` match {
        case EnumAsScalaString =>
          val defaultTree: Tree = REF("getSchema").DOT("getFields").DOT("get").APPLY(REF("field$")).DOT("schema")
          val schemaTree: Tree =
            if (isUnionMember) defaultTree.DOT("getTypes").DOT("get").APPLY(defaultTree.DOT("getIndexNamed").APPLY(LIT(schema.getFullName())))
            else defaultTree
          NEW(
            REF("org.apache.avro.generic.GenericData.EnumSymbol").APPLY(schemaTree, tree))
        case JavaEnum | ScalaEnumeration | ScalaCaseObjectEnum => tree
      }
    }
    case _ => tree
  }

}
