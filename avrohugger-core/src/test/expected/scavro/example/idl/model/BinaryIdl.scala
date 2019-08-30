/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import example.idl.{BinaryIdl => JBinaryIdl}

final case class BinaryIdl(data: Array[Byte]) extends AvroSerializeable {
  type J = JBinaryIdl
  override def toAvro: JBinaryIdl = {
    new JBinaryIdl(java.nio.ByteBuffer.wrap(data))
  }
}

object BinaryIdl {
  implicit def reader = new AvroReader[BinaryIdl] {
    override type J = JBinaryIdl
  }
  implicit val metadata: AvroMetadata[BinaryIdl, JBinaryIdl] = new AvroMetadata[BinaryIdl, JBinaryIdl] {
    override val avroClass: Class[JBinaryIdl] = classOf[JBinaryIdl]
    override val schema: Schema = JBinaryIdl.getClassSchema()
    override val fromAvro: (JBinaryIdl) => BinaryIdl = {
      (j: JBinaryIdl) => BinaryIdl(j.getData match {
        case (buffer: java.nio.ByteBuffer) => {
          buffer.array()
        }
      })
    }
  }
}