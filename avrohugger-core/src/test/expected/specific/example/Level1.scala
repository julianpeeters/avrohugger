/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example

import scala.annotation.switch

final case class Level1(var level2: Level2) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(new Level2)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        level2
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.level2 = {
        value
      }.asInstanceOf[Level2]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = Level1.SCHEMA$
}

object Level1 {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Level1\",\"namespace\":\"example\",\"fields\":[{\"name\":\"level2\",\"type\":{\"type\":\"record\",\"name\":\"Level2\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"}]}}]}")
}