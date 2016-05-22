import test._

import org.specs2.mutable.Specification

class SpecificNestedSpec extends Specification {

  "A case class with a `Vector[Vector[String]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest28(Vector(Vector("blackbird.grackle")))
      val record2 = AvroTypeProviderTest28(Vector(Vector("pheasant.turkey")))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `Vector[Vector[Int]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest29(Vector(Vector(1, 2)))
      val record2 = AvroTypeProviderTest29(Vector(Vector(3, 4)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Option[Vector[String]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest30(Some(Vector("starling.oriole")))
      val record2 = AvroTypeProviderTest30(Some(Vector("buzzard.hawk")))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Option[Vector[Int]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest31(Some(Vector(5, 6)))
      val record2 = AvroTypeProviderTest31(Some(Vector(7, 8)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `Vector[Option[String]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest32(Vector(Some("cowbird")))
      val record2 = AvroTypeProviderTest32(Vector(Some("cuckoo")))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `Vector[Option[Int]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest33(Vector(Some(1)))
      val record2 = AvroTypeProviderTest33(Vector(Some(2)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `Option[Vector[Option[String]]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest34(Some(Vector(Some("cowbird"), None)))
      val record2 = AvroTypeProviderTest34(Some(Vector(Some("lark"), None)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `Option[Vector[Option[Int]]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest35(Some(Vector(Some(1), None)))
      val record2 = AvroTypeProviderTest35(Some(Vector(Some(2), None)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `Vector[Option[Vector[Option[String]]]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest36(Vector(None, Some(Vector(Some("cowbird"), None))))
      val record2 = AvroTypeProviderTest36(Vector(None, Some(Vector(Some("goldfinch"), None))))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `Vector[Option[Vector[Option[Int]]]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest37(Vector(None, Some(Vector(Some(1), None))))
      val record2 = AvroTypeProviderTest37(Vector(None, Some(Vector(Some(2), None))))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a Map[Int, Map[Int, Int]] field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestMap07(Map("art"->Map("explode"->4)))
      val record2 = AvroTypeProviderTestMap07(Map("science"->Map("define"->4)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a Vector[Map[String, Map[Int, String]]] field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestMap08(Vector(Map("hare"->Map("serpent"->"eagle"))))
      val record2 = AvroTypeProviderTestMap08(Vector(Map("snake"->Map("bear"->"deer"))))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a Option[Map[String, Option[Vector[String]]]] field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestMap09(Some(Map("Eje"->None)))
      val record2 = AvroTypeProviderTestMap09(Some(Map("Rayo"->None)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }
  
  "A case class with a Option[Map[String, Option[Vector[Map[String, Option[Vector[String]]]]]]] field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestMap13(Some(Map("Gerboa"->Some(Vector(Map("tail" -> Some(Vector("fat", "long"))))))))
      val record2 = AvroTypeProviderTestMap13(Some(Map("Chinchilla"->None)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }
  
  "A case class with a Option[Map[String, Vector[Option[Vector[Map[String, Option[Vector[Vector[String]]]]]]]]] field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestMap14(Some(Map("lawn"->Vector(Some(Vector(Vector(Map("care" -> Some(Vector("mow", "weed"))))))))))
      val record2 = AvroTypeProviderTestMap14(Some(Map("field"->Vector(None))))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }
  
}
