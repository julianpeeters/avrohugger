/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import example.idl.{Recursive => JRecursive}

case class Recursive(name: String, recursive: Option[Recursive]) extends AvroSerializeable {
  type J = JRecursive
  override def toAvro: JRecursive = {
    new JRecursive(name, recursive match {
      case Some(x) => x match {
        case Recursive(name, recursive) => new JRecursive(name, recursive match {
          case Some(x) => x match {
            case Recursive(name, recursive) => new JRecursive(name)
          }
          case None => null
        })
      }
      case None => null
    })
  }
}

object Recursive {
  implicit def reader = new AvroReader[Recursive] {
    override type J = JRecursive
  }
  implicit val metadata: AvroMetadata[Recursive, JRecursive] = new AvroMetadata[Recursive, JRecursive] {
    override val avroClass: Class[JRecursive] = classOf[JRecursive]
    override val schema: Schema = JRecursive.getClassSchema()
    override val fromAvro: (JRecursive) => Recursive = {
      (j: JRecursive) => Recursive(j.getName.toString, j.getRecursive match {
        case null => None
        case _ => Some(Recursive(j.getRecursive.getName.toString, j.getRecursive.getRecursive match {
          case null => None
          case _ => Some(Recursive(j.getRecursive.getRecursive.getName.toString))
        }))
      })
    }
  }
}