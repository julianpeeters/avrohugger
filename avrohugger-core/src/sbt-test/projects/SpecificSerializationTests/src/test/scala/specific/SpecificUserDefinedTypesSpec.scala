import test._

// Specs2
import org.specs2.mutable.Specification

class SpecificUserDefinedTypesSpec extends Specification {

  "A case class with another record as a field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest58(AvroTypeProviderTest00(1))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with an `Float` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest59(AvroTypeProviderTest58(AvroTypeProviderTest00(1)))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with an `Long` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest60(AvroTypeProviderTest00(1), AvroTypeProviderTest58(AvroTypeProviderTest00(2)))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with a field that is list of a user-defined type" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest61(List(AvroTypeProviderTest00(1), AvroTypeProviderTest00(2)))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with a field that is list of a nested user-defined type" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest62(List(AvroTypeProviderTest58(AvroTypeProviderTest00(1)), AvroTypeProviderTest58(AvroTypeProviderTest00(2))))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }



/* //TODO make readable file for this class - not very urgent since this field type is tested in other contexts also
  "A case class with a field that is list of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest63(List(AvroTypeProviderTest00(1), AvroTypeProviderTest00(2)), List(AvroTypeProviderTest60(AvroTypeProviderTest00(3), AvroTypeProviderTest58(AvroTypeProviderTest00(2)))))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }
*/

  "A case class with a field that is list of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest64(Some(AvroTypeProviderTest00(1)))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with a field that is list of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest65(None)
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with a field that is list of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest66(Some(AvroTypeProviderTest58(AvroTypeProviderTest00(1))))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with a field that is list of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest67(Some(AvroTypeProviderTest00(1)), Some(AvroTypeProviderTest60(AvroTypeProviderTest00(4), AvroTypeProviderTest58(AvroTypeProviderTest00(1)))))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with a field that is list of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest68(Some(List(Some(AvroTypeProviderTest00(1)), None)), List(None, Some(List(AvroTypeProviderTest01(1F), AvroTypeProviderTest01(2F)))))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }
}
