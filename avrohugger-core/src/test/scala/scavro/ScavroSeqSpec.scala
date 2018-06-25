package scavro

import avrohugger._
import avrohugger.format.Scavro
import avrohugger.format.abstractions.SourceFormat
import org.specs2._

class ScavroSeqSpec extends Specification with SeqSpec {
  override def sourceFormat: SourceFormat = Scavro

  override def actualOutput: String = super.actualOutput.replace("array/ArrayIdl.scala", "array/model/ArrayIdl.scala")
}