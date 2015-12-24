import test._

import org.specs2.mutable.Specification

class SpecificNestedSpec extends Specification {

  "A case class with a `List[List[String]]` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest28(List(List("blackbird.grackle")))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with a `List[List[Int]]` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest29(List(List(1, 2)))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with an `Option[List[String]]` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest30(Some(List("starling.oriole")))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with an `Option[List[Int]]` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest31(Some(List(5, 6)))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with a `List[Option[String]]` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest32(List(Some("cowbird")))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with a `List[Option[Int]]` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest33(List(Some(1)))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with a `Option[List[Option[String]]]` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest34(Some(List(Some("cowbird"), None)))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with a `Option[List[Option[Int]]]` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest35(Some(List(Some(1), None)))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with a `List[Option[List[Option[String]]]]` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest36(List(None, Some(List(Some("cowbird"), None))))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with a `List[Option[List[Option[Int]]]]` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest37(List(None, Some(List(Some(1), None))))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with a Map[Int, Map[Int, Int]] field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTestMap07(Map("art"->Map("explode"->4)))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with a List[Map[String, Map[Int, String]]] field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTestMap08(List(Map("hare"->Map("serpent"->"eagle"))))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }


  "A case class with a Option[Map[String, Option[List[String]]]] field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTestMap09(Some(Map("Eje"->None)))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }
}
