/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.example.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import com.example.{NoSpaces4 => JNoSpaces4, NoSpaces5 => JNoSpaces5}

/** Example adapted from https://github.com/delagoya. This is a comment for the whole protocol */
sealed trait Example4 extends AvroSerializeable with Product with Serializable

final case class NoSpaces4(comment_property1: String) extends AvroSerializeable with Example4 {
  type J = JNoSpaces4
  override def toAvro: JNoSpaces4 = {
    new JNoSpaces4(comment_property1)
  }
}

final object NoSpaces4 {
  implicit def reader = new AvroReader[NoSpaces4] {
    override type J = JNoSpaces4
  }
  implicit val metadata: AvroMetadata[NoSpaces4, JNoSpaces4] = new AvroMetadata[NoSpaces4, JNoSpaces4] {
    override val avroClass: Class[JNoSpaces4] = classOf[JNoSpaces4]
    override val schema: Schema = JNoSpaces4.getClassSchema()
    override val fromAvro: (JNoSpaces4) => NoSpaces4 = {
      (j: JNoSpaces4) => NoSpaces4(j.getCommentProperty1)
    }
  }
}

final case class NoSpaces5(comment_property2: String) extends AvroSerializeable with Example4 {
  type J = JNoSpaces5
  override def toAvro: JNoSpaces5 = {
    new JNoSpaces5(comment_property2)
  }
}

final object NoSpaces5 {
  implicit def reader = new AvroReader[NoSpaces5] {
    override type J = JNoSpaces5
  }
  implicit val metadata: AvroMetadata[NoSpaces5, JNoSpaces5] = new AvroMetadata[NoSpaces5, JNoSpaces5] {
    override val avroClass: Class[JNoSpaces5] = classOf[JNoSpaces5]
    override val schema: Schema = JNoSpaces5.getClassSchema()
    override val fromAvro: (JNoSpaces5) => NoSpaces5 = {
      (j: JNoSpaces5) => NoSpaces5(j.getCommentProperty2)
    }
  }
}