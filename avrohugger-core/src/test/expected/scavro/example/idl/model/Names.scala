/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import example.idl.{Names => JNames}

final case class Names(public$: String, `ends_with_`: String) extends AvroSerializeable {
  type J = JNames
  override def toAvro: JNames = {
    new JNames(public$, `ends_with_`)
  }
}

object Names {
  implicit def reader = new AvroReader[Names] {
    override type J = JNames
  }
  implicit val metadata: AvroMetadata[Names, JNames] = new AvroMetadata[Names, JNames] {
    override val avroClass: Class[JNames] = classOf[JNames]
    override val schema: Schema = JNames.getClassSchema()
    override val fromAvro: (JNames) => Names = {
      (j: JNames) => Names(j.getPublic$.toString, j.getEndsWith.toString)
    }
  }
}