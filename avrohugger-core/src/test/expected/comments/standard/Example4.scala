/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.example

/** Example adapted from https://github.com/delagoya. This is a comment for the whole protocol */
sealed trait Example4 extends Product with Serializable

final case class NoSpaces4(comment_property1: String) extends Example4

final case class NoSpaces5(comment_property2: String) extends Example4