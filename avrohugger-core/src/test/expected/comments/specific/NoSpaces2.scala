/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.example

import scala.annotation.switch

/** Example adapted from https://github.com/delagoya. This is a comment for the whole protocol */


/** @param comment_property This is a single line comment on a field, in a record that has no comment. */
final case class NoSpaces2(var comment_property: String) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this("")
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        comment_property
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.comment_property = {
        value.toString
      }.asInstanceOf[String]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = NoSpaces2.SCHEMA$
}

object NoSpaces2 {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"NoSpaces2\",\"namespace\":\"com.example\",\"fields\":[{\"name\":\"comment_property\",\"type\":\"string\",\"doc\":\"This is a single line comment on a field, in a record that has no comment.\"}]}")
}