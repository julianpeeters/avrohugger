
import test._

// Specs2
import org.specs2.mutable.Specification

class Specific2ArityHomoNestedSpec extends Specification {

  "A case class with more than one `Vector[Vector[String]]` field" should {
    "serialize and deserialize correctly" in {

      val record1 = AvroTypeProviderTest38(
        Vector(
          Vector("hi.bye"),
          Vector("yay.nay")
        ),
        Vector(
          Vector("one.two"),
          Vector("three.four")
        )
      )
      val record2 = AvroTypeProviderTest38(
        Vector(
          Vector("goo.ga"),
          Vector("caloo.calay")
        ),
        Vector(
          Vector("one.two"),
          Vector("three.four")
        )
      )
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)

    }
  }

  "A case class with more than one `Vector[Vector[String]]` field" should {
    "serialize and deserialize correctly" in {

      val record1 = AvroTypeProviderTest39(
        Vector(
          Vector(1, 2),
          Vector(3, 4)
        ),
        Vector(
          Vector(5, 6),
          Vector(7, 8)
        )
      )
      val record2 = AvroTypeProviderTest39(
        Vector(
          Vector(11, 12),
          Vector(13, 14)
        ),
        Vector(
          Vector(15, 16),
          Vector(17, 18)
        )
      )
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)

    }
  }

  "A case class with more than one `Option[Vector[String]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest40(Some(Vector("up.down")), Some(Vector("left.right")))
      val record2 = AvroTypeProviderTest40(Some(Vector("b.a")), Some(Vector("select.start")))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with more than one `Option[Vector[Int]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest41(Some(Vector(1, 2)), Some(Vector(3, 4)))
      val record2 = AvroTypeProviderTest41(Some(Vector(11, 12)), Some(Vector(13, 14)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with more than one `Vector[Option[String]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest42(Vector(None, Some("red")), Vector(Some("blue"), None))
      val record2 = AvroTypeProviderTest42(Vector(None, Some("green")), Vector(Some("yellow"), None))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with more than one `Vector[Option[Int]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest43(Vector(Some(1), None), Vector(Some(3), None))
      val record2 = AvroTypeProviderTest43(Vector(Some(6), None), Vector(Some(8), None))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with more than one `Option[Vector[Option[String]]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest44(Some(Vector(Some("gold"), None)), Some(Vector(Some("silver"), None)))
      val record2 = AvroTypeProviderTest44(Some(Vector(Some("copper"), None)), Some(Vector(Some("bronze"), None)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with more than one `Option[Vector[Option[Int]]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest45(Some(Vector(Some(8), None)), Some(Vector(Some(10), None)))
      val record2 = AvroTypeProviderTest45(Some(Vector(Some(9), None)), Some(Vector(Some(11), None)))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with more than one `Vector[Option[Vector[String]]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest46(Vector(None, Some(Vector(Some("green"), None))), Vector(None, Some(Vector(None, Some("yellow")))) )
      val record2 = AvroTypeProviderTest46(Vector(None, Some(Vector(Some("orange"), None))), Vector(None, Some(Vector(None, Some("purple")))) )
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }


  "A case class with more than one `Vector[Option[Vector[Int]]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest47(Vector(None, Some(Vector(Some(2), None))), Vector(None, Some(Vector(None, Some(4)))) )
      val record2 = AvroTypeProviderTest47(Vector(None, Some(Vector(Some(7), None))), Vector(None, Some(Vector(None, Some(9)))) )
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
