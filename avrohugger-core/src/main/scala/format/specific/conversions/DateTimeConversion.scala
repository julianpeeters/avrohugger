package avrohugger
package format
package specific
package conversions

import java.lang
import java.time.{Instant, LocalDate, LocalDateTime, ZoneId}

import org.apache.avro.{Conversion, LogicalType, Schema}

object DateTimeConversion extends Conversion[LocalDateTime] {
  override def getConvertedType: Class[LocalDateTime] = classOf[LocalDateTime]

  override def getLogicalTypeName = "timestamp-millis"

  override def fromLong(millisFromEpoch: lang.Long, schema: Schema, `type`: LogicalType): LocalDateTime =
    LocalDateTime.ofInstant(Instant.ofEpochMilli(millisFromEpoch), ZoneId.of("UTC"))

  override def toLong(timestamp: LocalDateTime, schema: Schema, `type`: LogicalType): lang.Long =
    timestamp.atZone(ZoneId.of("UTC")).toInstant.toEpochMilli
}
