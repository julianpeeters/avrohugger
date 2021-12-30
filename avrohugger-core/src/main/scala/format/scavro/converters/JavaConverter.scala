package avrohugger
package format
package scavro
package converters

import matchers.TypeMatcher
import stores.ClassStore
import types._
import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.Schema

import scala.collection.JavaConverters._


class JavaConverter(
  classStore: ClassStore,
  namespace: Option[String],
  typeMatcher: TypeMatcher) {
  
  def checkCustomNumberType(
    numberType: AvroScalaNumberType,
    defaultNumberType: AvroScalaNumberType,
    tree: Tree,
    nativeType: String): Tree = {
    if (numberType == defaultNumberType) tree
    else tree DOT nativeType
  }
  
  def checkCustomEnumType(
    schema: Schema,
    enumType: AvroScalaEnumType,
    tree: Tree): Tree = {
    enumType match {
      case EnumAsScalaString => tree
      case JavaEnum | ScalaEnumeration | ScalaCaseObjectEnum => {
        val conversionCases = schema.getEnumSymbols.asScala.map(enumSymbol => {
          CASE(REF(schema.getName) DOT(enumSymbol)) ==> {
            (REF("J" + schema.getName) DOT(enumSymbol))
          }
        })
        tree MATCH(conversionCases)
      }
    }
  }

  // Recursive definition takes a field's schema, and a tree that represents the
  // source code to be written. The initial tree that is passed in is a 
  // REF("fieldName"), which is wrapped in a pattern match tree (e.g., to sort 
  // None and Some(x) if the field is a union). A Schema is passed in order to 
  // get access to the field's type parameters while the tree is built up.
  def convertToJava(
    schema: Schema,
    tree: Tree,
    fieldPath: List[String] = List.empty): Tree  = {
      
    schema.getType match {
      case Schema.Type.ENUM  => {
        checkCustomEnumType(schema, typeMatcher.avroScalaTypes.`enum`, tree)
      }
      case Schema.Type.RECORD => {
        tree DOT "toAvro"
      }
      case Schema.Type.UNION => {
        val types = schema.getTypes().asScala
        // check if it's the kind of union that we support (nullable fields)
        if (types.length != 2 ||
           !types.map(x => x.getType).contains(Schema.Type.NULL) || 
            types.filterNot(x => x.getType == Schema.Type.NULL).length != 1) {
          sys.error("Unions beyond nullable fields are not supported")
        }
        else {
          val maybeType = types.find(x => x.getType != Schema.Type.NULL)
          maybeType match {
            case Some(schema) =>
            val convertedToJava = convertToJava(schema, REF("x"), fieldPath)
              val conversionCases = List(
                CASE(SOME(ID("x"))) ==> convertedToJava,
                CASE(NONE)          ==> NULL
              )
              tree MATCH(conversionCases:_*)
            case None => sys.error("There was no type in this union")
          }
        }
      }
      case Schema.Type.ARRAY => {
        val elementType = {
          typeMatcher.toJavaType(classStore, namespace, schema.getElementType)
        }
        val JavaArrayClass = TYPE_REF(REF("java.util.List") APPLYTYPE(elementType))
        val ArrayListClass = TYPE_REF(REF("java.util.ArrayList") APPLYTYPE(elementType))
        BLOCK(
          VAL("array", JavaArrayClass) := NEW(ArrayListClass),
          tree FOREACH( LAMBDA(PARAM("element")) ==> 
            BLOCK(
              REF("array").DOT("add").APPLY(convertToJava(schema.getElementType, REF("element"), fieldPath))
            )
          ),
          REF("array")
        )
      }
      case Schema.Type.MAP => {
        val keyType = typeMatcher.avroStringType
        val valueType = {
          typeMatcher.toJavaType(classStore, namespace, schema.getValueType)
        }
        val JavaMapClass = TYPE_REF(REF("java.util.Map") APPLYTYPE(keyType, valueType))
        val HashMapClass = TYPE_REF(REF("java.util.HashMap") APPLYTYPE(keyType, valueType))
        val valueConversion = convertToJava(schema.getValueType, REF("value"), fieldPath)
        BLOCK( 
          VAL("map", JavaMapClass) := NEW(HashMapClass),
          tree FOREACH( LAMBDA(PARAM("kvp")) ==> 
            BLOCK(
              VAL("key") := REF("kvp._1"),
              VAL("value") := REF("kvp._2"),
              REF("map").DOT("put").APPLY(REF("key"), valueConversion)
            )
          ),
          REF("map")
        )
      }
      case Schema.Type.INT    => checkCustomNumberType(typeMatcher.avroScalaTypes.int, ScalaInt, tree, "toInt")
      case Schema.Type.FLOAT  => checkCustomNumberType(typeMatcher.avroScalaTypes.float, ScalaFloat, tree, "toFloat")
      case Schema.Type.DOUBLE => checkCustomNumberType(typeMatcher.avroScalaTypes.double, ScalaDouble, tree, "toDouble")
      case Schema.Type.LONG   => checkCustomNumberType(typeMatcher.avroScalaTypes.long, ScalaLong, tree, "toLong")
      case Schema.Type.FIXED  => sys.error("the FIXED datatype is not yet supported")
      case Schema.Type.BYTES  => REF("java.nio.ByteBuffer") DOT "wrap" APPLY tree
      case _ => tree
    }
  }
  
}
