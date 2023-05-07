/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import example.idl.{Level0 => JLevel0, Level1 => JLevel1, Level2 => JLevel2}

sealed trait NestedProtocol extends AvroSerializeable with Product with Serializable

final case class Level2(name: String) extends AvroSerializeable with NestedProtocol {
  type J = JLevel2
  override def toAvro: JLevel2 = {
    new JLevel2(name)
  }
}

final object Level2 {
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

final case class Level1(level2: example.idl.Level2) extends AvroSerializeable with NestedProtocol {
  type J = JLevel1
  override def toAvro: JLevel1 = {
    new JLevel1(level2.toAvro)
  }
}

final object Level1 {
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

final case class Level0(level1: example.idl.Level1) extends AvroSerializeable with NestedProtocol {
  type J = JLevel0
  override def toAvro: JLevel0 = {
    new JLevel0(level1.toAvro)
  }
}

final object Level0 {
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