import test._
import org.specs2.mutable.Specification

class SpecificComplexTest extends Specification {

  "A case class with an empty `Option[String]` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest07(None)
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with an empty `Option[Int]` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest08(None)
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with an `List[String]` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest10(List("head", "tail"))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with an `List[Int]` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest11(List(1, 2))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with an `Option[String]` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest12(Some("I'm here"))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with an `Option[Int]` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest13(Some(1))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with a `Map[String, Int]` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTestMap01(Map("bongo"->2))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with a `Map[String, String]` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTestMap02(Map("4"->"four"))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with a `Map[String, List[Int]]` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTestMap03(Map("sherpa"->Some(List(5,6))))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with an enumeration field" should {
    "serialize and deserialize correctly" in {
      val record = Compass(Direction.NORTH)
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

}
