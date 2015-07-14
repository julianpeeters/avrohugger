
// Specs2
import org.specs2.mutable.Specification

import java.io.File

import org.apache.avro.generic._
import org.apache.avro.specific._
import org.apache.avro.Schema
import org.apache.avro.Schema.{Type => AvroType}
import org.apache.avro.file._
import test._ 


class Specific14Test extends Specification {

  "A case class with an `Int` field in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest14(1, 2)

      val file = File.createTempFile("AvroTypeProviderTest14", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest14]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest14](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest14](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest14](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific15Test extends Specification {

  "A case class with an `Float` field in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest15(1F, 2F)

      val file = File.createTempFile("AvroTypeProviderTest15", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest15]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest15](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest15](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest15](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific16Test extends Specification {

  "A case class with an `Long` field in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest16(1L, 2L)

      val file = File.createTempFile("AvroTypeProviderTest16", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest16]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest16](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest16](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest16](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific17Test extends Specification {

  "A case class with an `Double` field in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest17(1D, 2D)

      val file = File.createTempFile("AvroTypeProviderTest17", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest17]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest17](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest17](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest17](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific18Test extends Specification {

  "A case class with an `Boolean` field in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest18(true, false)

      val file = File.createTempFile("AvroTypeProviderTest18", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest18]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest18](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest18](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest18](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific19Test extends Specification {

  "A case class with an `String` field in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest19("1", "2")

      val file = File.createTempFile("AvroTypeProviderTest19", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest19]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest19](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest19](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest19](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific20Test extends Specification {

  "A case class with an `Null` field in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest20(null, null)

      val file = File.createTempFile("AvroTypeProviderTest20", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest20]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest20](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest20](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest20](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific24Test extends Specification {

  "A case class with an `List[String]` field in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest24(List("mekka", "lekka", "hi"), List("mekka", "hiney", "ho"))

      val file = File.createTempFile("AvroTypeProviderTest24", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest24]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest24](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest24](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest24](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific25Test extends Specification {

  "A case class with an `List[Int]` field in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest25(List(1, 2), List(3,4))

      val file = File.createTempFile("AvroTypeProviderTest25", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest25]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest25](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest25](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest25](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific26Test extends Specification {

  "A case class with an `Option[String]` field in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest26(Some("sun"), Some("moon"))

      val file = File.createTempFile("AvroTypeProviderTest26", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest26]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest26](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest26](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest26](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class Specific27Test extends Specification {

  "A case class with an `Option[Int]` field in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest27(Some(1), Some(2))

      val file = File.createTempFile("AvroTypeProviderTest27", "avro")
        file.deleteOnExit()

      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTest27]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTest27](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val schema = new DataFileReader(file, new GenericDatumReader[GenericRecord]).getSchema
      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTest27](schema)
      val dataFileReader = new DataFileReader[AvroTypeProviderTest27](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}



class SpecificMap04Test extends Specification {

  "A case class with two Map[Int, Int] fields" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTestMap04(Map("Gorgonzola"->2), Map("Cheddar"->4))

      val file = File.createTempFile("AvroTypeProviderTestMap04", "avro")
                file.deleteOnExit()


      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTestMap04]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTestMap04](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTestMap04](AvroTypeProviderTestMap04.SCHEMA$)
      val dataFileReader = new DataFileReader[AvroTypeProviderTestMap04](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class SpecificMap05Test extends Specification {

  "A case class with two Map[Int, String] fields" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTestMap05(Map("Havana"->"Cuba"), Map("Blue World"->"series"))

      val file = File.createTempFile("AvroTypeProviderTestMap05", "avro")
                file.deleteOnExit()


      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTestMap05]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTestMap05](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTestMap05](AvroTypeProviderTestMap05.SCHEMA$)
      val dataFileReader = new DataFileReader[AvroTypeProviderTestMap05](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}

class SpecificMap06Test extends Specification {

  "A case class with two Map[String, Option[List[Int]]] fields" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTestMap06(Map("Olala"->Some(List(1,4))), Map("Rumpole"->None))

      val file = File.createTempFile("AvroTypeProviderTestMap06", "avro")
                file.deleteOnExit()


      val userDatumWriter = new SpecificDatumWriter[AvroTypeProviderTestMap06]
      val dataFileWriter = new DataFileWriter[AvroTypeProviderTestMap06](userDatumWriter)
        dataFileWriter.create(record.getSchema(), file);
        dataFileWriter.append(record);
        dataFileWriter.close();

      val userDatumReader = new SpecificDatumReader[AvroTypeProviderTestMap06](AvroTypeProviderTestMap06.SCHEMA$)
      val dataFileReader = new DataFileReader[AvroTypeProviderTestMap06](file, userDatumReader)
      val sameRecord = dataFileReader.next()

      sameRecord must ===(record)
    }
  }
}