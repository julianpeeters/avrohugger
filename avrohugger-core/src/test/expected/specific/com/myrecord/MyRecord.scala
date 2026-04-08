/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.myrecord

import scala.annotation.switch

final case class MyRecord(var MyID: com.myrecord.ID, var OtherRecord: com.otherrecord.OtherRecord) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(new com.myrecord.ID, new com.otherrecord.OtherRecord)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        MyID
      }.asInstanceOf[AnyRef]
      case 1 => {
        OtherRecord
      }.asInstanceOf[AnyRef]
      case _ => throw new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.MyID = {
        value
      }.asInstanceOf[com.myrecord.ID]
      case 1 => this.OtherRecord = {
        value
      }.asInstanceOf[com.otherrecord.OtherRecord]
      case _ => throw new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = com.myrecord.MyRecord.SCHEMA$
}

object MyRecord {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"MyRecord\",\"namespace\":\"com.myrecord\",\"fields\":[{\"name\":\"MyID\",\"type\":{\"type\":\"record\",\"name\":\"ID\",\"fields\":[{\"name\":\"value\",\"type\":\"string\"}]}},{\"name\":\"OtherRecord\",\"type\":{\"type\":\"record\",\"name\":\"OtherRecord\",\"namespace\":\"com.otherrecord\",\"fields\":[{\"name\":\"OtherId\",\"type\":{\"type\":\"record\",\"name\":\"ID\",\"fields\":[{\"name\":\"value\",\"type\":\"string\"}]}}]}}]}")
}