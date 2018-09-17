package avrohugger
package types

import org.apache.avro.Schema

sealed trait AvroScalaDecimalType extends Product with Serializable
case object ScalaBigDecimal extends AvroScalaDecimalType

sealed trait AvroScalaDateType extends Product with Serializable
case object JavaSqlDate extends AvroScalaDateType
case object JavaTimeLocalDate extends AvroScalaDateType

sealed trait AvroScalaTimestampMillisType extends Product with Serializable
case object JavaSqlTimestamp extends AvroScalaTimestampMillisType
case object JavaTimeInstant extends AvroScalaTimestampMillisType

sealed trait AvroUuidType extends Product with Serializable
case object JavaUuid extends AvroUuidType

sealed abstract class LogicalType(name: String)
case object Decimal extends LogicalType("decimal")
case object Date extends LogicalType("date")
case object TimestampMillis extends LogicalType("timestamp-millis")
case object UUID extends LogicalType("uuid")

object LogicalType {
  
  def apply(name: String): Option[LogicalType] = name match {
    case "decimal" => Some(Decimal)
    case "date" => Some(Date)
    case "timestamp-millis" => Some(TimestampMillis)
    case "uuid" => Some(UUID)
    case _ => None
  }
  
  def foldLogicalTypes[A](schema: Schema, default: => A)(cases : PartialFunction[LogicalType, A]): A =
    Option(schema.getLogicalType) match {
      case Some(tpe) => LogicalType(tpe.getName).map(cases(_)).getOrElse(default)
      case _ => default
    }

}