/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.logical

import scala.annotation.switch

final case class LogicalSc(var data: BigDecimal, var fxField: example.logical.fxType, var ts: java.time.Instant, var dt: java.time.LocalDate, var uuid: java.util.UUID, var dataBig: BigDecimal, var tm: java.time.LocalTime, var timeMicros: java.time.LocalTime, var timestampMicros: java.time.ZonedDateTime, var localTimestampMicros: java.time.LocalDateTime, var localTimestampMillis: java.time.LocalDateTime) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(scala.math.BigDecimal(0), example.logical.fxType(scala.math.BigDecimal(0)), java.time.Instant.now, java.time.LocalDate.now, java.util.UUID.randomUUID, scala.math.BigDecimal(0), java.time.LocalTime.now, java.time.LocalTime.MIDNIGHT, java.time.ZonedDateTime.of(java.time.LocalDateTime.MIN, java.time.ZoneId.of("UTC")), java.time.LocalDateTime.MIN, java.time.LocalDateTime.MIN)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        val schema = getSchema.getFields().get(field$).schema()
        val decimalType = schema.getLogicalType().asInstanceOf[org.apache.avro.LogicalTypes.Decimal]
        val scale = decimalType.getScale()
        val scaledValue = data.setScale(scale)
        val bigDecimal = scaledValue.bigDecimal
        LogicalSc.decimalConversion.toBytes(bigDecimal, schema, decimalType)
      }.asInstanceOf[AnyRef]
      case 1 => {
        val schema = getSchema.getFields().get(field$).schema()
        val decimalType = schema.getLogicalType().asInstanceOf[org.apache.avro.LogicalTypes.Decimal]
        val scale = decimalType.getScale()
        val scaledValue = fxField.bigDecimal.setScale(scale)
        val bigDecimal = scaledValue.bigDecimal
        LogicalSc.decimalConversion.toFixed(bigDecimal, schema, decimalType)
      }.asInstanceOf[AnyRef]
      case 2 => {
        ts.toEpochMilli
      }.asInstanceOf[AnyRef]
      case 3 => {
        dt.toEpochDay.toInt
      }.asInstanceOf[AnyRef]
      case 4 => {
        uuid.toString
      }.asInstanceOf[AnyRef]
      case 5 => {
        val schema = getSchema.getFields().get(field$).schema()
        val decimalType = schema.getLogicalType().asInstanceOf[org.apache.avro.LogicalTypes.Decimal]
        val scale = decimalType.getScale()
        val scaledValue = dataBig.setScale(scale)
        val bigDecimal = scaledValue.bigDecimal
        LogicalSc.decimalConversion.toBytes(bigDecimal, schema, decimalType)
      }.asInstanceOf[AnyRef]
      case 6 => {
        tm.get(java.time.temporal.ChronoField.MILLI_OF_DAY)
      }.asInstanceOf[AnyRef]
      case 7 => {
        // avro time-micros long stores the number of microseconds after midnight, 00:00:00.000000
        {
          timeMicros.toNanoOfDay / 1000L
        }
      }.asInstanceOf[AnyRef]
      case 8 => {
        // avro timestamp-micros long stores the number of microseconds from the unix epoch, 1 January 1970 00:00:00.000000 UTC
        {
          timestampMicros.toEpochSecond * 1000000L + (timestampMicros.getNano / 1000L)
        }
      }.asInstanceOf[AnyRef]
      case 9 => {
        // avro local-timestamp-micros long stores the number of microseconds, from 1 January 1970 00:00:00.000000
        {
          localTimestampMicros.toEpochSecond(java.time.ZoneOffset.UTC) * 1000000L + (localTimestampMicros.getNano / 1000L)
        }
      }.asInstanceOf[AnyRef]
      case 10 => {
        // avro local-timestamp-millis long stores the number of millis, from 1 January 1970 00:00:00.000000
        {
          localTimestampMillis.toEpochSecond(java.time.ZoneOffset.UTC) * 1000L + (localTimestampMillis.getNano / 1000000L)
        }
      }.asInstanceOf[AnyRef]
      case _ => throw new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.data = {
        value match {
          case (buffer: java.nio.ByteBuffer) => {
            val schema = getSchema.getFields().get(field$).schema()
            val decimalType = schema.getLogicalType().asInstanceOf[org.apache.avro.LogicalTypes.Decimal]
            BigDecimal(LogicalSc.decimalConversion.fromBytes(buffer, schema, decimalType))
          }
          case _ => throw new org.apache.avro.AvroRuntimeException("expected type java.nio.ByteBuffer")
        }
      }.asInstanceOf[BigDecimal]
      case 1 => this.fxField = {
        value
      }.asInstanceOf[example.logical.fxType]
      case 2 => this.ts = {
        value match {
          case (l: Long) => {
            java.time.Instant.ofEpochMilli(l)
          }
          case _ => throw new org.apache.avro.AvroRuntimeException("expected type Long")
        }
      }.asInstanceOf[java.time.Instant]
      case 3 => this.dt = {
        value match {
          case (i: Integer) => {
            java.time.LocalDate.ofEpochDay(i.toLong)
          }
          case _ => throw new org.apache.avro.AvroRuntimeException("expected type Integer")
        }
      }.asInstanceOf[java.time.LocalDate]
      case 4 => this.uuid = {
        value match {
          case (chars: java.lang.CharSequence) => {
            java.util.UUID.fromString(chars.toString)
          }
          case _ => throw new org.apache.avro.AvroRuntimeException("expected type CharSequence")
        }
      }.asInstanceOf[java.util.UUID]
      case 5 => this.dataBig = {
        value match {
          case (buffer: java.nio.ByteBuffer) => {
            val schema = getSchema.getFields().get(field$).schema()
            val decimalType = schema.getLogicalType().asInstanceOf[org.apache.avro.LogicalTypes.Decimal]
            BigDecimal(LogicalSc.decimalConversion.fromBytes(buffer, schema, decimalType))
          }
          case _ => throw new org.apache.avro.AvroRuntimeException("expected type java.nio.ByteBuffer")
        }
      }.asInstanceOf[BigDecimal]
      case 6 => this.tm = {
        value match {
          case (i: Integer) => {
            java.time.LocalTime.ofNanoOfDay(i * 1000000L)
          }
          case _ => throw new org.apache.avro.AvroRuntimeException("expected type Integer")
        }
      }.asInstanceOf[java.time.LocalTime]
      case 7 => this.timeMicros = {
        // avro time-micros long stores the number of microseconds after midnight, 00:00:00.000000
        value match {
          case (l: Long) => {
            java.time.LocalTime.ofNanoOfDay(l * 1000L)
          }
          case _ => throw new org.apache.avro.AvroRuntimeException("expected type Long")
        }
      }.asInstanceOf[java.time.LocalTime]
      case 8 => this.timestampMicros = {
        // avro timestamp-micros long stores the number of microseconds from the unix epoch, 1 January 1970 00:00:00.000000 UTC
        value match {
          case (l: Long) => {
            java.time.ZonedDateTime.of(java.time.LocalDateTime.ofEpochSecond(l / 1000000L, (l % 1000000L).toInt * 1000, java.time.ZoneOffset.UTC), java.time.ZoneId.of("UTC"))
          }
          case _ => throw new org.apache.avro.AvroRuntimeException("expected type Long")
        }
      }.asInstanceOf[java.time.ZonedDateTime]
      case 9 => this.localTimestampMicros = {
        // avro local-timestamp-micros long stores the number of microseconds, from 1 January 1970 00:00:00.000000
        value match {
          case (l: Long) => {
            java.time.LocalDateTime.ofEpochSecond(l / 1000000L, (l % 1000000L).toInt * 1000, java.time.ZoneOffset.UTC)
          }
          case _ => throw new org.apache.avro.AvroRuntimeException("expected type Long")
        }
      }.asInstanceOf[java.time.LocalDateTime]
      case 10 => this.localTimestampMillis = {
        // avro local-timestamp-millis long stores the number of millis, from 1 January 1970 00:00:00.000000
        value match {
          case (l: Long) => {
            java.time.LocalDateTime.ofEpochSecond(l / 1000L, (l % 1000L).toInt * 1000000, java.time.ZoneOffset.UTC)
          }
          case _ => throw new org.apache.avro.AvroRuntimeException("expected type Long")
        }
      }.asInstanceOf[java.time.LocalDateTime]
      case _ => throw new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = example.logical.LogicalSc.SCHEMA$
}

