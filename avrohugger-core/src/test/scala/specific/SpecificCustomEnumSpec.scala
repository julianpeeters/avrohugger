package avrohugger

import java.io.File

import avrohugger._
import avrohugger.format.SpecificRecord
import avrohugger.types._
import org.specs2._

class SpecificCustomEnumSpec extends Specification {
  
  def is = s2"""
    Specific Generator should
    correctly generate strings with java enums when asked for $e1
    correctly generate files with java enums when asked for $e2
    correctly generate strings with enums as scala strings when asked for $e5
    correctly generate files with enums as scala strings when asked for $e6
  """
  
  def e1 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
    val gen = new Generator(
      SpecificRecord,
      Some(SpecificRecord.defaultTypes.copy(enum = JavaEnum, protocol = ScalaADT)),
      Map(
        ("example.idl" -> "example.idl.java"),
        ("other.ns" -> "other.ns.java")))
    val List(dep3, dep2, dep1, dep1a, adt) = gen.fileToStrings(infile)
  
    val expectedADT = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/idl/java/ImportProtocol.scala")
    val expectedDep1a = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/idl/java/DefaultEnum.java")
    val expectedDep1 = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/idl/java/Defaults.scala")
    val expectedDep2 = util.Util.readFile("avrohugger-core/src/test/expected/specific/other/ns/java/ExternalDependency.scala")
    val expectedDep3 = util.Util.readFile("avrohugger-core/src/test/expected/specific/other/ns/java/Suit.java")
  
    adt === expectedADT
    dep1a === expectedDep1a
    dep1 === expectedDep1
    dep2 === expectedDep2
    dep3 === expectedDep3
    
  }
  
  def e2 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
    val gen = new Generator(
      SpecificRecord,
      Some(SpecificRecord.defaultTypes.copy(enum = JavaEnum, protocol = ScalaADT)),
      Map(
        ("example.idl" -> "example.idl.java"),
        ("other.ns" -> "other.ns.java")))
      val outDir = gen.defaultOutputDir + "/specific/"
    gen.fileToFile(infile, outDir)
  
    val adt = util.Util.readFile("target/generated-sources/specific/example/idl/java/ImportProtocol.scala")
    val dep1a = util.Util.readFile("target/generated-sources/specific/example/idl/java/DefaultEnum.java")
    val dep1 = util.Util.readFile("target/generated-sources/specific/example/idl/java/Defaults.scala")
    val dep2 = util.Util.readFile("target/generated-sources/specific/other/ns/java/ExternalDependency.scala")
    val dep3 = util.Util.readFile("target/generated-sources/specific/other/ns/java/Suit.java")
      
    val expectedADT = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/idl/java/ImportProtocol.scala")
    val expectedDep1a = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/idl/java/DefaultEnum.java")
    val expectedDep1 = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/idl/java/Defaults.scala")
    val expectedDep2 = util.Util.readFile("avrohugger-core/src/test/expected/specific/other/ns/java/ExternalDependency.scala")
    val expectedDep3 = util.Util.readFile("avrohugger-core/src/test/expected/specific/other/ns/java/Suit.java")
  
    adt === expectedADT
    dep1a === expectedDep1a
    dep1 === expectedDep1
    dep2 === expectedDep2
    dep3 === expectedDep3      
  }
  
  
  
  def e5 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
    val gen = new Generator(
      SpecificRecord,
      Some(SpecificRecord.defaultTypes.copy(enum = EnumAsScalaString, protocol = ScalaADT)),
      Map(
        ("example.idl" -> "example.idl.string"),
        ("other.ns" -> "other.ns.string")))
    val List(dep2, dep1, adt) = gen.fileToStrings(infile)
  
    val expectedADT = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/idl/string/ImportProtocol.scala")
    val expectedDep1 = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/idl/string/Defaults.scala")
    val expectedDep2 = util.Util.readFile("avrohugger-core/src/test/expected/specific/other/ns/string/ExternalDependency.scala")
    
    adt === expectedADT
    dep1 === expectedDep1
    dep2 === expectedDep2
    
  }
  
  def e6 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
    val gen = new Generator(
      SpecificRecord,
      Some(SpecificRecord.defaultTypes.copy(enum = EnumAsScalaString, protocol = ScalaADT)),
      Map(
        ("example.idl" -> "example.idl.string"),
        ("other.ns" -> "other.ns.string")))
      val outDir = gen.defaultOutputDir + "/specific/"
    gen.fileToFile(infile, outDir)
  
    val adt = util.Util.readFile("target/generated-sources/specific/example/idl/string/ImportProtocol.scala")
    val dep1 = util.Util.readFile("target/generated-sources/specific/example/idl/string/Defaults.scala")
    val dep2 = util.Util.readFile("target/generated-sources/specific/other/ns/string/ExternalDependency.scala")
      
    val expectedADT = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/idl/string/ImportProtocol.scala")
    val expectedDep1 = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/idl/string/Defaults.scala")
    val expectedDep2 = util.Util.readFile("avrohugger-core/src/test/expected/specific/other/ns/string/ExternalDependency.scala")
  
    adt === expectedADT
    dep1 === expectedDep1
    dep2 === expectedDep2
  }

}
