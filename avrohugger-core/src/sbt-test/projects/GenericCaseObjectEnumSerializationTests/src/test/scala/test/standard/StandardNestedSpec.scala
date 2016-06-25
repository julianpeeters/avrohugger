import test._

import org.specs2.mutable.Specification
import com.sksamuel.avro4s.RecordFormat

class StandardNestedSpec extends Specification {

  "A case class with a `List[List[String]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest28(List(List("blackbird.grackle")))
      val record2 = AvroTypeProviderTest28(List(List("pheasant.turkey")))
      val format = RecordFormat[AvroTypeProviderTest28]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `List[List[Int]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest29(List(List(1, 2)))
      val record2 = AvroTypeProviderTest29(List(List(3, 4)))
      val format = RecordFormat[AvroTypeProviderTest29]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Option[List[String]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest30(Some(List("starling.oriole")))
      val record2 = AvroTypeProviderTest30(Some(List("buzzard.hawk")))
      val format = RecordFormat[AvroTypeProviderTest30]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Option[List[Int]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest31(Some(List(5, 6)))
      val record2 = AvroTypeProviderTest31(Some(List(7, 8)))
      val format = RecordFormat[AvroTypeProviderTest31]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `List[Option[String]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest32(List(Some("cowbird")))
      val record2 = AvroTypeProviderTest32(List(Some("cuckoo")))
      val format = RecordFormat[AvroTypeProviderTest32]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `List[Option[Int]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest33(List(Some(1)))
      val record2 = AvroTypeProviderTest33(List(Some(2)))
      val format = RecordFormat[AvroTypeProviderTest33]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `Option[List[Option[String]]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest34(Some(List(Some("cowbird"), None)))
      val record2 = AvroTypeProviderTest34(Some(List(Some("lark"), None)))
      val format = RecordFormat[AvroTypeProviderTest34]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `Option[List[Option[Int]]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest35(Some(List(Some(1), None)))
      val record2 = AvroTypeProviderTest35(Some(List(Some(2), None)))
      val format = RecordFormat[AvroTypeProviderTest35]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `List[Option[List[Option[String]]]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest36(List(None, Some(List(Some("cowbird"), None))))
      val record2 = AvroTypeProviderTest36(List(None, Some(List(Some("goldfinch"), None))))
      val format = RecordFormat[AvroTypeProviderTest36]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `List[Option[List[Option[Int]]]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest37(List(None, Some(List(Some(1), None))))
      val record2 = AvroTypeProviderTest37(List(None, Some(List(Some(2), None))))
      val format = RecordFormat[AvroTypeProviderTest37]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }
/*
Avro4s is used to convert to `GenericRecord` for testing, chokes on `Map`

  "A case class with a Map[Int, Map[Int, Int]] field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestMap07(Map("art"->Map("explode"->4)))
      val record2 = AvroTypeProviderTestMap07(Map("science"->Map("define"->4)))
      val format = RecordFormat[AvroTypeProviderTestMap07]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a List[Map[String, Map[Int, String]]] field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestMap08(List(Map("hare"->Map("serpent"->"eagle"))))
      val record2 = AvroTypeProviderTestMap08(List(Map("snake"->Map("bear"->"deer"))))
      val format = RecordFormat[AvroTypeProviderTestMap08]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a Option[Map[String, Option[List[String]]]] field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestMap09(Some(Map("Eje"->None)))
      val record2 = AvroTypeProviderTestMap09(Some(Map("Rayo"->None)))
      val format = RecordFormat[AvroTypeProviderTestMap09]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }
  */
}
