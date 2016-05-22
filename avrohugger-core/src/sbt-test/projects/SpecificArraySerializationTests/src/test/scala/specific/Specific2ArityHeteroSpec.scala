import test._

import org.specs2.mutable.Specification

class Specific2ArityHeteroSpec extends Specification {

  "A case class with an `Int` field coexisting with a non-`Int` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest48(1, "bonjour")
      val record2 = AvroTypeProviderTest48(2, "moshi")
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `String` field coexisting with a non-`Int` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest49("bueno", 2)
      val record2 = AvroTypeProviderTest49("hola", 3)
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Option[String]` field coexisting with an `Option[Int]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest50(Some("tropics"), Some(3))
      val record2 = AvroTypeProviderTest50(Some("equator"), Some(4))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Option[Int]` field coexisting with an `Option[String]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest51(Some(4), Some("level"))
      val record2 = AvroTypeProviderTest51(Some(5), Some("inclined"))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `Array[String]` field coexisting with a `Array[Int]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest52(Array("am.pm"), Array(5,6))
      val record2 = AvroTypeProviderTest52(Array("time"), Array(7,8))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Array[Int]` field coexisting with a `Array[String]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest53(Array(7, 8), Array("bon.sois"))
      val record2 = AvroTypeProviderTest53(Array(9, 10), Array("mon.amis"))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Option[Array[Option[String]]]` field coexisting with a `Option[Array[Option[Int]]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest54(Some(Array(Some("bronco"), None)), Some(Array(Some(9), None)))
      val record2 = AvroTypeProviderTest54(Some(Array(Some("bull"), None)), Some(Array(Some(11), None)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Option[Array[Option[Int]]]` field coexisting with a `Option[Array[Option[String]]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest55(Some(Array(Some(10), None)), Some(Array(Some("bronca"), None)))
      val record2 = AvroTypeProviderTest55(Some(Array(Some(12), None)), Some(Array(Some("cow"), None)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Array[Option[Array[Option[String]]]]` field coexisting with a `Array[Option[Array[Option[Int]]]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest56(Array(Some(Array(Some("tibetan"), None)), None), Array(Some(Array(Some(11), None)), None))
      val record2 = AvroTypeProviderTest56(Array(Some(Array(Some("nepalese"), None)), None), Array(Some(Array(Some(13), None)), None))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Int` field coexisting with a non-`Int` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest57(Array(Some(Array(Some(12), None)), None), Array(Some(Array(Some("fire"), None)), None))
      val record2 = AvroTypeProviderTest57(Array(Some(Array(Some(15), None)), None), Array(Some(Array(Some("ice"), None)), None))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }
}
