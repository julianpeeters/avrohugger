/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.example.avrohugger.unions_with_coproduct_avsc2

import scala.annotation.switch

final case class UnionOfMoreThanTwoNonNullTypes(var r6: (Boolean| Int| String)) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(false)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        r6
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.r6 = {
        value
      }.asInstanceOf[(Boolean| Int| String)]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = com.example.avrohugger.unions_with_coproduct_avsc2.UnionOfMoreThanTwoNonNullTypes.SCHEMA$
}

object UnionOfMoreThanTwoNonNullTypes {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"UnionOfMoreThanTwoNonNullTypes\",\"namespace\":\"com.example.avrohugger.unions_with_coproduct_avsc2\",\"fields\":[{\"name\":\"r6\",\"type\":[\"boolean\",\"int\",\"string\"]}]}")
}