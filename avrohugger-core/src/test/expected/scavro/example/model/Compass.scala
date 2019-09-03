/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import example.{Compass => JCompass, Direction => JDirection}

final case class Compass(direction: Direction.Value) extends AvroSerializeable {
  type J = JCompass
  override def toAvro: JCompass = {
    new JCompass(direction match {
      case Direction.NORTH => JDirection.NORTH
      case Direction.SOUTH => JDirection.SOUTH
      case Direction.EAST => JDirection.EAST
      case Direction.WEST => JDirection.WEST
    })
  }
}

object Compass {
  implicit def reader = new AvroReader[Compass] {
    override type J = JCompass
  }
  implicit val metadata: AvroMetadata[Compass, JCompass] = new AvroMetadata[Compass, JCompass] {
    override val avroClass: Class[JCompass] = classOf[JCompass]
    override val schema: Schema = JCompass.getClassSchema()
    override val fromAvro: (JCompass) => Compass = {
      (j: JCompass) => Compass(j.getDirection match {
        case JDirection.NORTH => Direction.NORTH
        case JDirection.SOUTH => Direction.SOUTH
        case JDirection.EAST => Direction.EAST
        case JDirection.WEST => Direction.WEST
      })
    }
  }
}