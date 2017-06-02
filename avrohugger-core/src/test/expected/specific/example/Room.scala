/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example

import scala.annotation.switch

import example.types.door

case class Room(var door: door = example.types.door.NORTH) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(example.types.door.NORTH)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        door
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.door = {
        value
      }.asInstanceOf[door]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = Room.SCHEMA$
}

object Room {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Room\",\"namespace\":\"example\",\"fields\":[{\"name\":\"door\",\"type\":{\"type\":\"enum\",\"name\":\"door\",\"namespace\":\"example.types\",\"symbols\":[\"NORTH\",\"SOUTH\",\"EAST\",\"WEST\"]},\"default\":\"NORTH\"}]}")
}