package avrohugger
package types

import org.apache.avro.Schema

sealed trait AvroScalaDecimalType extends Product with Serializable
case class ScalaBigDecimal(maybeRoundingMode: Option[BigDecimal.RoundingMode.Value]) extends AvroScalaDecimalType
case class ScalaBigDecimalWithPrecision(maybeRoundingMode: Option[BigDecimal.RoundingMode.Value]) extends AvroScalaDecimalType

sealed trait AvroScalaDateType extends Product with Serializable
case object JavaSqlDate extends AvroScalaDateType
case object JavaTimeLocalDate extends AvroScalaDateType

sealed trait AvroScalaTimestampMillisType extends Product with Serializable
case object JavaSqlTimestamp extends AvroScalaTimestampMillisType
case object JavaTimeInstant extends AvroScalaTimestampMillisType

sealed trait AvroUuidType extends Product with Serializable
case object JavaUuid extends AvroUuidType

sealed abstract class LogicalType(name: String)
case class Decimal(precision: Int, scale: Int) extends LogicalType("decimal")
case object Date extends LogicalType("date")
case object TimestampMillis extends LogicalType("timestamp-millis")
case object UUID extends LogicalType("uuid")

object LogicalType {
  
  def apply(logicalType: org.apache.avro.LogicalType): Option[LogicalType] = logicalType match {
    case d: org.apache.avro.LogicalTypes.Decimal => Some(Decimal(d.getPrecision, d.getScale))
    case _: org.apache.avro.LogicalTypes.Date => Some(Date)
    case _: org.apache.avro.LogicalTypes.TimestampMillis => Some(TimestampMillis)
    case _ if logicalType.getName == "uuid" => Some(UUID)
    case _ => None
  }
  
  def foldLogicalTypes[A](schema: Schema, default: => A)(cases : PartialFunction[LogicalType, A]): A =
    Option(schema.getLogicalType) match {
      case Some(tpe) => LogicalType(tpe).flatMap(cases.lift(_)).getOrElse(default)
      case _ => default
    }

}