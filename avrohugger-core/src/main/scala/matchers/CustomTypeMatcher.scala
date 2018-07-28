package avrohugger
package matchers

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

}