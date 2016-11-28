import test._
import org.specs2.mutable.Specification
import com.sksamuel.avro4s.RecordFormat

class StandardDefaultValuesSpec extends Specification {
  /*
  Avro4s is used to convert to `GenericRecord` for testing, chokes on `map`
  "A case class with default values" should {
    "deserialize correctly" in {
    
      val record = DefaultTest()
      val records = List(format.to(record))
      
      val fileName = s"${records.head.getClass.getName}"
      val fileEnding = "avro"
      val file = File.createTempFile(fileName, fileEnding)
      file.deleteOnExit()
      SpecificTestUtil.write(file, records)
      
      val sameRecord = reader.read(filename).head
      
      sameRecord.suit === SPADES
      sameRecord.number === 0
      sameRecord.str === "str"
      sameRecord.optionString === None
      sameRecord.optionStringValue === Some("default")
      sameRecord.embedded === Embedded(1)
      sameRecord.defaultArray === Array(1,3,4,5)
      sameRecord.optionalEnum === None
      sameRecord.defaultMap === Map("Hello" -> "world", "Merry" -> "Christmas")
      sameRecord.byt === "\u00FF".getBytes
    }
  }
  */
}
