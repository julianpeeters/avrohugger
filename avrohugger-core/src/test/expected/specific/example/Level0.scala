/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example

import scala.annotation.switch

final case class Level0(var level1: Level1) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(new Level1)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        level1
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.level1 = {
        value
      }.asInstanceOf[Level1]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = Level0.SCHEMA$
}

object Level0 {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Level0\",\"namespace\":\"example\",\"fields\":[{\"name\":\"level1\",\"type\":{\"type\":\"record\",\"name\":\"Level1\",\"fields\":[{\"name\":\"level2\",\"type\":{\"type\":\"record\",\"name\":\"Level2\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"}]}}]}}]}")
}