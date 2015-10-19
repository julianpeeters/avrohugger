package avrohugger
package format
package specific
package converters

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.Schema

import scala.language.postfixOps
import scala.collection.JavaConversions._


object ScalaConverter {
  // takes as args a REF wrapped according to field Type
  def convertFromJava(schema: Schema, tree: Tree): Tree = { 

    schema.getType match {
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
      case Schema.Type.STRING => tree TOSTRING
      case Schema.Type.ARRAY => {
        val GenericDataArray = RootClass.newClass("org.apache.avro.generic.GenericData.Array[_]")
        val applyParam = REF("array") DOT("iterator")
        val resultExpr = {
          BLOCK(
            REF("scala.collection.JavaConversions.asScalaIterator")
            .APPLY(applyParam)
            .DOT("toList")
            .MAP(LAMBDA(PARAM("x")) ==> BLOCK(convertFromJava(schema.getElementType, REF("x"))))
          )
        }
        val arrayConversion = CASE(ID("array") withType(GenericDataArray)) ==> resultExpr 
        tree MATCH(arrayConversion)
      }
      case Schema.Type.MAP => {
        val JavaMap = RootClass.newClass("java.util.Map[_,_]")
        val resultExpr = {
          BLOCK(
            REF("scala.collection.JavaConversions.mapAsScalaMap")
           // .APPLY(tree)
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