package specific

import avrohugger._
import avrohugger.format.SpecificRecord
import org.specs2._

class SpecificNestedSpec extends Specification {

  def is = s2"""
    A Specific Generator should
      generate import only when needed $e1
  """

  def e1 = {
    val infile1 =
      new java.io.File("avrohugger-core/src/test/avro/import_nested1.avsc")
    val infile2 =
      new java.io.File("avrohugger-core/src/test/avro/import_nested2.avsc")
    val gen = new Generator(format = SpecificRecord)
    val outDir = gen.defaultOutputDir + "/specific"
    gen.fileToFile(infile1, outDir)
    gen.fileToFile(infile2, outDir)

    val generated =
      util.Util.readFile(s"$outDir/example/importing/nested/bar/BarOuter.scala")
    val expected = 
      util.Util.readFile("avrohugger-core/src/test/expected/specific/example/importing/nested/bar/BarOuter.scala")
    generated ==== expected
  }
}
