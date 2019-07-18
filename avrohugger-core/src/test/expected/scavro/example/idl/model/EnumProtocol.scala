/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import example.idl.{Card => JCard, Suit => JSuit}

sealed trait EnumProtocol

final object Suit extends Enumeration with EnumProtocol {
  type Suit = Value
  val SPADES, DIAMONDS, CLUBS, HEARTS = Value
}

final case class Card(suit: Suit.Value, number: Int) extends AvroSerializeable with EnumProtocol {
  type J = JCard
  override def toAvro: JCard = {
    new JCard(suit match {
      case Suit.SPADES => JSuit.SPADES
      case Suit.DIAMONDS => JSuit.DIAMONDS
      case Suit.CLUBS => JSuit.CLUBS
      case Suit.HEARTS => JSuit.HEARTS
    }, number)
  }
}

final object Card {
  implicit def reader = new AvroReader[Card] {
    override type J = JCard
  }
  implicit val metadata: AvroMetadata[Card, JCard] = new AvroMetadata[Card, JCard] {
    override val avroClass: Class[JCard] = classOf[JCard]
    override val schema: Schema = JCard.getClassSchema()
    override val fromAvro: (JCard) => Card = {
      (j: JCard) => Card(j.getSuit match {
        case JSuit.SPADES => Suit.SPADES
        case JSuit.DIAMONDS => Suit.DIAMONDS
        case JSuit.CLUBS => Suit.CLUBS
        case JSuit.HEARTS => Suit.HEARTS
      }, j.getNumber)
    }
  }
}