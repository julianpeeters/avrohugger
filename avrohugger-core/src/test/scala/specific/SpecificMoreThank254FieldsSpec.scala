package avrohugger
package test
package specific

import avrohugger.format.SpecificRecord
import org.specs2._

import java.io.File

/**
  * Test generating classes when >254 fields.
  */
class SpecificMoreThank254FieldsSpec extends Specification {

  val avdlPath = "avrohugger-core/src/test/avro/MoreThan254Fields.avdl"
  val avscPath = "avrohugger-core/src/test/avro/MoreThan254Fields.avsc"
  
  val generator = new Generator(SpecificRecord)
  val outDirNonRestricted = generator.defaultOutputDir + "/specific/moreThan254"

  def is =
    s2"""
  A Specific Generator should
    generate classes when there are more than 254 fields
      with AVDLs $e1
      with AVSCs $e2
      with AVDL strings $e3
      with AVSC strings $e4
"""

  def e1 = {
    generator.fileToFile(new File(avdlPath), outDirNonRestricted)
    val source = util.Util.readFile(s"$outDirNonRestricted/test/avdl/MoreThan254Fields.scala")
    source must not contain("case class")
  }

  def e2 = {
    generator.fileToFile(new File(avscPath), outDirNonRestricted)
    val source = util.Util.readFile(s"$outDirNonRestricted/test/avsc/MoreThan254Fields.scala")
    source must not contain("case class")
  }

  def e3 = {
    val inputString = util.Util.readFile(avdlPath)
    generator.stringToFile(inputString, outDirNonRestricted)
    val source = util.Util.readFile(s"$outDirNonRestricted/test/avdl/MoreThan254Fields.scala")
    source must not contain("case class")
  }

  def e4 = {
    val inputString = util.Util.readFile(avscPath)
    generator.stringToFile(inputString, outDirNonRestricted)
    val source = util.Util.readFile(s"$outDirNonRestricted/test/avsc/MoreThan254Fields.scala")
    source must not contain("case class")
  }
  
}
