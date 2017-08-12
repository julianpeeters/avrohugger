package avrohugger
package test
package specific

import java.io.File

import avrohugger.Generator
import avrohugger.format.SpecificRecord
import avrohugger.StringGenerator
import org.specs2._

class SpecificFileToStringsSpec extends mutable.Specification {
    
  // tests specific to fileToX
  "A. correctly generate a simple case class definition from AVRO" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/twitter.avro")
    val gen = new Generator(SpecificRecord)
    val List(source) = gen.fileToStrings(infile)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/specific/com/miguno/avro/twitter_schema.scala")
    source === expected      
  }

  // tests common to fileToX and stringToX
  "1. correctly generate from a protocol with messages" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/mail.avpr")
    val gen = new Generator(SpecificRecord)
    val List(sourceTrait, sourceRecord) = gen.fileToStrings(infile)
    
    val expectedTrait = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/proto/Mail.scala")
    val expectedRecord = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/proto/Message.scala")

    sourceTrait === expectedTrait
    sourceRecord === expectedRecord
  }

  "2. correctly generate a simple case class definition in a package" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/user.avsc")
    val gen = new Generator(SpecificRecord)
    val List(source) = gen.fileToStrings(infile)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/User.scala")
    source === expected
  }

  "3. correctly generate a simple case class definition in the default package" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/AvroTypeProviderTestNoNamespace.avsc")
    val gen = new Generator(SpecificRecord)
    val List(source) = gen.fileToStrings(infile)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/specific/AvroTypeProviderTestNoNamespace.scala")
    source === expected
  }
     
  "4. correctly generate a nested case class definition from a schema" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/nested.avsc")
    val gen = new Generator(SpecificRecord)
    val List(source2, source1, source0) = gen.fileToStrings(infile)

    val expected0 = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/Level0.scala")
    val expected1 = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/Level1.scala")
    val expected2 = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/Level2.scala")
    
    source0 === expected0
    source1 === expected1
    source2 === expected2
  }

  "5. correctly generate a nested case class from IDL" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/nested.avdl")
    val gen = new Generator(SpecificRecord)
    val List(source) = gen.fileToStrings(infile)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/idl/NestedProtocol.scala")
    source === expected      
  }

  "6. correctly generate a recursive case class from IDL" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/recursive.avdl")
    val gen = new Generator(SpecificRecord)
    val List(source) = gen.fileToStrings(infile)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/idl/Recursive.scala")
    source === expected
  }
  
  "7. correctly generate enums from schema" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/enums.avsc")
    val gen = new Generator(SpecificRecord)
    val List(source) = gen.fileToStrings(infile)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/Suit.java")
    source === expected
  }

  "8. correctly generate enums from protocol" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/enums.avpr")
    val gen = new Generator(SpecificRecord)
    val List(sourceRecord, sourceEnum) = gen.fileToStrings(infile)
    
    val expectedEnum = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/proto/Suit.java")
    val expectedRecord = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/proto/Card.scala")
    
    sourceEnum === expectedEnum
    sourceRecord === expectedRecord
  }
  
  "9. correctly generate enums from IDL" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/enums.avdl")
    val gen = new Generator(SpecificRecord)
    val List(sourceRecord, sourceEnum) = gen.fileToStrings(infile)
    
    val expectedEnum = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/idl/Suit.java")
    val expectedRecord = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/idl/Card.scala")
    
    sourceEnum === expectedEnum
    sourceRecord === expectedRecord
  }
    
  "10. correctly generate nested enums" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/enums_nested.avsc")
    val gen = new Generator(SpecificRecord)
    val List(sourceEnum, sourceRecord) = gen.fileToStrings(infile)

    val expectedEnum = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/Direction.java")
    val expectedRecord = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/Compass.scala")

    sourceEnum === expectedEnum
    sourceRecord === expectedRecord
  }
  
  "11. correctly generate bytes from schema" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/bytes.avsc")
    val gen = new Generator(SpecificRecord)
    val List(source) = gen.fileToStrings(infile)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/BinarySc.scala")
    source === expected
  }
  
  "12. correctly generate bytes from protocol" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/bytes.avpr")
    val gen = new Generator(SpecificRecord)
    val List(source) = gen.fileToStrings(infile)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/proto/BinaryPr.scala")
    source === expected
  }
  
  "13. correctly generate bytes from IDL" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/bytes.avdl")
    val gen = new Generator(SpecificRecord)
    val List(source) = gen.fileToStrings(infile)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/idl/BinaryIdl.scala")
    source === expected
  }
 
  "14. correctly generate records depending on others defined in a different- and same-namespaced AVDL and AVSC" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
    val gen = new Generator(SpecificRecord)
    val List(dep3, dep2, dep1, enum, adt) = gen.fileToStrings(infile)
    
    val expectedADT = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/idl/ImportProtocol.scala")
    val expectedDep1 = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/idl/Defaults.scala")
    val expectedEnum = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/idl/DefaultEnum.java")
    val expectedDep2 = util.Util.readFile("avrohugger-core/src/test/expected/specific/other/ns/ExternalDependency.scala")
    val expectedDep3 = util.Util.readFile("avrohugger-core/src/test/expected/specific/other/ns/Suit.java")
    
    adt === expectedADT
    dep1 === expectedDep1
    enum === expectedEnum
    dep2 === expectedDep2
    dep3 === expectedDep3
  }
 
  "15. correctly generate an empty case class definition" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/AvroTypeProviderTestEmptyRecord.avdl")
    val gen = new Generator(SpecificRecord)
    val List(source) = gen.fileToStrings(infile)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/specific/test/Calculator.scala")
    source === expected
  }
    
  "16. correctly generate default values" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/defaults.avdl")
    val gen = new Generator(SpecificRecord)
    val List(sourceRecord, sourceEnum) = gen.fileToStrings(infile)

    val expectedRecord = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/idl/Defaults.scala")
    val expectedEnum = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/idl/DefaultEnum.java")
  
    sourceRecord === expectedRecord
    sourceEnum === expectedEnum
  }
  
  import util.GlobalTests
  for ((test, idx) <- GlobalTests.tests.zipWithIndex) {
    s"${idx + 17}. ${test.description}" in {
      test.toSpec(SpecificRecord).checkFileToStrings
    }
  }
}