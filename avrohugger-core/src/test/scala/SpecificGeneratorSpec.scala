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

case class Message(to: String, from: String, body: String) extends SpecificRecordBase = {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Message\",\"namespace\":\"example.proto\",\"fields\":[{\"name\":\"to\",\"type\":\"string\"},{\"name\":\"from\",\"type\":\"string\"},{\"name\":\"body\",\"type\":\"string\"}]}")
  def this = this("", "", "")
  def get(field: Int): AnyRef = {
    field match {
      case pos if pos == 0 => to.asInstanceOf[AnyRef]
      case pos if pos == 1 => from.asInstanceOf[AnyRef]
      case pos if pos == 2 => body.asInstanceOf[AnyRef]
    }
  }
  def put(field: Int, value: Any): Unit = {
    field match {
      case pos if pos == 0 => this.to = value.asInstanceOf[String]
      case pos if pos == 1 => this.from = value.asInstanceOf[String]
      case pos if pos == 2 => this.body = value.asInstanceOf[String]
    }
  }
  def getSchema: org.apache.avro.Schema = SCHEMA$
}"""
    }
    
   /* 
    "correctly generate a set of complex case class definitions in the default package" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/enron.avsc")
      val gen = new Generator
      gen.fromFile(infile)
      val source0 = scala.io.Source.fromFile("target/generated-sources/TUPLE_0.scala").mkString
      val source1 = scala.io.Source.fromFile("target/generated-sources/TUPLE_1.scala").mkString
      val source2 = scala.io.Source.fromFile("target/generated-sources/TUPLE_2.scala").mkString
      val source3 = scala.io.Source.fromFile("target/generated-sources/TUPLE_3.scala").mkString
      val source4 = scala.io.Source.fromFile("target/generated-sources/TUPLE_4.scala").mkString
      println(source0)
      source0 === 
"""/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
{
  case class TUPLE_0(message_id: Option[String], date: Option[String], from: Option[TUPLE_1], subject: Option[String], body: Option[String], tos: Option[List[Option[TUPLE_2]]], ccs: Option[List[Option[TUPLE_3]]], bccs: Option[List[Option[TUPLE_4]]])
}"""
    
      source1 === 
"""/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
{
  case class TUPLE_1(address: Option[String], name: Option[String])
}"""

      source2 === 
"""/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
{
  case class TUPLE_2(address: Option[String], name: Option[String])
}"""

      source3 === 
"""/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
{
  case class TUPLE_3(address: Option[String], name: Option[String])
}"""

      source4 === 
"""/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
{
  case class TUPLE_4(address: Option[String], name: Option[String])
}"""

    }
   */   
  }

}

