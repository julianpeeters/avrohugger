
import test._

// Specs2
import org.specs2.mutable.Specification

class Specific2ArityHomoNestedSpec extends Specification {

  "A case class with more than one `List[List[String]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest38(
        List(
          List("hi.bye"),
          List("yay.nay")
        ),
        List(
          List("one.two"),
          List("three.four")
        )
      )
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with more than one `List[List[String]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest39(
        List(
          List(1, 2),
          List(3, 4)
        ),
        List(
          List(5, 6),
          List(7, 8)
        )
      )
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with more than one `Option[List[String]]` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest40(Some(List("up.down")), Some(List("left.right")))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with more than one `Option[List[Int]]` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest41(Some(List(1, 2)), Some(List(3, 4)))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with more than one `List[Option[String]]` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest42(List(None, Some("red")), List(Some("blue"), None))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with more than one `List[Option[Int]]` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest43(List(Some(1), None), List(Some(3), None))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with more than one `Option[List[Option[String]]]` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest44(Some(List(Some("gold"), None)), Some(List(Some("silver"), None)))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with more than one `Option[List[Option[Int]]]` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest45(Some(List(Some(8), None)), Some(List(Some(10), None)))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with more than one `List[Option[List[String]]]` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest46(List(None, Some(List(Some("green"), None))), List(None, Some(List(None, Some("yellow")))) )
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }


  "A case class with more than one `List[Option[List[Int]]]` field" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTest47(List(None, Some(List(Some(2), None))), List(None, Some(List(None, Some(4)))) )
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }

  "A case class with two Map[String, Map[String, Int]] fields" should {
    "serialize and deserialize correctly" in {
      val record = AvroTypeProviderTestMap10(Map("glory"->Map("kitty"->3)), Map("pride"->Map("doggy"->4)))
      SpecificTestUtil.verifyWriteAndRead(record)
    }
  }
}
