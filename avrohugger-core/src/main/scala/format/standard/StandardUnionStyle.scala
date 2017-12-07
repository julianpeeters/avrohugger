package avrohugger
package format
package standard

sealed abstract class UnionStyle extends Product with Serializable
case object Default extends UnionStyle
case object ShapelessCoproduct extends UnionStyle