package avrohugger
package format
package scavro
package converters

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.Schema

import scala.collection.JavaConversions._


class JavaConverter(classStore: ClassStore, namespace: Option[String], typeMatcher: TypeMatcher) {
  
  def checkCustomNumberType(maybeCustomNumber: Option[Class[_]], tree: Tree, nativeType: String): Tree = {
    maybeCustomNumber match {
      case Some(x) => tree DOT nativeType
      case None    => tree
    }
  }


  // Recursive definition takes a field's schema, and a tree that represents the source code to be written.
  // The initial tree that is passed in is a REF("fieldName"), which is wrapped in a pattern match tree (e.g.,
  // to sort None and Some(x) if the field is a union). A Schema is passed in order to get access to the field's type
  // parameters while the tree is built up.
  def convertToJava(schema: Schema, tree: Tree): Tree  = {
    schema.getType match {
      case Schema.Type.ENUM  => {
        val conversionCases = schema.getEnumSymbols.map(enumSymbol => {
          CASE(REF(schema.getName) DOT(enumSymbol)) ==> (REF("J" + schema.getName) DOT(enumSymbol))
        })
        tree MATCH(conversionCases)
      }
      case Schema.Type.RECORD => {
        val scalaClass = classStore.generatedClasses(schema)
        val javaClass = REF("J" + scalaClass.toString)
        val ids = schema.getFields.map(field => ID(field.name))
        val fieldConversions = schema.getFields.map(field => convertToJava(field.schema, REF(field.name)))
        val conversionCases = List(
          CASE(scalaClass UNAPPLY(ids)) ==> NEW(javaClass APPLY(fieldConversions))
        )
        tree MATCH(conversionCases:_*)
      }
      case Schema.Type.UNION => {
        // check if it's the kind of union that we support (i.e. nullable fields)
        if (schema.getTypes.length != 2 || 
           !schema.getTypes.map(x => x.getType).contains(Schema.Type.NULL) || 
            schema.getTypes.filterNot(x => x.getType == Schema.Type.NULL).length != 1) {
          sys.error("Unions beyond nullable fields are not supported")
        }
        else {
          val maybeType = schema.getTypes.find(x => x.getType != Schema.Type.NULL)
          if (maybeType.isDefined) {
            val conversionCases = List(
              CASE(SOME(ID("x"))) ==> convertToJava(maybeType.get, REF("x")),
              CASE(NONE)          ==> NULL
            )
            tree MATCH(conversionCases:_*)
          }
          else sys.error("There was no type in this union")
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
              REF("array").DOT("add").APPLY(convertToJava(schema.getElementType, REF("element")))
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
        BLOCK( 
          VAL("map", JavaMapClass) := NEW(HashMapClass),
	        tree FOREACH( LAMBDA(PARAM("kvp")) ==> 
	        	BLOCK(
	        	  VAL("key") := REF("kvp._1"),
	        	  VAL("value") := REF("kvp._2"),
              REF("map").DOT("put").APPLY(REF("key"), convertToJava(schema.getValueType, REF("value")))
			      )
          ),
          REF("map")
        )
      }
      case Schema.Type.INT    => checkCustomNumberType(typeMatcher.typeMap.get("int"), tree, "toInt")
      case Schema.Type.FLOAT  => checkCustomNumberType(typeMatcher.typeMap.get("float"), tree, "toFloat")
      case Schema.Type.DOUBLE => checkCustomNumberType(typeMatcher.typeMap.get("double"), tree, "toDouble")
      case Schema.Type.LONG   => checkCustomNumberType(typeMatcher.typeMap.get("long"), tree, "toLong")
      case Schema.Type.FIXED  => sys.error("the FIXED datatype is not yet supported")
      case _ => tree
    }
  }
  
}
