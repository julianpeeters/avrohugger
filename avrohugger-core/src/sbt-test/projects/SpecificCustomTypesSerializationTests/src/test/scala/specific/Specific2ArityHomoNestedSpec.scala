
import test._

// Specs2
import org.specs2.mutable.Specification

class Specific2ArityHomoNestedSpec extends Specification {

  "A case class with more than one `Array[Array[String]]` field" should {
    "serialize and deserialize correctly" in {

      val record1 = AvroTypeProviderTest38(
        Array(
          Array("hi.bye"),
          Array("yay.nay")
        ),
        Array(
          Array("one.two"),
          Array("three.four")
        )
      )
      val record2 = AvroTypeProviderTest38(
        Array(
          Array("goo.ga"),
          Array("caloo.calay")
        ),
        Array(
          Array("one.two"),
          Array("three.four")
        )
      )
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)

    }
  }

  "A case class with more than one `Array[Array[String]]` field" should {
    "serialize and deserialize correctly" in {

      val record1 = AvroTypeProviderTest39(
        Array(
          Array(1, 2),
          Array(3, 4)
        ),
        Array(
          Array(5, 6),
          Array(7, 8)
        )
      )
      val record2 = AvroTypeProviderTest39(
        Array(
          Array(11, 12),
          Array(13, 14)
        ),
        Array(
          Array(15, 16),
          Array(17, 18)
        )
      )
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)

    }
  }

  "A case class with more than one `Option[Array[String]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest40(Some(Array("up.down")), Some(Array("left.right")))
      val record2 = AvroTypeProviderTest40(Some(Array("b.a")), Some(Array("select.start")))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with more than one `Option[Array[Int]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest41(Some(Array(1, 2)), Some(Array(3, 4)))
      val record2 = AvroTypeProviderTest41(Some(Array(11, 12)), Some(Array(13, 14)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with more than one `Array[Option[String]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest42(Array(None, Some("red")), Array(Some("blue"), None))
      val record2 = AvroTypeProviderTest42(Array(None, Some("green")), Array(Some("yellow"), None))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with more than one `Array[Option[Int]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest43(Array(Some(1), None), Array(Some(3), None))
      val record2 = AvroTypeProviderTest43(Array(Some(6), None), Array(Some(8), None))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with more than one `Option[Array[Option[String]]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest44(Some(Array(Some("gold"), None)), Some(Array(Some("silver"), None)))
      val record2 = AvroTypeProviderTest44(Some(Array(Some("copper"), None)), Some(Array(Some("bronze"), None)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with more than one `Option[Array[Option[Int]]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest45(Some(Array(Some(8), None)), Some(Array(Some(10), None)))
      val record2 = AvroTypeProviderTest45(Some(Array(Some(9), None)), Some(Array(Some(11), None)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with more than one `Array[Option[Array[String]]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest46(Array(None, Some(Array(Some("green"), None))), Array(None, Some(Array(None, Some("yellow")))) )
      val record2 = AvroTypeProviderTest46(Array(None, Some(Array(Some("orange"), None))), Array(None, Some(Array(None, Some("purple")))) )
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }


  "A case class with more than one `Array[Option[Array[Int]]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest47(Array(None, Some(Array(Some(2), None))), Array(None, Some(Array(None, Some(4)))) )
      val record2 = AvroTypeProviderTest47(Array(None, Some(Array(Some(7), None))), Array(None, Some(Array(None, Some(9)))) )
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with two Map[String, Map[String, Int]] fields" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestMap10(Map("glory"->Map("kitty"->3)), Map("pride"->Map("doggy"->4)))
      val record2 = AvroTypeProviderTestMap10(Map("sweet"->Map("horsey"->3)), Map("sour"->Map("piggy"->4)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }
}
