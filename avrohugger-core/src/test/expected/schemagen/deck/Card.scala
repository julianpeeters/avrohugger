/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package deck

import scala.annotation.switch

case class Card(var suit: Suit = Suit.SPADES, var number: Int) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(Suit.SPADES, 0)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        suit
      }.asInstanceOf[AnyRef]
      case 1 => {
        number
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.suit = {
        value
      }.asInstanceOf[Suit]
      case 1 => this.number = {
        value
      }.asInstanceOf[Int]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = deck.Card.SCHEMA$
}

object Card {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Card\",\"namespace\":\"deck\",\"fields\":[{\"name\":\"suit\",\"type\":{\"type\":\"enum\",\"name\":\"Suit\",\"symbols\":[\"SPADES\",\"DIAMONDS\",\"CLUBS\",\"HEARTS\"]},\"default\":\"SPADES\"},{\"name\":\"number\",\"type\":\"int\"}]}")
}