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


object ScalaConverter {

  def checkCustomArrayType(
    arrayType: AvroScalaArrayType,
    elementType: Type,
    seqArgs: Typed,
    defaultConversion: Tree) = {
    val classTagIdent = REF(s"scala.reflect.ClassTag(classOf[$elementType])")
    val arrayConversion = ARRAY(seqArgs).APPLY(classTagIdent).AS(TYPE_ARRAY(elementType))
    arrayType match {
      case ScalaArray  => arrayConversion
      case ScalaList   => LIST(seqArgs)
      case ScalaSeq    => SEQ(seqArgs)
      case ScalaVector => VECTOR(seqArgs)
    }
  }

  val CharSequenceClass = definitions.getClass("java.lang.CharSequence")

// takes as args a REF wrapped according to field Type
  def convertFromJava(
    classStore: ClassStore,
    namespace: Option[String],
    schema: Schema,
    schemaAccessor: Tree,
    isUnionMember: Boolean,
    tree: Tree, 
    typeMatcher: TypeMatcher,
    classSymbol: ClassSymbol): Tree = {

    schema.getType match {
      case Schema.Type.ARRAY => {
        val elementSchema = schema.getElementType
        val elementType = typeMatcher.toScalaType(classStore, namespace, elementSchema)
        val JavaList = RootClass.newClass("java.util.List[_]")
        val applyParam = REF("array") DOT("iterator")
        val elementConversion = convertFromJava(
          classStore,
          namespace,
          elementSchema,
          if (isUnionMember) arrayAccessor(unionAccessor(schemaAccessor, schema.getFullName)) else arrayAccessor(schemaAccessor),
          false,
          REF("x"),
          typeMatcher,
          classSymbol)
        val seqArgs = {
          SEQARG(
            REF("scala.collection.JavaConverters.asScalaIteratorConverter").APPLY(applyParam).DOT("asScala").DOT("toSeq")
              .MAP(LAMBDA(PARAM("x")) ==> BLOCK(elementConversion))
          )
        }
        val arrayType = typeMatcher.avroScalaTypes.array
        val resultExpr = BLOCK(
          checkCustomArrayType(arrayType, elementType, seqArgs, LIST(seqArgs))
        )
        val arrayConversion = CASE(ID("array") withType(JavaList)) ==> resultExpr
        val errorMessage = INTERP("s", LIT(s"expected array with type $JavaList, found "), LIT("array"))
        val errorExpr = NEW("org.apache.avro.AvroRuntimeException", errorMessage)
        val conversionCases = List(arrayConversion)
        val arrayMatchError = CASE(WILDCARD) ==> errorExpr
        tree MATCH(conversionCases:_*)
      }
      case Schema.Type.STRING =>
        LogicalType.foldLogicalTypes(
          schema = schema,
          default = tree TOSTRING) {
          case UUID =>
            typeMatcher.avroScalaTypes.uuid match {
              case JavaUuid => {
                val UuidClass = RootClass.newClass("java.util.UUID")
                val resultExpr = BLOCK(UuidClass.DOT("fromString").APPLY(REF("chars").TOSTRING))
                val charSequenceConversion = CASE(ID("chars") withType CharSequenceClass) ==> resultExpr
                tree MATCH charSequenceConversion
              }
            }
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
              PAREN(REF("key"), convertFromJava(
                classStore,
                namespace,
                schema.getValueType,
                if (isUnionMember) mapAccessor(unionAccessor(schemaAccessor, schema.getFullName)) else mapAccessor(schemaAccessor),
                false,
                REF("value"),
                typeMatcher,
                classSymbol)))
            )
          )
        }
        val mapConversion = CASE(ID("map") withType(JavaMap)) ==> resultExpr
        tree MATCH(mapConversion)
      }
      case Schema.Type.FIXED => sys.error("the FIXED datatype is not yet supported")
      case Schema.Type.BYTES => {
        val JavaBuffer = RootClass.newClass("java.nio.ByteBuffer")
        val resultExpr = schema.getLogicalType match {
          case decimal: LogicalTypes.Decimal => {
            val Decimal = RootClass.newClass("org.apache.avro.LogicalTypes.Decimal")
            Block(
              VAL("schema") := {if (isUnionMember) unionAccessor(schemaAccessor, schema.getFullName) else schemaAccessor},
              VAL("decimalType") := REF("schema").DOT("getLogicalType").APPLY().AS(Decimal),
              REF("BigDecimal").APPLY(classSymbol.DOT("decimalConversion").DOT("fromBytes").APPLY(REF("buffer"),REF("schema"),REF("decimalType")))
            )
          }
          case _ => Block(
            VAL("dup") := REF("buffer").DOT("duplicate").APPLY(),
            VAL("array") := NEW("Array[Byte]", REF("dup").DOT("remaining")),
            REF("dup") DOT "get" APPLY(REF("array")),
            REF("array")
          )
        }  
        val bufferConversion = CASE(ID("buffer") withType (JavaBuffer)) ==> resultExpr
        tree MATCH bufferConversion
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
          val someExpr = SOME(convertFromJava(
            classStore,
            namespace,
            typeParamSchema,
            schemaAccessor,
            true,
            tree,
            typeMatcher,
            classSymbol))
          val someConversion = CASE(WILDCARD) ==> someExpr
          val conversionCases = List(nullConversion, someConversion)
          tree MATCH(conversionCases:_*)
        }
      }
      case Schema.Type.ENUM => {
        typeMatcher.avroScalaTypes.enum match {
          case EnumAsScalaString => tree TOSTRING
          case JavaEnum | ScalaEnumeration | ScalaCaseObjectEnum => tree
        }
      }
      case Schema.Type.LONG => {
        Option(schema.getLogicalType()) match {
          case Some(logicalType) => {
            if (logicalType.getName == "timestamp-millis") {
              typeMatcher.avroScalaTypes.timestampMillis match {
                case JavaSqlTimestamp => {
                  val TimestampClass = RootClass.newClass("java.sql.Timestamp")
                  val resultExpr = BLOCK(NEW(TimestampClass, REF("l")))
                  val longConversion = CASE(ID("l") withType (LongClass)) ==> resultExpr
                  tree MATCH longConversion
                }
                case JavaTimeInstant  => {
                  val InstantClass = RootClass.newClass("java.time.Instant")
                  val resultExpr = BLOCK(InstantClass.DOT("ofEpochMilli").APPLY(REF("l")))
                  val longConversion = CASE(ID("l") withType (LongClass)) ==> resultExpr
                  tree MATCH longConversion
                } 
              }
            }
            else tree
          }
          case None => tree
        }
      }
      case Schema.Type.INT => {
        Option(schema.getLogicalType()) match {
          case Some(logicalType) => {
            if (logicalType.getName == "date") {
              typeMatcher.avroScalaTypes.date match {
                case JavaSqlDate => {
                  val IntegerClass = RootClass.newClass("Integer")
                  val SqlDateClass = RootClass.newClass("java.sql.Date")
                  val resultExpr = BLOCK(NEW(SqlDateClass, REF("i").DOT("toLong").DOT("*").APPLY(LIT(86400000L))))
                  val integerConversion = CASE(ID("i") withType (IntegerClass)) ==> resultExpr
                  tree MATCH integerConversion
                }
                case JavaTimeLocalDate => {
                  val IntegerClass = RootClass.newClass("Integer")
                  val LocalDateClass = RootClass.newClass("java.time.LocalDate")
                  val resultExpr = BLOCK(LocalDateClass.DOT("ofEpochDay").APPLY(REF("i").DOT("toInt")))
                  val integerConversion = CASE(ID("i") withType (IntegerClass)) ==> resultExpr
                  tree MATCH integerConversion
                } 
              }
              
            }
            else tree
          }
          case None => tree
        }
      }
      case _ => tree
    }
  }

}
