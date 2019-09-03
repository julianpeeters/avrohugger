/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.miguno.avro.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import com.miguno.avro.{twitter_schema => Jtwitter_schema}

/**
 * @param username Name of the user account on Twitter.com
 * @param tweet The content of the user's Twitter message
 * @param timestamp Unix epoch time in milliseconds
 */
final case class twitter_schema(username: String, tweet: String, timestamp: Long) extends AvroSerializeable {
  type J = Jtwitter_schema
  override def toAvro: Jtwitter_schema = {
    new Jtwitter_schema(username, tweet, timestamp)
  }
}

object twitter_schema {
  implicit def reader = new AvroReader[twitter_schema] {
    override type J = Jtwitter_schema
  }
  implicit val metadata: AvroMetadata[twitter_schema, Jtwitter_schema] = new AvroMetadata[twitter_schema, Jtwitter_schema] {
    override val avroClass: Class[Jtwitter_schema] = classOf[Jtwitter_schema]
    override val schema: Schema = Jtwitter_schema.getClassSchema()
    override val fromAvro: (Jtwitter_schema) => twitter_schema = {
      (j: Jtwitter_schema) => twitter_schema(j.getUsername.toString, j.getTweet.toString, j.getTimestamp.toLong)
    }
  }
}