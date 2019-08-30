/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.miguno.avro

import scala.annotation.switch

/**
 * @param username Name of the user account on Twitter.com
 * @param tweet The content of the user's Twitter message
 * @param timestamp Unix epoch time in milliseconds
 */
final case class twitter_schema(var username: String, var tweet: String, var timestamp: Long) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this("", "", 0L)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        username
      }.asInstanceOf[AnyRef]
      case 1 => {
        tweet
      }.asInstanceOf[AnyRef]
      case 2 => {
        timestamp
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.username = {
        value.toString
      }.asInstanceOf[String]
      case 1 => this.tweet = {
        value.toString
      }.asInstanceOf[String]
      case 2 => this.timestamp = {
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