package avrohugger
package matchers
package custom

import avrohugger.matchers.custom.CustomUtils._
import avrohugger.stores.ClassStore
import avrohugger.types._
import org.apache.avro.Schema
import treehugger.forest._
import treehuggerDSL._
import definitions._

object CustomTypeMatcher {

  def checkCustomArrayType(arrayType: AvroScalaArrayType) = arrayType match {
    case ScalaArray => TYPE_ARRAY(_)
    case ScalaList => TYPE_LIST(_)
    case ScalaSeq => TYPE_SEQ(_)
    case ScalaVector => TYPE_VECTOR(_)
  }

  def checkCustomEnumType(
    enumType: AvroScalaEnumType,
    classStore: ClassStore,
    schema: Schema,
    useFullName: Boolean = false
  ) = enumType match {
    case JavaEnum => classStore.generatedClasses.get(schema.getFullName)
    case ScalaEnumeration =>
      if (useFullName)
        RootClass.newClass(s"${schema.getNamespace()}.${classStore.generatedClasses.get(schema.getFullName)}")
      else
        classStore.generatedClasses.get(schema.getFullName)
    case ScalaCaseObjectEnum => classStore.generatedClasses.get(schema.getFullName)
    case EnumAsScalaString => StringClass
  }

  def checkCustomNumberType(numberType: AvroScalaNumberType) = numberType match {
    case ScalaDouble => DoubleClass
    case ScalaFloat => FloatClass
    case ScalaLong => LongClass
    case ScalaInt => IntClass
  }

  def checkCustomDateType(dateType: AvroScalaDateType) = dateType match {
    case JavaTimeLocalDate => RootClass.newClass(nme.createNameType("java.time.LocalDate"))
    case JavaSqlDate => RootClass.newClass(nme.createNameType("java.sql.Date"))
    case UnderlyingPrimitive => IntClass
  }

  def checkCustomTimestampMillisType(timestampType: AvroScalaTimestampMillisType) = timestampType match {
    case JavaSqlTimestamp => RootClass.newClass(nme.createNameType("java.sql.Timestamp"))
    case JavaTimeInstant => RootClass.newClass(nme.createNameType("java.time.Instant"))
    case UnderlyingPrimitive => LongClass
  }

  def checkCustomTimeMillisType(timeType: AvroScalaTimeMillisType) = timeType match {
    case JavaSqlTime => RootClass.newClass(nme.createNameType("java.sql.Time"))
    case JavaTimeLocalTime => RootClass.newClass(nme.createNameType("java.time.LocalTime"))
    case UnderlyingPrimitive => LongClass
  }

  def checkCustomDecimalType(decimalType: AvroScalaDecimalType, schema: Schema): Type =
    LogicalType.foldLogicalTypes(
      schema = schema,
      default = TYPE_ARRAY(ByteClass)) {
      case Decimal(precision, scale) => decimalType match {
        case ScalaBigDecimal(_) => BigDecimalClass
        case ScalaBigDecimalWithPrecision(_) => decimalTaggedType(precision, scale)
      }
    }

  def checkCustomTimeMicrosType(timeType: AvroScalaTimeType) = timeType match {
    case JavaTimeLocalTime => RootClass.newClass(nme.createNameType("java.time.LocalTime"))
    case UnderlyingPrimitive => LongClass
  }

  def checkCustomTimestampMicrosType(timeType: AvroScalaTimestampType) = timeType match {
    case JavaTimeZonedDateTime => RootClass.newClass(nme.createNameType("java.time.ZonedDateTime"))
    case UnderlyingPrimitive => LongClass
  }

  def checkCustomLocalTimestampMicrosType(timeType: AvroScalaLocalTimestampType) = timeType match {
    case JavaTimeLocalDateTime => RootClass.newClass(nme.createNameType("java.time.LocalDateTime"))
    case UnderlyingPrimitive => LongClass
  }

  def checkCustomLocalTimestampMillisType(timeType: AvroScalaLocalTimestampType) = timeType match {
    case JavaTimeLocalDateTime => RootClass.newClass(nme.createNameType("java.time.LocalDateTime"))
    case UnderlyingPrimitive => LongClass
  }
}