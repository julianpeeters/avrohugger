/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package test

sealed trait Calculator extends Product with Serializable

final case class Added(value: Int) extends Calculator

final case class Subtracted(value: Int) extends Calculator

final case class Divided(value: Int) extends Calculator

final case class Multiplied(value: Int) extends Calculator

final case class Reset() extends Calculator