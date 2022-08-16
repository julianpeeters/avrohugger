package avrohugger
package test
package specific

import avrohugger._
import avrohugger.format.SpecificRecord
import org.specs2._

class SpecificNullableBigDecimalSpec extends Specification {
  def is = skipAll ^ s2"SpecificFileGenerator should correctly support nullable decimals $e1"

  def e1 = {
    val infile = new java.io.File("avrohugger-core/src/test/avro/nullable_bigdecimal.avsc")
    val gen = new Generator(SpecificRecord)
    val outDir = gen.defaultOutputDir + "/specific/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/specific/com/example/NullableDecimal.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/specific/com/example/NullableDecimal.scala")
  }
}