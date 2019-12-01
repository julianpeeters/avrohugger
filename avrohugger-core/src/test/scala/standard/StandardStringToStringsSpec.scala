package avrohugger
package test
package standard

import java.io.File

import avrohugger.Generator
import avrohugger.format.Standard
import avrohugger.generators.StringGenerator
import avrohugger.types._
import org.specs2._

class StandardStringToStringsSpec extends Specification {

  def is = s2"""
    Standard Generator stringToStrings method should
      correctly generate a simple case class with a schema in its companion $eB
      correctly generate from a protocol with messages $e1
      correctly generate a simple case class definition in a package $e2
      correctly generate a simple case class definition in the default package $e3
      correctly generate a nested case class definition from a schema $e4
      correctly generate a nested case class from IDL $e5
      correctly generate a recursive case class from IDL $e6
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
      correctly generate logical types values $e22
      correctly generate logical types values with tagged decimals $e23
      correctly generate optional logical types from IDL tagged decimals $e24
      correctly generate an either containing logical types from IDL tagged decimals $e25
      correctly generate a coproduct containing logical types from IDL tagged decimals $e26
  """
  
  def eB = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/relative.avsc")
    val gen = new Generator(Standard, Some(Standard.defaultTypes.copy(record = ScalaCaseClassWithSchema)))
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Relative.scala")
    source === expected
  }
  
  // tests common to fileToX and stringToX
  def e1 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/mail.avpr")
    val gen = new Generator(Standard)
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/proto/Message.scala")
    source === expected
  }

  def e2 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/user.avsc")
    val gen = new Generator(Standard)
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/User.scala")
    source === expected
  }
  
  def e3 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/AvroTypeProviderTestNoNamespace.avsc")
    val gen = new Generator(Standard)
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/AvroTypeProviderTestNoNamespace.scala")
    source === expected
  }
  
  def e4 = {
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
  
  def e5 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/nested.avdl")
    val myAvroScalaCustomTypes = Standard.defaultTypes.copy(protocol = types.ScalaADT)
    val gen = new Generator(format = Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/NestedProtocol.scala")
    source === expected      
  }
  
  def e6 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/recursive.avdl")
    val gen = new Generator(Standard)
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/Recursive.scala")
    source === expected
  }

  def e7 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/enums.avsc")
    val gen = new Generator(Standard)
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Suit.scala")
    source === expected
  }
  
  def e8 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/enums.avpr")
    val myAvroScalaCustomTypes = Standard.defaultTypes.copy(protocol = types.ScalaADT)
    val gen = new Generator(format = Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/proto/EnumProtocol.scala")
    source === expected
  }
  
  def e9 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/enums.avdl")
    val myAvroScalaCustomTypes = Standard.defaultTypes.copy(protocol = types.ScalaADT)
    val gen = new Generator(format = Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/EnumProtocol.scala")
    source === expected
  }
  
  def e10 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/enums_nested.avsc")
    val gen = new Generator(Standard)
    val List(sourceEnum, sourceRecord) = gen.stringToStrings(inputString)

    val expectedEnum = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Direction.scala")
    val expectedRecord = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Compass.scala")

    sourceEnum === expectedEnum
    sourceRecord === expectedRecord
  }
  
  def e11 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/bytes.avsc")
    val gen = new Generator(Standard)
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/BinarySc.scala")
    source === expected
  }
  
  def e12 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/bytes.avpr")
    val gen = new Generator(Standard)
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/proto/BinaryPr.scala")
    source === expected
  }
  
  def e13 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/bytes.avdl")
    val gen = new Generator(Standard)
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/BinaryIdl.scala")
    source === expected
  }
  
  def e14 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/import.avdl")
    val gen = new Generator(Standard)
    gen.stringToStrings(inputString) must throwA(new java.lang.RuntimeException("Imports not supported in String IDLs, only avdl files."))
  }
  
  def e15 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/AvroTypeProviderTestEmptyRecord.avdl")
    val myAvroScalaCustomTypes = Standard.defaultTypes.copy(protocol = types.ScalaADT)
    val gen = new Generator(format = Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/test/Calculator.scala")
    source === expected
  }
  
  def e16 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/defaults.avdl")
    val myAvroScalaCustomTypes = Standard.defaultTypes.copy(protocol = types.ScalaADT)
    val gen = new Generator(format = Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val List(source) = gen.stringToStrings(inputString)

    val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/Defaults.scala")
    source === expected
  }

  def e21 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/AvroTypeProviderTestProtocol.avdl")
    val gen = new Generator(format = Standard)
  
    val List(source) = gen.stringToStrings(inputString)
  
    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/test/Joystick.scala")
  }

  def e22 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/logical.avdl")
    val gen = new Generator(Standard)
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/LogicalIdl.scala")
    source === expected
  }

  def e23 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/logical.avdl")
    val myAvroScalaCustomTypes = Standard.defaultTypes.copy(decimal = ScalaBigDecimalWithPrecision(None))
    val gen = new Generator(Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard-tagged/example/idl/LogicalIdl.scala")
    source === expected
  }

  def e24 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/logical_optional.avdl")
    val myAvroScalaCustomTypes = Standard.defaultTypes.copy(decimal = ScalaBigDecimalWithPrecision(None))
    val gen = new Generator(Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard-tagged/example/idl/LogicalOptionalIdl.scala")
    source === expected
  }

  def e25 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/logical_either.avdl")
    val myAvroScalaCustomTypes = Standard.defaultTypes.copy(decimal = ScalaBigDecimalWithPrecision(None))
    val gen = new Generator(Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard-tagged/example/idl/LogicalEitherIdl.scala")
    source === expected
  }

  def e26 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/logical_coproduct.avdl")
    val myAvroScalaCustomTypes = Standard.defaultTypes.copy(decimal = ScalaBigDecimalWithPrecision(None))
    val gen = new Generator(Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard-tagged/example/idl/LogicalCoproductIdl.scala")
    source === expected
  }
}
