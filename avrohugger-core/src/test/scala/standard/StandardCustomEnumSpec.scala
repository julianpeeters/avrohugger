package avrohugger

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
    correctly generate strings with enums as scala strings when asked for $e5
    correctly generate files with enums as scala strings when asked for $e6
    correctly generate strings with scala 3 enums when asked for $e7
    correctly generate files with Scala 3 enums when asked for $e8
  """
  
  def e1 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
    val gen = Generator(
      Standard,
      Some(Standard.defaultTypes.copy(`enum` = JavaEnum, protocol = ScalaADT)),
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
    val gen = Generator(
      Standard,
      Some(Standard.defaultTypes.copy(`enum` = JavaEnum, protocol = ScalaADT)),
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
    val gen = Generator(
      Standard,
      Some(Standard.defaultTypes.copy(`enum` = ScalaCaseObjectEnum, protocol = ScalaADT)),
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
    val gen = Generator(
      Standard,
      Some(Standard.defaultTypes.copy(`enum` = ScalaCaseObjectEnum, protocol = ScalaADT)),
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
  
    adt  === expectedADT
    dep1 === expectedDep1
    dep2 === expectedDep2
    dep3 === expectedDep3
  }
  
  
  def e5 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
    val gen = Generator(
      Standard,
      Some(Standard.defaultTypes.copy(`enum` = EnumAsScalaString, protocol = ScalaADT)),
      Map(
        ("example.idl" -> "example.idl.string"),
        ("other.ns" -> "other.ns.string")))
    val List(dep2, dep1, adt) = gen.fileToStrings(infile)
  
    val expectedADT = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/string/ImportProtocol.scala")
    val expectedDep1 = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/string/Defaults.scala")
    val expectedDep2 = util.Util.readFile("avrohugger-core/src/test/expected/standard/other/ns/string/ExternalDependency.scala")
    
    adt === expectedADT
    dep1 === expectedDep1
    dep2 === expectedDep2
    
  }
  
  def e6 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
    val gen = Generator(
      Standard,
      Some(Standard.defaultTypes.copy(`enum` = EnumAsScalaString, protocol = ScalaADT)),
      Map(
        ("example.idl" -> "example.idl.string"),
        ("other.ns" -> "other.ns.string")))
      val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)
  
    val adt = util.Util.readFile("target/generated-sources/standard/example/idl/string/ImportProtocol.scala")
    val dep1 = util.Util.readFile("target/generated-sources/standard/example/idl/string/Defaults.scala")
    val dep2 = util.Util.readFile("target/generated-sources/standard/other/ns/string/ExternalDependency.scala")
      
    val expectedADT = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/string/ImportProtocol.scala")
    val expectedDep1 = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/string/Defaults.scala")
    val expectedDep2 = util.Util.readFile("avrohugger-core/src/test/expected/standard/other/ns/string/ExternalDependency.scala")
  
    adt === expectedADT
    dep1 === expectedDep1
    dep2 === expectedDep2
  }

  def e7 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
    val gen = Generator(
      Standard,
      Some(Standard.defaultTypes.copy(`enum` = Scala3Enum, protocol = ScalaADT)),
      Map(
        ("example.idl" -> "example.idl.scala3enum"),
        ("other.ns" -> "other.ns.scala3enum")))
    val List(dep3, dep2, dep1, adt) = gen.fileToStrings(infile)
    val expectedADT = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/scala3enum/ImportProtocol.scala")
    val expectedDep1 = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/scala3enum/Defaults.scala")
    val expectedDep2 = util.Util.readFile("avrohugger-core/src/test/expected/standard/other/ns/scala3enum/ExternalDependency.scala")
    val expectedDep3 = util.Util.readFile("avrohugger-core/src/test/expected/standard/other/ns/scala3enum/Suit.scala")
    adt === expectedADT
    dep1 === expectedDep1
    dep2 === expectedDep2
    dep3 === expectedDep3
  }

  def e8 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
    val gen = Generator(
      Standard,
      Some(Standard.defaultTypes.copy(`enum` = Scala3Enum, protocol = ScalaADT)),
      Map(
        ("example.idl" -> "example.idl.scala3enum"),
        ("other.ns" -> "other.ns.scala3enum")))
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val adt = util.Util.readFile("target/generated-sources/standard/example/idl/scala3enum/ImportProtocol.scala")
    val dep1 = util.Util.readFile("target/generated-sources/standard/example/idl/scala3enum/Defaults.scala")
    val dep2 = util.Util.readFile("target/generated-sources/standard/other/ns/scala3enum/ExternalDependency.scala")
    val dep3 = util.Util.readFile("target/generated-sources/standard/other/ns/scala3enum/Suit.scala")

    val expectedADT = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/scala3enum/ImportProtocol.scala")
    val expectedDep1 = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/scala3enum/Defaults.scala")
    val expectedDep2 = util.Util.readFile("avrohugger-core/src/test/expected/standard/other/ns/scala3enum/ExternalDependency.scala")
    val expectedDep3 = util.Util.readFile("avrohugger-core/src/test/expected/standard/other/ns/scala3enum/Suit.scala")
    adt === expectedADT
    dep1 === expectedDep1
    dep2 === expectedDep2
    dep3 === expectedDep3
  }

}
