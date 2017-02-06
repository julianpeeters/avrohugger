import test._

// Specs2
import org.specs2.mutable.Specification

import com.sksamuel.avro4s.RecordFormat

class StandardUserDefinedTypesSpec extends Specification {

  "A case class with another record as a field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest58(AvroTypeProviderTest00(1))
      val record2 = AvroTypeProviderTest58(AvroTypeProviderTest00(2))
      val format = RecordFormat[AvroTypeProviderTest58]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Float` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest59(AvroTypeProviderTest58(AvroTypeProviderTest00(1)))
      val record2 = AvroTypeProviderTest59(AvroTypeProviderTest58(AvroTypeProviderTest00(2)))
      val format = RecordFormat[AvroTypeProviderTest59]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Long` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest60(AvroTypeProviderTest00(1), AvroTypeProviderTest58(AvroTypeProviderTest00(2)))
      val record2 = AvroTypeProviderTest60(AvroTypeProviderTest00(3), AvroTypeProviderTest58(AvroTypeProviderTest00(4)))
      val format = RecordFormat[AvroTypeProviderTest60]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a field that is list of a user-defined type" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest61(List(AvroTypeProviderTest00(1), AvroTypeProviderTest00(2)))
      val record2 = AvroTypeProviderTest61(List(AvroTypeProviderTest00(3), AvroTypeProviderTest00(4)))
      val format = RecordFormat[AvroTypeProviderTest61]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a field that is list of a nested user-defined type" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest62(List(AvroTypeProviderTest58(AvroTypeProviderTest00(1)), AvroTypeProviderTest58(AvroTypeProviderTest00(2))))
      val record2 = AvroTypeProviderTest62(List(AvroTypeProviderTest58(AvroTypeProviderTest00(3)), AvroTypeProviderTest58(AvroTypeProviderTest00(4))))
      val format = RecordFormat[AvroTypeProviderTest62]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }



/* //TODO make readable file for this class - not very urgent since this field type is tested in other contexts also
  "A case class with a field that is list of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest63(List(AvroTypeProviderTest00(1), AvroTypeProviderTest00(2)), List(AvroTypeProviderTest60(AvroTypeProviderTest00(3), AvroTypeProviderTest58(AvroTypeProviderTest00(2)))))
      val record2 = AvroTypeProviderTest63(List(AvroTypeProviderTest00(3), AvroTypeProviderTest00(2)), List(AvroTypeProviderTest60(AvroTypeProviderTest00(3), AvroTypeProviderTest58(AvroTypeProviderTest00(2)))))
      val format = RecordFormat[AvroTypeProviderTest63]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)

    }
  }
*/

  "A case class with a field that is list of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest64(Some(AvroTypeProviderTest00(1)))
      val record2 = AvroTypeProviderTest64(Some(AvroTypeProviderTest00(2)))
      val format = RecordFormat[AvroTypeProviderTest64]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a field that is list of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest65(None)
      val record2 = AvroTypeProviderTest65(None)
      val format = RecordFormat[AvroTypeProviderTest65]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a field that is list of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest66(Some(AvroTypeProviderTest58(AvroTypeProviderTest00(1))))
      val record2 = AvroTypeProviderTest66(Some(AvroTypeProviderTest58(AvroTypeProviderTest00(2))))
      val format = RecordFormat[AvroTypeProviderTest66]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a field that is list of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest67(Some(AvroTypeProviderTest00(1)), Some(AvroTypeProviderTest60(AvroTypeProviderTest00(4), AvroTypeProviderTest58(AvroTypeProviderTest00(1)))))
      val record2 = AvroTypeProviderTest67(Some(AvroTypeProviderTest00(7)), Some(AvroTypeProviderTest60(AvroTypeProviderTest00(8), AvroTypeProviderTest58(AvroTypeProviderTest00(7)))))
      val format = RecordFormat[AvroTypeProviderTest67]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a field that is list of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest68(Some(List(Some(AvroTypeProviderTest00(1)), None)), List(None, Some(List(AvroTypeProviderTest01(1F), AvroTypeProviderTest01(2F)))))
      val record2 = AvroTypeProviderTest68(Some(List(Some(AvroTypeProviderTest00(3)), None)), List(None, Some(List(AvroTypeProviderTest01(3F), AvroTypeProviderTest01(4F)))))
      val format = RecordFormat[AvroTypeProviderTest68]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }
  
  "A case object that represents an empty record" should {
    "serialize and deserialize correctly" in {
      val record1 = Reset()
      val format = RecordFormat[Reset]
      val records = List(format.to(record1))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }
  
  // "A case class with fields that are records imported from avdl of a different namespace" should {
  //   "serialize and deserialize correctly" in {
  //     val record1 = DependentRecord(other.ns.ExternalDependency(1), 2)
  //     val record2 = DependentRecord(other.ns.ExternalDependency(3), 4)
  //     val format = RecordFormat[DependentRecord]
  //     val records = List(format.to(record1), format.to(record2))
  //     StandardTestUtil.verifyWriteAndRead(records)
  //   }
  // }
  // 
  // // Error: could not find implicit value for parameter fromRecord: com.sksamuel.avro4s.FromRecord[test.DependentRecord2]
  // // "A case class with fields that are imported enums from avsc" should {
  // //   "serialize and deserialize correctly" in {
  // //     val record1 = DependentRecord2(other.ns.Suit.SPADES, "John")
  // //     val record2 = DependentRecord2(other.ns.Suit.HEARTS, "Sandy")
  // //     val format = RecordFormat[DependentRecord2]
  // //     val records = List(format.to(record1), format.to(record2))
  // //     StandardTestUtil.verifyWriteAndRead(records)
  // //   }
  // // }
  // 
  // "A case class with fields that are imported records from avdl in the same namespace" should {
  //   "serialize and deserialize correctly" in {
  //     val record1 = DependentRecord3(Embedded(1), true)
  //     val record2 = DependentRecord3(Embedded(2), false)
  //     val format = RecordFormat[DependentRecord3]
  //     val records = List(format.to(record1), format.to(record2))
  //     StandardTestUtil.verifyWriteAndRead(records)
  //   }
  // }
  // 
  // "A case class with fields that are imported records from avdl in the same namespace" should {
  //   "serialize and deserialize correctly" in {
  //     val record1 = DependentRecord4(ComplexExternalDependency(model.v2.NestedRecord(Option(model.UnionRecord("hurrah")))))
  //     val record2 = DependentRecord4(ComplexExternalDependency(model.v2.NestedRecord(None)))
  //     val format = RecordFormat[DependentRecord4]
  //     val records = List(format.to(record1), format.to(record2))
  //     StandardTestUtil.verifyWriteAndRead(records)
  //   }
  // }
  
}
