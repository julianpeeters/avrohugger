/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example

import scala.annotation.switch

final case class Compass(var direction: Direction) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(null)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        direction
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.direction = {
        value
      }.asInstanceOf[Direction]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = Compass.SCHEMA$
}

object Compass {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Compass\",\"namespace\":\"example\",\"fields\":[{\"name\":\"direction\",\"type\":{\"type\":\"enum\",\"name\":\"Direction\",\"symbols\":[\"NORTH\",\"SOUTH\",\"EAST\",\"WEST\"]}}]}")
}