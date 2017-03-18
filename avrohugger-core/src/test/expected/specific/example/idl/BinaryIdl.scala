/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl

import scala.annotation.switch

case class BinaryIdl(var data: Array[Byte]) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(null)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case pos$ if pos$ == 0 => {
        java.nio.ByteBuffer.wrap(data)
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case pos$ if pos$ == 0 => this.data = {
        value match {
          case (buffer: java.nio.ByteBuffer) => {
            buffer.array()
          }
        }
      }.asInstanceOf[Array[Byte]]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = BinaryIdl.SCHEMA$
}

object BinaryIdl {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"BinaryIdl\",\"namespace\":\"example.idl\",\"fields\":[{\"name\":\"data\",\"type\":\"bytes\"}]}")
}