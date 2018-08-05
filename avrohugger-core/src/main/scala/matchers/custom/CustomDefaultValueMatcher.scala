package avrohugger
package matchers
package custom

import avrohugger.types._
import treehugger.forest._
import treehuggerDSL._
import definitions._

object CustomDefaultValueMatcher {
  
  def checkCustomDateType(value: Long, dateType: AvroScalaDateType) = dateType match {
    case JavaSqlDate => NEW("java.sql.Date", LIT(value))
    case JavaTimeLocalDate => REF("java.time.LocalDate.ofEpochDay") APPLY LIT(value)
  }
  
  def checkCustomTimestampMillisType(value: Long, timestampMillisType: AvroScalaTimestampMillisType) =
    timestampMillisType match {
      case JavaSqlTimestamp => NEW("java.sql.Timestamp", LIT(value))
      case JavaTimeInstant   => REF("java.time.Instant.ofEpochMilli") APPLY LIT(value)
    }

}