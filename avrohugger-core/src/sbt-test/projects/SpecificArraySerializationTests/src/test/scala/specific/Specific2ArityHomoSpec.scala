import test._
import org.specs2.mutable.Specification

class Specific2ArityHomoSpec extends Specification {

  "A case class with an `Int` field in the second position" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest14(1, 2)
      val record2 = AvroTypeProviderTest14(3, 4)
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Float` field in the second position" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest15(1F, 2F)
      val record2 = AvroTypeProviderTest15(3F, 4F)
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Long` field in the second position" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest16(1L, 2L)
      val record2 = AvroTypeProviderTest16(3L, 4L)
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Double` field in the second position" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest17(1D, 2D)
      val record2 = AvroTypeProviderTest17(3D, 4D)
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Boolean` field in the second position" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest18(true, false)
      val record2 = AvroTypeProviderTest18(false, true)
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `String` field in the second position" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest19("1", "2")
      val record2 = AvroTypeProviderTest19("1", "2")
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Null` field in the second position" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest20(null, null)
      val record2 = AvroTypeProviderTest20(null, null)
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Array[String]` field in the second position" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest24(Array("mekka.lekka.hi"), Array("mekka.hiney.ho"))
      val record2 = AvroTypeProviderTest24(Array("time"), Array("travel"))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Array[Int]` field in the second position" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest25(Array(1, 2), Array(3,4))
      val record2 = AvroTypeProviderTest25(Array(5, 6), Array(7,8))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Option[String]` field in the second position" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest26(Some("sun"), Some("moon"))
      val record2 = AvroTypeProviderTest26(Some("day"), Some("night"))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Option[Int]` field in the second position" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest27(Some(1), Some(2))
      val record2 = AvroTypeProviderTest27(Some(3), Some(4))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with two Map[Int, Int] fields" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestMap04(Map("Gorgonzola"->2), Map("Cheddar"->4))
      val record2 = AvroTypeProviderTestMap04(Map("Gouda"->5), Map("Swiss"->6))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with two Map[Int, String] fields" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestMap05(Map("Havana"->"Cuba"), Map("World"->"series"))
      val record2 = AvroTypeProviderTestMap05(Map("Bogota"->"Colombia"), Map("time"->"series"))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with two Map[String, Option[Array[Int]]] fields" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestMap06(Map("Olala"->Some(Array(1,4))), Map("Rumpole"->None))
      val record2 = AvroTypeProviderTestMap06(Map("Cran"->Some(Array(3,5))), Map("Doc"->None))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }
}
