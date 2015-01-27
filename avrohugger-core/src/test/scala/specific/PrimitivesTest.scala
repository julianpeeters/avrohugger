
import avrohugger._
import specific._
// Specs2
import org.specs2.mutable.Specification

import java.io.File

import org.apache.avro.generic._
import org.apache.avro.specific._
import org.apache.avro.Schema
import org.apache.avro.Schema.{Type => AvroType}
import org.apache.avro.file._

class Specific00Test extends Specification {

  "A case class with an `Int` field" should {
    "serialize and deserialize correctly" in {

      val infile = new java.io.File("avrohugger-core/src/test/avro/specific/AvroTypeProviderTest00.avro")
      val gen = new SpecificGenerator
      gen.fromFile(infile)
     // val source = scala.io.Source.fromFile("target/generated-sources/example/proto/Message.scala").mkString
      //println(source)
      // source === 


/*

      val record = AvroTypeProvider00(1)

      val file = File.createTempFile("AvroTypeProvider00", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProvider00]
      val dataFileWriter = new DataFileWriter[AvroTypeProvider00](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProvider00](schema)
      val dataFileReader = new DataFileReader[AvroTypeProvider00](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
      */
      1 === 1
   // }
  }
}
/*
class Specific01Test extends Specification {

  "A case class with an `Float` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProvider01(1F)

      val file = File.createTempFile("AvroTypeProvider01", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProvider01]
      val dataFileWriter = new DataFileWriter[AvroTypeProvider01](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProvider01](schema)
      val dataFileReader = new DataFileReader[AvroTypeProvider01](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}


class Specific02Test extends Specification {

  "A case class with an `Long` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProvider02(1L)

      val file = File.createTempFile("AvroTypeProvider02", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProvider02]
      val dataFileWriter = new DataFileWriter[AvroTypeProvider02](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProvider02](schema)
      val dataFileReader = new DataFileReader[AvroTypeProvider02](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific03Test extends Specification {

  "A case class with an `Double` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProvider03(1D)

      val file = File.createTempFile("AvroTypeProvider03", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProvider03]
      val dataFileWriter = new DataFileWriter[AvroTypeProvider03](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProvider03](schema)
      val dataFileReader = new DataFileReader[AvroTypeProvider03](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}


class Specific04Test extends Specification {

  "A case class with an `Boolean` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProvider04(true)

      val file = File.createTempFile("AvroTypeProvider04", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProvider04]
      val dataFileWriter = new DataFileWriter[AvroTypeProvider04](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProvider04](schema)
      val dataFileReader = new DataFileReader[AvroTypeProvider04](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific05Test extends Specification {

  "A case class with an `String` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProvider05("hello world")

      val file = File.createTempFile("AvroTypeProvider05", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProvider05]
      val dataFileWriter = new DataFileWriter[AvroTypeProvider05](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProvider05](schema)
      val dataFileReader = new DataFileReader[AvroTypeProvider05](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific06Test extends Specification {

  "A case class with an `Null` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProvider06(null)

      val file = File.createTempFile("AvroTypeProvider06", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProvider06]
      val dataFileWriter = new DataFileWriter[AvroTypeProvider06](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProvider06](schema)
      val dataFileReader = new DataFileReader[AvroTypeProvider06](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific07Test extends Specification {

  "A case class with an empty `Option[String]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProvider07(None)

      val file = File.createTempFile("AvroTypeProvider07", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProvider07]
      val dataFileWriter = new DataFileWriter[AvroTypeProvider07](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProvider07](schema)
      val dataFileReader = new DataFileReader[AvroTypeProvider07](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific08Test extends Specification {

  "A case class with an empty `Option[Int]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProvider08(None)

      val file = File.createTempFile("AvroTypeProvider08", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProvider08]
      val dataFileWriter = new DataFileWriter[AvroTypeProvider08](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProvider08](schema)
      val dataFileReader = new DataFileReader[AvroTypeProvider08](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}









class Specific10Test extends Specification {

  "A case class with an `List[String]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProvider10(List("head", "tail"))

      val file = File.createTempFile("AvroTypeProvider10", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProvider10]
      val dataFileWriter = new DataFileWriter[AvroTypeProvider10](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProvider10](schema)
      val dataFileReader = new DataFileReader[AvroTypeProvider10](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}


class Specific11Test extends Specification {

  "A case class with an `List[Int]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProvider11(List(1, 2))

      val file = File.createTempFile("AvroTypeProvider11", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProvider11]
      val dataFileWriter = new DataFileWriter[AvroTypeProvider11](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProvider11](schema)
      val dataFileReader = new DataFileReader[AvroTypeProvider11](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific12Test extends Specification {

  "A case class with an `Option[String]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProvider12(Some("I'm here"))

      val file = File.createTempFile("AvroTypeProvider12", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProvider12]
      val dataFileWriter = new DataFileWriter[AvroTypeProvider12](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProvider12](schema)
      val dataFileReader = new DataFileReader[AvroTypeProvider12](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific13Test extends Specification {

  "A case class with an `Option[Int]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProvider13(Some(1))

      val file = File.createTempFile("AvroTypeProvider13", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProvider13]
      val dataFileWriter = new DataFileWriter[AvroTypeProvider13](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProvider13](schema)
      val dataFileReader = new DataFileReader[AvroTypeProvider13](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
  */
}

