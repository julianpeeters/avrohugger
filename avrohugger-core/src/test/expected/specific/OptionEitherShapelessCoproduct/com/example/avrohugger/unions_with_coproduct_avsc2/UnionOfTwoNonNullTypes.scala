/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.example.avrohugger.unions_with_coproduct_avsc2

import scala.annotation.switch

final case class UnionOfTwoNonNullTypes(var r4: Either[Int, String]) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(Left(0))
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        r4
      }.asInstanceOf[AnyRef]
      case _ => throw new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.r4 = {
        value
      }.asInstanceOf[Either[Int, String]]
      case _ => throw new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = com.example.avrohugger.unions_with_coproduct_avsc2.UnionOfTwoNonNullTypes.SCHEMA$
}

object UnionOfTwoNonNullTypes {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"UnionOfTwoNonNullTypes\",\"namespace\":\"com.example.avrohugger.unions_with_coproduct_avsc2\",\"fields\":[{\"name\":\"r4\",\"type\":[\"int\",\"string\"]}]}")
}