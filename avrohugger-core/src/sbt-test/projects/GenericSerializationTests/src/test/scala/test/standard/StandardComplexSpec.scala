import test._
import org.specs2.mutable.Specification
import com.sksamuel.avro4s.RecordFormat

class StandardComplexTest extends Specification {

  "A case class with an empty `Option[String]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest07(None)
      val record2 = AvroTypeProviderTest07(None)
      val format = RecordFormat[AvroTypeProviderTest07]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an empty `Option[Int]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest08(None)
      val record2 = AvroTypeProviderTest08(None)
      val format = RecordFormat[AvroTypeProviderTest08]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `List[String]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest10(List("head", "tail"))
      val record2 = AvroTypeProviderTest10(List("top", "bottom"))
      val format = RecordFormat[AvroTypeProviderTest10]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `List[Int]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest11(List(1, 2))
      val record2 = AvroTypeProviderTest11(List(3, 4))
      val format = RecordFormat[AvroTypeProviderTest11]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Option[String]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest12(Some("I'm here"))
      val record2 = AvroTypeProviderTest12(Some("I'm there"))
      val format = RecordFormat[AvroTypeProviderTest12]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Option[Int]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest13(Some(1))
      val record2 = AvroTypeProviderTest13(Some(2))
      val format = RecordFormat[AvroTypeProviderTest13]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }
/*
Avro4s is used to convert to `GenericRecord` for testing, chokes on `Map`

  "A case class with a `Map[String, Int]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestMap01(Map("bongo"->2))
      val record2 = AvroTypeProviderTestMap01(Map("mongo"->3))
      val format = RecordFormat[AvroTypeProviderTestMap01]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `Map[String, String]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestMap02(Map("4"->"four"))
      val record2 = AvroTypeProviderTestMap02(Map("5"->"five"))
      val format = RecordFormat[AvroTypeProviderTestMap02]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `Map[String, List[Int]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestMap03(Map("sherpa"->Some(List(5,6))))
      val record2 = AvroTypeProviderTestMap03(Map("autobus"->Some(List(8,9))))
      val format = RecordFormat[AvroTypeProviderTestMap03]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }
  */

  "A case class with an enumeration field" should {
    "serialize and deserialize correctly" in {
      val record1 = Compass(Direction.NORTH)
      val record2 = Compass(Direction.SOUTH)
      val format = RecordFormat[Compass]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

}
