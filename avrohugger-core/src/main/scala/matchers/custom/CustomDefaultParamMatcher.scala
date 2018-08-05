package avrohugger
package matchers
package custom

import avrohugger.types._

import treehugger.forest._
import definitions._
import treehuggerDSL._
import java.sql.Timestamp

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
  }
  
  def checkCustomTimestampMillisType(timestampMillisType: AvroScalaTimestampMillisType) =
    timestampMillisType match {
      case JavaSqlTimestamp => NEW(REF("java.sql.Timestamp"), LIT(0L))
      case JavaTimeInstant  => REF("java.time.Instant.now")
    }
}