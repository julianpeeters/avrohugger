/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.example.avrohugger.unions_with_coproduct_avsc

import scala.annotation.switch

final case class r2(var f2: Double) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(0.0)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        f2
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.f2 = {
        value
      }.asInstanceOf[Double]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = com.example.avrohugger.unions_with_coproduct_avsc.r2.SCHEMA$
}

object r2 {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"r2\",\"namespace\":\"com.example.avrohugger.unions_with_coproduct_avsc\",\"fields\":[{\"name\":\"f2\",\"type\":\"double\"}]}")
}