
// Specs2
import org.specs2.mutable.Specification

class SpecificNoNamespaceTest extends Specification {

  "A case class with in the default package (i.e. without a namespace)" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTestNoNamespace(1)
      test.SpecificTestUtil.verifyWriteAndRead(record)
    }
  }
}
