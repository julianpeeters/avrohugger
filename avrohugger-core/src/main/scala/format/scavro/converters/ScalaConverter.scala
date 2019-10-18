package avrohugger
package format
package scavro
package converters

import matchers.TypeMatcher
import types._

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.Schema

import scala.language.postfixOps
import scala.collection.JavaConverters._

class ScalaConverter(typeMatcher: TypeMatcher) {

  def convertFromJava(
    schema: Schema,
    tree: Tree,
    fieldPath: List[String] = List.empty): Tree = {

    schema.getType match {
      case Schema.Type.ENUM  => typeMatcher.avroScalaTypes.enum match {
        case EnumAsScalaString => tree TOSTRING
        case JavaEnum | ScalaEnumeration | ScalaCaseObjectEnum => {
          val conversionCases = schema.getEnumSymbols.asScala.map(enumSymbol => {
            CASE(REF("J" + schema.getName) DOT(enumSymbol)) ==> (REF(schema.getName) DOT(enumSymbol))
          })
          tree MATCH(conversionCases)
        }
      }
      case Schema.Type.RECORD => {
        REF(schema.getName).DOT("metadata").DOT("fromAvro").APPLY(tree)
      }
      case Schema.Type.UNION  => {
        val types = schema.getTypes.asScala
        // check if it's the kind of union that we support (i.e. nullable fields)
        if (types.length != 2 ||
           !types.map(x => x.getType).contains(Schema.Type.NULL) ||
            types.filterNot(x => x.getType == Schema.Type.NULL).length != 1) {
              sys.error("Unions beyond nullable fields are not supported")
        }
        // the union represents a nullable field, the kind of union supported in avrohugger
        else {
          val typeParamSchema = types.find(x => x.getType != Schema.Type.NULL).get
          val nullConversion = CASE(NULL) ==> NONE
          val someConversion = CASE(WILDCARD) ==> SOME(convertFromJava(typeParamSchema, tree, fieldPath))
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
        val seqArgs = SEQARG(tree DOT "asScala")
        val collection = typeMatcher.avroScalaTypes.array match {
          case ScalaArray  => ARRAY(seqArgs)
          case ScalaList   => LIST(seqArgs)
          case ScalaSeq   => SEQ(seqArgs)
          case ScalaVector => VECTOR(seqArgs)
        }
        collection MAP(LAMBDA(PARAM("x")) ==> BLOCK(convertFromJava(schema.getElementType, REF("x"), fieldPath)))
      }
      case Schema.Type.MAP => {
        val JavaMap = RootClass.newClass("java.util.Map[_,_]")
        val resultExpr = {
          BLOCK(
            REF("scala.collection.JavaConverters.mapAsScalaMapConverter")
            .APPLY(REF("map"))
            .DOT("asScala")
            .DOT("toMap")
            .MAP(LAMBDA(PARAM("kvp")) ==> BLOCK(
              VAL("key") := REF("kvp._1").DOT("toString"),
              VAL("value") := REF("kvp._2"),
              PAREN(REF("key"), convertFromJava(schema.getValueType, REF("value"), fieldPath)))
            )
          )
        }
        val mapConversion = CASE(ID("map") withType(JavaMap)) ==> resultExpr
        tree MATCH(mapConversion)
      }
      case Schema.Type.FIXED    => sys.error("the FIXED datatype is not yet supported")
      case Schema.Type.BYTES    => {
        val JavaBuffer = RootClass.newClass("java.nio.ByteBuffer")
        tree MATCH CASE(ID("buffer") withType(JavaBuffer)) ==> Block(
          REF("buffer") DOT "array" APPLY(Nil)
        )
      }
      case _ => tree
    }
  }

}
