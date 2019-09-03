/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import example.{Level1 => JLevel1, Level2 => JLevel2}

final case class Level1(level2: Level2) extends AvroSerializeable {
  type J = JLevel1
  override def toAvro: JLevel1 = {
    new JLevel1(level2.toAvro)
  }
}

object Level1 {
  implicit def reader = new AvroReader[Level1] {
    override type J = JLevel1
  }
  implicit val metadata: AvroMetadata[Level1, JLevel1] = new AvroMetadata[Level1, JLevel1] {
    override val avroClass: Class[JLevel1] = classOf[JLevel1]
    override val schema: Schema = JLevel1.getClassSchema()
    override val fromAvro: (JLevel1) => Level1 = {
      (j: JLevel1) => Level1(Level2.metadata.fromAvro(j.getLevel2))
    }
  }
}