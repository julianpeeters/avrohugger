package avrohugger
package matchers
package custom

import avrohugger.matchers.custom.CustomUtils._
import avrohugger.types._
import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.Schema

object CustomDefaultParamMatcher {

  def checkCustomArrayType(arrayType: AvroScalaArrayType) = {
    arrayType match {
      case ScalaArray  => ArrayClass
      case ScalaList   => ListClass
      case ScalaSeq    => SeqClass
      case ScalaVector => VectorClass
    }
  }
  
  def checkCustomEnumType(enumType: AvroScalaEnumType) = {
    enumType match {
      case JavaEnum            => NULL // TODO Take first enum value?
      case ScalaEnumeration    => NULL // TODO Take first enum value?
      case ScalaCaseObjectEnum => NULL // TODO Take first enum value?
      case EnumAsScalaString   => LIT("")
    }
  }
  
  def checkCustomDateType(dateType: AvroScalaDateType) = dateType match {
    case JavaSqlDate       => NEW(REF("java.sql.Date"), LIT(0L))
    case JavaTimeLocalDate => REF("java.time.LocalDate.now")
    case UnderlyingPrimitive => LIT(0)
  }
  
  def checkCustomTimestampMillisType(timestampMillisType: AvroScalaTimestampMillisType) =
    timestampMillisType match {
      case JavaSqlTimestamp => NEW(REF("java.sql.Timestamp"), LIT(0L))
      case JavaTimeInstant  => REF("java.time.Instant.now")
      case UnderlyingPrimitive => LIT(0L)
    }

  def checkCustomDecimalType(decimalType: AvroScalaDecimalType, schema: Schema, default: => Tree, decimalValue: => Option[String] = None) = {
    val decimalValueRef = REF("scala.math.BigDecimal") APPLY decimalValue.map(LIT(_)).getOrElse(LIT(0))
    LogicalType.foldLogicalTypes[Tree](
      schema = schema,
      default = default) {
        case Decimal(precision, scale) => decimalType match {
          case ScalaBigDecimal(_) => decimalValueRef
          case ScalaBigDecimalWithPrecision(_) => decimalTagged(precision, scale) APPLY decimalValueRef
        }                
    }
  }

  def checkCustomTimeMillisType(timeMillisType: AvroScalaTimeMillisType) =
    timeMillisType match {
      case JavaSqlTime => NEW(REF("java.sql.Time"), LIT(0L))
      case JavaTimeLocalTime  => REF("java.time.LocalTime.now")
      case UnderlyingPrimitive => LIT(0L)
    }

  def checkCustomTimeMicrosType(timeMillisType: AvroScalaTimeType): Tree =
    timeMillisType match {
      case JavaTimeLocalTime => REF("java.time.LocalTime.MIDNIGHT")
      case UnderlyingPrimitive => LIT(0L)
    }

  def checkCustomTimestampMicrosType(timeMillisType: AvroScalaTimestampType): Tree =
    timeMillisType match {
      case JavaTimeZonedDateTime => REF("java.time.ZonedDateTime.of").APPLY(REF("java.time.LocalDateTime") DOT "MIN", REF("java.time.ZoneId") DOT "of" APPLY LIT("UTC"))
      case UnderlyingPrimitive => LIT(0L)
    }

  def checkCustomLocalTimestampMillisType(timeMillisType: AvroScalaLocalTimestampType): Tree =
    timeMillisType match {
      case JavaTimeLocalDateTime => REF("java.time.LocalDateTime") DOT "MIN"
      case UnderlyingPrimitive => LIT(0L)
    }

  def checkCustomLocalTimestampMicrosType(timeMillisType: AvroScalaLocalTimestampType): Tree =
    timeMillisType match {
      case JavaTimeLocalDateTime => REF("java.time.LocalDateTime") DOT "MIN"
      case UnderlyingPrimitive => LIT(0L)
    }
}