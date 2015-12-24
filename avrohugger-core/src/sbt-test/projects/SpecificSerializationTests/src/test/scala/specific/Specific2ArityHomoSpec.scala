import test._
import org.specs2.mutable.Specification

class Specific2ArityHomoSpec extends Specification {

  "A case class with an `Int` field in the second position" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest14(1, 2)
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with an `Float` field in the second position" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest15(1F, 2F)
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with an `Long` field in the second position" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest16(1L, 2L)
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with an `Double` field in the second position" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest17(1D, 2D)
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with an `Boolean` field in the second position" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest18(true, false)
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with an `String` field in the second position" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest19("1", "2")
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with an `Null` field in the second position" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest20(null, null)
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with an `List[String]` field in the second position" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest24(List("mekka.lekka.hi"), List("mekka.hiney.ho"))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with an `List[Int]` field in the second position" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest25(List(1, 2), List(3,4))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with an `Option[String]` field in the second position" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest26(Some("sun"), Some("moon"))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with an `Option[Int]` field in the second position" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest27(Some(1), Some(2))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with two Map[Int, Int] fields" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTestMap04(Map("Gorgonzola"->2), Map("Cheddar"->4))
      SpecificTestUtil.verifyWriteAndRead(record)

    }
  }

  "A case class with two Map[Int, String] fields" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTestMap05(Map("Havana"->"Cuba"), Map("Blue World"->"series"))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with two Map[String, Option[List[Int]]] fields" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTestMap06(Map("Olala"->Some(List(1,4))), Map("Rumpole"->None))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }
}
