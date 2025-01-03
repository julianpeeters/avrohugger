package specific

import avrohugger._
import avrohugger.format.SpecificRecord
import org.specs2._
import util.Util.LineEndingAmbiguousMatcherString

class SpecificDefaultValueFullnameSpec extends mutable.Specification {
  "a Generator" should {
    "use the fully qualified name if field name equals type name" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/field_type_equals_field_name.avsc")
      val gen = Generator(SpecificRecord)
      val outDir = gen.defaultOutputDir + "/specific/"
      gen.fileToFile(infile, outDir)
      val sourceRecord = util.Util.readFile(s"$outDir/example/Room.scala")
      sourceRecord ===/ util.Util.readFile("avrohugger-core/src/test/expected/specific/example/Room.scala")
    }
  }
}
