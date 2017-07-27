/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.avro.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import example.avro.{ClashInner => JClashInner, ClashOuter => JClashOuter}

import scala.collection.JavaConverters._

case class ClashOuter(inner: Option[Array[Option[ClashInner]]]) extends AvroSerializeable {
  type J = JClashOuter
  override def toAvro: JClashOuter = {
    new JClashOuter(inner match {
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
  }
}

object ClashOuter {
  implicit def reader = new AvroReader[ClashOuter] {
    override type J = JClashOuter
  }
  implicit val metadata: AvroMetadata[ClashOuter, JClashOuter] = new AvroMetadata[ClashOuter, JClashOuter] {
    override val avroClass: Class[JClashOuter] = classOf[JClashOuter]
    override val schema: Schema = JClashOuter.getClassSchema()
    override val fromAvro: (JClashOuter) => ClashOuter = {
      (j: JClashOuter) => ClashOuter(j.getInner match {
        case null => None
        case _ => Some(Array((j.getInner.asScala: _*)) map { x =>
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
      })
    }
  }
}