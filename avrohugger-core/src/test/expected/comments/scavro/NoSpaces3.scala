/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.example.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import com.example.{NoSpaces3 => JNoSpaces3}

/** Example adapted from https://github.com/delagoya. This is a comment for the whole protocol */


/**
 * The comment applies to the `NoSpaces3` record, but is not indented to the
 * level of the record specification.
 * @param comment_property 
 */
final case class NoSpaces3(comment_property: String) extends AvroSerializeable {
  type J = JNoSpaces3
  override def toAvro: JNoSpaces3 = {
    new JNoSpaces3(comment_property)
  }
}

object NoSpaces3 {
  implicit def reader = new AvroReader[NoSpaces3] {
    override type J = JNoSpaces3
  }
  implicit val metadata: AvroMetadata[NoSpaces3, JNoSpaces3] = new AvroMetadata[NoSpaces3, JNoSpaces3] {
    override val avroClass: Class[JNoSpaces3] = classOf[JNoSpaces3]
    override val schema: Schema = JNoSpaces3.getClassSchema()
    override val fromAvro: (JNoSpaces3) => NoSpaces3 = {
      (j: JNoSpaces3) => NoSpaces3(j.getCommentProperty.toString)
    }
  }
}