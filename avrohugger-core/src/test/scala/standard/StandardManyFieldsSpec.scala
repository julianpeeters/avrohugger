package avrohugger
package test
package standard

import java.io.File

import avrohugger.format.Standard
import org.specs2._

/**
  * Test generating classes when >22 fields.
  */
class StandardManyFieldsSpec extends Specification {

  val avdlPath = "avrohugger-core/src/test/avro/ManyFields.avdl"
  val avscPath = "avrohugger-core/src/test/avro/ManyFields.avsc"
  
  val genNonRestricted = Generator(Standard, restrictedFieldNumber = false)
  val outDirNonRestricted = genNonRestricted.defaultOutputDir + "/standard/non-restricted"
  val genRestricted = Generator(Standard, restrictedFieldNumber = true)
  val outDirRestricted = genRestricted.defaultOutputDir + "/standard/restricted"
  
  def is = s2"""
    A Standard Generator should
      generate cases classes when many fields are supported with AVDLs
        with AVDLs $e1
        with AVSCs $e2
        with AVDL strings $e3
        with AVSC strings $e4
      generate classes when many fields are NOT supported
        with AVDLs $e5
        with AVSCs $e6
        with AVDL strings $e7
        with AVSC strings $e8
  """

  def e1 = {
    genNonRestricted.fileToFile(new File(avdlPath), outDirNonRestricted)
    val source = util.Util.readFile(s"$outDirNonRestricted/test/avdl/ManyFieldsProtocol.scala")
    source must contain("case class")
  }

  def e2 = {
    genNonRestricted.fileToFile(new File(avscPath), outDirNonRestricted)
    val source = util.Util.readFile(s"$outDirNonRestricted/test/avsc/ManyFields.scala")
    source must contain("case class")
  }

  def e3 = {
    val inputString = util.Util.readFile(avdlPath)
    genNonRestricted.stringToFile(inputString, outDirNonRestricted)
    val source = util.Util.readFile(s"$outDirNonRestricted/test/avdl/ManyFieldsProtocol.scala")
    source must contain("case class")
  }

  def e4 = {
    val inputString = util.Util.readFile(avscPath)
    genNonRestricted.stringToFile(inputString, outDirNonRestricted)
    val source = util.Util.readFile(s"$outDirNonRestricted/test/avsc/ManyFields.scala")
    source must contain("case class")
  }

  def e5 = {
    genRestricted.fileToFile(new File(avdlPath), outDirRestricted)
    val source = util.Util.readFile(s"$outDirRestricted/test/avdl/ManyFieldsProtocol.scala")
    source must not contain ("case class")
  }

  def e6 = {
    genRestricted.fileToFile(new File(avscPath), outDirRestricted)
    val source = util.Util.readFile(s"$outDirRestricted/test/avsc/ManyFields.scala")
    source must not contain ("case class")
  }

  def e7 = {
    val inputString = util.Util.readFile(avdlPath)
    genRestricted.stringToFile(inputString, outDirRestricted)
    val source = util.Util.readFile(s"$outDirRestricted/test/avdl/ManyFieldsProtocol.scala")
    source must not contain ("case class")
  }

  def e8 = {
    val inputString = util.Util.readFile(avscPath)
    genRestricted.stringToFile(inputString, outDirRestricted)
    val source = util.Util.readFile(s"$outDirRestricted/test/avsc/ManyFields.scala")
    source must not contain ("case class")
  }

}
