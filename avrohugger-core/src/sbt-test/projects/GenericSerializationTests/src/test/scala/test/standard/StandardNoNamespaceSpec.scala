import org.specs2.mutable.Specification
import com.sksamuel.avro4s.RecordFormat

class StandardNoNamespaceTest extends Specification {

  "A case class with in the default package (i.e. without a namespace)" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestNoNamespace(1)
      val record2 = AvroTypeProviderTestNoNamespace(2)
      val format = RecordFormat[AvroTypeProviderTestNoNamespace]
      val records = List(format.to(record1), format.to(record2))
      test.StandardTestUtil.verifyWriteAndRead(records)
    }
  }

}
