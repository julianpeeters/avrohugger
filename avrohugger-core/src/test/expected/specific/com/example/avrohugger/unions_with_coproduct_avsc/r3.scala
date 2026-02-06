/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.example.avrohugger.unions_with_coproduct_avsc

import scala.annotation.switch

final case class r3(var f3: Boolean) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(false)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        f3
      }.asInstanceOf[AnyRef]
      case _ => throw new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.f3 = {
        value
      }.asInstanceOf[Boolean]
      case _ => throw new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = com.example.avrohugger.unions_with_coproduct_avsc.r3.SCHEMA$
}

object r3 {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"r3\",\"namespace\":\"com.example.avrohugger.unions_with_coproduct_avsc\",\"fields\":[{\"name\":\"f3\",\"type\":\"boolean\"}]}")
}