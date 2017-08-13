import test._

import org.specs2._
import com.sksamuel.avro4s.RecordFormat

class Standard2ArityHeteroSpec extends Specification {
  
  def is = s2"""
    This is a specification to check de/serialization of generated classes with fields of different types

    A case class with an `Int` field coexisting with a non-`Int` field should
      serialize and deserialize correctly $e1
    
    A case class with an `String` field coexisting with a non-`Int` field should
      serialize and deserialize correctly $e2
      
    A case class with an `Option[String]` field coexisting with an `Option[Int]` field should
      serialize and deserialize correctly $e3
  
    A case class with an `Option[Int]` field coexisting with an `Option[String]` field should
      serialize and deserialize correctly $e4
  
    A case class with a `List[String]` field coexisting with a `List[Int]` field should
      serialize and deserialize correctly $e5
  
    A case class with an `List[Int]` field coexisting with a `List[String]` field should
      serialize and deserialize correctly $e6
  
    A case class with an `Option[List[Option[String]]]` field coexisting with a `Option[List[Option[Int]]]` field should
      serialize and deserialize correctly $e7
  
    A case class with an `Option[List[Option[Int]]]` field coexisting with a `Option[List[Option[String]]]` field should
      serialize and deserialize correctly $e8
  
    A case class with an `List[Option[List[Option[String]]]]` field coexisting with a `List[Option[List[Option[Int]]]]` field should
      serialize and deserialize correctly $e9
                                      
    A case class with an `Int` field coexisting with a non-`Int` field should
      serialize and deserialize correctly $e10
  """
  
  def e1 = {
    val record1 = AvroTypeProviderTest48(1, "bonjour")
    val record2 = AvroTypeProviderTest48(2, "moshi")
    val format = RecordFormat[AvroTypeProviderTest48]
    val records = List(format.to(record1), format.to(record2))
    StandardTestUtil.verifyWriteAndRead(records)
  }

  def e2 = {
    val record1 = AvroTypeProviderTest49("bueno", 2)
    val record2 = AvroTypeProviderTest49("hola", 3)
    val format = RecordFormat[AvroTypeProviderTest49]
    val records = List(format.to(record1), format.to(record2))
    StandardTestUtil.verifyWriteAndRead(records)
  }

  def e3 = {
    val record1 = AvroTypeProviderTest50(Some("tropics"), Some(3))
    val record2 = AvroTypeProviderTest50(Some("equator"), Some(4))
    val format = RecordFormat[AvroTypeProviderTest50]
    val records = List(format.to(record1), format.to(record2))
    StandardTestUtil.verifyWriteAndRead(records)
  }

  def e4 = {
    val record1 = AvroTypeProviderTest51(Some(4), Some("level"))
    val record2 = AvroTypeProviderTest51(Some(5), Some("inclined"))
    val format = RecordFormat[AvroTypeProviderTest51]
    val records = List(format.to(record1), format.to(record2))
    StandardTestUtil.verifyWriteAndRead(records)
  }

  def e5 = {
    val record1 = AvroTypeProviderTest52(List("am.pm"), List(5,6))
    val record2 = AvroTypeProviderTest52(List("time"), List(7,8))
    val format = RecordFormat[AvroTypeProviderTest52]
    val records = List(format.to(record1), format.to(record2))
    StandardTestUtil.verifyWriteAndRead(records)
  }

  def e6 = {
    val record1 = AvroTypeProviderTest53(List(7, 8), List("bon.sois"))
    val record2 = AvroTypeProviderTest53(List(9, 10), List("mon.amis"))
    val format = RecordFormat[AvroTypeProviderTest53]
    val records = List(format.to(record1), format.to(record2))
    StandardTestUtil.verifyWriteAndRead(records)
  }

  def e7 = {
    val record1 = AvroTypeProviderTest54(Some(List(Some("bronco"), None)), Some(List(Some(9), None)))
    val record2 = AvroTypeProviderTest54(Some(List(Some("bull"), None)), Some(List(Some(11), None)))
    val format = RecordFormat[AvroTypeProviderTest54]
    val records = List(format.to(record1), format.to(record2))
    StandardTestUtil.verifyWriteAndRead(records)
  }

  def e8 = {
    val record1 = AvroTypeProviderTest55(Some(List(Some(10), None)), Some(List(Some("bronca"), None)))
    val record2 = AvroTypeProviderTest55(Some(List(Some(12), None)), Some(List(Some("cow"), None)))
    val format = RecordFormat[AvroTypeProviderTest55]
    val records = List(format.to(record1), format.to(record2))
    StandardTestUtil.verifyWriteAndRead(records)
  }

  def e9 = {
    val record1 = AvroTypeProviderTest56(List(Some(List(Some("tibetan"), None)), None), List(Some(List(Some(11), None)), None))
    val record2 = AvroTypeProviderTest56(List(Some(List(Some("nepalese"), None)), None), List(Some(List(Some(13), None)), None))
    val format = RecordFormat[AvroTypeProviderTest56]
    val records = List(format.to(record1), format.to(record2))
    StandardTestUtil.verifyWriteAndRead(records)
  }

  def e10 = {
    val record1 = AvroTypeProviderTest57(List(Some(List(Some(12), None)), None), List(Some(List(Some("fire"), None)), None))
    val record2 = AvroTypeProviderTest57(List(Some(List(Some(15), None)), None), List(Some(List(Some("ice"), None)), None))
    val format = RecordFormat[AvroTypeProviderTest57]
    val records = List(format.to(record1), format.to(record2))
    StandardTestUtil.verifyWriteAndRead(records)
  }
}
