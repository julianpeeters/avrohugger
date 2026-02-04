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
import scala.jdk.CollectionConverters._


object ScalaConverter {

  def asScalaIteratorConverter(partialVersion: String): String =
    partialVersion match {
      case "2.11" => "scala.collection.JavaConverters.asScalaIteratorConverter"
      case "2.12" => "scala.collection.JavaConverters.asScalaIteratorConverter"
      case "2.13" => "scala.jdk.CollectionConverters.IteratorHasAsScala"
      case _ => "scala.jdk.CollectionConverters.IteratorHasAsScala"
    }

  def mapAsScalaMapConverter(partialVersion: String): String =
    partialVersion match {
      case "2.11" => "scala.collection.JavaConverters.mapAsScalaMapConverter"
      case "2.12" => "scala.collection.JavaConverters.mapAsScalaMapConverter"
      case "2.13" => "scala.jdk.CollectionConverters.MapHasAsScala"
      case _ => "scala.jdk.CollectionConverters.MapHasAsScala"
    }

  def asScalaBufferConverter(partialVersion: String): String = {
    partialVersion match {
      case "2.11" => "scala.collection.JavaConverters.asScalaBufferConverter"
      case "2.12" => "scala.collection.JavaConverters.asScalaBufferConverter"
      case "2.13" => "scala.jdk.CollectionConverters.ListHasAsScala"
      case _ => "scala.jdk.CollectionConverters.ListHasAsScala"
    }
  }

