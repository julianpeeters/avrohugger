import test._
import org.specs2.mutable.Specification

class SpecificPrimitivesSpec extends Specification {

  "A case class with an `Int` field" should {
    "deserialize correctly" in {
      val record = AvroTypeProviderTest00(1)
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with an `Float` field" should {
    "deserialize correctly" in {
      val record = AvroTypeProviderTest01(1F)
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with an `Long` field" should {
    "deserialize correctly" in {
      val record = AvroTypeProviderTest02(1L)
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with an `Double` field" should {
    "deserialize correctly" in {
      val record = AvroTypeProviderTest03(1D)
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with an `Boolean` field" should {
    "deserialize correctly" in {
      val record = AvroTypeProviderTest04(true)
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with an `String` field" should {
    "deserialize correctly" in {
      val record = AvroTypeProviderTest05("hello world")
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with an `Null` field" should {
    "deserialize correctly" in {
      val record = AvroTypeProviderTest06(null)
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

}
