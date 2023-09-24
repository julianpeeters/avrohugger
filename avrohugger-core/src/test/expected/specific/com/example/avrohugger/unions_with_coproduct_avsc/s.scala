/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.example.avrohugger.unions_with_coproduct_avsc

import scala.annotation.switch

import shapeless.{:+:, CNil, Coproduct}

final case class s(var s: String, var ns: Option[String], var multi2: Either[com.example.avrohugger.unions_with_coproduct_avsc.r1, com.example.avrohugger.unions_with_coproduct_avsc.r2], var multi2opt: Option[Either[com.example.avrohugger.unions_with_coproduct_avsc.r1, com.example.avrohugger.unions_with_coproduct_avsc.r2]], var multi3: com.example.avrohugger.unions_with_coproduct_avsc.r3 :+: com.example.avrohugger.unions_with_coproduct_avsc.r1 :+: com.example.avrohugger.unions_with_coproduct_avsc.r2 :+: CNil, var multi3opt: Option[com.example.avrohugger.unions_with_coproduct_avsc.r3 :+: com.example.avrohugger.unions_with_coproduct_avsc.r1 :+: com.example.avrohugger.unions_with_coproduct_avsc.r2 :+: CNil]) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this("", None, Left(new r1), None, shapeless.Coproduct[com.example.avrohugger.unions_with_coproduct_avsc.r3 :+: com.example.avrohugger.unions_with_coproduct_avsc.r1 :+: com.example.avrohugger.unions_with_coproduct_avsc.r2 :+: CNil](new r3), None)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        s
      }.asInstanceOf[AnyRef]
      case 1 => {
        ns match {
          case Some(x) => x
          case None => null
        }
      }.asInstanceOf[AnyRef]
      case 2 => {
        multi2
      }.asInstanceOf[AnyRef]
      case 3 => {
        multi2opt match {
          case Some(x) => x
          case None => null
        }
      }.asInstanceOf[AnyRef]
      case 4 => {
        multi3
      }.asInstanceOf[AnyRef]
      case 5 => {
        multi3opt match {
          case Some(x) => x
          case None => null
        }
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.s = {
        value.toString
      }.asInstanceOf[String]
      case 1 => this.ns = {
        value match {
          case null => None
          case _ => Some(value.toString)
        }
      }.asInstanceOf[Option[String]]
      case 2 => this.multi2 = {
        value
      }.asInstanceOf[Either[com.example.avrohugger.unions_with_coproduct_avsc.r1, com.example.avrohugger.unions_with_coproduct_avsc.r2]]
      case 3 => this.multi2opt = {
        value match {
          case null => None
          case _ => Some(value)
        }
      }.asInstanceOf[Option[Either[com.example.avrohugger.unions_with_coproduct_avsc.r1, com.example.avrohugger.unions_with_coproduct_avsc.r2]]]
      case 4 => this.multi3 = {
        value
      }.asInstanceOf[com.example.avrohugger.unions_with_coproduct_avsc.r3 :+: com.example.avrohugger.unions_with_coproduct_avsc.r1 :+: com.example.avrohugger.unions_with_coproduct_avsc.r2 :+: CNil]
      case 5 => this.multi3opt = {
        value match {
          case null => None
          case _ => Some(value)
        }
      }.asInstanceOf[Option[com.example.avrohugger.unions_with_coproduct_avsc.r3 :+: com.example.avrohugger.unions_with_coproduct_avsc.r1 :+: com.example.avrohugger.unions_with_coproduct_avsc.r2 :+: CNil]]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = com.example.avrohugger.unions_with_coproduct_avsc.s.SCHEMA$
}

object s {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"s\",\"namespace\":\"com.example.avrohugger.unions_with_coproduct_avsc\",\"fields\":[{\"name\":\"s\",\"type\":\"string\"},{\"name\":\"ns\",\"type\":[\"null\",\"string\"]},{\"name\":\"multi2\",\"type\":[{\"type\":\"record\",\"name\":\"r1\",\"fields\":[{\"name\":\"f1\",\"type\":\"int\"}]},{\"type\":\"record\",\"name\":\"r2\",\"fields\":[{\"name\":\"f2\",\"type\":\"double\"}]}]},{\"name\":\"multi2opt\",\"type\":[\"null\",\"r1\",\"r2\"]},{\"name\":\"multi3\",\"type\":[{\"type\":\"record\",\"name\":\"r3\",\"fields\":[{\"name\":\"f3\",\"type\":\"boolean\"}]},\"r1\",\"r2\"]},{\"name\":\"multi3opt\",\"type\":[\"null\",\"r3\",\"r1\",\"r2\"]}]}")
}