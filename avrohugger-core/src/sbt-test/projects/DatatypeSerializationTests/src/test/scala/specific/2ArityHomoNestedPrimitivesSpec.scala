
import test._

// Specs2
import org.specs2.mutable.Specification

import java.io.File

import org.apache.avro.generic._
import org.apache.avro.specific._
import org.apache.avro.Schema
import org.apache.avro.Schema.{Type => AvroType}
import org.apache.avro.file._

class Specific38Test extends Specification {

  "A case class with more than one `List[List[String]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest38(
        List(
          List("hi", "bye"), 
          List("yay", "nay")
        ), 
        List(
          List("one", "two"),
          List("three", "four")
        )
      )

      val file = File.createTempFile("AvroTypeProviderTest38", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest38]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest38](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest38](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest38](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific39Test extends Specification {

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

      val file = File.createTempFile("AvroTypeProviderTest39", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest39]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest39](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest39](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest39](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific40Test extends Specification {

  "A case class with more than one `Option[List[String]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest40(Some(List("up", "down")), Some(List("left", "right")))

      val file = File.createTempFile("AvroTypeProviderTest40", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest40]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest40](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest40](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest40](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific41Test extends Specification {

  "A case class with more than one `Option[List[Int]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest41(Some(List(1, 2)), Some(List(3, 4)))

      val file = File.createTempFile("AvroTypeProviderTest41", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest41]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest41](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest41](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest41](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific42Test extends Specification {

  "A case class with more than one `List[Option[String]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest42(List(None, Some("red")), List(Some("blue"), None))

      val file = File.createTempFile("AvroTypeProviderTest42", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest42]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest42](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest42](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest42](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific43Test extends Specification {

  "A case class with more than one `List[Option[Int]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest43(List(Some(1), None), List(Some(3), None))

      val file = File.createTempFile("AvroTypeProviderTest43", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest43]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest43](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest43](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest43](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific44Test extends Specification {

  "A case class with more than one `Option[List[Option[String]]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest44(Some(List(Some("gold"), None)), Some(List(Some("silver"), None)))

      val file = File.createTempFile("AvroTypeProviderTest44", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest44]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest44](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest44](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest44](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific45Test extends Specification {

  "A case class with more than one `Option[List[Option[Int]]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest45(Some(List(Some(8), None)), Some(List(Some(10), None)))

      val file = File.createTempFile("AvroTypeProviderTest45", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest45]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest45](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest45](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest45](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific46Test extends Specification {

  "A case class with more than one `List[Option[List[String]]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest46(List(None, Some(List(Some("green"), None))), List(None, Some(List(None, Some("yellow")))) )

      val file = File.createTempFile("AvroTypeProviderTest46", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest46]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest46](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest46](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest46](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific47Test extends Specification {

  "A case class with more than one `List[Option[List[Int]]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest47(List(None, Some(List(Some(2), None))), List(None, Some(List(None, Some(4)))) )

      val file = File.createTempFile("AvroTypeProviderTest47", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest47]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest47](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest47](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest47](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class SpecificMap10Test extends Specification {

  "A case class with two Map[String, Map[String, Int]] fields" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTestMap10(Map("glory"->Map("kitty"->3)), Map("pride"->Map("doggy"->4)))

      val file = File.createTempFile("SpecificTestMap10", "avro")
        

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTestMap10]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTestMap10](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTestMap10](AvroTypeProviderTestMap10.SCHEMA$)
      val dataFileReader = new DataFileReader[AvroTypeProviderTestMap10](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}