/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.proto.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import example.proto.{Message => JMessage}

final case class Message(to: String, from: String, body: String) extends AvroSerializeable {
  type J = JMessage
  override def toAvro: JMessage = {
    new JMessage(to, from, body)
  }
}

object Message {
  implicit def reader = new AvroReader[Message] {
    override type J = JMessage
  }
  implicit val metadata: AvroMetadata[Message, JMessage] = new AvroMetadata[Message, JMessage] {
    override val avroClass: Class[JMessage] = classOf[JMessage]
    override val schema: Schema = JMessage.getClassSchema()
    override val fromAvro: (JMessage) => Message = {
      (j: JMessage) => Message(j.getTo.toString, j.getFrom.toString, j.getBody.toString)
    }
  }
}