/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.example.avrohugger.unions_with_coproduct_avsc2

import scala.annotation.switch

import shapeless.{:+:, CNil}

final case class UnionOfNullWithTwoNonNullTypes(var r5: Option[Int :+: String :+: CNil]) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(None)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        r5 match {
          case Some(x) => x
          case None => null
        }
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.r5 = {
        value match {
          case null => None
          case _ => Some(value)
        }
      }.asInstanceOf[Option[Int :+: String :+: CNil]]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = com.example.avrohugger.unions_with_coproduct_avsc2.UnionOfNullWithTwoNonNullTypes.SCHEMA$
}

object UnionOfNullWithTwoNonNullTypes {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"UnionOfNullWithTwoNonNullTypes\",\"namespace\":\"com.example.avrohugger.unions_with_coproduct_avsc2\",\"fields\":[{\"name\":\"r5\",\"type\":[\"null\",\"int\",\"string\"]}]}")
}