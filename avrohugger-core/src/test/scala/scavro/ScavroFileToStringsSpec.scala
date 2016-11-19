package avrohugger
package test
package scavro

import java.io.File

import avrohugger.Generator
import avrohugger.format.Scavro
import avrohugger.StringGenerator
import org.specs2._

class ScavroFileToStringsSpec extends mutable.Specification {

  "Scavro Generator fileToStrings method" should {
    
    // tests specific to fileToX
    "A. correctly generate a simple case class definition from AVRO" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/twitter.avro")
      val gen = new Generator(Scavro)
      val List(source) = gen.fileToStrings(infile)
      val expected = util.Util.readFile("avrohugger-core/src/test/expected/scavro/com/miguno/avro/model/twitter_schema.scala")
      source === expected      
    }

    // tests common to fileToX and stringToX
    "1. correctly generate from a protocol with messages" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/mail.avpr")
      val gen = new Generator(Scavro)
      val List(source) = gen.fileToStrings(infile)
      val expected = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/proto/model/Message.scala")
      source === expected
    }

    "2. correctly generate a simple case class definition in a package" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/user.avsc")
      val gen = new Generator(Scavro)
      val List(source) = gen.fileToStrings(infile)
      val expected = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/model/User.scala")
      source === expected
    }
    
    "3. correctly generate a simple case class definition in the default package" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/AvroTypeProviderTestNoNamespace.avsc")
      val gen = new Generator(Scavro)
      gen.fileToStrings(infile) must throwA(new java.lang.RuntimeException("Scavro requires a namespace because Java classes cannot be imported from the default package"))
    }
    
    "4. correctly generate a nested case class definition from a schema" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/nested.avsc")
      val gen = new Generator(Scavro)
      val List(source2, source1, source0) = gen.fileToStrings(infile)

      val expected0 = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/model/Level0.scala")
      val expected1 = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/model/Level1.scala")
      val expected2 = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/model/Level2.scala")
      
      source0 === expected0
      source1 === expected1
      source2 === expected2
    }
    
    "5. correctly generate a nested case class from IDL" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/nested.avdl")
      val gen = new Generator(Scavro)
      val List(source) = gen.fileToStrings(infile)
      val expected = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/model/NestedProtocol.scala")
      source === expected      
    }
    
    "6. correctly generate a recursive case class from IDL" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/recursive.avdl")
      val gen = new Generator(Scavro)
      val List(source) = gen.fileToStrings(infile)
      val expected = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/model/Recursive.scala")
      source === expected
    }

    "7. correctly generate enums from schema" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/enums.avsc")
      val gen = new Generator(Scavro)
      val List(source) = gen.fileToStrings(infile)
      val expected = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/model/Suit.scala")
      source === expected
    }
    
    "8. correctly generate enums from protocol" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/enums.avpr")
      val gen = new Generator(Scavro)
      val List(source) = gen.fileToStrings(infile)
      val expected = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/proto/model/EnumProtocol.scala")
      source === expected
    }
    
    "9. correctly generate enums from IDL" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/enums.avdl")
      val gen = new Generator(Scavro)
      val List(source) = gen.fileToStrings(infile)
      val expected = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/model/EnumProtocol.scala")
      source === expected
    }
    
    "10. correctly generate nested enums" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/enums_nested.avsc")
      val gen = new Generator(Scavro)
      val List(sourceEnum, sourceRecord) = gen.fileToStrings(infile)

      val expectedEnum = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/model/Direction.scala")
      val expectedRecord = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/model/Compass.scala")

      sourceEnum === expectedEnum
      sourceRecord === expectedRecord
    }
    
    "11. correctly generate bytes from schema" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/bytes.avsc")
      val gen = new Generator(Scavro)
      val List(source) = gen.fileToStrings(infile)
      val expected = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/model/BinarySc.scala")
      source === expected
    }
    
    "12. correctly generate bytes from protocol" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/bytes.avpr")
      val gen = new Generator(Scavro)
      val List(source) = gen.fileToStrings(infile)
      val expected = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/proto/model/BinaryPr.scala")
      source === expected
    }
    
    "13. correctly generate bytes from IDL" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/bytes.avdl")
      val gen = new Generator(Scavro)
      val List(source) = gen.fileToStrings(infile)
      val expected = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/model/BinaryIdl.scala")
      source === expected
    }
    
    "14. correctly generate records depending on others defined in a different- and same-namespaced AVDL and AVSC" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
      val gen = new Generator(Scavro)
      val List(dep3, dep2, dep1, adt) = gen.fileToStrings(infile)

      val expectedADT = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/model/ImportProtocol.scala")
      val expectedDep1 = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/model/Defaults.scala")
      val expectedDep2 = util.Util.readFile("avrohugger-core/src/test/expected/scavro/other/ns/model/ExternalDependency.scala")
      val expectedDep3 = util.Util.readFile("avrohugger-core/src/test/expected/scavro/other/ns/model/Suit.scala")
      
      adt === expectedADT
      dep1 === expectedDep1
      dep2 === expectedDep2
      dep3 === expectedDep3
    }
    
    "15. correctly generate an empty case class definition" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/AvroTypeProviderTestEmptyRecord.avdl")
      val gen = new Generator(Scavro)
      val List(source) = gen.fileToStrings(infile)
      val expected = util.Util.readFile("avrohugger-core/src/test/expected/scavro/test/model/Calculator.scala")
      source === expected
    }
    
    "16. correctly generate default values" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/defaults.avdl")
      val gen = new Generator(Scavro)
      val List(source) = gen.fileToStrings(infile)

      val expected = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/model/Defaults.scala")
      source === expected
    }

  }

}
