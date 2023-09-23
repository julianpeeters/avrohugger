/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.example.avrohugger.unions_with_coproduct_avsc

import scala.annotation.switch

final case class r1(var f1: Int) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(0)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        f1
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.f1 = {
        value
      }.asInstanceOf[Int]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = com.example.avrohugger.unions_with_coproduct_avsc.r1.SCHEMA$
}

object r1 {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"r1\",\"namespace\":\"com.example.avrohugger.unions_with_coproduct_avsc\",\"fields\":[{\"name\":\"f1\",\"type\":\"int\"}]}")
}