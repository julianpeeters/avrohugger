package standard

import avrohugger._
import avrohugger.format.Standard
import avrohugger.format.abstractions.SourceFormat
import org.specs2._

class StandardSeqSpec extends Specification with SeqSpec {
  override def sourceFormat: SourceFormat = Standard

  override def expectedOutput(formatType: String, arrayType: String): String =
    s"avrohugger-core/src/test/expected/$formatType/example/idl/array/ArrayAs$arrayType.scala"
}
