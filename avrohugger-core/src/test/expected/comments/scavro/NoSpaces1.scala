/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.example.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import com.example.{NoSpaces1 => JNoSpaces1}

/**
 * The comment applies to the `NoSpaces1` record, but is not indented to the
 * level of the record specification.
 * @param single_line_comment_property This is a single line comment that is indented for readability,
 * and is not affected by indentation.
 * @param multi_line_property This multi-line comment on `mult_line_property` that would be affected by indentation.
 * 
 * This is another paragraph
 * 
 * 
 *     This is an indented block and should be shown as
 *     such.
 * 
 * Here is a code block that apparently does not work for avrodoc. E.g. no [GFM](https://help.github.com/articles/github-flavored-markdown) support.
 * 
 * ```ruby
 * # this is a Ruby code block
 * def method(arg1, arg2=nil)
 *   puts "hello world!"
 * end
 * ```
 */
final case class NoSpaces1(single_line_comment_property: String, multi_line_property: String) extends AvroSerializeable {
  type J = JNoSpaces1
  override def toAvro: JNoSpaces1 = {
    new JNoSpaces1(single_line_comment_property, multi_line_property)
  }
}

object NoSpaces1 {
  implicit def reader = new AvroReader[NoSpaces1] {
    override type J = JNoSpaces1
  }
  implicit val metadata: AvroMetadata[NoSpaces1, JNoSpaces1] = new AvroMetadata[NoSpaces1, JNoSpaces1] {
    override val avroClass: Class[JNoSpaces1] = classOf[JNoSpaces1]
    override val schema: Schema = JNoSpaces1.getClassSchema()
    override val fromAvro: (JNoSpaces1) => NoSpaces1 = {
      (j: JNoSpaces1) => NoSpaces1(j.getSingleLineCommentProperty.toString, j.getMultiLineProperty.toString)
    }
  }
}