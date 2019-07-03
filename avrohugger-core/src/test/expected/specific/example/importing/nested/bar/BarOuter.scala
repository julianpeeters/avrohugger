/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.importing.nested.bar

import scala.annotation.switch

case class BarOuter(var inner: BarInner) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(new BarInner)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        inner
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.inner = {
        value
      }.asInstanceOf[BarInner]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = BarOuter.SCHEMA$
}

object BarOuter {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"BarOuter\",\"namespace\":\"example.importing.nested.bar\",\"fields\":[{\"name\":\"inner\",\"type\":{\"type\":\"record\",\"name\":\"BarInner\",\"fields\":[{\"name\":\"id\",\"type\":\"string\"},{\"name\":\"status\",\"type\":{\"type\":\"enum\",\"name\":\"Status\",\"namespace\":\"example.importing.nested.foo\",\"symbols\":[\"Created\",\"InProgress\",\"Finished\"]}}]}}]}")
}