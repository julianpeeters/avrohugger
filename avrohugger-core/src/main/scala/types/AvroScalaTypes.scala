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
  fixed:    AvroScalaFixedType    = ScalaBinary,
  record:   AvroScalaRecordType   = ScalaCaseClass,
  enum:     AvroScalaEnumType     = ScalaEnumeration,
  union:    AvroScalaUnionType    = OptionEitherShapelessCoproduct,
  array:    AvroScalaArrayType    = ScalaList,
  map:      AvroScalaMapType      = ScalaMap,
  protocol: AvroScalaProtocolType = NoTypeGenerated
)

object AvroScalaTypes {
  def defaults = new AvroScalaTypes()
}