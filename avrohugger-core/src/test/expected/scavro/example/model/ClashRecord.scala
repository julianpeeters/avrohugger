/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.avro.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import example.avro.{ClashInner => JClashInner, ClashOuter => JClashOuter, ClashRecord => JClashRecord}

import scala.collection.JavaConverters._

final case class ClashRecord(some: Int, outer: ClashOuter, id: Int) extends AvroSerializeable {
  type J = JClashRecord
  override def toAvro: JClashRecord = {
    new JClashRecord(some, outer.toAvro, id)
  }
}

object ClashRecord {
  implicit def reader = new AvroReader[ClashRecord] {
    override type J = JClashRecord
  }
  implicit val metadata: AvroMetadata[ClashRecord, JClashRecord] = new AvroMetadata[ClashRecord, JClashRecord] {
    override val avroClass: Class[JClashRecord] = classOf[JClashRecord]
    override val schema: Schema = JClashRecord.getClassSchema()
    override val fromAvro: (JClashRecord) => ClashRecord = {
      (j: JClashRecord) => ClashRecord(j.getSome.toInt, ClashOuter.metadata.fromAvro(j.getOuter), j.getId.toInt)
    }
  }
}