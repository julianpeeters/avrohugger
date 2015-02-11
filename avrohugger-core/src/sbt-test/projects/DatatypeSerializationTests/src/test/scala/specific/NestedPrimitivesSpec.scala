import test._

// Specs2
import org.specs2.mutable.Specification

import java.io.File

import org.apache.avro.generic._
import org.apache.avro.specific._
import org.apache.avro.Schema
import org.apache.avro.Schema.{Type => AvroType}
import org.apache.avro.file._

class Specific28Test extends Specification {

  "A case class with a `List[List[String]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest28(List(List("blackbird", "grackle")))

      val file = File.createTempFile("AvroTypeProviderTest28", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest28]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest28](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest28](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest28](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific29Test extends Specification {

  "A case class with a `List[List[Int]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest29(List(List(1, 2)))

      val file = File.createTempFile("AvroTypeProviderTest29", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest29]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest29](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest29](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest29](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific30Test extends Specification {

  "A case class with an `Option[List[String]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest30(Some(List("starling", "oriole")))

      val file = File.createTempFile("AvroTypeProviderTest30", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest30]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest30](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest30](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest30](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific31Test extends Specification {

  "A case class with an `Option[List[Int]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest31(Some(List(5, 6)))

      val file = File.createTempFile("AvroTypeProviderTest31", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest31]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest31](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest31](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest31](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific32Test extends Specification {

  "A case class with a `List[Option[String]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest32(List(Some("cowbird")))

      val file = File.createTempFile("AvroTypeProviderTest32", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest32]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest32](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest32](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest32](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific33Test extends Specification {

  "A case class with a `List[Option[Int]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest33(List(Some(1)))

      val file = File.createTempFile("AvroTypeProviderTest33", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest33]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest33](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest33](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest33](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}


class Specific34Test extends Specification {

  "A case class with a `Option[List[Option[String]]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest34(Some(List(Some("cowbird"), None)))

      val file = File.createTempFile("AvroTypeProviderTest34", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest34]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest34](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest34](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest34](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific35Test extends Specification {

  "A case class with a `Option[List[Option[Int]]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest35(Some(List(Some(1), None)))

      val file = File.createTempFile("AvroTypeProviderTest35", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest35]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest35](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest35](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest35](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}


class Specific36Test extends Specification {

  "A case class with a `List[Option[List[Option[String]]]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest36(List(None, Some(List(Some("cowbird"), None))))

      val file = File.createTempFile("AvroTypeProviderTest36", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest36]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest36](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest36](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest36](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific37Test extends Specification {

  "A case class with a `List[Option[List[Option[Int]]]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest37(List(None, Some(List(Some(1), None))))

      val file = File.createTempFile("AvroTypeProviderTest37", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest37]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest37](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest37](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest37](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}
