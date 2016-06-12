package avrohugger
package test
package standard

import java.io.File

import avrohugger.Generator
import avrohugger.format.Standard
import avrohugger.StringGenerator

import org.specs2._

class StandardStringToStringsSpec extends mutable.Specification {

  "Standard Generator stringToStrings method" should {
    
    // tests common to fileToX and stringToX
    "1. correctly generate from a protocol with messages" in {
      val inputString = util.Util.readFile("avrohugger-core/src/test/avro/mail.avpr")
      val gen = new Generator(Standard)
      val List(source) = gen.stringToStrings(inputString)
      val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/proto/Message.scala")
      source === expected
    }

    "2. correctly generate a simple case class definition in a package" in {
      val inputString = util.Util.readFile("avrohugger-core/src/test/avro/user.avsc")
      val gen = new Generator(Standard)
      val List(source) = gen.stringToStrings(inputString)
      val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/User.scala")
      source === expected
    }
    
    "3. correctly generate a simple case class definition in the default package" in {
      val inputString = util.Util.readFile("avrohugger-core/src/test/avro/AvroTypeProviderTestNoNamespace.avsc")
      val gen = new Generator(Standard)
      val List(source) = gen.stringToStrings(inputString)
      val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/AvroTypeProviderTestNoNamespace.scala")
      source === expected
    }
    
    "4. correctly generate a nested case class definition from a schema" in {
      val inputString = util.Util.readFile("avrohugger-core/src/test/avro/nested.avsc")
      val gen = new Generator(Standard)
      val List(source2, source1, source0) = gen.stringToStrings(inputString)

      val expected0 = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Level0.scala")
      val expected1 = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Level1.scala")
      val expected2 = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Level2.scala")
      
      source0 === expected0
      source1 === expected1
      source2 === expected2
    }
    
    "5. correctly generate a nested case class from IDL" in {
      val inputString = util.Util.readFile("avrohugger-core/src/test/avro/nested.avdl")
      val gen = new Generator(Standard)
      val List(source) = gen.stringToStrings(inputString)
      val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/NestedProtocol.scala")
      source === expected      
    }
    
    "6. correctly generate a recursive case class from IDL" in {
      val inputString = util.Util.readFile("avrohugger-core/src/test/avro/recursive.avdl")
      val gen = new Generator(Standard)
      val List(source) = gen.stringToStrings(inputString)
      val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/Recursive.scala")
      source === expected
    }

    "7. correctly generate enums from schema" in {
      val inputString = util.Util.readFile("avrohugger-core/src/test/avro/enums.avsc")
      val gen = new Generator(Standard)
      val List(source) = gen.stringToStrings(inputString)
      val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Suit.scala")
      source === expected
    }
    
    "8. correctly generate enums from protocol" in {
      val inputString = util.Util.readFile("avrohugger-core/src/test/avro/enums.avpr")
      val gen = new Generator(Standard)
      val List(source) = gen.stringToStrings(inputString)
      val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/proto/EnumProtocol.scala")
      source === expected
    }
    
    "9. correctly generate enums from IDL" in {
      val inputString = util.Util.readFile("avrohugger-core/src/test/avro/enums.avdl")
      val gen = new Generator(Standard)
      val List(source) = gen.stringToStrings(inputString)
      val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/EnumProtocol.scala")
      source === expected
    }
    
    "10. correctly generate nested enums" in {
      val inputString = util.Util.readFile("avrohugger-core/src/test/avro/enums_nested.avsc")
      val gen = new Generator(Standard)
      val List(sourceEnum, sourceRecord) = gen.stringToStrings(inputString)

      val expectedEnum = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Direction.scala")
      val expectedRecord = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Compass.scala")

      sourceEnum === expectedEnum
      sourceRecord === expectedRecord
    }
    
    "11. correctly generate bytes from schema" in {
      val inputString = util.Util.readFile("avrohugger-core/src/test/avro/bytes.avsc")
      val gen = new Generator(Standard)
      val List(source) = gen.stringToStrings(inputString)
      val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/BinarySc.scala")
      source === expected
    }
    
    "12. correctly generate bytes from protocol" in {
      val inputString = util.Util.readFile("avrohugger-core/src/test/avro/bytes.avpr")
      val gen = new Generator(Standard)
      val List(source) = gen.stringToStrings(inputString)
      val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/proto/BinaryPr.scala")
      source === expected
    }
    
    "13. correctly generate bytes from IDL" in {
      val inputString = util.Util.readFile("avrohugger-core/src/test/avro/bytes.avdl")
      val gen = new Generator(Standard)
      val List(source) = gen.stringToStrings(inputString)
      val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/BinaryIdl.scala")
      source === expected
    }
    
    "14. correctly generate records depending on others defined in a different- and same-namespaced AVDL and AVSC" in {
      val inputString = util.Util.readFile("avrohugger-core/src/test/avro/import.avdl")
      val gen = new Generator(Standard)
      gen.stringToStrings(inputString) must throwA(new java.lang.RuntimeException("Imports not supported in String IDLs, only avdl files."))
    }
    
    "15. correctly generate an empty case class definition" in {
      val inputString = util.Util.readFile("avrohugger-core/src/test/avro/AvroTypeProviderTestEmptyRecord.avdl")
      val gen = new Generator(Standard)
      val List(source) = gen.stringToStrings(inputString)
      val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/test/Calculator.scala")
      source === expected
    }
    
    "16. correctly generate default values" in {
      val inputString = util.Util.readFile("avrohugger-core/src/test/avro/defaults.avdl")
      val gen = new Generator(Standard)
      val List(source) = gen.stringToStrings(inputString)

      val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/Defaults.scala")
      source === expected
    }
  }

}
