package scavro

import org.specs2.mutable.Specification

import org.oedura.scavro.{AvroReader, AvroWriter}

import test.scavro._

class Scavro00Test extends Specification {
  "A generated case class with an `Int` field" should {
    "serialize and deserialize correctly" in {

      val filename = "AvroTypeProviderTest00.avro"

      val record = AvroTypeProviderTest00(1)

      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest00](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest00] =
        AvroReader[AvroTypeProviderTest00]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)

    }
  }
}

class Scavro01Test extends Specification {

  "A generated case class with an `Float` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest01(1F)

      val filename = "AvroTypeProviderTest01.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest01](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest01] =
        AvroReader[AvroTypeProviderTest01]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}


class Scavro02Test extends Specification {

  "A generated case class with an `Long` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest02(1L)

      val filename = "AvroTypeProviderTest02.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest02](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest02] =
        AvroReader[AvroTypeProviderTest02]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro03Test extends Specification {

  "A generated case class with an `Double` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest03(1D)

      val filename = "AvroTypeProviderTest03.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest03](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest03] =
        AvroReader[AvroTypeProviderTest03]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)

    }
  }
}


class Scavro04Test extends Specification {

  "A generated case class with an `Boolean` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest04(true)

      val filename = "AvroTypeProviderTest04.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest04](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest04] =
        AvroReader[AvroTypeProviderTest04]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)

    }
  }
}

class Scavro05Test extends Specification {

  "A generated case class with an `String` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest05("hello world")

      val filename = "AvroTypeProviderTest05.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest05](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest05] =
        AvroReader[AvroTypeProviderTest05]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)

    }
  }
}

class Scavro06Test extends Specification {

  "A generated case class with an `Null` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest06(null)

      val filename = "AvroTypeProviderTest06.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest06](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest06] =
        AvroReader[AvroTypeProviderTest06]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)

    }
  }
}

class Scavro07Test extends Specification {

  "A generated case class with an empty `Option[String]` field" should {
    "serialize and deserialize correctly as None" in {

      val record = AvroTypeProviderTest07(None)

      val filename = "AvroTypeProviderTest07.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest07](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest07] =
        AvroReader[AvroTypeProviderTest07]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)

    }
  }
}

class Scavro08Test extends Specification {

  "A generated case class with an empty `Option[Int]` field" should {
    "serialize and deserialize correctly as None" in {

      val record = AvroTypeProviderTest08(None)

      val filename = "AvroTypeProviderTest08.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest08](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest08] =
        AvroReader[AvroTypeProviderTest08]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)

    }
  }
}









class Scavro10Test extends Specification {

  "A generated case class with an `List[String]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest10(List("head", "tail"))

      val filename = "AvroTypeProviderTest10.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest10](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest10] =
        AvroReader[AvroTypeProviderTest10]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)

    }
  }
}


class Scavro11Test extends Specification {

  "A generated case class with an `List[Int]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest11(List(1, 2))

      val filename = "AvroTypeProviderTest11.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest11](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest11] =
        AvroReader[AvroTypeProviderTest11]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)

    }
  }
}

class Scavro12Test extends Specification {

  "A generated case class with an `Option[String]` field" should {
    "serialize and deserialize correctly as `Some`" in {

      val record = AvroTypeProviderTest12(Some("I'm here"))

      val filename = "AvroTypeProviderTest12.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest12](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest12] =
        AvroReader[AvroTypeProviderTest12]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)

    }
  }
}

class Scavro13Test extends Specification {

  "A generated case class with an `Option[Int]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest13(Some(1))

      val filename = "AvroTypeProviderTest13.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest13](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest13] =
        AvroReader[AvroTypeProviderTest13]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)

    }
  }

}


class ScavroMap01Test extends Specification {

  "A case class with a `Map[String, Int]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTestMap01(Map("bongo"->2))

      val filename = "AvroTypeProviderTestMap01.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTestMap01](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTestMap01] =
        AvroReader[AvroTypeProviderTestMap01]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)

    }
  }
}

class ScavroMap02Test extends Specification {

  "A case class with a `Map[String, String]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTestMap02(Map("4"->"four"))

      val filename = "AvroTypeProviderTestMap02.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTestMap02](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTestMap02] =
        AvroReader[AvroTypeProviderTestMap02]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)


    }
  }
}





class ScavroMap03Test extends Specification {

  "A case class with a `Map[String, List[Int]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTestMap03(Map("sherpa"->Some(List(5,6))))

      val filename = "AvroTypeProviderTestMap03.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTestMap03](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTestMap03] =
        AvroReader[AvroTypeProviderTestMap03]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)

    }
  }
}


class ScavroEnum01Test extends Specification {

  "An enum" should {
    "serialize and deserialize correctly" in {

      val record = Compass(Direction.NORTH)

      val filename = "Compass.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[Compass](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[Compass] =
        AvroReader[Compass]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)

    }
  }
}

class ScavroEmptyRecordTest extends Specification {

  "An case class that represents an empty record" should {
    "serialize and deserialize correctly" in {

      val record = Reset()

      val filename = "Reset.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[Reset](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[Reset] =
        AvroReader[Reset]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }


}
