package avrohugger
package test
package specific

import java.io.File

import avrohugger.Generator
import avrohugger.format.SpecificRecord
import avrohugger.generators.StringGenerator

import org.specs2._

class SpecificStringToStringsSpec extends Specification {

  def is = s2"""
    SpecificRecord Generator stringToStrings method should
      correctly generate from a protocol with messages $e1
      correctly generate a simple case class definition in a package $e2
      correctly generate a simple case class definition in the default package $e3
      correctly generate a nested case class definition from a schema $e4
      correctly generate a nested case class from IDL $e5
      correctly generate a recursive case class from IDL" $e6
      correctly generate enums from schema $e7
      correctly generate enums from protocol $e8
      correctly generate enums from IDL $e9
      correctly generate nested enums $e10
      correctly generate bytes from schema $e11
      correctly generate bytes from protocol $e12
      correctly generate bytes from IDL $e13
      correctly generate records depending on others defined in a different- and same-namespaced AVDL and AVSC $e14
      correctly generate an empty case class definition $e15
      correctly generate default values $e16
      
      
      
      
      correctly generate a protocol with no ADT when asked $e21
  """
  
  // tests common to fileToX and stringToX
  def e1 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/mail.avpr")
    val gen = new Generator(SpecificRecord)
    val List(sourceTrait, sourceRecord) = gen.stringToStrings(inputString)
    
    val expectedTrait = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/proto/Mail.scala")
    val expectedRecord = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/proto/Message.scala")

    sourceTrait === expectedTrait
    sourceRecord === expectedRecord
  }
  
  def e2 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/user.avsc")
    val gen = new Generator(SpecificRecord)
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/User.scala")
    source === expected
  }
  
  def e3 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/AvroTypeProviderTestNoNamespace.avsc")
    val gen = new Generator(SpecificRecord)
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/specific/AvroTypeProviderTestNoNamespace.scala")
    source === expected
  }
  
  def e4 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/nested.avsc")
    val gen = new Generator(SpecificRecord)
    val List(source2, source1, source0) = gen.stringToStrings(inputString)

    val expected0 = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/Level0.scala")
    val expected1 = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/Level1.scala")
    val expected2 = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/Level2.scala")
    
    source0 === expected0
    source1 === expected1
    source2 === expected2
  }
  
  def e5 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/nested.avdl")
    val myAvroScalaCustomTypes = SpecificRecord.defaultTypes.copy(protocol = types.ScalaADT)
    val gen = new Generator(format = SpecificRecord, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/idl/NestedProtocol.scala")
    source === expected      
  }
  
  def e6 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/recursive.avdl")
    val gen = new Generator(SpecificRecord)
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/idl/Recursive.scala")
    source === expected
  }

  def e7 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/enums.avsc")
    val gen = new Generator(SpecificRecord)
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/Suit.java")
    source === expected
  }
  
  def e8 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/enums.avpr")
    val gen = new Generator(SpecificRecord)
    val List(sourceRecord, sourceEnum) = gen.stringToStrings(inputString)
    
    val expectedEnum = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/proto/Suit.java")
    val expectedRecord = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/proto/Card.scala")
    
    sourceEnum === expectedEnum
    sourceRecord === expectedRecord
  }
  
  def e9 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/enums.avdl")
    val gen = new Generator(SpecificRecord)
    val List(sourceRecord, sourceEnum) = gen.stringToStrings(inputString)
    
    val expectedEnum = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/idl/Suit.java")
    val expectedRecord = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/idl/Card.scala")
    
    sourceEnum === expectedEnum
    sourceRecord === expectedRecord
  }
  
  def e10 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/enums_nested.avsc")
    val gen = new Generator(SpecificRecord)
    val List(sourceEnum, sourceRecord) = gen.stringToStrings(inputString)

    val expectedEnum = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/Direction.java")
    val expectedRecord = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/Compass.scala")

    sourceEnum === expectedEnum
    sourceRecord === expectedRecord
  }
  
  def e11 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/bytes.avsc")
    val gen = new Generator(SpecificRecord)
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/BinarySc.scala")
    source === expected
  }
  
  def e12 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/bytes.avpr")
    val gen = new Generator(SpecificRecord)
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/proto/BinaryPr.scala")
    source === expected
  }
  
  def e13 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/bytes.avdl")
    val gen = new Generator(SpecificRecord)
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/idl/BinaryIdl.scala")
    source === expected
  }
  
  def e14 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/import.avdl")
    val gen = new Generator(SpecificRecord)
    gen.stringToStrings(inputString) must throwA(new java.lang.RuntimeException("Imports not supported in String IDLs, only avdl files."))
  }
  
  def e15 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/AvroTypeProviderTestEmptyRecord.avdl")
    val myAvroScalaCustomTypes = SpecificRecord.defaultTypes.copy(protocol = types.ScalaADT)
    val gen = new Generator(format = SpecificRecord, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/specific/test/Calculator.scala")
    source === expected
  }
  
  def e16 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/defaults.avdl")
    val gen = new Generator(SpecificRecord)
    val List(sourceRecord, sourceEnum) = gen.stringToStrings(inputString)

    val expectedRecord = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/idl/Defaults.scala")
    val expectedEnum = util.Util.readFile("avrohugger-core/src/test/expected/specific/example/idl/DefaultEnum.java")
  
    sourceRecord === expectedRecord
    sourceEnum === expectedEnum
  }
  
  
  
  
  
  
  def e21 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/AvroTypeProviderTestProtocol.avdl")
    val gen = new Generator(format = SpecificRecord)
  
    val List(source) = gen.stringToStrings(inputString)
  
    source === util.Util.readFile("avrohugger-core/src/test/expected/specific/test/Joystick.scala")
  }

}
