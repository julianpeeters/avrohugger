/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.miguno.avro

case class twitter_schema(var username: String, var tweet: String, var timestamp: Long) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this("", "", 1L)
  def get(field: Int): AnyRef = {
    field match {
      case pos if pos == 0 => {
        username
      }.asInstanceOf[AnyRef]
      case pos if pos == 1 => {
        tweet
      }.asInstanceOf[AnyRef]
      case pos if pos == 2 => {
        timestamp
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field: Int, value: Any): Unit = {
    field match {
      case pos if pos == 0 => this.username = {
        value match {
          case (value: org.apache.avro.util.Utf8) => value.toString
          case _ => value
        }
      }.asInstanceOf[String]
      case pos if pos == 1 => this.tweet = {
        value match {
          case (value: org.apache.avro.util.Utf8) => value.toString
          case _ => value
        }
      }.asInstanceOf[String]
      case pos if pos == 2 => this.timestamp = {
        value
      }.asInstanceOf[Long]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = twitter_schema.SCHEMA$
}

object twitter_schema {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"twitter_schema\",\"namespace\":\"com.miguno.avro\",\"fields\":[{\"name\":\"username\",\"type\":\"string\",\"doc\":\"Name of the user account on Twitter.com\"},{\"name\":\"tweet\",\"type\":\"string\",\"doc\":\"The content of the user\'s Twitter message\"},{\"name\":\"timestamp\",\"type\":\"long\",\"doc\":\"Unix epoch time in milliseconds\"}],\"doc:\":\"A basic schema for storing Twitter messages\"}")
}