/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.example

import scala.annotation.switch

/** Example adapted from https://github.com/delagoya. This is a comment for the whole protocol */


/**
 * The comment applies to the `NoSpaces1` record, but is not indented to the
 * level of the record specification.
 * @param single_line_comment_property This is a single line comment that is indented for readability,
 *     and is not affected by indentation.
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
final case class NoSpaces1(var single_line_comment_property: String, var multi_line_property: String) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this("", "")
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        single_line_comment_property
      }.asInstanceOf[AnyRef]
      case 1 => {
        multi_line_property
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.single_line_comment_property = {
        value.toString
      }.asInstanceOf[String]
      case 1 => this.multi_line_property = {
        value.toString
      }.asInstanceOf[String]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = NoSpaces1.SCHEMA$
}

object NoSpaces1 {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"NoSpaces1\",\"namespace\":\"com.example\",\"doc\":\"The comment applies to the `NoSpaces1` record, but is not indented to the\\nlevel of the record specification.\",\"fields\":[{\"name\":\"single_line_comment_property\",\"type\":\"string\",\"doc\":\"This is a single line comment that is indented for readability,\\n    and is not affected by indentation.\"},{\"name\":\"multi_line_property\",\"type\":\"string\",\"doc\":\"This multi-line comment on `mult_line_property` that would be affected by indentation.\\n\\nThis is another paragraph\\n\\n\\n    This is an indented block and should be shown as\\n    such.\\n\\nHere is a code block that apparently does not work for avrodoc. E.g. no [GFM](https://help.github.com/articles/github-flavored-markdown) support.\\n\\n```ruby\\n# this is a Ruby code block\\ndef method(arg1, arg2=nil)\\n  puts \\\"hello world!\\\"\\nend\\n```\"}]}")
}