package avrohugger
package test
package standard

import java.io.File
import avrohugger._
import avrohugger.format.Standard
import avrohugger.types._
import org.specs2._
import util.Util.checkFileExist

import scala.util.Try

class StandardFileToFileSpec extends Specification {

  def is =
    sequential ^ s2"""
    Standard Generator fileToFile method should
    
    correctly generate a simple case class definition from AVRO $eA
    correctly generate a simple case class with a schema in its companion $eB
    not generate copy of imported classes in the importing package $e0
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
    correctly generate union values without shapeless Coproduct $e17
    correctly generate union values with shapeless Coproduct $e18
    correctly generate all union values with optional shapeless Coproduct when instructed by generator $e19
    correctly generate all union values with option shapeless Coproduct when instructed by generator $e20
    correctly generate all union values with Scala 3 union type when instructed by generator $e21

    correctly generate union default option either parameter values $e22
    correctly generate union default option values $e23
    correctly generate union default optional parameter values $e24
    currectly generate union default scala3 union parameter values $e25

    correctly generate a protocol with no ADT when asked $e26
    correctly generate a protocol with no ADT when asked $e27
    correctly generate a protocol with no ADT when asked $e28
    correctly generate logical types from schema $e29
    correctly generate logical types from protocol $e30
    correctly generate logical types with custom date, timestamp and time types $e32
    correctly generate logical types from schema with tagged decimals $e33
    correctly generate logical types from protocol tagged decimals $e34
    correctly generate logical types with custom date, timestamp and time types tagged decimals $e36
    correctly generate optional logical types from IDL tagged decimals $e37
    correctly generate an either containing logical types from IDL tagged decimals $e38
    correctly generate a coproduct containing logical types from IDL tagged decimals $e39
    correctly generate a protocol with special strings $e40

    correctly generate fixed from schema $e41
    correctly generate simple fixed bytes from schema $e42
    correctly generate record with fixed bytes from schema $e43
    correctly generate nested fixed bytes from schema $e44

    correctly generate nested references with custom namespaces $e45
    correctly handle underlyingType for date/time related fields $e46
  """

  // correctly generate logical types from IDL $e31
  // correctly generate logical types from IDL tagged decimals $e35


