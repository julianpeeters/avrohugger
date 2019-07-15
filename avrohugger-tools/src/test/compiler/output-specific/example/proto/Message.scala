/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.proto

import scala.annotation.switch

case class Message(var to: org.apache.avro.util.Utf8, var from: org.apache.avro.util.Utf8, var body: org.apache.avro.util.Utf8) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this("", "", "")
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        to
      }.asInstanceOf[AnyRef]
      case 1 => {
        from
      }.asInstanceOf[AnyRef]
      case 2 => {
        body
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.to = {
        value
      }.asInstanceOf[org.apache.avro.util.Utf8]
      case 1 => this.from = {
        value
      }.asInstanceOf[org.apache.avro.util.Utf8]
      case 2 => this.body = {
        value
      }.asInstanceOf[org.apache.avro.util.Utf8]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = Message.SCHEMA$
}

object Message {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Message\",\"namespace\":\"example.proto\",\"fields\":[{\"name\":\"to\",\"type\":\"string\"},{\"name\":\"from\",\"type\":\"string\"},{\"name\":\"body\",\"type\":\"string\"}]}")
}