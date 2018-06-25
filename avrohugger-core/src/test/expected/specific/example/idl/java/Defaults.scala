/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl.java

sealed trait Defaults extends Product with Serializable

final case class Embedded(inner: Int) extends Defaults

final case class DefaultTest(suit: DefaultEnum = DefaultEnum.SPADES, number: Int = 0, str: String = "str", optionString: Option[String] = None, optionStringValue: Option[String] = Some("default"), embedded: Embedded = new Embedded(1), defaultArray: Seq[Int] = Seq(1, 3, 4, 5), optionalEnum: Option[DefaultEnum] = None, defaultMap: Map[String, String] = Map("Hello" -> "world", "Merry" -> "Christmas"), byt: Array[Byte] = Array[Byte](-61, -65)) extends Defaults