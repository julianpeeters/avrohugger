/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.example.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import com.example.{NoSpaces6 => JNoSpaces6, NoSpaces7 => JNoSpaces7}

/** Example adapted from https://github.com/delagoya. This is a comment for the whole protocol */


final case class NoSpaces6(comment_property1: String) extends AvroSerializeable {
  type J = JNoSpaces6
  override def toAvro: JNoSpaces6 = {
    new JNoSpaces6(comment_property1)
  }
}

object NoSpaces6 {
  implicit def reader = new AvroReader[NoSpaces6] {
    override type J = JNoSpaces6
  }
  implicit val metadata: AvroMetadata[NoSpaces6, JNoSpaces6] = new AvroMetadata[NoSpaces6, JNoSpaces6] {
    override val avroClass: Class[JNoSpaces6] = classOf[JNoSpaces6]
    override val schema: Schema = JNoSpaces6.getClassSchema()
    override val fromAvro: (JNoSpaces6) => NoSpaces6 = {
      (j: JNoSpaces6) => NoSpaces6(j.getCommentProperty1.toString)
    }
  }
}

final case class NoSpaces7(comment_property2: String) extends AvroSerializeable {
  type J = JNoSpaces7
  override def toAvro: JNoSpaces7 = {
    new JNoSpaces7(comment_property2)
  }
}

object NoSpaces7 {
  implicit def reader = new AvroReader[NoSpaces7] {
    override type J = JNoSpaces7
  }
  implicit val metadata: AvroMetadata[NoSpaces7, JNoSpaces7] = new AvroMetadata[NoSpaces7, JNoSpaces7] {
    override val avroClass: Class[JNoSpaces7] = classOf[JNoSpaces7]
    override val schema: Schema = JNoSpaces7.getClassSchema()
    override val fromAvro: (JNoSpaces7) => NoSpaces7 = {
      (j: JNoSpaces7) => NoSpaces7(j.getCommentProperty2.toString)
    }
  }
}