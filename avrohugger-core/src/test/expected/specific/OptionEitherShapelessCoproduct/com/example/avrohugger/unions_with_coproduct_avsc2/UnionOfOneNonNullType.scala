/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.example.avrohugger.unions_with_coproduct_avsc2

import scala.annotation.switch

final case class UnionOfOneNonNullType(var f3: Map[String, String]) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(Map("" -> ""))
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        f3
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.f3 = {
        value
      }.asInstanceOf[Map[String, String]]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = com.example.avrohugger.unions_with_coproduct_avsc2.UnionOfOneNonNullType.SCHEMA$
}

object UnionOfOneNonNullType {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"UnionOfOneNonNullType\",\"namespace\":\"com.example.avrohugger.unions_with_coproduct_avsc2\",\"fields\":[{\"name\":\"f3\",\"type\":[{\"type\":\"map\",\"values\":\"string\"}]}]}")
}