/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example

import scala.annotation.switch

import test.major.Vehicle

/**
 * Auto-Generated Schema
 * @param vehicle Auto-Generated Field
 */
case class Garage(var vehicle: Vehicle) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(new Vehicle)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        vehicle
      }.asInstanceOf[AnyRef]
      case _ => throw new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.vehicle = {
        value
      }.asInstanceOf[Vehicle]
      case _ => throw new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = Garage.SCHEMA$
}

object Garage {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Garage\",\"namespace\":\"example\",\"doc\":\"Auto-Generated Schema\",\"fields\":[{\"name\":\"vehicle\",\"type\":{\"type\":\"record\",\"name\":\"Vehicle\",\"namespace\":\"test.major\",\"doc\":\"Auto-Generated Schema\",\"fields\":[{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Auto-Generated Field\"}]},\"doc\":\"Auto-Generated Field\"}]}")
}