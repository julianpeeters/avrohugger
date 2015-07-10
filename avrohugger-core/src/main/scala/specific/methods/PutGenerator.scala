package avrohugger
package specific
package methods

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.Schema

import scala.collection.JavaConversions._
import scala.language.postfixOps

object PutGenerator {

    def toDef(classStore: ClassStore, namespace: Option[String], indexedFields: List[IndexedField]) = {

      // put  
      def asPutCase(field: IndexedField) = {

        // takes as args a REF properly wrapped according to field Type
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
                val typeParam = schema.getTypes.find(x => x.getType != Schema.Type.NULL).get
                REF("Option") APPLY(convertFromJava(typeParam, tree))
              }
            }
            case Schema.Type.STRING => {
              // strings need converting from Utf8, except when a nullable field is `null`
              val Utf8Symbol = RootClass.newClass("org.apache.avro.util.Utf8")
              val stringConversionCases = List(
                CASE(ID("value") withType(Utf8Symbol)) ==> (tree TOSTRING),
                CASE(WILDCARD)                         ==> tree
              )
              tree MATCH(stringConversionCases:_*)
            }
            case Schema.Type.ARRAY => {
              val GenericDataArray = RootClass.newClass("org.apache.avro.generic.GenericData.Array[_]")
              val applyParam = REF("array") DOT("iterator")
              val resultExpr = {
                schema.getElementType.getType match {
                  // types that need converting
                  case Schema.Type.STRING | Schema.Type.ARRAY | Schema.Type.UNION => {
                    BLOCK(
                      REF("scala.collection.JavaConversions.asScalaIterator")
                      .APPLY(applyParam)
                      .DOT("toList")
                      .MAP(LAMBDA(PARAM("x")) ==> BLOCK(convertFromJava(schema.getElementType, REF("x"))))
                    )
                  }
                  // types that aren't yet supported
                  case Schema.Type.FIXED | 
                    Schema.Type.BYTES | 
                    Schema.Type.MAP | 
                    Schema.Type.ENUM => {
                    sys.error("unsupported type")
                  }
                  case _ => { 
                    BLOCK(
                      REF("scala.collection.JavaConversions.asScalaIterator")
                      .APPLY(applyParam)
                      .DOT("toList")
                    )
                  }
                }
              }

              val nullConversion = CASE(NULL) ==> NULL
              val arrayConversion = CASE(ID("array") withType(GenericDataArray)) ==> resultExpr 

              tree MATCH(nullConversion, arrayConversion)
            }
            case Schema.Type.FIXED    => sys.error("the FIXED datatype is not yet supported")
            case Schema.Type.ENUM     => sys.error("the ENUM datatype is not yet supported")
            case Schema.Type.MAP      => sys.error("the MAP datatype is not yet supported")
            case Schema.Type.BYTES    => sys.error("the BYTES datatype is not yet supported")
            case _ => tree
          }
        }

        CASE (ID("pos"), IF(REF("pos") INT_== LIT(field.idx))) ==> {
          THIS DOT field.avroField.name := BLOCK(convertFromJava(field.avroField.schema, REF("value")))
          .AS(TypeMatcher.toType(classStore, namespace, field.avroField.schema))
        }
      }
      
      val errorCase = CASE(WILDCARD) ==> NEW("org.apache.avro.AvroRuntimeException", LIT("Bad index"))
      val casesPut = indexedFields.map(field => asPutCase(field)):+ errorCase

      DEF("put", UnitClass) withParams(PARAM("field", IntClass), PARAM("value", AnyClass)) := BLOCK(
        REF("field") MATCH(casesPut:_*)
      )

    }

}