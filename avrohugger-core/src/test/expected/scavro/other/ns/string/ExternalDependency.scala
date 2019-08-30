/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package other.ns.string

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import other.ns.{ExternalDependency => JExternalDependency}

final case class ExternalDependency(number: Int) extends AvroSerializeable {
  type J = JExternalDependency
  override def toAvro: JExternalDependency = {
    new JExternalDependency(number)
  }
}

object ExternalDependency {
  implicit def reader = new AvroReader[ExternalDependency] {
    override type J = JExternalDependency
  }
  implicit val metadata: AvroMetadata[ExternalDependency, JExternalDependency] = new AvroMetadata[ExternalDependency, JExternalDependency] {
    override val avroClass: Class[JExternalDependency] = classOf[JExternalDependency]
    override val schema: Schema = JExternalDependency.getClassSchema()
    override val fromAvro: (JExternalDependency) => ExternalDependency = {
      (j: JExternalDependency) => ExternalDependency(j.getNumber.toInt)
    }
  }
}