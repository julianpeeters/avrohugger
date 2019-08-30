/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import example.{BinarySc => JBinarySc}

final case class BinarySc(data: Array[Byte]) extends AvroSerializeable {
  type J = JBinarySc
  override def toAvro: JBinarySc = {
    new JBinarySc(java.nio.ByteBuffer.wrap(data))
  }
}

object BinarySc {
  implicit def reader = new AvroReader[BinarySc] {
    override type J = JBinarySc
  }
  implicit val metadata: AvroMetadata[BinarySc, JBinarySc] = new AvroMetadata[BinarySc, JBinarySc] {
    override val avroClass: Class[JBinarySc] = classOf[JBinarySc]
    override val schema: Schema = JBinarySc.getClassSchema()
    override val fromAvro: (JBinarySc) => BinarySc = {
      (j: JBinarySc) => BinarySc(j.getData match {
        case (buffer: java.nio.ByteBuffer) => {
          buffer.array()
        }
      })
    }
  }
}