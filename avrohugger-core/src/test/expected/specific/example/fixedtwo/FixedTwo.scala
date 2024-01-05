/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package fixedtwo

import scala.annotation.switch

import fixedtwo.two.fixed

import fixedtwo.one.fixed

final case class FixedTwo(var first: fixedtwo.one.fixed, var second: fixedtwo.two.fixed) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(fixedtwo.one.fixed(Array()), fixedtwo.two.fixed(Array()))
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        first
      }.asInstanceOf[AnyRef]
      case 1 => {
        second
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.first = {
        value
      }.asInstanceOf[fixedtwo.one.fixed]
      case 1 => this.second = {
        value
      }.asInstanceOf[fixedtwo.two.fixed]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = fixedtwo.FixedTwo.SCHEMA$
}

object FixedTwo {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"FixedTwo\",\"namespace\":\"fixedtwo\",\"fields\":[{\"name\":\"first\",\"type\":{\"type\":\"fixed\",\"name\":\"fixed\",\"namespace\":\"fixedtwo.one\",\"size\":16}},{\"name\":\"second\",\"type\":{\"type\":\"fixed\",\"name\":\"fixed\",\"namespace\":\"fixedtwo.two\",\"size\":16}}]}")
}