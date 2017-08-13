package deck

case class Card(suit: Suit = Suit.SPADES, number: Int)

object Suit extends Enumeration {
  type Suit = Value
  val SPADES, DIAMONDS, CLUBS, HEARTS = Value
}