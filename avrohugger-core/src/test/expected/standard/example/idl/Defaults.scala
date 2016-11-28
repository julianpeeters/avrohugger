/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl

sealed trait Defaults

final object DefaultEnum extends Enumeration with Defaults {
  type DefaultEnum = Value
  val SPADES, DIAMONDS, CLUBS, HEARTS = Value
}

final case class Embedded(inner: Int) extends Defaults

final case class DefaultTest(suit: DefaultEnum.Value = DefaultEnum.SPADES, number: Int = 0, str: String = "str", optionString: Option[String] = None, optionStringValue: Option[String] = Some("default"), embedded: Embedded = new Embedded(1), defaultArray: List[Int] = List(1, 3, 4, 5), optionalEnum: Option[DefaultEnum.Value] = None, defaultMap: Map[String, String] = Map("Hello" -> "world", "Merry" -> "Christmas"), byt: Array[Byte] = Array[Byte](-61, -65)) extends Defaults