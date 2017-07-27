/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.avro.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import example.avro.{ClashInner => JClashInner, ClashRecord => JClashRecord, ClashOuter => JClashOuter}

import scala.collection.JavaConverters._

case class ClashRecord(some: Int, outer: ClashOuter, id: Int) extends AvroSerializeable {
  type J = JClashRecord
  override def toAvro: JClashRecord = {
    new JClashRecord(some, outer match {
      case ClashOuter(inner) => new JClashOuter(inner match {
        case Some(x) => {
          val array: java.util.List[JClashInner] = new java.util.ArrayList[JClashInner]
          x foreach { element =>
            array.add(element match {
              case Some(x) => x match {
                case ClashInner(some, other, id) => new JClashInner(some match {
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
              case None => null
            })
          }
          array
        }
        case None => null
      })
    }, id)
  }
}

object ClashRecord {
  implicit def reader = new AvroReader[ClashRecord] {
    override type J = JClashRecord
  }
  implicit val metadata: AvroMetadata[ClashRecord, JClashRecord] = new AvroMetadata[ClashRecord, JMessy] {
    override val avroClass: Class[JClashRecord] = classOf[JClashRecord]
    override val schema: Schema = JClashRecord.getClassSchema()
    override val fromAvro: (JClashRecord) => ClashRecord = {
      (j: JClashRecord) => ClashRecord(j.getSome.toInt, ClashOuter(j.getOuter.getInner match {
        case null => None
        case _ => Some(Array((j.getOuter.getInner.asScala: _*)) map { x =>
          x match {
            case null => None
            case _ => Some(ClashInner(x.getSome match {
              case null => None
              case _ => Some(x.getSome.toInt)
            }, x.getOther match {
              case null => None
              case _ => Some(x.getOther.toInt)
            }, x.getId match {
              case null => None
              case _ => Some(x.getId.toInt)
            }))
          }
        })
      }), j.getId.toInt)
    }
  }
}