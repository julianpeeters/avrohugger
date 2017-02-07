package avrohugger

import java.io.File

import avrohugger._
import avrohugger.format.Scavro
import org.specs2._

class ScavroCustomEnumSpec extends mutable.Specification {

  "a Generator" should {

    "correctly generate strings with java enums when asked for" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
      val gen = new Generator(
        Scavro,
        Map.empty,
        Map(
          ("example.idl" -> "example.idl.java.model"),
          ("other.ns" -> "other.ns.java")),
        Map(("enum"-> "java enum")))
      val List(dep3, dep2, dep1, dep1a, adt) = gen.fileToStrings(infile)

      val expectedADT = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/java/model/ImportProtocol.scala")
      val expectedDep1a = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/java/model/DefaultEnum.java")
      val expectedDep1 = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/java/model/Defaults.scala")
      val expectedDep2 = util.Util.readFile("avrohugger-core/src/test/expected/scavro/other/ns/java/ExternalDependency.scala")
      val expectedDep3 = util.Util.readFile("avrohugger-core/src/test/expected/scavro/other/ns/java/Suit.java")

      adt === expectedADT
      dep1a === expectedDep1a
      dep1 === expectedDep1
      dep2 === expectedDep2
      dep3 === expectedDep3

    }

    "correctly generate files with java enums when asked for" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
      val gen = new Generator(
        Scavro,
        Map.empty,
        Map(
          ("example.idl" -> "example.idl.java.model"),
          ("other.ns" -> "other.ns.java")),
        Map(("enum"-> "java enum")))
      val outDir = gen.defaultOutputDir + "/scavro/"
      gen.fileToFile(infile, outDir)

      val adt = util.Util.readFile("target/generated-sources/scavro/example/idl/java/model/ImportProtocol.scala")
      val dep1a = util.Util.readFile("target/generated-sources/scavro/example/idl/java/model/DefaultEnum.java")
      val dep1 = util.Util.readFile("target/generated-sources/scavro/example/idl/java/model/Defaults.scala")
      val dep2 = util.Util.readFile("target/generated-sources/scavro/other/ns/java/ExternalDependency.scala")
      val dep3 = util.Util.readFile("target/generated-sources/scavro/other/ns/java/Suit.java")

      val expectedADT = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/java/model/ImportProtocol.scala")
      val expectedDep1a = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/java/model/DefaultEnum.java")
      val expectedDep1 = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/java/model/Defaults.scala")
      val expectedDep2 = util.Util.readFile("avrohugger-core/src/test/expected/scavro/other/ns/java/ExternalDependency.scala")
      val expectedDep3 = util.Util.readFile("avrohugger-core/src/test/expected/scavro/other/ns/java/Suit.java")

      adt === expectedADT
      dep1a === expectedDep1a
      dep1 === expectedDep1
      dep2 === expectedDep2
      dep3 === expectedDep3
    }

    "correctly generate strings with case object enums when asked for" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
      val gen = new Generator(
        Scavro,
        Map.empty,
        Map(
          ("example.idl" -> "example.idl.case.model"),
          ("other.ns" -> "other.ns.case")),
        Map(("enum"-> "case object")))
      val List(dep3, dep2, dep1, adt) = gen.fileToStrings(infile)

      val expectedADT = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/case/model/ImportProtocol.scala")
      val expectedDep1 = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/case/model/Defaults.scala")
      val expectedDep2 = util.Util.readFile("avrohugger-core/src/test/expected/scavro/other/ns/case/ExternalDependency.scala")
      val expectedDep3 = util.Util.readFile("avrohugger-core/src/test/expected/scavro/other/ns/case/Suit.scala")

      adt === expectedADT
      dep1 === expectedDep1.dropRight(1) // why is a newline being added onto the expected source?
      dep2 === expectedDep2
      dep3 === expectedDep3

    }

    "correctly generate files with case object enums when asked for" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
      val gen = new Generator(
        Scavro,
        Map.empty,
        Map(
          ("example.idl" -> "example.idl.case.model"),
          ("other.ns" -> "other.ns.case")),
        Map(("enum"-> "case object")))
        val outDir = gen.defaultOutputDir + "/scavro/"
      gen.fileToFile(infile, outDir)

      val adt = util.Util.readFile("target/generated-sources/scavro/example/idl/case/model/ImportProtocol.scala")
      val dep1 = util.Util.readFile("target/generated-sources/scavro/example/idl/case/model/Defaults.scala")
      val dep2 = util.Util.readFile("target/generated-sources/scavro/other/ns/case/ExternalDependency.scala")
      val dep3 = util.Util.readFile("target/generated-sources/scavro/other/ns/case/Suit.scala")

      val expectedADT = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/case/model/ImportProtocol.scala")
      val expectedDep1 = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/case/model/Defaults.scala")
      val expectedDep2 = util.Util.readFile("avrohugger-core/src/test/expected/scavro/other/ns/case/ExternalDependency.scala")
      val expectedDep3 = util.Util.readFile("avrohugger-core/src/test/expected/scavro/other/ns/case/Suit.scala")

      adt === expectedADT
      dep1 === expectedDep1.dropRight(1) // why is a newline being added onto the expected source?
      dep2 === expectedDep2
      dep3 === expectedDep3
    }

  }
}
