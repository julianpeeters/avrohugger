package avrohugger
package types

case class AvroScalaTypes(
  // primitive
  int:      AvroScalaNumberType   = ScalaInt,
  long:     AvroScalaNumberType   = ScalaLong,
  float:    AvroScalaNumberType   = ScalaFloat,
  double:   AvroScalaNumberType   = ScalaDouble,
  boolean:  AvroScalaBooleanType  = ScalaBoolean,
  string:   AvroScalaStringType   = ScalaString,
  `null`:   AvroScalaNullType     = ScalaNull,
  bytes:    AvroScalaBytesType    = ScalaByteArray,
  // complex
  fixed:    AvroScalaFixedType    = ScalaCaseClassWrapper,
  record:   AvroScalaRecordType   = ScalaCaseClass,
  `enum`:     AvroScalaEnumType   = ScalaEnumeration,
  union:    AvroScalaUnionType    = OptionEitherShapelessCoproduct,
  array:    AvroScalaArrayType    = ScalaSeq,
  map:      AvroScalaMapType      = ScalaMap,
  protocol: AvroScalaProtocolType = NoTypeGenerated,
  // logical
  decimal:  AvroScalaDecimalType  = ScalaBigDecimal(None),
  date:     AvroScalaDateType     = JavaTimeLocalDate,
  timestampMillis: AvroScalaTimestampMillisType = JavaTimeInstant,
  timeMillis: AvroScalaTimeMillisType = JavaTimeLocalTime,
  timeMicros: AvroScalaTimeType = JavaTimeLocalTime,
  timestampMicros: AvroScalaTimestampType = JavaTimeZonedDateTime,
  localTimestampMillis: AvroScalaLocalTimestampType = JavaTimeLocalDateTime,
  localTimestampMicros: AvroScalaLocalTimestampType = JavaTimeLocalDateTime,
  uuid:     AvroUuidType          = JavaUuid
)

object AvroScalaTypes {
  val defaults = new AvroScalaTypes()
}