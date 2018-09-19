package avrohugger
package matchers
package custom

import treehugger.forest._
import treehuggerDSL._

object CustomUtils {

  val numberToNat: PartialFunction[Int, Type] = {
    case n if n <= 22 => TYPE_REF("shapeless.Nat._" + n)
  }

  def decimalTagged(precisionNat: Option[Type], scaleNat: Option[Type]): Tree = (precisionNat, scaleNat) match {
    case (Some(p), Some(s)) =>
      REF("shapeless.tag") APPLYTYPE TYPE_TUPLE(p, s).tpe APPLYTYPE TYPE_REF("scala.math.BigDecimal").tpe
    case _ =>
      REF("scala.math.BigDecimal")
  }

}
