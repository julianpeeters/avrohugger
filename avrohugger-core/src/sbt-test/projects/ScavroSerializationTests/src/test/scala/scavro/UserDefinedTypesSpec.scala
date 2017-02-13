package scavro

import scavrotest._

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

  "A case class with a field that has a user-defined type" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest61(List(AvroTypeProviderTest00(1), AvroTypeProviderTest00(2)))

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

  "A case class with a field that has a nested user-defined type" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest62(List(AvroTypeProviderTest58(AvroTypeProviderTest00(1)), AvroTypeProviderTest58(AvroTypeProviderTest00(2))))

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

  "A case class with a field that has a nested user-defined type in the second position" should {
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

  "A case class with a field that has a nested user-defined type in the second position" should {
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

  "A case class with a field that has a nested user-defined type in the second position" should {
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

  "A case class with a field that has a nested user-defined type in the second position" should {
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

  "A case class with a field that has a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest68(Some(List(Some(AvroTypeProviderTest00(1)), None)), List(None, Some(AvroTypeProviderTest01(1F))))

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

  "A case class with a field that has a nested user-defined type in the second position" should {
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

  "A case class with a field that has a nested user-defined type in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTestMap13(Map("optional" -> List(List("maybe"))),
        Map("gratis" -> List(List(1.0))))
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

class ScavroDependentRecordTest extends Specification {

  "A case class with fields that are records imported from avdl of a different namespace" should {
    "serialize and deserialize correctly" in {
      val record1 = DependentRecord(alt.ns.model.ExternalDependency(1), 2)
      val record2 = DependentRecord(alt.ns.model.ExternalDependency(3), 4)

      val filename = "import.avro"


      val records = List(record1, record2)
      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[DependentRecord](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[DependentRecord] =
        AvroReader[DependentRecord]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }

  "A case class with fields that are imported enums from avsc" should {
    "serialize and deserialize correctly" in {
      val record1 = DependentRecord2(other.ns.model.Suit.SPADES, "John")
      val record2 = DependentRecord2(other.ns.model.Suit.HEARTS, "Sandy")

      val filename = "import2.avro"


      val records = List(record1, record2)
      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[DependentRecord2](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[DependentRecord2] =
        AvroReader[DependentRecord2]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }

  "A case class with fields that are imported records from avdl in the same namespace" should {
    "serialize and deserialize correctly" in {
      val record1 = DependentRecord3(Embedded(1), true)
      val record2 = DependentRecord3(Embedded(2), false)

      val filename = "import3.avro"


      val records = List(record1, record2)
      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[DependentRecord3](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[DependentRecord3] =
        AvroReader[DependentRecord3]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }

  "A case class with fields that are imported records from avdl in the same namespace" should {
    "serialize and deserialize correctly" in {
      val record1 = DependentRecord4(ComplexExternalDependency(scavromodelv2.NestedRecord(Option(scavromodel.UnionRecord("hurrah")))))
      val record2 = DependentRecord4(ComplexExternalDependency(scavromodelv2.NestedRecord(None)))

      val filename = "import4.avro"


      val records = List(record1, record2)
      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[DependentRecord4](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[DependentRecord4] =
        AvroReader[DependentRecord4]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}



