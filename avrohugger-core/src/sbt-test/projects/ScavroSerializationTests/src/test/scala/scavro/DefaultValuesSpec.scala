package scavro

import org.specs2.mutable.Specification

import org.oedura.scavro.{AvroReader, AvroWriter}

import scavrotest._

class DefaultValuesSpec extends Specification {
  "A case class with default values" should {
    "deserialize correctly" in {

      val filename = "DefaultTest.avro"

      val record = DefaultTest()

      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[DefaultTest](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[DefaultTest] = AvroReader[DefaultTest]
      val sameRecord = reader.read(filename).head

      sameRecord.suit === scavrotest.DefaultEnum.SPADES
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
