/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.importing.nested.bar.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import example.importing.nested.bar.{BarInner => JBarInner, BarOuter => JBarOuter}

import example.importing.nested.foo.{Status => JStatus}

case class BarOuter(inner: BarInner) extends AvroSerializeable {
  type J = JBarOuter
  override def toAvro: JBarOuter = {
    new JBarOuter(inner.toAvro)
  }
}

object BarOuter {
  implicit def reader = new AvroReader[BarOuter] {
    override type J = JBarOuter
  }
  implicit val metadata: AvroMetadata[BarOuter, JBarOuter] = new AvroMetadata[BarOuter, JBarOuter] {
    override val avroClass: Class[JBarOuter] = classOf[JBarOuter]
    override val schema: Schema = JBarOuter.getClassSchema()
    override val fromAvro: (JBarOuter) => BarOuter = {
      (j: JBarOuter) => BarOuter(BarInner.metadata.fromAvro(j.getInner))
    }
  }
}