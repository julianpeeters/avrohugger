package specific

import avrohugger._
import avrohugger.format.SpecificRecord
import org.specs2._

class SpecificSameRecordNameSpec extends mutable.Specification {
  "a Generator" should {
    "use the fully qualified name of the records" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/SameRecordNameDifferentNamespace.avsc")
      val gen = new Generator(SpecificRecord)
      val outDir = gen.defaultOutputDir + "/specific/"
      gen.fileToFile(infile, outDir)
      val sourceRecord = scala.io.Source.fromFile(s"$outDir/com/countries/Country.scala").mkString
      sourceRecord ==== util.Util.readFile("avrohugger-core/src/test/expected/specific/com/countries/Country.scala")
    }
  }
}
