/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package test.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import test.{Down => JDown, Up => JUp}

final case class Up(value: Int) extends AvroSerializeable {
  type J = JUp
  override def toAvro: JUp = {
    new JUp(value)
  }
}

object Up {
  implicit def reader = new AvroReader[Up] {
    override type J = JUp
  }
  implicit val metadata: AvroMetadata[Up, JUp] = new AvroMetadata[Up, JUp] {
    override val avroClass: Class[JUp] = classOf[JUp]
    override val schema: Schema = JUp.getClassSchema()
    override val fromAvro: (JUp) => Up = {
      (j: JUp) => Up(j.getValue.toInt)
    }
  }
}

final case class Down(value: Int) extends AvroSerializeable {
  type J = JDown
  override def toAvro: JDown = {
    new JDown(value)
  }
}

object Down {
  implicit def reader = new AvroReader[Down] {
    override type J = JDown
  }
  implicit val metadata: AvroMetadata[Down, JDown] = new AvroMetadata[Down, JDown] {
    override val avroClass: Class[JDown] = classOf[JDown]
    override val schema: Schema = JDown.getClassSchema()
    override val fromAvro: (JDown) => Down = {
      (j: JDown) => Down(j.getValue.toInt)
    }
  }
}