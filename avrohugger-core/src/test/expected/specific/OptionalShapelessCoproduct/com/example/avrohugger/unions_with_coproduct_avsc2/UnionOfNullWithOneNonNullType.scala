/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.example.avrohugger.unions_with_coproduct_avsc2

import scala.annotation.switch

import shapeless.{:+:, CNil}

final case class UnionOfNullWithOneNonNullType(var f3: Option[Double :+: CNil]) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(None)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        f3 match {
          case Some(x) => x
          case None => null
        }
      }.asInstanceOf[AnyRef]
      case _ => throw new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.f3 = {
        value match {
          case null => None
          case _ => Some(value)
        }
      }.asInstanceOf[Option[Double :+: CNil]]
      case _ => throw new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = com.example.avrohugger.unions_with_coproduct_avsc2.UnionOfNullWithOneNonNullType.SCHEMA$
}

object UnionOfNullWithOneNonNullType {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"UnionOfNullWithOneNonNullType\",\"namespace\":\"com.example.avrohugger.unions_with_coproduct_avsc2\",\"fields\":[{\"name\":\"f3\",\"type\":[\"null\",\"double\"]}]}")
}