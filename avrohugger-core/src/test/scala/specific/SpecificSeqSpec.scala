package specific

import avrohugger._
import avrohugger.format.SpecificRecord
import avrohugger.format.abstractions.SourceFormat
import org.specs2._

class SpecificSeqSpec extends Specification with SeqSpec {
  override def sourceFormat: SourceFormat = SpecificRecord

  override def expectedOutput(formatType: String, arrayType: String): String =
    s"avrohugger-core/src/test/expected/$formatType/example/idl/array/scala-${avrohugger.internal.ScalaVersion.version}/ArrayAs$arrayType.scala"
}
