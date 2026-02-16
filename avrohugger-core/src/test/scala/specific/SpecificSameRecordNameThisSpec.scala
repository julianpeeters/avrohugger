package specific

import avrohugger._
import avrohugger.format.SpecificRecord
import org.specs2._

class SpecificSameRecordNameThisSpec extends mutable.Specification {
  "a Generator" should {
    "use the fully qualified name of the records in the `this`-method" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/SameRecordNameDifferentNamespaceThis.avsc")
      val gen = Generator(SpecificRecord)
      val outDir = gen.defaultOutputDir + "/specific/"
      gen.fileToFile(infile, outDir)
      val sourceRecord = util.Util.readFile(s"$outDir/com/myrecord/MyRecord.scala")
      sourceRecord === util.Util.readFile("avrohugger-core/src/test/expected/specific/com/myrecord/MyRecord.scala")
    }
  }
}
