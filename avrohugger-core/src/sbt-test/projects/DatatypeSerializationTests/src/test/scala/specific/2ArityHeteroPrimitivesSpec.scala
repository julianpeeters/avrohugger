import test._

import org.specs2.mutable.Specification

import org.apache.avro.generic._
import org.apache.avro.specific._
import org.apache.avro.Schema
import org.apache.avro.Schema.{Type => AvroType}
import org.apache.avro.file._

import java.io.File

class Specific48Test extends Specification {

  "A case class with an `Int` field coexisting with a non-`Int` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest48(1, "bonjour")

      val file = File.createTempFile("AvroTypeProviderTest48", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest48]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest48](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest48](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest48](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific49Test extends Specification {

  "A case class with an `String` field coexisting with a non-`Int` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest49("bueno", 2)

      val file = File.createTempFile("AvroTypeProviderTest49", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest49]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest49](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest49](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest49](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific50Test extends Specification {

  "A case class with an `Option[String]` field coexisting with an `Option[Int]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest50(Some("tropics"), Some(3))

      val file = File.createTempFile("AvroTypeProviderTest50", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest50]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest50](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest50](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest50](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific51Test extends Specification {

  "A case class with an `Option[Int]` field coexisting with an `Option[String]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest51(Some(4), Some("level"))

      val file = File.createTempFile("AvroTypeProviderTest51", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest51]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest51](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest51](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest51](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific52Test extends Specification {

  "A case class with a `List[String]` field coexisting with a `List[Int]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest52(List("am", "pm"), List(5,6))

      val file = File.createTempFile("AvroTypeProviderTest52", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest52]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest52](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest52](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest52](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific53Test extends Specification {

  "A case class with an `List[Int]` field coexisting with a `List[String]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest53(List(7, 8), List("bon", "sois"))

      val file = File.createTempFile("AvroTypeProviderTest53", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest53]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest53](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest53](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest53](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific54Test extends Specification {

  "A case class with an `Option[List[Option[String]]]` field coexisting with a `Option[List[Option[Int]]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest54(Some(List(Some("bronco"), None)), Some(List(Some(9), None)))

      val file = File.createTempFile("AvroTypeProviderTest54", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest54]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest54](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest54](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest54](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific55Test extends Specification {

  "A case class with an `Option[List[Option[Int]]]` field coexisting with a `Option[List[Option[String]]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest55(Some(List(Some(10), None)), Some(List(Some("bronca"), None)))

      val file = File.createTempFile("AvroTypeProviderTest55", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest55]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest55](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest55](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest55](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific56Test extends Specification {

  "A case class with an `List[Option[List[Option[String]]]]` field coexisting with a `List[Option[List[Option[Int]]]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest56(List(Some(List(Some("tibetan"), None)), None), List(Some(List(Some(11), None)), None))

      val file = File.createTempFile("AvroTypeProviderTest56", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest56]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest56](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest56](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest56](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific57Test extends Specification {

  "A case class with an `Int` field coexisting with a non-`Int` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest57(List(Some(List(Some(12), None)), None), List(Some(List(Some("fire"), None)), None))

      val file = File.createTempFile("AvroTypeProviderTest57", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest57]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest57](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest57](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest57](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}
