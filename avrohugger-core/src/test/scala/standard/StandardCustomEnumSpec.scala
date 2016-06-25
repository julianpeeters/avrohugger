package avrohugger

import java.io.File

import avrohugger._
import avrohugger.format.Standard
import org.specs2._

class StandardCustomEnumSpec extends mutable.Specification {

  "a Generator" should {

    "correctly generate strings with java enums when asked for" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
      val gen = new Generator(
        Standard,
        Map.empty,
        Map(
          ("example.idl" -> "example.idl.java"),
          ("other.ns" -> "other.ns.java")),
        Map(("enum"-> "java enum")))
      val List(dep3, dep2, dep1, dep1a, adt) = gen.fileToStrings(infile)
  
      val expectedADT = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/java/ImportProtocol.scala")
      val expectedDep1a = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/java/DefaultEnum.java")
      val expectedDep1 = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/java/Defaults.scala")
      val expectedDep2 = util.Util.readFile("avrohugger-core/src/test/expected/standard/other/ns/java/ExternalDependency.scala")
      val expectedDep3 = util.Util.readFile("avrohugger-core/src/test/expected/standard/other/ns/java/Suit.java")
  
      adt === expectedADT
      dep1a === expectedDep1a
      dep1 === expectedDep1
      dep2 === expectedDep2
      dep3 === expectedDep3
      
    }
    
    "correctly generate files with java enums when asked for" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
      val gen = new Generator(
        Standard,
        Map.empty,
        Map(
          ("example.idl" -> "example.idl.java"),
          ("other.ns" -> "other.ns.java")),
        Map(("enum"-> "java enum")))
        val outDir = gen.defaultOutputDir + "/standard/"
      gen.fileToFile(infile, outDir)

      val adt = util.Util.readFile("target/generated-sources/standard/example/idl/java/ImportProtocol.scala")
      val dep1a = util.Util.readFile("target/generated-sources/standard/example/idl/java/DefaultEnum.java")
      val dep1 = util.Util.readFile("target/generated-sources/standard/example/idl/java/Defaults.scala")
      val dep2 = util.Util.readFile("target/generated-sources/standard/other/ns/java/ExternalDependency.scala")
      val dep3 = util.Util.readFile("target/generated-sources/standard/other/ns/java/Suit.java")
        
      val expectedADT = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/java/ImportProtocol.scala")
      val expectedDep1a = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/java/DefaultEnum.java")
      val expectedDep1 = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/java/Defaults.scala")
      val expectedDep2 = util.Util.readFile("avrohugger-core/src/test/expected/standard/other/ns/java/ExternalDependency.scala")
      val expectedDep3 = util.Util.readFile("avrohugger-core/src/test/expected/standard/other/ns/java/Suit.java")
  
      adt === expectedADT
      dep1a === expectedDep1a
      dep1 === expectedDep1
      dep2 === expectedDep2
      dep3 === expectedDep3      
    }

    
    "correctly generate strings with case object enums when asked for" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
      val gen = new Generator(
        Standard,
        Map.empty,
        Map(
          ("example.idl" -> "example.idl.case"),
          ("other.ns" -> "other.ns.case")),
        Map(("enum"-> "case object")))
      val List(dep3, dep2, dep1, adt) = gen.fileToStrings(infile)
  
      val expectedADT = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/case/ImportProtocol.scala")
      val expectedDep1 = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/case/Defaults.scala")
      val expectedDep2 = util.Util.readFile("avrohugger-core/src/test/expected/standard/other/ns/case/ExternalDependency.scala")
      val expectedDep3 = util.Util.readFile("avrohugger-core/src/test/expected/standard/other/ns/case/Suit.scala")
  
      adt === expectedADT
      dep1 === expectedDep1
      dep2 === expectedDep2
      dep3 === expectedDep3
      
    }
  
    "correctly generate files with case object enums when asked for" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
      val gen = new Generator(
        Standard,
        Map.empty,
        Map(
          ("example.idl" -> "example.idl.case"),
          ("other.ns" -> "other.ns.case")),
        Map(("enum"-> "case object")))
        val outDir = gen.defaultOutputDir + "/standard/"
      gen.fileToFile(infile, outDir)

      val adt = util.Util.readFile("target/generated-sources/standard/example/idl/case/ImportProtocol.scala")
      val dep1 = util.Util.readFile("target/generated-sources/standard/example/idl/case/Defaults.scala")
      val dep2 = util.Util.readFile("target/generated-sources/standard/other/ns/case/ExternalDependency.scala")
      val dep3 = util.Util.readFile("target/generated-sources/standard/other/ns/case/Suit.scala")
        
      val expectedADT = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/case/ImportProtocol.scala")
      val expectedDep1 = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/case/Defaults.scala")
      val expectedDep2 = util.Util.readFile("avrohugger-core/src/test/expected/standard/other/ns/case/ExternalDependency.scala")
      val expectedDep3 = util.Util.readFile("avrohugger-core/src/test/expected/standard/other/ns/case/Suit.scala")
  
      adt === expectedADT
      dep1 === expectedDep1
      dep2 === expectedDep2
      dep3 === expectedDep3      
    }

  }
}
