/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.avro.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import example.avro.{ClashInner => JClashInner}

final case class ClashInner(some: Option[Int], other: Option[Int], id: Option[Int]) extends AvroSerializeable {
  type J = JClashInner
  override def toAvro: JClashInner = {
    new JClashInner(some match {
      case Some(x) => x
      case None => null
    }, other match {
      case Some(x) => x
      case None => null
    }, id match {
      case Some(x) => x
      case None => null
    })
  }
}

object ClashInner {
  implicit def reader = new AvroReader[ClashInner] {
    override type J = JClashInner
  }
  implicit val metadata: AvroMetadata[ClashInner, JClashInner] = new AvroMetadata[ClashInner, JClashInner] {
    override val avroClass: Class[JClashInner] = classOf[JClashInner]
    override val schema: Schema = JClashInner.getClassSchema()
    override val fromAvro: (JClashInner) => ClashInner = {
      (j: JClashInner) => ClashInner(j.getSome match {
        case null => None
        case _ => Some(j.getSome.toInt)
      }, j.getOther match {
        case null => None
        case _ => Some(j.getOther.toInt)
      }, j.getId match {
        case null => None
        case _ => Some(j.getId.toInt)
      })
    }
  }
}