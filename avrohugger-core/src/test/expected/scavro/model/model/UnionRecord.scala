/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package model.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import model.{UnionRecord => JUnionRecord}

final case class UnionRecord(blah: String) extends AvroSerializeable {
  type J = JUnionRecord
  override def toAvro: JUnionRecord = {
    new JUnionRecord(blah)
  }
}

object UnionRecord {
  implicit def reader = new AvroReader[UnionRecord] {
    override type J = JUnionRecord
  }
  implicit val metadata: AvroMetadata[UnionRecord, JUnionRecord] = new AvroMetadata[UnionRecord, JUnionRecord] {
    override val avroClass: Class[JUnionRecord] = classOf[JUnionRecord]
    override val schema: Schema = JUnionRecord.getClassSchema()
    override val fromAvro: (JUnionRecord) => UnionRecord = {
      (j: JUnionRecord) => UnionRecord(j.getBlah.toString)
    }
  }
}