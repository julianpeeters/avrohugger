/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package model.v2

import scala.annotation.switch

import model.UnionRecord

final case class NestedRecord(var nestedunion: Option[UnionRecord]) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(None)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        nestedunion match {
          case Some(x) => x
          case None => null
        }
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.nestedunion = {
        value match {
          case null => None
          case _ => Some(value)
        }
      }.asInstanceOf[Option[UnionRecord]]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = NestedRecord.SCHEMA$
}

object NestedRecord {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"NestedRecord\",\"namespace\":\"model.v2\",\"fields\":[{\"name\":\"nestedunion\",\"type\":[\"null\",{\"type\":\"record\",\"name\":\"UnionRecord\",\"namespace\":\"model\",\"fields\":[{\"name\":\"blah\",\"type\":\"string\"}]}]}]}")
}