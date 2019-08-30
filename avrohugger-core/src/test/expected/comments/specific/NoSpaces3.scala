/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.example

import scala.annotation.switch

/** Example adapted from https://github.com/delagoya. This is a comment for the whole protocol */


/**
 * The comment applies to the `NoSpaces3` record, but is not indented to the
 * level of the record specification.
 * @param comment_property 
 */
final case class NoSpaces3(var comment_property: String) extends org.apache.avro.specific.SpecificRecordBase {
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
  def getSchema: org.apache.avro.Schema = NoSpaces3.SCHEMA$
}

object NoSpaces3 {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"NoSpaces3\",\"namespace\":\"com.example\",\"doc\":\"The comment applies to the `NoSpaces3` record, but is not indented to the\\nlevel of the record specification.\",\"fields\":[{\"name\":\"comment_property\",\"type\":\"string\"}]}")
}