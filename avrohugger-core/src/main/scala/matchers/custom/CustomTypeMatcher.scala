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
    case ScalaArray  => TYPE_ARRAY(_)
    case ScalaList   => TYPE_LIST(_)
    case ScalaSeq    => TYPE_SEQ(_)
    case ScalaVector => TYPE_VECTOR(_)
  }
  
  def checkCustomEnumType(
    enumType: AvroScalaEnumType,
    classStore: ClassStore,
    schema: Schema) = enumType match {
      case JavaEnum => classStore.generatedClasses(schema)
      case ScalaEnumeration => classStore.generatedClasses(schema)
      case ScalaCaseObjectEnum => classStore.generatedClasses(schema)
      case EnumAsScalaString => StringClass
    }

  def checkCustomNumberType(numberType: AvroScalaNumberType) = numberType match {
    case ScalaDouble => DoubleClass
    case ScalaFloat  => FloatClass
    case ScalaLong   => LongClass
    case ScalaInt    => IntClass
  }
  
  def checkCustomDateType(dateType: AvroScalaDateType) = dateType match {
    case JavaTimeLocalDate => RootClass.newClass(nme.createNameType("java.time.LocalDate"))
    case JavaSqlDate       => RootClass.newClass(nme.createNameType("java.sql.Date"))
  } 
    
  def checkCustomTimestampMillisType(timestampType: AvroScalaTimestampMillisType) = timestampType match {
    case JavaSqlTimestamp => RootClass.newClass(nme.createNameType("java.sql.Timestamp"))
    case JavaTimeInstant  => RootClass.newClass(nme.createNameType("java.time.Instant"))
  }

  def checkCustomDecimalType(decimalType: AvroScalaDecimalType, schema: Schema) =
      LogicalType.foldLogicalTypes(
        schema = schema,
        default = TYPE_ARRAY(ByteClass)) {
          case Decimal(precision, scale) => decimalType match {
            case ScalaBigDecimal(_) => BigDecimalClass
            case ScalaBigDecimalWithPrecision(_) => decimalTaggedType(precision, scale)
          }
        }
}