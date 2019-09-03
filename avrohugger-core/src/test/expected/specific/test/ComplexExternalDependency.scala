/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package test

import scala.annotation.switch

import model.UnionRecord

import model.v2.NestedRecord

final case class ComplexExternalDependency(var nestedrecord: NestedRecord) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(new NestedRecord)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        nestedrecord
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.nestedrecord = {
        value
      }.asInstanceOf[NestedRecord]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = ComplexExternalDependency.SCHEMA$
}

object ComplexExternalDependency {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"ComplexExternalDependency\",\"namespace\":\"test\",\"fields\":[{\"name\":\"nestedrecord\",\"type\":{\"type\":\"record\",\"name\":\"NestedRecord\",\"namespace\":\"model.v2\",\"fields\":[{\"name\":\"nestedunion\",\"type\":[\"null\",{\"type\":\"record\",\"name\":\"UnionRecord\",\"namespace\":\"model\",\"fields\":[{\"name\":\"blah\",\"type\":\"string\"}]}]}]}}]}")
}