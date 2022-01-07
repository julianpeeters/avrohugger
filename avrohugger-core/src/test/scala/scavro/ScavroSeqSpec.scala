package scavro

import avrohugger._
import avrohugger.format.Scavro
import avrohugger.format.abstractions.SourceFormat
import org.specs2._

class ScavroSeqSpec extends Specification with SeqSpec {
  override def sourceFormat: SourceFormat = Scavro

  override def actualOutput: String = super.actualOutput.replace("array/ArrayIdl.scala", "array/model/ArrayIdl.scala")

  override def expectedOutput(formatType: String, arrayType: String): String =
    s"avrohugger-core/src/test/expected/$formatType/example/idl/array/ArrayAs$arrayType.scala"
}