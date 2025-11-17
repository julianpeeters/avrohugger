/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.example.avrohugger.unions_with_coproduct_avsc2

import scala.annotation.switch

final case class UnionOfNullWithMoreThanTwoNonNullTypes(var r7: Option[(Boolean| Int| String)]) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(None)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        r7 match {
          case Some(x) => x
          case None => null
        }
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.r7 = {
        value match {
          case null => None
          case _ => Some(value)
        }
      }.asInstanceOf[Option[(Boolean| Int| String)]]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = com.example.avrohugger.unions_with_coproduct_avsc2.UnionOfNullWithMoreThanTwoNonNullTypes.SCHEMA$
}

object UnionOfNullWithMoreThanTwoNonNullTypes {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"UnionOfNullWithMoreThanTwoNonNullTypes\",\"namespace\":\"com.example.avrohugger.unions_with_coproduct_avsc2\",\"fields\":[{\"name\":\"r7\",\"type\":[\"null\",\"boolean\",\"int\",\"string\"]}]}")
}