import test._

// Specs2
import org.specs2.mutable.Specification

import java.io.File

import org.apache.avro.generic._
import org.apache.avro.specific._
import org.apache.avro.Schema
import org.apache.avro.Schema.{Type => AvroType}
import org.apache.avro.file._


class Specific58Test extends Specification {

  "A case class with another record as a field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest58(AvroTypeProviderTest00(1))

      val file = File.createTempFile("AvroTypeProviderTest58", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest58]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest58](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest58](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest58](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific59Test extends Specification {

  "A case class with an `Float` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest59(AvroTypeProviderTest58(AvroTypeProviderTest00(1)))

      val file = File.createTempFile("AvroTypeProviderTest59", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest59]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest59](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest59](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest59](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}


class Specific60Test extends Specification {

  "A case class with an `Long` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest60(AvroTypeProviderTest00(1), AvroTypeProviderTest58(AvroTypeProviderTest00(2)))

      val file = File.createTempFile("AvroTypeProviderTest60", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest60]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest60](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest60](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest60](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific61Test extends Specification {

  "A case class with a field that is list of a user-defined type" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest61(List(AvroTypeProviderTest00(1), AvroTypeProviderTest00(2)))

      val file = File.createTempFile("AvroTypeProviderTest61", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest61]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest61](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest61](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest61](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific62Test extends Specification {

  "A case class with a field that is list of a nested user-defined type" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest62(List(AvroTypeProviderTest58(AvroTypeProviderTest00(1)), AvroTypeProviderTest58(AvroTypeProviderTest00(2))))

      val file = File.createTempFile("AvroTypeProviderTest62", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest62]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest62](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest62](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest62](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}



class Specific64Test extends Specification {

  "A case class with a field that is list of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest64(Some(AvroTypeProviderTest00(1)))

      val file = File.createTempFile("AvroTypeProviderTest64", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest64]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest64](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest64](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest64](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific65Test extends Specification {

  "A case class with a field that is list of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest65(None)

      val file = File.createTempFile("AvroTypeProviderTest65", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest65]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest65](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest65](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest65](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific66Test extends Specification {

  "A case class with a field that is list of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest66(Some(AvroTypeProviderTest58(AvroTypeProviderTest00(1))))

      val file = File.createTempFile("AvroTypeProviderTest66", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest66]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest66](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest66](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest66](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific67Test extends Specification {

  "A case class with a field that is list of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest67(Some(AvroTypeProviderTest00(1)), Some(AvroTypeProviderTest60(AvroTypeProviderTest00(4), AvroTypeProviderTest58(AvroTypeProviderTest00(1)))))

      val file = File.createTempFile("AvroTypeProviderTest67", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest67]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest67](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest67](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest67](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific68Test extends Specification {

  "A case class with a field that is list of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest68(Some(List(Some(AvroTypeProviderTest00(1)), None)), List(None, Some(List(AvroTypeProviderTest01(1F), AvroTypeProviderTest01(2F)))))

      val file = File.createTempFile("AvroTypeProviderTest68", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest68]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest68](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest68](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest68](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}
