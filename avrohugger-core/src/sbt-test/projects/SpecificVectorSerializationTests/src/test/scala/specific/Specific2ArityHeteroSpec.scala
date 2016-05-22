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

  "A case class with a `Vector[String]` field coexisting with a `Vector[Int]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest52(Vector("am.pm"), Vector(5,6))
      val record2 = AvroTypeProviderTest52(Vector("time"), Vector(7,8))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Vector[Int]` field coexisting with a `Vector[String]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest53(Vector(7, 8), Vector("bon.sois"))
      val record2 = AvroTypeProviderTest53(Vector(9, 10), Vector("mon.amis"))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Option[Vector[Option[String]]]` field coexisting with a `Option[Vector[Option[Int]]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest54(Some(Vector(Some("bronco"), None)), Some(Vector(Some(9), None)))
      val record2 = AvroTypeProviderTest54(Some(Vector(Some("bull"), None)), Some(Vector(Some(11), None)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Option[Vector[Option[Int]]]` field coexisting with a `Option[Vector[Option[String]]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest55(Some(Vector(Some(10), None)), Some(Vector(Some("bronca"), None)))
      val record2 = AvroTypeProviderTest55(Some(Vector(Some(12), None)), Some(Vector(Some("cow"), None)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Vector[Option[Vector[Option[String]]]]` field coexisting with a `Vector[Option[Vector[Option[Int]]]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest56(Vector(Some(Vector(Some("tibetan"), None)), None), Vector(Some(Vector(Some(11), None)), None))
      val record2 = AvroTypeProviderTest56(Vector(Some(Vector(Some("nepalese"), None)), None), Vector(Some(Vector(Some(13), None)), None))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Int` field coexisting with a non-`Int` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest57(Vector(Some(Vector(Some(12), None)), None), Vector(Some(Vector(Some("fire"), None)), None))
      val record2 = AvroTypeProviderTest57(Vector(Some(Vector(Some(15), None)), None), Vector(Some(Vector(Some("ice"), None)), None))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }
}
