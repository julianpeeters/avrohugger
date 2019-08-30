/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl

import scala.annotation.switch

final case class Names(var public$: String, var `ends_with_`: String) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this("", "")
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        public$
      }.asInstanceOf[AnyRef]
      case 1 => {
        `ends_with_`
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.public$ = {
        value.toString
      }.asInstanceOf[String]
      case 1 => this.`ends_with_` = {
        value.toString
      }.asInstanceOf[String]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = Names.SCHEMA$
}

object Names {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Names\",\"namespace\":\"example.idl\",\"fields\":[{\"name\":\"public\",\"type\":\"string\"},{\"name\":\"ends_with_\",\"type\":\"string\"}]}")
}