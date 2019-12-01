package avrohugger
package test
package standard

import avrohugger._
import avrohugger.format.Standard
import avrohugger.types._
import org.specs2._

class StandardStringToFileSpec extends Specification {
  
  def is = s2"""
    Standard Generator stringToFile method should
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
      correctly generate logical types from IDL $e22
      correctly generate logical types from IDL with tagged decimals $e23
      correctly generate optional logical types from IDL tagged decimals $e24
      correctly generate an either containing logical types from IDL tagged decimals $e25
      correctly generate a coproduct containing logical types from IDL tagged decimals $e26
  """
  
  def eB = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/relative.avsc")
    val gen = new Generator(Standard, Some(Standard.defaultTypes.copy(record = ScalaCaseClassWithSchema)))
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.stringToFile(inputString, outDir)
    
    val source = util.Util.readFile(s"$outDir/example/Relative.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Relative.scala")
  }

  // tests common to fileToX and stringToX
  def e1 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/mail.avpr")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.stringToFile(inputString, outDir)

    val source = util.Util.readFile(s"$outDir/example/proto/Message.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/proto/Message.scala")
  }

  def e2 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/user.avsc")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.stringToFile(inputString, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/User.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/User.scala")
  }

  def e3 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/AvroTypeProviderTestNoNamespace.avsc")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.stringToFile(inputString, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/AvroTypeProviderTestNoNamespace.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/AvroTypeProviderTestNoNamespace.scala")
  }

  def e4 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/nested.avsc")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.stringToFile(inputString, outDir)

    val source0 = util.Util.readFile("target/generated-sources/standard/example/Level0.scala")
    val source1 = util.Util.readFile("target/generated-sources/standard/example/Level1.scala")
    val source2 = util.Util.readFile("target/generated-sources/standard/example/Level2.scala")

    source0 === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Level0.scala")
    source1 === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Level1.scala")
    source2 === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Level2.scala")
  }

  def e5 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/nested.avdl")
    val myAvroScalaCustomTypes = Standard.defaultTypes.copy(protocol = types.ScalaADT)
    val gen = new Generator(format = Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.stringToFile(inputString, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/idl/NestedProtocol.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/NestedProtocol.scala")
  }

  def e6 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/recursive.avdl")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.stringToFile(inputString, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/idl/Recursive.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/Recursive.scala")
  }

  def e7 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/enums.avsc")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.stringToFile(inputString, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/Suit.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Suit.scala")
  }

  def e8 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/enums.avpr")
    val myAvroScalaCustomTypes = Standard.defaultTypes.copy(protocol = types.ScalaADT)
    val gen = new Generator(format = Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.stringToFile(inputString, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/proto/EnumProtocol.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/proto/EnumProtocol.scala")
  }

  def e9 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/enums.avdl")
    val myAvroScalaCustomTypes = Standard.defaultTypes.copy(protocol = types.ScalaADT)
    val gen = new Generator(format = Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.stringToFile(inputString, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/idl/EnumProtocol.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/EnumProtocol.scala")
  }

  def e10 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/enums_nested.avsc")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.stringToFile(inputString, outDir)

    val sourceEnum = util.Util.readFile("target/generated-sources/standard/example/Direction.scala")
    val sourceRecord = util.Util.readFile("target/generated-sources/standard/example/Compass.scala")

    sourceEnum === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Direction.scala")
    sourceRecord === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Compass.scala")
  }

  def e11 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/bytes.avsc")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.stringToFile(inputString, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/BinarySc.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/BinarySc.scala")
  }

  def e12 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/bytes.avpr")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.stringToFile(inputString, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/proto/BinaryPr.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/proto/BinaryPr.scala")
  }

  def e13 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/bytes.avdl")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.stringToFile(inputString, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/idl/BinaryIdl.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/BinaryIdl.scala")
  }

  def e14 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/import.avdl")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.stringToFile(inputString, outDir) must throwA(new java.lang.RuntimeException("Imports not supported in String IDLs, only avdl files."))
  }

  def e15 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/AvroTypeProviderTestEmptyRecord.avdl")
    val myAvroScalaCustomTypes = Standard.defaultTypes.copy(protocol = types.ScalaADT)
    val gen = new Generator(format = Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.stringToFile(inputString, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/test/Calculator.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/test/Calculator.scala")
  }

  def e16 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/defaults.avdl")
    val myAvroScalaCustomTypes = Standard.defaultTypes.copy(protocol = types.ScalaADT)
    val gen = new Generator(format = Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.stringToFile(inputString, outDir)

    val adt = util.Util.readFile("target/generated-sources/standard/example/idl/Defaults.scala")

    adt === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/Defaults.scala")
  }

  def e21 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/AvroTypeProviderTestProtocol.avdl")
    val gen = new Generator(format = Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.stringToFile(inputString, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/test/Joystick.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/test/Joystick.scala")
  }

  def e22 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/logical.avdl")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.stringToFile(inputString, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/idl/LogicalIdl.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/LogicalIdl.scala")
  }

  def e23 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/logical.avdl")
    val myAvroScalaCustomTypes = Standard.defaultTypes.copy(decimal = ScalaBigDecimalWithPrecision(None))
    val gen = new Generator(Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/standard-tagged/"
    gen.stringToFile(inputString, outDir)

    val source = util.Util.readFile("target/generated-sources/standard-tagged/example/idl/LogicalIdl.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard-tagged/example/idl/LogicalIdl.scala")
  }

  def e24 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/logical_optional.avdl")
    val myAvroScalaCustomTypes = Standard.defaultTypes.copy(decimal = ScalaBigDecimalWithPrecision(None))
    val gen = new Generator(Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/standard-tagged/"
    gen.stringToFile(inputString, outDir)

    val source = util.Util.readFile("target/generated-sources/standard-tagged/example/idl/LogicalOptionalIdl.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard-tagged/example/idl/LogicalOptionalIdl.scala")
  }

  def e25 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/logical_either.avdl")
    val myAvroScalaCustomTypes = Standard.defaultTypes.copy(decimal = ScalaBigDecimalWithPrecision(None))
    val gen = new Generator(Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/standard-tagged/"
    gen.stringToFile(inputString, outDir)

    val source = util.Util.readFile("target/generated-sources/standard-tagged/example/idl/LogicalEitherIdl.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard-tagged/example/idl/LogicalEitherIdl.scala")
  }

  def e26 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/logical_coproduct.avdl")
    val myAvroScalaCustomTypes = Standard.defaultTypes.copy(decimal = ScalaBigDecimalWithPrecision(None))
    val gen = new Generator(Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/standard-tagged/"
    gen.stringToFile(inputString, outDir)

    val source = util.Util.readFile("target/generated-sources/standard-tagged/example/idl/LogicalCoproductIdl.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard-tagged/example/idl/LogicalCoproductIdl.scala")
  }
}
