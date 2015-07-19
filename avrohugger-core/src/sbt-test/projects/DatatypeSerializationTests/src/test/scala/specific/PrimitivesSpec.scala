

// Specs2
import org.specs2.mutable.Specification

import java.io.File

import org.apache.avro.generic._
import org.apache.avro.specific._
//import org.apache.avro.Schema
//import org.apache.avro.Schema.{Type => AvroType}
import org.apache.avro.file._

  import test._


class Specific00Test extends Specification {
  "A generated case class with an `Int` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest00(1)

      val file = File.createTempFile("AvroTypeProviderTest00", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest00]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest00](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest00](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest00](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
      

    }
  }
}

class Specific01Test extends Specification {

  "A generated case class with an `Float` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest01(1F)

      val file = File.createTempFile("AvroTypeProviderTest01", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest01]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest01](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest01](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest01](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}


class Specific02Test extends Specification {

  "A generated case class with an `Long` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest02(1L)

      val file = File.createTempFile("AvroTypeProviderTest02", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest02]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest02](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest02](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest02](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific03Test extends Specification {

  "A generated case class with an `Double` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest03(1D)

      val file = File.createTempFile("AvroTypeProviderTest03", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest03]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest03](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest03](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest03](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}


class Specific04Test extends Specification {

  "A generated case class with an `Boolean` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest04(true)

      val file = File.createTempFile("AvroTypeProviderTest04", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest04]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest04](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest04](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest04](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific05Test extends Specification {

  "A generated case class with an `String` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest05("hello world")

      val file = File.createTempFile("AvroTypeProviderTest05", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest05]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest05](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest05](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest05](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific06Test extends Specification {

  "A generated case class with an `Null` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest06(null)

      val file = File.createTempFile("AvroTypeProviderTest06", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest06]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest06](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest06](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest06](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific07Test extends Specification {

  "A generated case class with an empty `Option[String]` field" should {
    "serialize and deserialize correctly as None" in {

      val record = AvroTypeProviderTest07(None)

      val file = File.createTempFile("AvroTypeProviderTest07", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest07]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest07](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest07](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest07](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific08Test extends Specification {

  "A generated case class with an empty `Option[Int]` field" should {
    "serialize and deserialize correctly as None" in {

      val record = AvroTypeProviderTest08(None)

      val file = File.createTempFile("AvroTypeProviderTest08", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest08]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest08](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest08](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest08](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}









class Specific10Test extends Specification {

  "A generated case class with an `List[String]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest10(List("head", "tail"))

      val file = File.createTempFile("AvroTypeProviderTest10", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest10]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest10](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest10](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest10](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}


class Specific11Test extends Specification {

  "A generated case class with an `List[Int]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest11(List(1, 2))

      val file = File.createTempFile("AvroTypeProviderTest11", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest11]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest11](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest11](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest11](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific12Test extends Specification {

  "A generated case class with an `Option[String]` field" should {
    "serialize and deserialize correctly as `Some`" in {

      val record = AvroTypeProviderTest12(Some("I'm here"))

      val file = File.createTempFile("AvroTypeProviderTest12", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest12]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest12](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest12](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest12](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific13Test extends Specification {

  "A generated case class with an `Option[Int]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest13(Some(1))

      val file = File.createTempFile("AvroTypeProviderTest13", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest13]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest13](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest13](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest13](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
  
}


class SpecificMap01Test extends Specification {

  "A case class with a `Map[String, Int]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTestMap01(Map("bongo"->2))

      val file = File.createTempFile("AvroTypeProviderTestMap01", "avro")
                file.deleteOnExit()


      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTestMap01]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTestMap01](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTestMap01](AvroTypeProviderTestMap01.SCHEMA$)
      val dataFileReader = new DataFileReader[AvroTypeProviderTestMap01](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class SpecificMap02Test extends Specification {

  "A case class with a `Map[String, String]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTestMap02(Map("4"->"four"))

      val file = File.createTempFile("AvroTypeProviderTestMap02", "avro")
                file.deleteOnExit()


      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTestMap02]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTestMap02](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTestMap02](AvroTypeProviderTestMap02.SCHEMA$)
      val dataFileReader = new DataFileReader[AvroTypeProviderTestMap02](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class SpecificMap03Test extends Specification {

  "A case class with a `Map[String, List[Int]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTestMap03(Map("sherpa"->Some(List(5,6))))

      val file = File.createTempFile("AvroTypeProviderTestMap03", "avro")
                file.deleteOnExit()


      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTestMap03]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTestMap03](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTestMap03](AvroTypeProviderTestMap03.SCHEMA$)
      val dataFileReader = new DataFileReader[AvroTypeProviderTestMap03](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}


class SpecificEnum01Test extends Specification {

  "A case class with a `Enumeration` field" should {
    "serialize and deserialize correctly" in {

      val record = Compass(Direction.NORTH)

      val file = File.createTempFile("Compass", "avro")
                file.deleteOnExit()


      val userDatumWriter = new SpecificDatumWriter[Compass]
      val dataFileWriter = new DataFileWriter[Compass](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val userDatumReader = new SpecificDatumReader[Compass](Compass.SCHEMA$)
      val dataFileReader = new DataFileReader[Compass](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}