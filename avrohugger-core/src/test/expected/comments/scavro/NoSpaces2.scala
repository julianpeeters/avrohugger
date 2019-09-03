/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.example.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import com.example.{NoSpaces2 => JNoSpaces2}

/** Example adapted from https://github.com/delagoya. This is a comment for the whole protocol */


/** @param comment_property This is a single line comment on a field, in a record that has no comment. */
final case class NoSpaces2(comment_property: String) extends AvroSerializeable {
  type J = JNoSpaces2
  override def toAvro: JNoSpaces2 = {
    new JNoSpaces2(comment_property)
  }
}

object NoSpaces2 {
  implicit def reader = new AvroReader[NoSpaces2] {
    override type J = JNoSpaces2
  }
  implicit val metadata: AvroMetadata[NoSpaces2, JNoSpaces2] = new AvroMetadata[NoSpaces2, JNoSpaces2] {
    override val avroClass: Class[JNoSpaces2] = classOf[JNoSpaces2]
    override val schema: Schema = JNoSpaces2.getClassSchema()
    override val fromAvro: (JNoSpaces2) => NoSpaces2 = {
      (j: JNoSpaces2) => NoSpaces2(j.getCommentProperty.toString)
    }
  }
}