package avrohugger
package test
package scavro

import java.io.File

import avrohugger.format.Scavro
import org.specs2._

/**
  * Test generating classes when >22 fields.
  */
class ScavroManyFieldsSpec extends mutable.Specification {

  val avdlPath = "avrohugger-core/src/test/avro/ManyFields.avdl"
  val avscPath = "avrohugger-core/src/test/avro/ManyFields.avsc"

  "Standard Generator " should {
    "generate cases classes when many fields are supported with AVDLs" in {

      val gen = new Generator(Scavro, restrictedFieldNumber = false)
      val outDir = gen.defaultOutputDir + "/scavro/non-restricted"

      "with AVDLs" in {

        gen.fileToFile(new File(avdlPath), outDir)
        val source = util.Util.readFile(s"$outDir/test/avdl/model/ManyFieldsProtocol.scala")

        source must contain("case class")
      }

      "with AVSCs" in {
        gen.fileToFile(new File(avscPath), outDir)
        val source = util.Util.readFile(s"$outDir/test/avsc/model/ManyFields.scala")

        source must contain("case class")
      }

      "with AVDL strings" in {
        val inputString = util.Util.readFile(avdlPath)
        gen.stringToFile(inputString, outDir)
        val source = util.Util.readFile(s"$outDir/test/avdl/model/ManyFieldsProtocol.scala")

        source must contain("case class")
      }

      "with AVDL strings" in {
        val inputString = util.Util.readFile(avscPath)
        gen.stringToFile(inputString, outDir)
        val source = util.Util.readFile(s"$outDir/test/avsc/model/ManyFields.scala")

        source must contain("case class")
      }
    }

    "generate classes when many fields are NOT supported" in {
      val gen = new Generator(Scavro, restrictedFieldNumber = true)
      val outDir = gen.defaultOutputDir + "/scavro/restricted"

      "with AVDLs" in {
        gen.fileToFile(new File(avdlPath), outDir)
        val source = util.Util.readFile(s"$outDir/test/avdl/model/ManyFieldsProtocol.scala")

        source must not contain ("case class")
      }

      "with AVSCs" in {
        gen.fileToFile(new File(avscPath), outDir)
        val source = util.Util.readFile(s"$outDir/test/avsc/model/ManyFields.scala")

        source must not contain ("case class")
      }

      "with AVDL strings" in {
        val inputString = util.Util.readFile(avdlPath)
        gen.stringToFile(inputString, outDir)
        val source = util.Util.readFile(s"$outDir/test/avdl/model/ManyFieldsProtocol.scala")

        source must not contain ("case class")
      }

      "with AVDL strings" in {
        val inputString = util.Util.readFile(avscPath)
        gen.stringToFile(inputString, outDir)
        val source = util.Util.readFile(s"$outDir/test/avsc/model/ManyFields.scala")

        source must not contain ("case class")
      }
    }
  }

}
