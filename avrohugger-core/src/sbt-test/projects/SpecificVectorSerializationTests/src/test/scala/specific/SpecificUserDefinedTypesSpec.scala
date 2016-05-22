import test._

// Specs2
import org.specs2.mutable.Specification

class SpecificUserDefinedTypesSpec extends Specification {

  "A case class with another record as a field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest58(AvroTypeProviderTest00(1))
      val record2 = AvroTypeProviderTest58(AvroTypeProviderTest00(2))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Float` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest59(AvroTypeProviderTest58(AvroTypeProviderTest00(1)))
      val record2 = AvroTypeProviderTest59(AvroTypeProviderTest58(AvroTypeProviderTest00(2)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Long` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest60(AvroTypeProviderTest00(1), AvroTypeProviderTest58(AvroTypeProviderTest00(2)))
      val record2 = AvroTypeProviderTest60(AvroTypeProviderTest00(3), AvroTypeProviderTest58(AvroTypeProviderTest00(4)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a field that is list of a user-defined type" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest61(Vector(AvroTypeProviderTest00(1), AvroTypeProviderTest00(2)))
      val record2 = AvroTypeProviderTest61(Vector(AvroTypeProviderTest00(3), AvroTypeProviderTest00(4)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a field that is list of a nested user-defined type" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest62(Vector(AvroTypeProviderTest58(AvroTypeProviderTest00(1)), AvroTypeProviderTest58(AvroTypeProviderTest00(2))))
      val record2 = AvroTypeProviderTest62(Vector(AvroTypeProviderTest58(AvroTypeProviderTest00(3)), AvroTypeProviderTest58(AvroTypeProviderTest00(4))))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }



/* //TODO make readable file for this class - not very urgent since this field type is tested in other contexts also
  "A case class with a field that is list of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest63(Vector(AvroTypeProviderTest00(1), AvroTypeProviderTest00(2)), Vector(AvroTypeProviderTest60(AvroTypeProviderTest00(3), AvroTypeProviderTest58(AvroTypeProviderTest00(2)))))
      val record2 = AvroTypeProviderTest63(Vector(AvroTypeProviderTest00(3), AvroTypeProviderTest00(2)), Vector(AvroTypeProviderTest60(AvroTypeProviderTest00(3), AvroTypeProviderTest58(AvroTypeProviderTest00(2)))))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)

    }
  }
*/

  "A case class with a field that is list of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest64(Some(AvroTypeProviderTest00(1)))
      val record2 = AvroTypeProviderTest64(Some(AvroTypeProviderTest00(2)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a field that is list of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest65(None)
      val record2 = AvroTypeProviderTest65(None)
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a field that is list of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest66(Some(AvroTypeProviderTest58(AvroTypeProviderTest00(1))))
      val record2 = AvroTypeProviderTest66(Some(AvroTypeProviderTest58(AvroTypeProviderTest00(2))))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a field that is list of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest67(Some(AvroTypeProviderTest00(1)), Some(AvroTypeProviderTest60(AvroTypeProviderTest00(4), AvroTypeProviderTest58(AvroTypeProviderTest00(1)))))
      val record2 = AvroTypeProviderTest67(Some(AvroTypeProviderTest00(7)), Some(AvroTypeProviderTest60(AvroTypeProviderTest00(8), AvroTypeProviderTest58(AvroTypeProviderTest00(7)))))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a field that is list of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest68(Some(Vector(Some(AvroTypeProviderTest00(1)), None)), Vector(None, Some(Vector(AvroTypeProviderTest01(1F), AvroTypeProviderTest01(2F)))))
      val record2 = AvroTypeProviderTest68(Some(Vector(Some(AvroTypeProviderTest00(3)), None)), Vector(None, Some(Vector(AvroTypeProviderTest01(3F), AvroTypeProviderTest01(4F)))))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }
  
  "A case object that represents an empty record" should {
    "serialize and deserialize correctly" in {
      val record1 = Reset()
      val records = List(record1)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }
  
}
