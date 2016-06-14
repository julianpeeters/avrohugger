package avrohugger
package matchers

import treehugger.forest._
import treehuggerDSL._
import definitions._

object CustomTypeMatcher {
  
  def checkCustomArrayType(
    maybeCustomArray: Option[Class[_]],
    defaultType: Type => Type) = {
    maybeCustomArray match {
      case Some(c) if c == classOf[Array[_]]  => TYPE_ARRAY(_)
      case Some(c) if c == classOf[List[_]]   => TYPE_LIST(_)
      case Some(c) if c == classOf[Vector[_]] => TYPE_VECTOR(_)
      case _                                  => defaultType(_)
    }
  }
  
  
  // Scavro allows number types to be remapped.
  def checkCustomNumberType(
    maybeCustomNumber: Option[Class[_]],
    defaultClass: Symbol) = {
    maybeCustomNumber match {
      case Some(c) if c == classOf[Double] => DoubleClass
      case Some(c) if c == classOf[Float]  => FloatClass
      case Some(c) if c == classOf[Long]   => LongClass
      case Some(c) if c == classOf[Int]    => IntClass
      case _                               => defaultClass
    }
  }

  
}

