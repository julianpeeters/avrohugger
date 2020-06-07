package avrohugger


import avrohugger.format.abstractions.SourceFormat
import avrohugger.types._
import org.specs2.Specification
import org.specs2.matcher.MatchResult
import org.specs2.specification.core.SpecStructure

trait SeqSpec {
  self: Specification =>

  val protocolFile = new java.io.File("avrohugger-core/src/test/avro/array.avdl")

  def sourceFormat: SourceFormat
  final def generator: Generator = Generator(sourceFormat)
  final def defaultTypes: AvroScalaTypes = generator.format.defaultTypes
  final def formatType: String = sourceFormat.getClass.getSimpleName.takeWhile(_.isLetterOrDigit).toLowerCase

  def is: SpecStructure =
    s2"""A $formatType Generator should
    correctly generate strings with Seq when asked for ${strings(types = defaultTypes.copy(array = ScalaSeq))}
    correctly generate files with Seq when asked for ${files(types = defaultTypes.copy(array = ScalaSeq))}
    correctly generate strings with List when asked for ${strings(types = defaultTypes.copy(array = ScalaList))}
    correctly generate files with List when asked for ${files(types = defaultTypes.copy(array = ScalaList))}
    correctly generate strings with Array when asked for ${strings(types = defaultTypes.copy(array = ScalaArray))}
    correctly generate files with Array when asked for ${files(types = defaultTypes.copy(array = ScalaArray))}
    correctly generate strings with Vector when asked for ${strings(types = defaultTypes.copy(array = ScalaVector))}
    correctly generate files with Vector when asked for ${files(types = defaultTypes.copy(array = ScalaVector))}"""

  def expectedOutput(formatType: String, arrayType: String): String


  def actualOutput = "example/idl/array/ArrayIdl.scala"


  final def strings(types: AvroScalaTypes = defaultTypes, input: java.io.File = protocolFile): MatchResult[String] = {
    val gen = generator.copy(avroScalaCustomTypes = Some(types))

    val outputArrayType = types.array.toString

    val List(dep1) = gen.fileToStrings(input)

    val expectedDep1 = util.Util.readFile(expectedOutput(formatType, outputArrayType)).dropRight(1)

    dep1 === expectedDep1

  }

  final def files(types: AvroScalaTypes = defaultTypes,
                  input: java.io.File = protocolFile,
                  actualOutput: String = actualOutput): MatchResult[String] = {
    val gen = generator.copy(avroScalaCustomTypes = Some(types))
    val outputArrayType = types.array.toString

    val outDir = s"${gen.defaultOutputDir}/$formatType/$outputArrayType/"
    gen.fileToFile(input, outDir)

    val dep1 = util.Util.readFile(s"target/generated-sources/$formatType/$outputArrayType/$actualOutput")

    val expectedDep1 = util.Util.readFile(expectedOutput(formatType, outputArrayType)).dropRight(1)

    dep1 === expectedDep1
  }

}
