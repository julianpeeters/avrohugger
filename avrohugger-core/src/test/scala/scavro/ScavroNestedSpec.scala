package scavro

import avrohugger._
import avrohugger.format.Scavro
import org.specs2._

class ScavroNestedSpec extends Specification {

  def is = s2"""
    A Scavro Generator should
      generate import only when needed $e1
  """

  def e1 = {
    val infile1 =
      new java.io.File("avrohugger-core/src/test/avro/import_nested1.avsc")
    val infile2 =
      new java.io.File("avrohugger-core/src/test/avro/import_nested2.avsc")
    val gen = new Generator(format = Scavro)
    val outDir = gen.defaultOutputDir + "/scavro"
    gen.fileToFile(infile1, outDir)
    gen.fileToFile(infile2, outDir)

    val generated =
      util.Util.readFile(s"$outDir/example/importing/nested/bar/model/BarOuter.scala")
    val expected = 
      util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/importing/nested/bar/model/BarOuter.scala")
    generated ==== expected

  }

}
