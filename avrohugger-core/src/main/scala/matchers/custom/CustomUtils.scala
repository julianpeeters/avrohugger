package avrohugger
package matchers
package custom

import treehugger.forest._
import treehuggerDSL._

object CustomUtils {

  def decimalTagged(precision: Int, scale: Int): Tree =
    REF("shapeless.tag") APPLYTYPE precisionScaleType(precision, scale) APPLYTYPE TYPE_REF("scala.math.BigDecimal")

  def decimalTaggedType(precision: Int, scale: Int): Type =
    // TYPE_REF(REF("scala.math.BigDecimal") INFIX ("@@", precisionScaleType(precision, scale)))
    TYPE_REF(REF("@@") APPLYTYPE(TYPE_REF("scala.math.BigDecimal"), precisionScaleType(precision, scale)))


  private[this] def numberToNat(int: Int): Type =
    int.toString.map(n => TYPE_REF("shapeless.Nat._" + n)).toList match {
      case head :: Nil => head
      case list        => TYPE_TUPLE(list)
    }

  private[this] def precisionScaleType(precision: Int, scale: Int): Type =
    TYPE_TUPLE(numberToNat(precision), numberToNat(scale))

}
