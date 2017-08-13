/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package test

import scala.annotation.switch

/**
 * Auto-Generated Schema
 * @param name Auto-Generated Field
 */
case class Pet(var name: String) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this("")
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        name
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.name = {
        value.toString
      }.asInstanceOf[String]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = Pet.SCHEMA$
}

object Pet {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Pet\",\"namespace\":\"test\",\"doc\":\"Auto-Generated Schema\",\"fields\":[{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Auto-Generated Field\"}]}")
}