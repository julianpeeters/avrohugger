import avrohugger._
import specific._

import org.specs2._
import mutable._
import specification._

class SpecificGeneratorSpec extends mutable.Specification {

  "a SpecificGenerator" should {
    
    "correctly generate a case class definition that extends `SpecificRecordBase` in a package" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/mail.avpr")
      val gen = new SpecificGenerator
      gen.fromFile(infile)
      val source = scala.io.Source.fromFile("target/generated-sources/example/proto/Message.scala").mkString
      println(source)
       source === 
"""/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.proto

case class Message(var to: String, var from: String, var body: String) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this("", "", "")
  def get(field: Int): AnyRef = {
    field match {
      case pos if pos == 0 => {
        to
      }.asInstanceOf[AnyRef]
      case pos if pos == 1 => {
        from
      }.asInstanceOf[AnyRef]
      case pos if pos == 2 => {
        body
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field: Int, value: Any): Unit = {
    field match {
      case pos if pos == 0 => this.to = {
        value match {
          case (value: org.apache.avro.util.Utf8) => value.toString
          case _ => value
        }
      }.asInstanceOf[String]
      case pos if pos == 1 => this.from = {
        value match {
          case (value: org.apache.avro.util.Utf8) => value.toString
          case _ => value
        }
      }.asInstanceOf[String]
      case pos if pos == 2 => this.body = {
        value match {
          case (value: org.apache.avro.util.Utf8) => value.toString
          case _ => value
        }
      }.asInstanceOf[String]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = Message.SCHEMA$
}

object Message {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Message\",\"namespace\":\"example.proto\",\"fields\":[{\"name\":\"to\",\"type\":\"string\"},{\"name\":\"from\",\"type\":\"string\"},{\"name\":\"body\",\"type\":\"string\"}]}")
}"""
    }
  }

}

