package avrohugger
package format
package scavro
package converters

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.Schema

import scala.language.postfixOps
import scala.collection.JavaConversions._

class ScalaConverter(typeMatcher: TypeMatcher) {

  def convertFromJava(schema: Schema, tree: Tree): Tree = { 

    schema.getType match {
      case Schema.Type.ENUM  => {
        val conversionCases = schema.getEnumSymbols.map(enumSymbol => {
          CASE(REF("J" + schema.getName) DOT(enumSymbol)) ==> (REF(schema.getName) DOT(enumSymbol))
        })
        tree MATCH(conversionCases)
      }
      case Schema.Type.RECORD  => {
        val params = schema.getFields.map(field => {
          convertFromJava(field.schema, tree DOT("get" + field.name.head.toUpper + field.name.tail) )
        })
        REF(schema.getName) APPLY params
      }
      case Schema.Type.UNION  => {
        // check if it's the kind of union that we support (i.e. nullable fields)
        if (schema.getTypes.length != 2 || 
           !schema.getTypes.map(x => x.getType).contains(Schema.Type.NULL) || 
            schema.getTypes.filterNot(x => x.getType == Schema.Type.NULL).length != 1) {
              sys.error("Unions beyond nullable fields are not supported")
        }
        // the union represents a nullable field, the kind of union supported in avrohugger
        else {
          val typeParamSchema = schema.getTypes.find(x => x.getType != Schema.Type.NULL).get
          val nullConversion = CASE(NULL) ==> NONE
          val someConversion = CASE(WILDCARD) ==> SOME(convertFromJava(typeParamSchema, tree))
          val conversionCases = List(nullConversion, someConversion)
          tree MATCH(conversionCases:_*)
        }
      }
      case Schema.Type.NULL => NULL
      case Schema.Type.STRING => tree TOSTRING
      case Schema.Type.INT => tree DOT "toInt"
      case Schema.Type.FLOAT => tree DOT "toFloat"
      case Schema.Type.DOUBLE => tree DOT "toDouble"
      case Schema.Type.LONG => tree DOT "toLong"

      case Schema.Type.ARRAY => {
        val seqArgs = SEQARG(tree)
        val collection = typeMatcher.typeMap.get("array") match {
          case Some("Array") => ARRAY(seqArgs)
          case Some("List")  => LIST(seqArgs)
          case Some("Seq")   => SEQ(seqArgs)
          // default array mapping is currently List, but only for historical reasons
          case _             => LIST(seqArgs) 
        }
        collection MAP(LAMBDA(PARAM("x")) ==> BLOCK(convertFromJava(schema.getElementType, REF("x"))))
      }
      case Schema.Type.MAP => {
        val JavaMap = RootClass.newClass("java.util.Map[_,_]")
        val resultExpr = {
          BLOCK(
            REF("scala.collection.JavaConversions.mapAsScalaMap")
            .APPLY(REF("map"))
            .DOT("toMap")
            .MAP(LAMBDA(PARAM("kvp")) ==> BLOCK(
              VAL("key") := REF("kvp._1").DOT("toString"), 
              VAL("value") := REF("kvp._2"),
              PAREN(REF("key"), convertFromJava(schema.getValueType, REF("value"))))
            )
          )
        }
        val mapConversion = CASE(ID("map") withType(JavaMap)) ==> resultExpr 
        tree MATCH(mapConversion)
      }
      case Schema.Type.FIXED    => sys.error("the FIXED datatype is not yet supported")
      case Schema.Type.BYTES    => sys.error("the BYTES datatype is not yet supported")
      case _ => tree
    }
  }

}