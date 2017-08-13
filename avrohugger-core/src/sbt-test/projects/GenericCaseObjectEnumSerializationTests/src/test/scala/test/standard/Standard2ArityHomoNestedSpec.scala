
import test._

// Specs2
import org.specs2._
import com.sksamuel.avro4s.RecordFormat

class Standard2ArityHomoNestedSpec extends Specification {
  
  def is = s2"""
    This is a specification to check de/serialization of generated classes with fields of identical types
    
    A case class with more than one `List[List[String]]` field should
      serialize and deserialize correctly $e1
      
    A case class with more than one `List[List[String]]` field should
      serialize and deserialize correctly $e2

    A case class with more than one `Option[List[String]]` field should
      serialize and deserialize correctly $e3
    
    A case class with more than one `Option[List[Int]]` field should
      serialize and deserialize correctly $e4
    
    A case class with more than one `List[Option[String]]` field should
      serialize and deserialize correctly $e5
    
    A case class with more than one `List[Option[Int]]` field should
      serialize and deserialize correctly $e6
    
    A case class with more than one `Option[List[Option[String]]]` field should
      serialize and deserialize correctly $e7
    
    A case class with more than one `Option[List[Option[Int]]]` field should
      serialize and deserialize correctly $e8
    
    A case class with more than one `List[Option[List[String]]]` field should
      serialize and deserialize correctly $e9
    
    A case class with more than one `List[Option[List[Int]]]` field should
      serialize and deserialize correctly $e10
  """

  def e1 = {
    val record1 = AvroTypeProviderTest38(
      List(
        List("hi.bye"),
        List("yay.nay")
      ),
      List(
        List("one.two"),
        List("three.four")
      )
    )
    val record2 = AvroTypeProviderTest38(
      List(
        List("goo.ga"),
        List("caloo.calay")
      ),
      List(
        List("one.two"),
        List("three.four")
      )
    )
    val format = RecordFormat[AvroTypeProviderTest38]
    val records = List(format.to(record1), format.to(record2))
    StandardTestUtil.verifyWriteAndRead(records)
  }



  def e2 = {
      val record1 = AvroTypeProviderTest39(
        List(
          List(1, 2),
          List(3, 4)
        ),
        List(
          List(5, 6),
          List(7, 8)
        )
      )
      val record2 = AvroTypeProviderTest39(
        List(
          List(11, 12),
          List(13, 14)
        ),
        List(
          List(15, 16),
          List(17, 18)
        )
      )
      val format = RecordFormat[AvroTypeProviderTest39]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }

  def e3 = {
      val record1 = AvroTypeProviderTest40(Some(List("up.down")), Some(List("left.right")))
      val record2 = AvroTypeProviderTest40(Some(List("b.a")), Some(List("select.start")))
      val format = RecordFormat[AvroTypeProviderTest40]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }

  def e4 = {
      val record1 = AvroTypeProviderTest41(Some(List(1, 2)), Some(List(3, 4)))
      val record2 = AvroTypeProviderTest41(Some(List(11, 12)), Some(List(13, 14)))
      val format = RecordFormat[AvroTypeProviderTest41]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }

  def e5 = {
      val record1 = AvroTypeProviderTest42(List(None, Some("red")), List(Some("blue"), None))
      val record2 = AvroTypeProviderTest42(List(None, Some("green")), List(Some("yellow"), None))
      val format = RecordFormat[AvroTypeProviderTest42]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }

  def e6 = {
      val record1 = AvroTypeProviderTest43(List(Some(1), None), List(Some(3), None))
      val record2 = AvroTypeProviderTest43(List(Some(6), None), List(Some(8), None))
      val format = RecordFormat[AvroTypeProviderTest43]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }

  def e7 = {
      val record1 = AvroTypeProviderTest44(Some(List(Some("gold"), None)), Some(List(Some("silver"), None)))
      val record2 = AvroTypeProviderTest44(Some(List(Some("copper"), None)), Some(List(Some("bronze"), None)))
      val format = RecordFormat[AvroTypeProviderTest44]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }

  def e8 = {
      val record1 = AvroTypeProviderTest45(Some(List(Some(8), None)), Some(List(Some(10), None)))
      val record2 = AvroTypeProviderTest45(Some(List(Some(9), None)), Some(List(Some(11), None)))
      val format = RecordFormat[AvroTypeProviderTest45]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }

  def e9 = {
      val record1 = AvroTypeProviderTest46(List(None, Some(List(Some("green"), None))), List(None, Some(List(None, Some("yellow")))) )
      val record2 = AvroTypeProviderTest46(List(None, Some(List(Some("orange"), None))), List(None, Some(List(None, Some("purple")))) )
      val format = RecordFormat[AvroTypeProviderTest46]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }

  def e10 = {
      val record1 = AvroTypeProviderTest47(List(None, Some(List(Some(2), None))), List(None, Some(List(None, Some(4)))) )
      val record2 = AvroTypeProviderTest47(List(None, Some(List(Some(7), None))), List(None, Some(List(None, Some(9)))) )
      val format = RecordFormat[AvroTypeProviderTest47]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
/*
Avro4s is used to convert to `GenericRecord` for testing, chokes on `Map`

  A case class with two Map[String, Map[String, Int]] fields should
  serialize and deserialize correctly $e11
  def e11 = {
      val record1 = AvroTypeProviderTestMap10(Map("glory"->Map("kitty"->3)), Map("pride"->Map("doggy"->4)))
      val record2 = AvroTypeProviderTestMap10(Map("sweet"->Map("horsey"->3)), Map("sour"->Map("piggy"->4)))
      val format = RecordFormat[AvroTypeProviderTestMap10]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  */
}
