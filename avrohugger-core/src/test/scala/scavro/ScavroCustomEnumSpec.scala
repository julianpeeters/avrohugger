package avrohugger

import avrohugger.format.Scavro
import avrohugger.types._
import org.specs2._

class ScavroCustomEnumSpec extends Specification {

  def is = s2"""
    A Scavro Generator should
    correctly generate strings with java enums when asked for $e1
    correctly generate files with java enums when asked for $e2
    correctly generate strings with case object enums when asked for $e3
    correctly generate files with case object enums when asked for $e4
    correctly generate strings with enums as scala strings when asked for $e5
    correctly generate files with enums as scala strings when asked for $e6
  """
  
  def e1 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
    val gen = new Generator(
      Scavro,
      Some(Scavro.defaultTypes.copy(enum = JavaEnum)),
      Map(
        ("example.idl" -> "example.idl.java.model"),
        ("other.ns" -> "other.ns.java")))
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
  
  def e2 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
    val gen = new Generator(
      Scavro,
      Some(Scavro.defaultTypes.copy(enum = JavaEnum)),
      Map(
        ("example.idl" -> "example.idl.java.model"),
        ("other.ns" -> "other.ns.java")))
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

  def e3 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
    val gen = new Generator(
      Scavro,
      Some(Scavro.defaultTypes.copy(enum = ScalaCaseObjectEnum)),
      Map(
        ("example.idl" -> "example.idl.case.model"),
        ("other.ns" -> "other.ns.case")))
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

  def e4 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
    val gen = new Generator(
      Scavro,
      Some(Scavro.defaultTypes.copy(enum = ScalaCaseObjectEnum)),
      Map(
        ("example.idl" -> "example.idl.case.model"),
        ("other.ns" -> "other.ns.case")))
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
  
  def e5 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
    val gen = new Generator(
      Scavro,
      Some(Scavro.defaultTypes.copy(enum = EnumAsScalaString, protocol = ScalaADT)),
      Map(
        ("example.idl" -> "example.idl.string.model"),
        ("other.ns" -> "other.ns.string")))
    val List(dep2, dep1, adt) = gen.fileToStrings(infile)
  
    val expectedADT = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/string/model/ImportProtocol.scala")
    val expectedDep1 = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/string/model/Defaults.scala")
    val expectedDep2 = util.Util.readFile("avrohugger-core/src/test/expected/scavro/other/ns/string/ExternalDependency.scala")
    
    adt === expectedADT
    dep1 === expectedDep1
    dep2 === expectedDep2
    
  }
  
  def e6 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
    val gen = new Generator(
      Scavro,
      Some(Scavro.defaultTypes.copy(enum = EnumAsScalaString, protocol = ScalaADT)),
      Map(
        ("example.idl" -> "example.idl.string.model"),
        ("other.ns" -> "other.ns.string")))
      val outDir = gen.defaultOutputDir + "/scavro/"
    gen.fileToFile(infile, outDir)
  
    val adt = util.Util.readFile("target/generated-sources/scavro/example/idl/string/model/ImportProtocol.scala")
    val dep1 = util.Util.readFile("target/generated-sources/scavro/example/idl/string/model/Defaults.scala")
    val dep2 = util.Util.readFile("target/generated-sources/scavro/other/ns/string/ExternalDependency.scala")
      
    val expectedADT = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/string/model/ImportProtocol.scala")
    val expectedDep1 = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/string/model/Defaults.scala")
    val expectedDep2 = util.Util.readFile("avrohugger-core/src/test/expected/scavro/other/ns/string/ExternalDependency.scala")
  
    adt === expectedADT
    dep1 === expectedDep1
    dep2 === expectedDep2
  }
  
}
