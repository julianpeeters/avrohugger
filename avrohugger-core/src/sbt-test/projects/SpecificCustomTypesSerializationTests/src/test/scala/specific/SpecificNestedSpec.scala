import test._

import org.specs2.mutable.Specification

class SpecificNestedSpec extends Specification {

  "A case class with a `Array[Array[String]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest28(Array(Array("blackbird.grackle")))
      val record2 = AvroTypeProviderTest28(Array(Array("pheasant.turkey")))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `Array[Array[Int]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest29(Array(Array(1, 2)))
      val record2 = AvroTypeProviderTest29(Array(Array(3, 4)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Option[Array[String]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest30(Some(Array("starling.oriole")))
      val record2 = AvroTypeProviderTest30(Some(Array("buzzard.hawk")))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Option[Array[Int]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest31(Some(Array(5, 6)))
      val record2 = AvroTypeProviderTest31(Some(Array(7, 8)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `Array[Option[String]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest32(Array(Some("cowbird")))
      val record2 = AvroTypeProviderTest32(Array(Some("cuckoo")))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `Array[Option[Int]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest33(Array(Some(1)))
      val record2 = AvroTypeProviderTest33(Array(Some(2)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `Option[Array[Option[String]]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest34(Some(Array(Some("cowbird"), None)))
      val record2 = AvroTypeProviderTest34(Some(Array(Some("lark"), None)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `Option[Array[Option[Int]]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest35(Some(Array(Some(1), None)))
      val record2 = AvroTypeProviderTest35(Some(Array(Some(2), None)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `Array[Option[Array[Option[String]]]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest36(Array(None, Some(Array(Some("cowbird"), None))))
      val record2 = AvroTypeProviderTest36(Array(None, Some(Array(Some("goldfinch"), None))))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `Array[Option[Array[Option[Int]]]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest37(Array(None, Some(Array(Some(1), None))))
      val record2 = AvroTypeProviderTest37(Array(None, Some(Array(Some(2), None))))
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

  "A case class with a Array[Map[String, Map[Int, String]]] field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestMap08(Array(Map("hare"->Map("serpent"->"eagle"))))
      val record2 = AvroTypeProviderTestMap08(Array(Map("snake"->Map("bear"->"deer"))))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a Option[Map[String, Option[Array[String]]]] field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestMap09(Some(Map("Eje"->None)))
      val record2 = AvroTypeProviderTestMap09(Some(Map("Rayo"->None)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }
  
  "A case class with a Option[Map[String, Option[Array[Map[String, Option[Array[String]]]]]]] field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestMap13(Some(Map("Gerboa"->Some(Array(Map("tail" -> Some(Array("fat", "long"))))))))
      val record2 = AvroTypeProviderTestMap13(Some(Map("Chinchilla"->None)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }
  
  "A case class with a Option[Map[String, Array[Option[Array[Map[String, Option[Array[Array[String]]]]]]]]] field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestMap14(Some(Map("lawn"->Array(Some(Array(Array(Map("care" -> Some(Array("mow", "weed"))))))))))
      val record2 = AvroTypeProviderTestMap14(Some(Map("field"->Array(None))))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }
  
}
