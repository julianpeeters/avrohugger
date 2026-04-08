/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.proto

sealed trait EnumProtocol

final object Suit extends Enumeration with EnumProtocol {
  type Suit = Value
  val SPADES, HEARTS, DIAMONDS, CLUBS = Value
}

final case class Card(suit: example.proto.Suit.Value, number: Int) extends EnumProtocol