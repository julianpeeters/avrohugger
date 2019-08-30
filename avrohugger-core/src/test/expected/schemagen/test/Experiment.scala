/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package test

import scala.annotation.switch

/**
 * Auto-Generated Schema
 * @param name Auto-Generated Field
 * @param age Auto-Generated Field
 */
final case class Experiment(var name: String, var age: Int) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this("", 0)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        name
      }.asInstanceOf[AnyRef]
      case 1 => {
        age
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.name = {
        value.toString
      }.asInstanceOf[String]
      case 1 => this.age = {
        value
      }.asInstanceOf[Int]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = Experiment.SCHEMA$
}

object Experiment {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Experiment\",\"namespace\":\"test\",\"doc\":\"Auto-Generated Schema\",\"fields\":[{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Auto-Generated Field\"},{\"name\":\"age\",\"type\":\"int\",\"doc\":\"Auto-Generated Field\"}]}")
}