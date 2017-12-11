package avrohugger

import java.io.File

import avrohugger._
import avrohugger.format.Standard
import avrohugger.types._
import org.specs2._

class StandardCustomEnumSpec extends Specification {
  
  def is = s2"""
    Standard Generator should
      correctly generate strings with java enums when asked for $e1
      correctly generate files with java enums when asked for $e2
      correctly generate strings with case object enums when asked for $e3
      correctly generate files with case object enums when asked for $e4
  """
  
  def e1 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
    val gen = new Generator(
      Standard,
      Some(Standard.defaultTypes.copy(enum = JavaEnum)),
      Map(
        ("example.idl" -> "example.idl.java"),
        ("other.ns" -> "other.ns.java")))
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
  
  def e2 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
    val gen = new Generator(
      Standard,
      Some(Standard.defaultTypes.copy(enum = JavaEnum)),
      Map(
        ("example.idl" -> "example.idl.java"),
        ("other.ns" -> "other.ns.java")))
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
  
  def e3 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
    val gen = new Generator(
      Standard,
      Some(Standard.defaultTypes.copy(enum = ScalaCaseObjectEnum)),
      Map(
        ("example.idl" -> "example.idl.case"),
        ("other.ns" -> "other.ns.case")))
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
  
  def e4 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
    val gen = new Generator(
      Standard,
      Some(Standard.defaultTypes.copy(enum = ScalaCaseObjectEnum)),
      Map(
        ("example.idl" -> "example.idl.case"),
        ("other.ns" -> "other.ns.case")))
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
