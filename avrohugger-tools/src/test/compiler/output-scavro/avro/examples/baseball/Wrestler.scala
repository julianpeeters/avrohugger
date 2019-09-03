/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package avro.examples.baseball.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import avro.examples.baseball.{Mascot => JMascot, Wrestler => JWrestler}

import scala.collection.JavaConverters._

final case class Wrestler(number: Int, first_name: String, last_name: String, nicknames: Array[Mascot]) extends AvroSerializeable {
  type J = JWrestler
  override def toAvro: JWrestler = {
    new JWrestler(number, first_name, last_name, {
      val array: java.util.List[JMascot] = new java.util.ArrayList[JMascot]
      nicknames foreach { element =>
        array.add(element.toAvro)
      }
      array
    })
  }
}

object Wrestler {
  implicit def reader = new AvroReader[Wrestler] {
    override type J = JWrestler
  }
  implicit val metadata: AvroMetadata[Wrestler, JWrestler] = new AvroMetadata[Wrestler, JWrestler] {
    override val avroClass: Class[JWrestler] = classOf[JWrestler]
    override val schema: Schema = JWrestler.getClassSchema()
    override val fromAvro: (JWrestler) => Wrestler = {
      (j: JWrestler) => Wrestler(j.getNumber.toInt, j.getFirstName.toString, j.getLastName.toString, Array((j.getNicknames.asScala: _*)) map { x =>
        Mascot.metadata.fromAvro(x)
      })
    }
  }
}