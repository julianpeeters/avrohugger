import test._
import org.specs2.mutable.Specification
import java.io.File
import org.apache.avro.generic.{ GenericDatumReader, GenericRecord}
import org.apache.avro.specific.{
  SpecificDatumReader,
  SpecificDatumWriter,
  SpecificRecordBase
}
import org.apache.avro.file.DataFileReader

import DefaultEnum._

class SpecificDefaultValuesSpec extends Specification {

  "A case class with default values" should {
    "deserialize correctly" in {
      val record = DefaultTest()
      val records = List(record)
      
      val fileName = s"${records.head.getClass.getName}"
      val fileEnding = "avro"
      val file = File.createTempFile(fileName, fileEnding)
      file.deleteOnExit()
      SpecificTestUtil.write(file, records)
      
      val dummyRecord = new GenericDatumReader[GenericRecord]
      val schema = new DataFileReader(file, dummyRecord).getSchema
      val userDatumReader = new SpecificDatumReader[DefaultTest](schema)
      val dataFileReader = new DataFileReader[DefaultTest](file, userDatumReader)
      val sameRecord = dataFileReader.next

      sameRecord.suit === SPADES
      sameRecord.number === 0
      sameRecord.str === "str"
      sameRecord.optionString === None
      sameRecord.optionStringValue === Some("default")
      sameRecord.embedded === Embedded(1)
      sameRecord.defaultArray === List(1,3,4,5)
      sameRecord.optionalEnum === None
      sameRecord.defaultMap === Map("Hello" -> "world", "Merry" -> "Christmas")
      sameRecord.byt === "\u00FF".getBytes
    }
  }

}
