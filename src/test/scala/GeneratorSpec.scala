import avrohugger._

import org.specs2._
import mutable._
import specification._

class GeneratorSpec extends mutable.Specification {

  "a Generator" should {
    
    "correctly generate a simple case class definition in a package" in {
      val infile = new java.io.File("src/test/avro/twitter.avro")
      Generator.fromFile(infile)
      val source = scala.io.Source.fromFile("target/generated-sources/com/miguno/avro/twitter_schema.scala").mkString
      println(source)
       source === 
"""/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.miguno.avro

case class twitter_schema(username: String, tweet: String, timestamp: Long)"""
    }
    
    
    "correctly generate a set of complex case class definitions in the default package" in {
      val infile = new java.io.File("src/test/avro/enron.avsc")
      Generator.fromFile(infile)
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
      
  }

}

