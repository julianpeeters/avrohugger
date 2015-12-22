
import java.io.File

import avrohugger._
import avrohugger.format.Standard
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
          |/**
          | * @param username Name of the user account on Twitter.com
          | * @param tweet The content of the user's Twitter message
          | * @param timestamp Unix epoch time in milliseconds
          | */
          |case class twitter_schema(username: String, tweet: String, timestamp: Long)
        """.stripMargin.trim
    }

    "correctly generate a simple case class definition in the default package" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/AvroTypeProviderTestNoNamespace.avro")
      val gen = new Generator(Standard)
      gen.fileToFile(infile)
      val source = scala.io.Source.fromFile("target/generated-sources/AvroTypeProviderTestNoNamespace.scala").mkString
      source ===
        """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |/**
          | * Auto-generated schema
          | * @param x Auto-Generated Field
          | */
          |case class AvroTypeProviderTestNoNamespace(x: Int)
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

    "correctly generate a nested case class definition from a schema as a string" in {
      val schemaString = """{
        |  "namespace": "example",
        |  "type": "record",
        |  "name": "Level0",
        |  "fields": [
        |    {
        |      "name": "level1",
        |      "type": {
        |        "name": "Level1",
        |        "type": "record",
        |        "fields": [
        |          {
        |            "name": "level2",
        |            "type": {
        |              "name": "Level2",
        |              "type": "record",
        |              "fields": [
        |                {
        |                  "name": "name",
        |                  "type": "string"
        |                }
        |              ]
        |            }
        |          }
        |        ]
        |      }
        |    }
        |  ]
        |}""".stripMargin
      val gen = new Generator(Standard)
      val List(source0, source1, source2) = gen.stringToStrings(schemaString)
      source0 ===
        """package example
          |
          |case class Level0(level1: Level1)
        """.stripMargin.trim

      source1 ===
        """package example
          |
          |case class Level1(level2: Level2)
        """.stripMargin.trim

      source2 ===
        """package example
          |
          |case class Level2(name: String)
        """.stripMargin.trim
    }
    


    "correctly generate a simple case class definition from protocol as a string" in {
      val schemaString = 
        """
          |{"namespace": "test.proto",
          | "protocol": "Warning",
          |
          | "types": [
          |     {"name": "Message", "type": "record",
          |      "fields": [
          |          {"name": "alert",   "type": "string"}
          |      ]
          |     }
          | ],
          |
          | "messages": {
          |     "send": {
          |         "request": [{"name": "message", "type": "Message"}],
          |         "response": "string"
          |     }
          | }
          |}
        """.stripMargin
      val gen = new Generator(Standard)
      val List(source) = gen.stringToStrings(schemaString)
      source ===
      """
        |package test.proto
        |
        |case class Message(alert: String)
      """.stripMargin.trim
    }

    "correctly generate a simple case class definition from idl as a string" in {
      val schemaString = 
        """
          |@namespace("example.idl")
          |
          |protocol StringProtocol {
          |
          |  record Age {
          |    int yearsOld = 0;
          |  }
          |}
        """.stripMargin
      val gen = new Generator(Standard)
      val List(source) = gen.stringToStrings(schemaString)
      source ===
        """package example.idl
          |
          |case class Age(yearsOld: Int)
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


  "correctly generate a recursive case class from IDL" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/recursive.avdl")
    val gen = new Generator(Standard)
    gen.fileToFile(infile)

    val source = scala.io.Source.fromFile("target/generated-sources/example/idl/Recursive.scala").mkString
    source ====
      """
        |/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
        |package example.idl
        |
        |case class Recursive(name: String, recursive: Option[Recursive])
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
        |case class Card(suit: Suit.Value, number: Int)
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
        |case class Card(suit: Suit.Value, number: Int)
      """.stripMargin.trim
  }

  "correctly generate records depending on others defined in a different AVDL file" in {
    val importing = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
    val gen = new Generator(Standard)
    gen.fileToFile(importing)

    val sourceEnum = scala.io.Source.fromFile("target/generated-sources/example/idl/DependentRecord.scala").mkString
    sourceEnum ====
      """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
        |package example.idl
        |
        |import other.ns.ExternalDependency
        |
        |case class DependentRecord(dependency: ExternalDependency, number: Int)
        |""".stripMargin.trim
  }

  "correctly generate records depending on others defined in a different AVDL file and in a nested field" in {
    val importing = new java.io.File("avrohugger-core/src/test/avro/import-nested.avdl")
    val gen = new Generator(Standard)
    gen.fileToFile(importing)

    val sourceEnum = scala.io.Source.fromFile("target/generated-sources/example/idl/DependentRecord.scala").mkString
    sourceEnum ====
      """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
        |package example.idl
        |
        |import other.ns.ExternalDependency
        |
        |case class DependentRecord(dependency: Option[ExternalDependency], number: Int)
        |""".stripMargin.trim
  }

  "not generate copy of imported classes in the importing package" in {
    val importing = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
    val gen = new Generator(Standard)
    gen.fileToFile(importing)

    (new File(s"target/generated-sources/example/idl/ExternalDependency.scala")).exists === false
  }

  "Generate imported classes in the declared package" in {
    val importing = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
    val gen = new Generator(Standard)
    gen.fileToFile(importing)

    (new File(s"target/generated-sources/other/ns/ExternalDependency.scala")).exists === true
  }

  
}
