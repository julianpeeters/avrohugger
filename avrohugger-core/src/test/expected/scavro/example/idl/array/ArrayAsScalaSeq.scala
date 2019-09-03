/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl.array.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import example.idl.array.{ArrayIdl => JArrayIdl}

import scala.collection.JavaConverters._

final case class ArrayIdl(data: Seq[Int]) extends AvroSerializeable {
  type J = JArrayIdl
  override def toAvro: JArrayIdl = {
    new JArrayIdl({
      val array: java.util.List[java.lang.Integer] = new java.util.ArrayList[java.lang.Integer]
      data foreach { element =>
        array.add(element)
      }
      array
    })
  }
}

object ArrayIdl {
  implicit def reader = new AvroReader[ArrayIdl] {
    override type J = JArrayIdl
  }
  implicit val metadata: AvroMetadata[ArrayIdl, JArrayIdl] = new AvroMetadata[ArrayIdl, JArrayIdl] {
    override val avroClass: Class[JArrayIdl] = classOf[JArrayIdl]
    override val schema: Schema = JArrayIdl.getClassSchema()
    override val fromAvro: (JArrayIdl) => ArrayIdl = {
      (j: JArrayIdl) => ArrayIdl(Seq((j.getData.asScala: _*)) map { x =>
        x.toInt
      })
    }
  }
}