  // tests standard to fileToX
  def eA = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/twitter.avro")
    val gen = Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/com/miguno/avro/twitter_schema.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/com/miguno/avro/twitter_schema.scala")
  }

  def eB = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/relative.avsc")
    val gen = Generator(Standard, Some(Standard.defaultTypes.copy(record = ScalaCaseClassWithSchema)))
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/Relative.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Relative.scala")
  }

  // tests standard to fileToFile
  def e0 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
    val gen = Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    new File(s"target/generated-sources/standard/example/idl/ExternalDependency.scala").exists === false
  }

  // tests common to fileToX and stringToX
  def e1 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/mail.avpr")
    val gen = Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile(s"$outDir/example/proto/Message.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/proto/Message.scala")
  }

  def e2 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/user.avsc")
    val gen = Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/User.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/User.scala")
  }

  def e3 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/AvroTypeProviderTestNoNamespace.avsc")
    val gen = Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/AvroTypeProviderTestNoNamespace.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/AvroTypeProviderTestNoNamespace.scala")
  }

  def e4 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/nested.avsc")
    val gen = Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source0 = util.Util.readFile("target/generated-sources/standard/example/Level0.scala")
    val source1 = util.Util.readFile("target/generated-sources/standard/example/Level1.scala")
    val source2 = util.Util.readFile("target/generated-sources/standard/example/Level2.scala")

    source0 === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Level0.scala")
    source1 === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Level1.scala")
    source2 === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Level2.scala")
  }

  def e5 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/nested.avdl")
    val myAvroScalaCustomTypes = Standard.defaultTypes.copy(protocol = types.ScalaADT)
    val gen = Generator(format = Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/idl/NestedProtocol.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/NestedProtocol.scala")
  }

  def e6 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/recursive.avdl")
    val gen = Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/idl/Recursive.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/Recursive.scala")
  }

  def e7 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/enums.avsc")
    val gen = Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/Suit.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Suit.scala")
  }

  def e8 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/enums.avpr")
    val myAvroScalaCustomTypes = Standard.defaultTypes.copy(protocol = types.ScalaADT)
    val gen = Generator(format = Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/proto/EnumProtocol.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/proto/EnumProtocol.scala")
  }

  def e9 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/enums.avdl")
    val myAvroScalaCustomTypes = Standard.defaultTypes.copy(protocol = types.ScalaADT)
    val gen = Generator(format = Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/idl/EnumProtocol.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/EnumProtocol.scala")
  }

  def e10 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/enums_nested.avsc")
    val gen = Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val sourceEnum = util.Util.readFile("target/generated-sources/standard/example/Direction.scala")
    val sourceRecord = util.Util.readFile("target/generated-sources/standard/example/Compass.scala")

    sourceEnum === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Direction.scala")
    sourceRecord === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Compass.scala")
  }

  def e11 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/bytes.avsc")
    val gen = Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/BinarySc.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/BinarySc.scala")
  }

  def e12 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/bytes.avpr")
    val gen = Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/proto/BinaryPr.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/proto/BinaryPr.scala")
  }

  def e13 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/bytes.avdl")
    val gen = Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/idl/BinaryIdl.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/BinaryIdl.scala")
  }

  def e14 = {
    val importing = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
    val gen = Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(importing, outDir)

    val sourceADT = util.Util.readFile("target/generated-sources/standard/example/idl/ImportProtocol.scala")
    val sourceDep1 = util.Util.readFile("target/generated-sources/standard/example/idl/Defaults.scala")
    val sourceDep2 = util.Util.readFile("target/generated-sources/standard/other/ns/ExternalDependency.scala")
    val sourceDep3 = util.Util.readFile("target/generated-sources/standard/other/ns/Suit.scala")

    sourceADT === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/ImportProtocol.scala")
    sourceDep1 === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/Defaults.scala")
    sourceDep2 === util.Util.readFile("avrohugger-core/src/test/expected/standard/other/ns/ExternalDependency.scala")
    sourceDep3 === util.Util.readFile("avrohugger-core/src/test/expected/standard/other/ns/Suit.scala")
  }

  def e15 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/AvroTypeProviderTestEmptyRecord.avdl")
    val myAvroScalaCustomTypes = Standard.defaultTypes.copy(protocol = types.ScalaADT)
    val gen = Generator(format = Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/test/Calculator.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/test/Calculator.scala")
  }

  def e16 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/defaults.avdl")
    val myAvroScalaCustomTypes = Standard.defaultTypes.copy(protocol = types.ScalaADT)
    val gen = Generator(format = Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val adt = util.Util.readFile("target/generated-sources/standard/example/idl/Defaults.scala")

    adt === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/Defaults.scala")
  }

  def e17 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/unions_without_coproduct.avdl")
    val gen = Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val adt = util.Util.readFile("target/generated-sources/standard/example/idl/WithoutShapelessCoproduct.scala")

    adt === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/WithoutShapelessCoproduct.scala")
  }

  def e18 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/unions_with_coproduct.avdl")
    val gen = Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val adt = util.Util.readFile("target/generated-sources/standard/example/idl/WithShapelessCoproduct.scala")

    adt === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/WithShapelessCoproduct.scala")
  }

  def e19 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/all_unions_as_optional_coproduct.avdl")
    val avroScalaCustomTypes = Standard.defaultTypes.copy(union = OptionalShapelessCoproduct)
    val gen = Generator(Standard, avroScalaCustomTypes = Some(avroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val adt = util.Util.readFile("target/generated-sources/standard/example/idl/AllUnionsWithOptionalShapelessCoproduct.scala")

    adt === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/AllUnionsWithOptionalShapelessCoproduct.scala")
  }

  def e20 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/all_unions_as_option_coproduct.avdl")
    val avroScalaCustomTypes = Standard.defaultTypes.copy(union = OptionShapelessCoproduct)
    val gen = Generator(Standard, avroScalaCustomTypes = Some(avroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val adt = util.Util.readFile("target/generated-sources/standard/example/idl/AllUnionsWithOptionShapelessCoproduct.scala")

    adt === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/AllUnionsWithOptionShapelessCoproduct.scala")
  }

  def e21 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/all_unions_as_scala3_union_type.avdl")
    val avroScalaCustomTypes = Standard.defaultTypes.copy(union = OptionScala3UnionType)
    val gen = Generator(Standard, avroScalaCustomTypes = Some(avroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val adt = util.Util.readFile("target/generated-sources/standard/example/idl/AllUnionsWithScala3UnionType.scala")

    adt === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/AllUnionsWithScala3UnionType.scala")
  }

  def e22 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/unions_option_either_default_param_values.avdl")
    val gen = Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val adt = util.Util.readFile("target/generated-sources/standard/example/idl/UnionsOptionEitherDefaultParamsValues.scala")

    adt === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/UnionsOptionEitherDefaultParamsValues.scala")
  }

  def e23 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/unions_option_default_param_values.avdl")
    val avroScalaCustomTypes = Standard.defaultTypes.copy(union = OptionShapelessCoproduct)
    val gen = Generator(Standard, avroScalaCustomTypes = Some(avroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/standard/"

    gen.fileToFile(infile, outDir)

    val adt = util.Util.readFile("target/generated-sources/standard/example/idl/UnionsOptionDefaultParamsValues.scala")

    adt === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/UnionsOptionDefaultParamsValues.scala")
  }

  def e24 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/unions_optional_default_param_values.avdl")
    val avroScalaCustomTypes = Standard.defaultTypes.copy(union = OptionalShapelessCoproduct)
    val gen = Generator(Standard, avroScalaCustomTypes = Some(avroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val adt = util.Util.readFile("target/generated-sources/standard/example/idl/UnionsOptionalDefaultParamsValues.scala")

    adt === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/UnionsOptionalDefaultParamsValues.scala")
  }

  def e25 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/unions_option_scala3_union_types.avdl")
    val avroScalaCustomTypes = Standard.defaultTypes.copy(union = OptionScala3UnionType)
    val gen = Generator(Standard, avroScalaCustomTypes = Some(avroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val adt = util.Util.readFile("target/generated-sources/standard/example/idl/UnionsOptionScala3UnionTypes.scala")

    adt === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/UnionsOptionScala3UnionTypes.scala")
  }

  def e26 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/AvroTypeProviderTestProtocol.avdl")
    val gen = Generator(format = Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/test/Joystick.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/test/Joystick.scala")
  }

  def e27 = {
    val inOrderSchema = new java.io.File("avrohugger-core/src/test/avro/order.avsc")
    val inReportSchema = new java.io.File("avrohugger-core/src/test/avro/userMonthlyReport.avsc")
    val gen = Generator(format = Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(inOrderSchema, outDir)
    gen.fileToFile(inReportSchema, outDir)


    val sources = for {
      orderSource <- Try(util.Util.readFile("target/generated-sources/standard/com/same/element/Order.scala"))
      reportSource <- Try(util.Util.readFile("target/generated-sources/standard/com/same/element/MonthlyReport.scala"))
      userSource <- Try(util.Util.readFile("target/generated-sources/standard/com/same/element/User.scala"))
    } yield {
      List(orderSource, reportSource, userSource)
    }

    sources must beSuccessfulTry

  }

  def e28 = {
    val inOrderSchema = new java.io.File("avrohugger-core/src/test/avro/order.avsc")
    val inReportSchema = new java.io.File("avrohugger-core/src/test/avro/userRedefinedMonthlyReport.avsc")
    val gen = Generator(format = Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(inOrderSchema, outDir)
    gen.fileToFile(inReportSchema, outDir) must throwA[Exception]
  }

  def e29 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/logical.avsc")
    val gen = Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/logical/LogicalSc.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/logical/LogicalSc.scala")
  }

  def e30 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/logical.avpr")
    val gen = Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/logical/proto/Logical.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/logical/proto/Logical.scala")
  }

  // def e31 = {
  //   val infile = new java.io.File("avrohugger-core/src/test/avro/logical.avdl")
  //   val gen = Generator(Standard)
  //   val outDir = gen.defaultOutputDir + "/standard/"
  //   gen.fileToFile(infile, outDir)

  //   val source = util.Util.readFile("target/generated-sources/standard/example/idl/LogicalIdl.scala")

  //   source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/LogicalIdl.scala")
  // }

  def e32 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/logicalsql.avsc")
    val avroScalaCustomTypes = Standard.defaultTypes.copy(date = JavaSqlDate, timestampMillis = JavaSqlTimestamp, timeMillis = JavaSqlTime)
    val gen = Generator(Standard, avroScalaCustomTypes = Some(avroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/logical/LogicalSql.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/logical/LogicalSql.scala")
  }

  def e33 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/logical.avsc")
    val myAvroScalaCustomTypes = Standard.defaultTypes.copy(decimal = ScalaBigDecimalWithPrecision(None))
    val gen = Generator(Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/standard-tagged/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard-tagged/example/logical/LogicalSc.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard-tagged/example/logical/LogicalSc.scala")
  }

  def e34 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/logical.avpr")
    val myAvroScalaCustomTypes = Standard.defaultTypes.copy(decimal = ScalaBigDecimalWithPrecision(None))
    val gen = Generator(Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/standard-tagged/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard-tagged/example/logical/proto/Logical.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard-tagged/example/logical/proto/Logical.scala")
  }

  // def e35 = {
  //   val infile = new java.io.File("avrohugger-core/src/test/avro/logical.avdl")
  //   val myAvroScalaCustomTypes = Standard.defaultTypes.copy(decimal = ScalaBigDecimalWithPrecision(None))
  //   val gen = Generator(Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
  //   val outDir = gen.defaultOutputDir + "/standard-tagged/"
  //   gen.fileToFile(infile, outDir)

  //   val source = util.Util.readFile("target/generated-sources/standard-tagged/example/idl/LogicalIdl.scala")

  //   source === util.Util.readFile("avrohugger-core/src/test/expected/standard-tagged/example/idl/LogicalIdl.scala")
  // }

  def e36 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/logicalsql.avsc")
    val avroScalaCustomTypes = Standard.defaultTypes.copy(date = JavaSqlDate, timestampMillis = JavaSqlTimestamp,
      decimal = ScalaBigDecimalWithPrecision(None), timeMillis = JavaSqlTime)
    val gen = Generator(Standard, avroScalaCustomTypes = Some(avroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/standard-tagged/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard-tagged/example/logical/LogicalSql.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard-tagged/example/logical/LogicalSql.scala")
  }

  def e37 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/logical_optional.avdl")
    val myAvroScalaCustomTypes = Standard.defaultTypes.copy(decimal = ScalaBigDecimalWithPrecision(None))
    val gen = Generator(Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/standard-tagged/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard-tagged/example/idl/LogicalOptionalIdl.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard-tagged/example/idl/LogicalOptionalIdl.scala")
  }

  def e38 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/logical_either.avdl")
    val myAvroScalaCustomTypes = Standard.defaultTypes.copy(decimal = ScalaBigDecimalWithPrecision(None))
    val gen = Generator(Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/standard-tagged/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard-tagged/example/idl/LogicalEitherIdl.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard-tagged/example/idl/LogicalEitherIdl.scala")
  }

  def e39 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/logical_coproduct.avdl")
    val myAvroScalaCustomTypes = Standard.defaultTypes.copy(decimal = ScalaBigDecimalWithPrecision(None))
    val gen = Generator(Standard, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/standard-tagged/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard-tagged/example/idl/LogicalCoproductIdl.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard-tagged/example/idl/LogicalCoproductIdl.scala")
  }

  def e40 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/special_names.avdl")
    val gen = Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/idl/Names.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/Names.scala")
  }

  def e41 = {
    val infile = new File("avrohugger-core/src/test/avro/fixed.avsc")
    val gen = Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    checkFileExist("avrohugger-core/src/test/expected/standard/example/logical/Fixed.scala") === false
  }

  def e42 = {
    val infile = new File("avrohugger-core/src/test/avro/fixed_bytes_simple.avsc")
    val gen = Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val expected = util.Util.readFile("avrohugger-core/src/test/expected/standard/example/shasimple/Sha256.scala")
    val generated = util.Util.readFile("target/generated-sources/standard/example/shasimple/Sha256.scala")

    expected === generated
  }

  def e43 = {
    val infile = new File("avrohugger-core/src/test/avro/fixed_bytes_record.avsc")
    val gen = Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    util.Util.readFile("avrohugger-core/src/test/expected/standard/example/sharecord/Sha256.scala") === util.Util.readFile("target/generated-sources/standard/example/sharecord/Sha256.scala")
    util.Util.readFile("avrohugger-core/src/test/expected/standard/example/sharecord/HashRecord.scala") === util.Util.readFile("target/generated-sources/standard/example/sharecord/HashRecord.scala")
  }

  def e44 = {
    val infile = new File("avrohugger-core/src/test/avro/fixed_bytes_nested.avsc")
    val gen = Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    util.Util.readFile("avrohugger-core/src/test/expected/standard/example/shanested/foo/HashRecord.scala") === util.Util.readFile("target/generated-sources/standard/example/shanested/foo/HashRecord.scala")
    util.Util.readFile("avrohugger-core/src/test/expected/standard/example/shanested/Prop.scala") === util.Util.readFile("target/generated-sources/standard/example/shanested/Prop.scala")
    util.Util.readFile("avrohugger-core/src/test/expected/standard/example/shanested/Sha256.scala") === util.Util.readFile("target/generated-sources/standard/example/shanested/Sha256.scala")
  }

  def e45 = {
    val infile = new File("avrohugger-core/src/test/avro/outer.avdl")
    val customNs = Map("a.b.c" -> "aa.bb.cc")
    val gen = Generator(Standard, avroScalaCustomNamespace = customNs)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/aa/bb/cc/Test.scala")
    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/aa/bb/cc/Test.scala")
  }

  def e46 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/date_time_related_fields.avsc")
    val avroScalaCustomTypes = Standard.defaultTypes.copy(date = UnderlyingPrimitive, timestampMillis = UnderlyingPrimitive, timestampMicros = UnderlyingPrimitive, localTimestampMicros = UnderlyingPrimitive, localTimestampMillis = UnderlyingPrimitive, timeMillis = UnderlyingPrimitive, timeMicros = UnderlyingPrimitive)
    val gen = Generator(Standard, avroScalaCustomTypes = Some(avroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/datetimerelatedfields/DateTimeRelatedFields.scala")
    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/datetimerelatedfields/DateTimeRelatedFields.scala")
  }

}