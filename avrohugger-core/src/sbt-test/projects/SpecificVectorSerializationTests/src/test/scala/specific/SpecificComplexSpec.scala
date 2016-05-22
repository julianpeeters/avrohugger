import test._
import org.specs2.mutable.Specification

class SpecificComplexTest extends Specification {

  "A case class with an empty `Option[String]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest07(None)
      val record2 = AvroTypeProviderTest07(None)
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an empty `Option[Int]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest08(None)
      val record2 = AvroTypeProviderTest08(None)
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Vector[String]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest10(Vector("head", "tail"))
      val record2 = AvroTypeProviderTest10(Vector("top", "bottom"))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Vector[Int]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest11(Vector(1, 2))
      val record2 = AvroTypeProviderTest11(Vector(3, 4))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Option[String]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest12(Some("I'm here"))
      val record2 = AvroTypeProviderTest12(Some("I'm there"))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Option[Int]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest13(Some(1))
      val record2 = AvroTypeProviderTest13(Some(2))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `Map[String, Int]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestMap01(Map("bongo"->2))
      val record2 = AvroTypeProviderTestMap01(Map("mongo"->3))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `Map[String, String]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestMap02(Map("4"->"four"))
      val record2 = AvroTypeProviderTestMap02(Map("5"->"five"))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `Map[String, Vector[Int]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestMap03(Map("sherpa"->Some(Vector(5,6))))
      val record2 = AvroTypeProviderTestMap03(Map("autobus"->Some(Vector(8,9))))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an enumeration field" should {
    "serialize and deserialize correctly" in {
      val record1 = Compass(Direction.NORTH)
      val record2 = Compass(Direction.SOUTH)
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

}
