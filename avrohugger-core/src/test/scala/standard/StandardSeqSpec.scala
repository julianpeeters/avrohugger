package standard

import avrohugger._
import avrohugger.format.Standard
import avrohugger.format.abstractions.SourceFormat
import org.specs2._

class StandardSeqSpec extends Specification with SeqSpec {
  override def sourceFormat: SourceFormat = Standard
}
