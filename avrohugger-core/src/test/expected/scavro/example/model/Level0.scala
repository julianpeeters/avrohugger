/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import example.{Level0 => JLevel0, Level1 => JLevel1, Level2 => JLevel2}

final case class Level0(level1: Level1) extends AvroSerializeable {
  type J = JLevel0
  override def toAvro: JLevel0 = {
    new JLevel0(level1.toAvro)
  }
}

object Level0 {
  implicit def reader = new AvroReader[Level0] {
    override type J = JLevel0
  }
  implicit val metadata: AvroMetadata[Level0, JLevel0] = new AvroMetadata[Level0, JLevel0] {
    override val avroClass: Class[JLevel0] = classOf[JLevel0]
    override val schema: Schema = JLevel0.getClassSchema()
    override val fromAvro: (JLevel0) => Level0 = {
      (j: JLevel0) => Level0(Level1.metadata.fromAvro(j.getLevel1))
    }
  }
}