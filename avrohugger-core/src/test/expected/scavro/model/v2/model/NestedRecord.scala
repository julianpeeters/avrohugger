/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package model.v2.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import model.model.UnionRecord

import model.{UnionRecord => JUnionRecord}

import model.v2.{NestedRecord => JNestedRecord}

final case class NestedRecord(nestedunion: Option[UnionRecord]) extends AvroSerializeable {
  type J = JNestedRecord
  override def toAvro: JNestedRecord = {
    new JNestedRecord(nestedunion match {
      case Some(x) => x.toAvro
      case None => null
    })
  }
}

object NestedRecord {
  implicit def reader = new AvroReader[NestedRecord] {
    override type J = JNestedRecord
  }
  implicit val metadata: AvroMetadata[NestedRecord, JNestedRecord] = new AvroMetadata[NestedRecord, JNestedRecord] {
    override val avroClass: Class[JNestedRecord] = classOf[JNestedRecord]
    override val schema: Schema = JNestedRecord.getClassSchema()
    override val fromAvro: (JNestedRecord) => NestedRecord = {
      (j: JNestedRecord) => NestedRecord(j.getNestedunion match {
        case null => None
        case _ => Some(UnionRecord.metadata.fromAvro(j.getNestedunion))
      })
    }
  }
}