import test.model._

// Specs2
import org.specs2.mutable.Specification

import org.oedura.scavro.{AvroReader, AvroWriter}



class Scavro58Test extends Specification {

  "A case class with another record as a field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest58(AvroTypeProviderTest00(1))

      val filename = "AvroTypeProviderTest58.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest58](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest58] =
        AvroReader[AvroTypeProviderTest58]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}
/*
class Scavro59Test extends Specification {

  "A case class with an `Float` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest59(AvroTypeProviderTest58(AvroTypeProviderTest00(1)))

      val filename = "AvroTypeProviderTest59.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest59](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest59] =
        AvroReader[AvroTypeProviderTest59]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}


class Scavro60Test extends Specification {

  "A case class with an `Long` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest60(AvroTypeProviderTest00(1), AvroTypeProviderTest58(AvroTypeProviderTest00(2)))

      val filename = "AvroTypeProviderTest60.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest60](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest60] =
        AvroReader[AvroTypeProviderTest60]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro61Test extends Specification {

  "A case class with a field that is Array of a user-defined type" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest61(Array(AvroTypeProviderTest00(1), AvroTypeProviderTest00(2)))

      val filename = "AvroTypeProviderTest61.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest61](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest61] =
        AvroReader[AvroTypeProviderTest61]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro62Test extends Specification {

  "A case class with a field that is Array of a nested user-defined type" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest62(Array(AvroTypeProviderTest58(AvroTypeProviderTest00(1)), AvroTypeProviderTest58(AvroTypeProviderTest00(2))))

      val filename = "AvroTypeProviderTest62.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest62](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest62] =
        AvroReader[AvroTypeProviderTest62]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}



class Scavro64Test extends Specification {

  "A case class with a field that is Array of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest64(Some(AvroTypeProviderTest00(1)))

      val filename = "AvroTypeProviderTest64.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest64](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest64] =
        AvroReader[AvroTypeProviderTest64]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro65Test extends Specification {

  "A case class with a field that is Array of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest65(None)

      val filename = "AvroTypeProviderTest65.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest65](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest65] =
        AvroReader[AvroTypeProviderTest65]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro66Test extends Specification {

  "A case class with a field that is Array of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest66(Some(AvroTypeProviderTest58(AvroTypeProviderTest00(1))))

      val filename = "AvroTypeProviderTest66.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest66](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest66] =
        AvroReader[AvroTypeProviderTest66]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro67Test extends Specification {

  "A case class with a field that is Array of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest67(Some(AvroTypeProviderTest00(1)), Some(AvroTypeProviderTest60(AvroTypeProviderTest00(4), AvroTypeProviderTest58(AvroTypeProviderTest00(1)))))

      val filename = "AvroTypeProviderTest67.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest67](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest67] =
        AvroReader[AvroTypeProviderTest67]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro68Test extends Specification {

  "A case class with a field that is Array of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest68(Some(Array(Some(AvroTypeProviderTest00(1)), None)), Array(None, Some(Array(AvroTypeProviderTest01(1F), AvroTypeProviderTest01(2F)))))

      val filename = "AvroTypeProviderTest68.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest68](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest68] =
        AvroReader[AvroTypeProviderTest68]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class ScavroMap12Test extends Specification {

  "A case class with a field that is Array of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTestMap12(Map("necessary" -> (Map("a"->AvroTypeProviderTest00(1)))),
        Map("possible" -> AvroTypeProviderTest58(AvroTypeProviderTest00(1))))
      val filename = "AvroTypeProviderTestMap12.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTestMap12](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTestMap12] =
        AvroReader[AvroTypeProviderTestMap12]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class ScavroMap13Test extends Specification {

  "A case class with a field that is Array of a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTestMap13(Map("optional" -> Array(Array("maybe"))),
        Map("gratis" -> Array(Array(1.0))))
      val filename = "AvroTypeProviderTestMap13.avsc"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTestMap13](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTestMap13] =
        AvroReader[AvroTypeProviderTestMap13]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}
*/
