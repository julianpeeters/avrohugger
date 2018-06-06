package avrohugger
package format
package specific
package conversions

import java.lang
import java.time.{Instant, LocalDate, LocalDateTime, ZoneId}

import org.apache.avro.{Conversion, LogicalType, Schema}

object DateConversion extends Conversion[LocalDate] {

  override def getConvertedType: Class[LocalDate] = classOf[LocalDate]

  override def getLogicalTypeName: String = "date"

  override def fromInt(daysFromEpoch: Integer, schema: Schema, `type`: LogicalType): LocalDate = LocalDate.ofEpochDay(daysFromEpoch.toInt)

  override def toInt(date: LocalDate, schema: Schema, `type`: LogicalType): Integer = date.toEpochDay.toInt
}
