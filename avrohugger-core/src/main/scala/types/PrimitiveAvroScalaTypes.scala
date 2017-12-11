package avrohugger
package types

// numbers
sealed trait AvroScalaNumberType extends Product with Serializable
case object ScalaInt extends AvroScalaNumberType
case object ScalaLong extends AvroScalaNumberType
case object ScalaFloat extends AvroScalaNumberType
case object ScalaDouble extends AvroScalaNumberType
// boolean
sealed trait AvroScalaBooleanType extends Product with Serializable
case object ScalaBoolean extends AvroScalaBooleanType
// strings
sealed trait AvroScalaStringType extends Product with Serializable
case object ScalaString extends AvroScalaStringType
// null
sealed trait AvroScalaNullType extends Product with Serializable
case object ScalaNull extends AvroScalaNullType
// bytes
sealed trait AvroScalaBytesType extends Product with Serializable
case object ScalaByteArray extends AvroScalaBytesType