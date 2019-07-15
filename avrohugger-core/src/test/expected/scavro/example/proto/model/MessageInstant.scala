/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.proto.model

import java.time.Instant

import org.apache.avro.Schema
import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

case class MessageInstant(to: org.apache.avro.util.Utf8, from: org.apache.avro.util.Utf8, body: org.apache.avro.util.Utf8, instant: Instant) extends AvroSerializeable {
  type J = JMessageInstant
  override def toAvro: JMessageInstant = {
    new JMessageInstant(to, from, body, instant)
  }
}

object MessageInstant {
  implicit def reader = new AvroReader[MessageInstant] {
    override type J = JMessageInstant
  }
  implicit val metadata: AvroMetadata[Message, JMessageInstant] = new AvroMetadata[MessageInstant, JMessageInstant] {
    override val avroClass: Class[JMessageInstant] = classOf[JMessageInstant]
    override val schema: Schema = JMessageInstant.getClassSchema()
    override val fromAvro: (JMessageInstant) => MessageInstant = {
      (j: JMessageInstant) => MessageInstant(j.getTo, j.getFrom, j.getBody, j.getInstant)
    }
  }
}