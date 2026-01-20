/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.example

import scala.annotation.switch

sealed trait Example4 extends org.apache.avro.specific.SpecificRecordBase with Product with Serializable

final case class NoSpaces4(var comment_property1: String) extends org.apache.avro.specific.SpecificRecordBase with Example4 {
  def this() = this("")
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        comment_property1
      }.asInstanceOf[AnyRef]
      case _ => throw new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.comment_property1 = {
        value.toString
      }.asInstanceOf[String]
      case _ => throw new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = com.example.NoSpaces4.SCHEMA$
}

final object NoSpaces4 {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"NoSpaces4\",\"namespace\":\"com.example\",\"fields\":[{\"name\":\"comment_property1\",\"type\":\"string\"}]}")
}

final case class NoSpaces5(var comment_property2: String) extends org.apache.avro.specific.SpecificRecordBase with Example4 {
  def this() = this("")
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        comment_property2
      }.asInstanceOf[AnyRef]
      case _ => throw new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.comment_property2 = {
        value.toString
      }.asInstanceOf[String]
      case _ => throw new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = com.example.NoSpaces5.SCHEMA$
}

final object NoSpaces5 {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"NoSpaces5\",\"namespace\":\"com.example\",\"fields\":[{\"name\":\"comment_property2\",\"type\":\"string\"}]}")
}