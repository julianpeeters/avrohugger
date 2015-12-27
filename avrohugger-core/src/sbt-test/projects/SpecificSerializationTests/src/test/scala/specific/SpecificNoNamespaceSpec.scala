import org.specs2.mutable.Specification

class SpecificNoNamespaceTest extends Specification {

  "A case class with in the default package (i.e. without a namespace)" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestNoNamespace(1)
      val record2 = AvroTypeProviderTestNoNamespace(2)
      val records = List(record1, record2)
      test.SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

}
