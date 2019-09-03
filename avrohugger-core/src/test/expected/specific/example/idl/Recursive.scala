/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl

import scala.annotation.switch

final case class Recursive(var name: String, var recursive: Option[Recursive]) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this("", None)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        name
      }.asInstanceOf[AnyRef]
      case 1 => {
        recursive match {
          case Some(x) => x
          case None => null
        }
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.name = {
        value.toString
      }.asInstanceOf[String]
      case 1 => this.recursive = {
        value match {
          case null => None
          case _ => Some(value)
        }
      }.asInstanceOf[Option[Recursive]]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = Recursive.SCHEMA$
}

object Recursive {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Recursive\",\"namespace\":\"example.idl\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"recursive\",\"type\":[\"null\",\"Recursive\"]}]}")
}