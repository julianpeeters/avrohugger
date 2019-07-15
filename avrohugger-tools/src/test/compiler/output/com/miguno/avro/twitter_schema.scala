/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.miguno.avro

/**
 * @param username Name of the user account on Twitter.com
 * @param tweet The content of the user's Twitter message
 * @param timestamp Unix epoch time in milliseconds
 */
case class twitter_schema(username: org.apache.avro.util.Utf8, tweet: org.apache.avro.util.Utf8, timestamp: Long)