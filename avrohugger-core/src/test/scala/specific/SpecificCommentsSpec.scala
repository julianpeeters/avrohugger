package avrohugger

import avrohugger.format.SpecificRecord
import org.specs2._
import util.Util.LineEndingAmbiguousMatcherString

class SpecificCommentsSpec extends Specification {
  
  def is = s2"""
    A Specific Generator should
      use the comments from idl files in their Scala counterparts $e1
      use the comments from idl fields even if the record has none $e2
      use the comments from idl records even if the fields have none $e3
      use the comments from the whole protocol as comment to the sealed trait of an ADT $e4
      use the comments from the whole protocol as comment to set of records $e5
  """

  def e1 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/comments1.avdl")
    val gen = Generator(SpecificRecord)
    val outDir = gen.defaultOutputDir + "/specific/"
    gen.fileToFile(infile, outDir)
    val sourceRecord = scala.io.Source.fromFile(s"$outDir/com/example/NoSpaces1.scala").mkString
    sourceRecord ===/ util.Util.readFile("avrohugger-core/src/test/expected/comments/specific/NoSpaces1.scala")
  }
  
  def e2 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/comments2.avdl")
    val gen = Generator(SpecificRecord)
    val outDir = gen.defaultOutputDir + "/specific/"
    gen.fileToFile(infile, outDir)
    val sourceRecord = scala.io.Source.fromFile(s"$outDir/com/example/NoSpaces2.scala").mkString
    sourceRecord ===/ util.Util.readFile("avrohugger-core/src/test/expected/comments/specific/NoSpaces2.scala")
  }
  
  def e3 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/comments3.avdl")
    val gen = Generator(SpecificRecord)
    val outDir = gen.defaultOutputDir + "/specific/"
    gen.fileToFile(infile, outDir)
    val sourceRecord = scala.io.Source.fromFile(s"$outDir/com/example/NoSpaces3.scala").mkString
    sourceRecord ===/ util.Util.readFile("avrohugger-core/src/test/expected/comments/specific/NoSpaces3.scala")
  }
  
  def e4 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/comments4.avdl")
    val myAvroScalaCustomTypes = SpecificRecord.defaultTypes.copy(protocol = types.ScalaADT)
    val gen = Generator(format = SpecificRecord, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val outDir = gen.defaultOutputDir + "/specific/"
    gen.fileToFile(infile, outDir)
    val sourceRecord = scala.io.Source.fromFile(s"$outDir/com/example/Example4.scala").mkString
    sourceRecord ===/ util.Util.readFile("avrohugger-core/src/test/expected/comments/specific/Example4.scala")
  }
  
  def e5 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/comments5.avdl")
    val gen = Generator(SpecificRecord)
    val outDir = gen.defaultOutputDir + "/specific/"
    gen.fileToFile(infile, outDir)
    val sourceRecord = scala.io.Source.fromFile(s"$outDir/com/example/Example5.scala").mkString
    sourceRecord ===/ util.Util.readFile("avrohugger-core/src/test/expected/comments/specific/Example5.scala")
  }
    
}
