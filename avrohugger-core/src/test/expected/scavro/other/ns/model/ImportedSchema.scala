/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package other.ns.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import other.ns.{ImportedSchema => JImportedSchema}

case class ImportedSchema(name: String) extends AvroSerializeable {
  type J = JImportedSchema
  override def toAvro: JImportedSchema = {
    new JImportedSchema(name)
  }
}

object ImportedSchema {
  implicit def reader = new AvroReader[ImportedSchema] {
    override type J = JImportedSchema
  }
  implicit val metadata: AvroMetadata[ImportedSchema, JImportedSchema] = new AvroMetadata[ImportedSchema, JImportedSchema] {
    override val avroClass: Class[JImportedSchema] = classOf[JImportedSchema]
    override val schema: Schema = JImportedSchema.getClassSchema()
    override val fromAvro: (JImportedSchema) => ImportedSchema = {
      (j: JImportedSchema) => ImportedSchema(j.getName.toString)
    }
  }
}