package avrohugger
package matchers
package custom

import treehugger.forest._
import treehuggerDSL._
import definitions._

object CustomUtils {

  val numberToNat: PartialFunction[Int, Type] = {
    case n if n <= 22 => TYPE_REF("shapeless.Nat._" + n)
  }

  def decimalTagged(precisionNat: Option[Type], scaleNat: Option[Type]): Tree = (precisionNat, scaleNat) match {
    case (Some(p), Some(s)) =>
      REF("shapeless.tag") APPLYTYPE TYPE_TUPLE(p, s).tpe APPLYTYPE TYPE_REF("scala.math.BigDecimal")
    case _ =>
      REF("scala.math.BigDecimal")
  }

  def decimalTaggedType(precisionNat: Option[Type], scaleNat: Option[Type]): Type = (precisionNat, scaleNat) match {
    case (Some(p), Some(s)) =>
      TYPE_REF(REF("scala.math.BigDecimal") INFIX("@@", TYPE_TUPLE(p, s)))
    case _ =>
      BigDecimalClass
  }

}
