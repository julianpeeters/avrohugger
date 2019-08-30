/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.proto.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import example.proto.{BinaryPr => JBinaryPr}

final case class BinaryPr(data: Array[Byte]) extends AvroSerializeable {
  type J = JBinaryPr
  override def toAvro: JBinaryPr = {
    new JBinaryPr(java.nio.ByteBuffer.wrap(data))
  }
}

object BinaryPr {
  implicit def reader = new AvroReader[BinaryPr] {
    override type J = JBinaryPr
  }
  implicit val metadata: AvroMetadata[BinaryPr, JBinaryPr] = new AvroMetadata[BinaryPr, JBinaryPr] {
    override val avroClass: Class[JBinaryPr] = classOf[JBinaryPr]
    override val schema: Schema = JBinaryPr.getClassSchema()
    override val fromAvro: (JBinaryPr) => BinaryPr = {
      (j: JBinaryPr) => BinaryPr(j.getData match {
        case (buffer: java.nio.ByteBuffer) => {
          buffer.array()
        }
      })
    }
  }
}