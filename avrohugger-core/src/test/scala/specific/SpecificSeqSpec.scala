package specific

import avrohugger._
import avrohugger.format.SpecificRecord
import avrohugger.format.abstractions.SourceFormat
import org.specs2._

class SpecificSeqSpec extends Specification with SeqSpec {
  override def sourceFormat: SourceFormat = SpecificRecord
}
