import avrohugger._

import org.specs2._
import mutable._
import specification._

class GeneratorSpec extends mutable.Specification {

  "a Generator" should {
    
    "correctly generate a simple case class definition in a package" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/twitter.avro")
      val gen = new Generator
      gen.fromFile(infile)
      val source = scala.io.Source.fromFile("target/generated-sources/com/miguno/avro/twitter_schema.scala").mkString
      println(source)
       source === 
"""/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.miguno.avro

case class twitter_schema(username: String, tweet: String, timestamp: Long)"""
    }
    
    
  }
}

