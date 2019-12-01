package avrohugger
package format
package specific
package converters

import SchemaAccessors._

import matchers.TypeMatcher
import stores.ClassStore
import types._

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.{LogicalTypes, Schema}
import scala.language.postfixOps
import scala.collection.JavaConverters._


object JavaConverter {

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
    typeMatcher: TypeMatcher): Tree = schema.getType match {
    case Schema.Type.UNION => {
      val types = schema.getTypes.asScala
      // check if it's the kind of union that we support (i.e. nullable fields)
      if (types.length != 2 ||
         !types.map(x => x.getType).contains(Schema.Type.NULL) ||
          types.filterNot(x => x.getType == Schema.Type.NULL).length != 1) {
        sys.error("Unions beyond nullable fields are not supported")
      }
      else {
        val maybeType = types.find(x => x.getType != Schema.Type.NULL)
        if (maybeType.isDefined) {
        val conversionCases = List(
          CASE(SOME(ID("x"))) ==> {
            convertToJava(
              maybeType.get,
              schemaAccessor,
              true,
              REF("x"),
              classSymbol,
              typeMatcher)
          },
          CASE(NONE)          ==> {
            NULL
          }
        )
        tree MATCH(conversionCases:_*)
        }
        else sys.error("There was no type in this union")
      }
    }
    case Schema.Type.ARRAY => {
      val applyParam = {
        BLOCK(tree MAP(LAMBDA(PARAM("x")) ==> BLOCK(
          convertToJava(
            schema.getElementType,
            if (isUnionMember) arrayAccessor(unionAccessor(schemaAccessor, schema.getFullName)) else arrayAccessor(schemaAccessor),
            false,
            REF("x"),
            classSymbol,
            typeMatcher)
        )))
      }
      REF("scala.collection.JavaConverters.bufferAsJavaListConverter").APPLY(applyParam DOT "toBuffer").DOT("asJava")
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
                if (isUnionMember) mapAccessor(unionAccessor(schemaAccessor, schema.getFullName)) else mapAccessor(schemaAccessor),
                false,
                REF("value"),
                classSymbol,
                typeMatcher))
          )
        ),
        REF("map")
      )
    }
    case Schema.Type.FIXED => sys.error("the FIXED datatype is not yet supported")
    case Schema.Type.BYTES => schema.getLogicalType match {
      case decimal: LogicalTypes.Decimal => {
        val Decimal = RootClass.newClass("org.apache.avro.LogicalTypes.Decimal")
        val scale = tree.DOT("setScale").APPLY(REF("scale"))
        def scaleAndRound(roundingMode: BigDecimal.RoundingMode.Value) =
          tree.DOT("setScale").APPLY(REF("scale"), REF("BigDecimal.RoundingMode."+ roundingMode.toString))
        Block(
          VAL("schema") := {if (isUnionMember) unionAccessor(schemaAccessor, schema.getFullName) else schemaAccessor},
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
      case _ => tree
    }
    case Schema.Type.INT => schema.getLogicalType match {
      case date: LogicalTypes.Date => typeMatcher.avroScalaTypes.date match {
        case JavaSqlDate       => tree.DOT("getTime").APPLY().DOT("/").APPLY(LIT(86400000))
        case JavaTimeLocalDate => tree.DOT("toEpochDay").DOT("toInt")
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
      typeMatcher.avroScalaTypes.enum match {
        case EnumAsScalaString =>
          NEW(
            REF("org.apache.avro.generic.GenericData.EnumSymbol").APPLY(
              REF("getSchema").DOT("getFields").DOT("get").APPLY(REF("field$")).DOT("schema"),
              tree))
        case JavaEnum | ScalaEnumeration | ScalaCaseObjectEnum => tree
      }
    }
    case _ => tree
  }

}
