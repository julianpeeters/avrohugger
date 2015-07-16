import avrohugger._
import org.specs2._

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
          |package com.miguno.avro
          |
          |case class twitter_schema(username: String, tweet: String, timestamp: Long)
        """.stripMargin.trim
    }

    "correctly generate a nested case class definition" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/nested.avsc")
      val gen = new Generator
      gen.fromFile(infile)

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
    val gen = new Generator
    gen.fromFile(infile)

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
    val gen = new Generator
    gen.fromFile(infile)

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
    val gen = new Generator
    gen.fromFile(infile)

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
    val gen = new Generator
    gen.fromFile(infile)

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
