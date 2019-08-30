/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package avro.examples.baseball.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import avro.examples.baseball.{Mascot => JMascot}

final case class Mascot(name: String) extends AvroSerializeable {
  type J = JMascot
  override def toAvro: JMascot = {
    new JMascot(name)
  }
}

object Mascot {
  implicit def reader = new AvroReader[Mascot] {
    override type J = JMascot
  }
  implicit val metadata: AvroMetadata[Mascot, JMascot] = new AvroMetadata[Mascot, JMascot] {
    override val avroClass: Class[JMascot] = classOf[JMascot]
    override val schema: Schema = JMascot.getClassSchema()
    override val fromAvro: (JMascot) => Mascot = {
      (j: JMascot) => Mascot(j.getName.toString)
    }
  }
}