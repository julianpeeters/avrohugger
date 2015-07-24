
import avrohugger._
import format.Standard
import org.specs2._

class StandardGeneratorSpec extends mutable.Specification {

  "a Generator" should {

    "correctly generate a simple case class definition in a package" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/twitter.avro")
      val gen = new Generator(Standard)
      gen.fileToFile(infile)
      val source = scala.io.Source.fromFile("target/generated-sources/com/miguno/avro/twitter_schema.scala").mkString
      source ===
        """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |package com.miguno.avro
          |
          |case class twitter_schema(username: String, tweet: String, timestamp: Long)
        """.stripMargin.trim
    }

    "correctly generate a simple case class definition from a schema as a string" in {
      val schemaString = """{"type":"record","name":"Animal","namespace":"test","fields":[{"name":"name","type":"string"}],"doc:":"A basic schema for storing Twitter messages"}"""
      val gen = new Generator(Standard)
      val List(source) = gen.stringToStrings(schemaString)
      source ===
        """package test
          |
          |case class Animal(name: String)
        """.stripMargin.trim
    }

    "correctly generate a nested case class definition" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/nested.avsc")
      val gen = new Generator(Standard)
      gen.fileToFile(infile)

      val source0 = scala.io.Source.fromFile("target/generated-sources/example/Level0.scala").mkString
      source0 ====
        """
          |/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |package example
          |
          |case class Level0(level1: Level1)
        """.stripMargin.trim

      val source1 = scala.io.Source.fromFile("target/generated-sources/example/Level1.scala").mkString
      source1 ====
        """
          |/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |package example
          |
          |case class Level1(level2: Level2)
        """.stripMargin.trim

      val source2 = scala.io.Source.fromFile("target/generated-sources/example/Level2.scala").mkString
      source2 ====
        """
          |/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |package example
          |
          |case class Level2(name: String)
        """.stripMargin.trim
    }
  }

  "correctly generate a nested case class from IDL" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/nested.avdl")
    val gen = new Generator(Standard)
    gen.fileToFile(infile)

    val source0 = scala.io.Source.fromFile("target/generated-sources/example/idl/Level0.scala").mkString
    source0 ====
      """
        |/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
        |package example.idl
        |
        |case class Level0(level1: Level1)
      """.stripMargin.trim

    val source1 = scala.io.Source.fromFile("target/generated-sources/example/idl/Level1.scala").mkString
    source1 ====
      """
        |/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
        |package example.idl
        |
        |case class Level1(level2: Level2)
      """.stripMargin.trim

    val source2 = scala.io.Source.fromFile("target/generated-sources/example/idl/Level2.scala").mkString
    source2 ====
      """
        |/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
        |package example.idl
        |
        |case class Level2(name: String)
      """.stripMargin.trim
  }

  "correctly generate enums" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/enums.avsc")
    val gen = new Generator(Standard)
    gen.fileToFile(infile)

    val sourceEnum = scala.io.Source.fromFile("target/generated-sources/example/Suit.scala").mkString
    sourceEnum ====
      """
        |/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
        |package example
        |
        |object Suit extends Enumeration {
        |  type Suit = Value
        |  val SPADES, DIAMONDS, CLUBS, HEARTS = Value
        |}
      """.stripMargin.trim
  }

  "correctly generate enums from protocol files" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/enums.avpr")
    val gen = new Generator(Standard)
    gen.fileToFile(infile)

    val sourceEnum = scala.io.Source.fromFile("target/generated-sources/example/proto/Suit.scala").mkString
    sourceEnum ====
      """
        |/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
        |package example.proto
        |
        |object Suit extends Enumeration {
        |  type Suit = Value
        |  val SPADES, HEARTS, DIAMONDS, CLUBS = Value
        |}
      """.stripMargin.trim

    val sourceCard = scala.io.Source.fromFile("target/generated-sources/example/proto/Card.scala").mkString
    sourceCard ====
      """
        |/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
        |package example.proto
        |
        |case class Card(suit: Suit, number: Int)
      """.stripMargin.trim
  }

  "correctly generate enums from IDL files" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/enums.avdl")
    val gen = new Generator(Standard)
    gen.fileToFile(infile)

    val sourceEnum = scala.io.Source.fromFile("target/generated-sources/example/idl/Suit.scala").mkString
    sourceEnum ====
      """
        |/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
        |package example.idl
        |
        |object Suit extends Enumeration {
        |  type Suit = Value
        |  val SPADES, DIAMONDS, CLUBS, HEARTS = Value
        |}
      """.stripMargin.trim

    val sourceCard = scala.io.Source.fromFile("target/generated-sources/example/idl/Card.scala").mkString
    sourceCard ====
      """
        |/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
        |package example.idl
        |
        |case class Card(suit: Suit, number: Int)
      """.stripMargin.trim
  }
}
