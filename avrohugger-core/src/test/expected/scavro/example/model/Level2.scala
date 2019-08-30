/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import example.{Level2 => JLevel2}

final case class Level2(name: String) extends AvroSerializeable {
  type J = JLevel2
  override def toAvro: JLevel2 = {
    new JLevel2(name)
  }
}

object Level2 {
  implicit def reader = new AvroReader[Level2] {
    override type J = JLevel2
  }
  implicit val metadata: AvroMetadata[Level2, JLevel2] = new AvroMetadata[Level2, JLevel2] {
    override val avroClass: Class[JLevel2] = classOf[JLevel2]
    override val schema: Schema = JLevel2.getClassSchema()
    override val fromAvro: (JLevel2) => Level2 = {
      (j: JLevel2) => Level2(j.getName.toString)
    }
  }
}