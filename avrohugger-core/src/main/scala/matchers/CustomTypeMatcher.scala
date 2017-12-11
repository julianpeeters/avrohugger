package avrohugger
package matchers

import avrohugger.types._

import treehugger.forest._
import treehuggerDSL._
import definitions._

object CustomTypeMatcher {

  def checkCustomArrayType(arrayType: AvroScalaArrayType) = arrayType match {
    case ScalaArray  => TYPE_ARRAY(_)
    case ScalaList   => TYPE_LIST(_)
    case ScalaVector => TYPE_VECTOR(_)
  }

  def checkCustomNumberType(numberType: AvroScalaNumberType) = numberType match {
    case ScalaDouble => DoubleClass
    case ScalaFloat  => FloatClass
    case ScalaLong   => LongClass
    case ScalaInt    => IntClass
  }

}