object LogicalSc {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"LogicalSc\",\"namespace\":\"example.logical\",\"fields\":[{\"name\":\"data\",\"type\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":9,\"scale\":2}},{\"name\":\"fxField\",\"type\":{\"type\":\"fixed\",\"name\":\"fxType\",\"size\":2,\"logicalType\":\"decimal\",\"precision\":2,\"scale\":1}},{\"name\":\"ts\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}},{\"name\":\"dt\",\"type\":{\"type\":\"int\",\"logicalType\":\"date\"}},{\"name\":\"uuid\",\"type\":{\"type\":\"string\",\"logicalType\":\"uuid\"}},{\"name\":\"dataBig\",\"type\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":20,\"scale\":12}},{\"name\":\"tm\",\"type\":{\"type\":\"int\",\"logicalType\":\"time-millis\"}},{\"name\":\"timeMicros\",\"type\":{\"type\":\"long\",\"logicalType\":\"time-micros\"}},{\"name\":\"timestampMicros\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-micros\"}},{\"name\":\"localTimestampMicros\",\"type\":{\"type\":\"long\",\"logicalType\":\"local-timestamp-micros\"}},{\"name\":\"localTimestampMillis\",\"type\":{\"type\":\"long\",\"logicalType\":\"local-timestamp-millis\"}}]}")
  val decimalConversion = new org.apache.avro.Conversions.DecimalConversion
}