/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl

sealed trait EnumProtocol

final object Suit extends Enumeration with EnumProtocol {
  type Suit = Value
  val SPADES, DIAMONDS, CLUBS, HEARTS = Value
}

final case class Card(suit: Suit.Value, number: Int) extends EnumProtocol