  def checkCustomArrayType(
    arrayType: AvroScalaArrayType,
    elementType: Type,
    asScalaTree: Tree): Tree = {
    val classTagIdent = REF(s"scala.reflect.ClassTag(classOf[$elementType])")
    arrayType match {
      case ScalaArray  => asScalaTree.DOT("toArray").APPLY(classTagIdent).AS(TYPE_ARRAY(elementType))
      case ScalaList   => asScalaTree.DOT("toList")
      case ScalaSeq    => asScalaTree.DOT("toSeq")
      case ScalaVector => asScalaTree.DOT("toVector")
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
    classSymbol: ClassSymbol,
    targetScalaPartialVersion: String): Tree = {
    schema.getType match {
      case Schema.Type.ARRAY => {
        val elementSchema = schema.getElementType
        val elementType = typeMatcher.toScalaType(classStore, namespace, elementSchema)
        val JavaList = javaListConvertor(targetScalaPartialVersion)
        val applyParam = REF("array") DOT("iterator")
        val elementConversion = convertFromJava(
          classStore,
          namespace,
          elementSchema,
          if (isUnionMember) arrayAccessor(unionAccessor(schemaAccessor, schema.getFullName, asScalaBufferConverter(targetScalaPartialVersion))) else arrayAccessor(schemaAccessor),
          false,
          REF("x"),
          typeMatcher,
          classSymbol,
          targetScalaPartialVersion)
        val asScalaTree: Tree = {
            REF(asScalaIteratorConverter(targetScalaPartialVersion)).APPLY(applyParam).DOT("asScala")
              .DOT("map").APPLY(LAMBDA(PARAM("x")) ==> BLOCK(elementConversion))
        }
        val arrayType = typeMatcher.avroScalaTypes.array
        val resultExpr = BLOCK(
          checkCustomArrayType(arrayType, elementType, asScalaTree)
        )
        val arrayConversion = CASE(ID("array") withType(JavaList)) ==> resultExpr
        val errorMessage = LIT(s"expected array with type java.util.List[_]")
        val errorExpr = THROW(NEW("org.apache.avro.AvroRuntimeException", errorMessage))
        val arrayMatchError = CASE(WILDCARD) ==> errorExpr
        val conversionCases = List(arrayConversion, arrayMatchError)
        tree MATCH(conversionCases)
      }
      case Schema.Type.STRING =>
        LogicalType.foldLogicalTypes(
          schema = schema,
          default = tree TOSTRING) {
          case UUID =>
            typeMatcher.avroScalaTypes.uuid match {
              case JavaUuid => {
                val UuidClass = RootClass.newClass("java.util.UUID")
                val resultExpr = UuidClass.DOT("fromString").APPLY(REF("chars").TOSTRING)
                val charSequenceConversion = CASE(ID("chars") withType CharSequenceClass) ==> resultExpr
                val uuidConversion = CASE(ID("u") withType (UuidClass)) ==> REF("u")
                val matchError = CASE(REF("x")) ==> THROW(NEW("org.apache.avro.AvroRuntimeException", LIT(s"expected type $CharSequenceClass or $UuidClass but found $${x.getClass}")))
                tree MATCH(charSequenceConversion, uuidConversion, matchError)
              }
            }
        }
      case Schema.Type.MAP => {
        val JavaMap = RootClass.newClass("java.util.Map[_,_]")
        val resultExpr = {
          BLOCK(
            REF(mapAsScalaMapConverter(targetScalaPartialVersion))
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
                if (isUnionMember) mapAccessor(unionAccessor(schemaAccessor, schema.getFullName, asScalaBufferConverter(targetScalaPartialVersion))) else mapAccessor(schemaAccessor),
                false,
                REF("value"),
                typeMatcher,
                classSymbol,
                targetScalaPartialVersion)))
            )
          )
        }
        val mapConversion = CASE(ID("map") withType(JavaMap)) ==> resultExpr
        val errorMessage = LIT(s"expected array with type java.util.List[_]")
        val errorExpr = THROW(NEW("org.apache.avro.AvroRuntimeException", errorMessage))
        val arrayMatchError = CASE(WILDCARD) ==> errorExpr
        tree MATCH(mapConversion, arrayMatchError)
      }
      case Schema.Type.FIXED => tree
      case Schema.Type.BYTES => {
        val JavaBuffer = RootClass.newClass("java.nio.ByteBuffer")
        val resultExpr = schema.getLogicalType match {
          case decimal: LogicalTypes.Decimal => {
            val Decimal = RootClass.newClass("org.apache.avro.LogicalTypes.Decimal")
            Block(
              VAL("schema") := {if (isUnionMember) unionAccessor(schemaAccessor, schema.getFullName, asScalaBufferConverter(targetScalaPartialVersion)) else schemaAccessor},
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
        val matchError = CASE(WILDCARD) ==> THROW(NEW("org.apache.avro.AvroRuntimeException", LIT(s"expected type $JavaBuffer")))
        tree MATCH(bufferConversion, matchError)
      }
      case Schema.Type.UNION => {
        val types = schema.getTypes().asScala.toList
        val hasNull = types.exists(_.getType == Schema.Type.NULL)
        val typeParamSchemas = types.filterNot(_.getType == Schema.Type.NULL)
        val expr = convertFromJava(
          classStore,
          namespace,
          typeParamSchemas.head,
          schemaAccessor,
          true,
          tree,
          typeMatcher,
          classSymbol,
          targetScalaPartialVersion
        )
        if (hasNull) {
          val nullConversion = CASE(NULL) ==> NONE
          val someConversion = CASE(WILDCARD) ==> SOME(expr)
          val conversionCases = List(nullConversion, someConversion)
          tree MATCH (conversionCases)
        } else {
          tree
        }
      }
      case Schema.Type.ENUM => {
        typeMatcher.avroScalaTypes.`enum` match {
          case EnumAsScalaString => tree TOSTRING
          case JavaEnum | ScalaEnumeration | ScalaCaseObjectEnum | Scala3Enum => tree
        }
      }
      case Schema.Type.LONG => {
        val InstantClass = RootClass.newClass("java.time.Instant")
        Option(schema.getLogicalType()) match {
          case Some(logicalType) => {
            if (logicalType.getName == "time-micros") {
              val matchError = CASE(REF("x")) ==> THROW(NEW("org.apache.avro.AvroRuntimeException", INTERP("s", LIT(s"expected type $LongClass or $InstantClass but found $${x.getClass}"))))
              (typeMatcher.avroScalaTypes.timeMicros match {
                case JavaTimeLocalTime =>
                  val LocalTimeClass = RootClass.newClass("java.time.LocalTime")
                  val longConversion = CASE(ID("l") withType (LongClass)) ==> LocalTimeClass.DOT("ofNanoOfDay").APPLY(REF("l").INFIX("*", LIT(1000L)))
                  val localTimeConversion = CASE(ID("t") withType (LocalTimeClass)) ==> REF("t")
                  tree MATCH(longConversion, localTimeConversion, matchError)
                case UnderlyingPrimitive => tree
              }) withComment "avro time-micros long stores the number of microseconds after midnight, 00:00:00.000000"
            } else if (logicalType.getName == "timestamp-millis") {
              val matchError = CASE(REF("x")) ==> THROW(NEW("org.apache.avro.AvroRuntimeException", INTERP("s", LIT(s"expected type $LongClass or $InstantClass but found $${x.getClass}"))))
              typeMatcher.avroScalaTypes.timestampMillis match {
                case JavaSqlTimestamp => {
                  val TimestampClass = RootClass.newClass("java.sql.Timestamp")
                  val resultExpr = NEW(TimestampClass, REF("l"))
                  val longConversion = CASE(ID("l") withType (LongClass)) ==> resultExpr
                  val timestampConversion = CASE(ID("i") withType (InstantClass)) ==> TimestampClass.DOT("from").APPLY(REF("i"))
                  tree MATCH(longConversion, timestampConversion, matchError)
                }
                case JavaTimeInstant  => {
                  val longConversion = CASE(ID("l") withType (LongClass)) ==> InstantClass.DOT("ofEpochMilli").APPLY(REF("l"))
                  val instantConversion = CASE(ID("i") withType (InstantClass)) ==> REF("i")
                  tree MATCH(longConversion, instantConversion, matchError)
                }
                case UnderlyingPrimitive => tree
              }
            } else if (logicalType.getName == "timestamp-micros") {
              (typeMatcher.avroScalaTypes.timestampMicros match {
                case JavaTimeZonedDateTime =>
                  val ZonedDateTime = RootClass.newClass("java.time.ZonedDateTime")
                  val LocalDateTime = RootClass.newClass("java.time.LocalDateTime")
                  val ZoneOffset = RootClass.newClass("java.time.ZoneOffset")
                  val ZoneId = RootClass.newClass("java.time.ZoneId")
                  val matchError = CASE(REF("x")) ==> THROW(NEW("org.apache.avro.AvroRuntimeException", INTERP("s", LIT(s"expected type $LongClass or $ZonedDateTime but found $${x.getClass}"))))
                  val resultExpr = ZonedDateTime.DOT("of").APPLY(LocalDateTime DOT "ofEpochSecond" APPLY(
                    REF("l").INFIX("/", LIT(1000000L)),
                    PAREN(REF("l").INFIX("%", LIT(1000000L))) DOT "toInt" INFIX("*", LIT(1000)),
                    ZoneOffset DOT "UTC"
                  ), ZoneId DOT "of" APPLY LIT("UTC"))
                  val longConversion = CASE(ID("l") withType (LongClass)) ==> resultExpr
                  val timeZoneConversion = CASE(ID("t") withType (ZonedDateTime)) ==> REF("t")
                  tree MATCH(longConversion, timeZoneConversion, matchError)
                case UnderlyingPrimitive => tree
              }) withComment "avro timestamp-micros long stores the number of microseconds from the unix epoch, 1 January 1970 00:00:00.000000 UTC"
            } else if (logicalType.getName == "local-timestamp-millis") {
              (typeMatcher.avroScalaTypes.localTimestampMillis match {
                case JavaTimeLocalDateTime =>
                  val LocalDateTime = RootClass.newClass("java.time.LocalDateTime")
                  val ZoneOffset = RootClass.newClass("java.time.ZoneOffset")
                  val resultExpr = LocalDateTime DOT "ofEpochSecond" APPLY(
                    REF("l").INFIX("/", LIT(1000L)),
                    PAREN(REF("l").INFIX("%", LIT(1000L))) DOT "toInt" INFIX("*", LIT(1000000)),
                    ZoneOffset DOT "UTC"
                  )
                  val longConversion = CASE(ID("l") withType (LongClass)) ==> resultExpr
                  val localDateTimeConversion = CASE(ID("t") withType (LocalDateTime)) ==> REF("t")
                  val matchError = CASE(REF("x")) ==> THROW(NEW("org.apache.avro.AvroRuntimeException", INTERP("s", LIT(s"expected type $LongClass or $LocalDateTime but found $${x.getClass}"))))
                  tree MATCH(longConversion, localDateTimeConversion, matchError)
                case UnderlyingPrimitive => tree
              }) withComment "avro local-timestamp-millis long stores the number of millis, from 1 January 1970 00:00:00.000000"
            } else if (logicalType.getName == "local-timestamp-micros") {
              (typeMatcher.avroScalaTypes.localTimestampMicros match {
                case JavaTimeLocalDateTime =>
                  val LocalDateTime = RootClass.newClass("java.time.LocalDateTime")
                  val ZoneOffset = RootClass.newClass("java.time.ZoneOffset")
                  val resultExpr = LocalDateTime DOT "ofEpochSecond" APPLY(
                    REF("l").INFIX("/", LIT(1000000L)),
                    PAREN(REF("l").INFIX("%", LIT(1000000L))) DOT "toInt" INFIX("*", LIT(1000)),
                    ZoneOffset DOT "UTC"
                  )
                  val longConversion = CASE(ID("l") withType (LongClass)) ==> resultExpr
                  val localDateTimeConversion = CASE(ID("t") withType (LocalDateTime)) ==> REF("t")
                  val matchError = CASE(REF("x")) ==> THROW(NEW("org.apache.avro.AvroRuntimeException", INTERP("s", LIT(s"expected type $LongClass or $LocalDateTime but found $${x.getClass}"))))
                  tree MATCH(longConversion, localDateTimeConversion, matchError)
                case UnderlyingPrimitive => tree
              }) withComment "avro local-timestamp-micros long stores the number of microseconds, from 1 January 1970 00:00:00.000000"
            }
            else tree
          }
          case None => tree
        }
      }
      case Schema.Type.INT => {
        Option(schema.getLogicalType()) match {
          case Some(logicalType) => {
            val IntegerClass = RootClass.newClass("Integer")
            val LocalDateClass = RootClass.newClass("java.time.LocalDate")
            if (logicalType.getName == "date") {
              val matchError = CASE(REF("x")) ==> THROW(NEW("org.apache.avro.AvroRuntimeException", INTERP("s", LIT(s"expected type $IntegerClass or $LocalDateClass but found $${x.getClass}"))))
              typeMatcher.avroScalaTypes.date match {
                case JavaSqlDate => {
                  val SqlDateClass = RootClass.newClass("java.sql.Date")
                  val resultExpr = NEW(SqlDateClass, REF("i").DOT("toLong").DOT("*").APPLY(LIT(86400000L)))
                  val integerConversion = CASE(ID("i") withType (IntegerClass)) ==> resultExpr
                  val dateConversion = CASE(ID("d") withType (LocalDateClass)) ==> SqlDateClass.DOT("valueOf").APPLY(REF("d"))
                  tree MATCH(integerConversion, dateConversion, matchError)
                }
                case JavaTimeLocalDate => {
                  val resultExpr = LocalDateClass.DOT("ofEpochDay").APPLY(REF("i").DOT("toLong"))
                  val integerConversion = CASE(ID("i") withType (IntegerClass)) ==> resultExpr
                  val dateConversion = CASE(ID("d") withType (LocalDateClass)) ==> REF("d")
                  tree MATCH(integerConversion, dateConversion, matchError)
                }
                case UnderlyingPrimitive => tree
              }
            }
            else if (logicalType.getName == "time-millis") {
              val LocalTimeClass = RootClass.newClass("java.time.LocalTime")
              val matchError = CASE(REF("x")) ==> THROW(NEW("org.apache.avro.AvroRuntimeException", INTERP("s", LIT(s"expected type $IntegerClass or $LocalTimeClass but found $${x.getClass}"))))
              typeMatcher.avroScalaTypes.timeMillis match {
                case JavaSqlTime => {
                  val SqlTimeClass = RootClass.newClass("java.sql.Time")
                  val resultExpr = NEW(SqlTimeClass, REF("i").DOT("toLong"))
                  val integerConversion = CASE(ID("i") withType (IntegerClass)) ==> resultExpr
                  val timeConversion = CASE(ID("t") withType (LocalTimeClass)) ==> SqlTimeClass.DOT("valueOf").APPLY(REF("t"))
                  tree MATCH(integerConversion, timeConversion, matchError)
                }
                case JavaTimeLocalTime => {
                  val resultExpr = LocalTimeClass.DOT("ofNanoOfDay").APPLY(REF("i").INFIX("*", LIT(1000000L)))
                  val integerConversion = CASE(ID("i") withType (IntegerClass)) ==> resultExpr
                  val timeConversion = CASE(ID("t") withType (LocalTimeClass)) ==> REF("t")
                  tree MATCH(integerConversion, timeConversion, matchError)
                }
                case UnderlyingPrimitive => tree
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

  def javaListConvertor(targetScalaPartialVersion: String) =
    if (targetScalaPartialVersion.startsWith("3."))
      RootClass.newClass("java.util.List[?]")
    else
      RootClass.newClass("java.util.List[_]")
}
