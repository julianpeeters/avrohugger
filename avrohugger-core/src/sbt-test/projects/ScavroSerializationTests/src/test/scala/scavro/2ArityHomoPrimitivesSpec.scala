package scavro

import org.specs2.mutable.Specification

import org.oedura.scavro.{AvroReader, AvroWriter}

import scavrotest._


class Scavro14Test extends Specification {

  "A case class with an `Int` field in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest14(1, 2)

      val filename = "AvroTypeProviderTest14.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest14](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest14] =
        AvroReader[AvroTypeProviderTest14]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro15Test extends Specification {

  "A case class with an `Float` field in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest15(1F, 2F)

      val filename = "AvroTypeProviderTest15.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest15](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest15] =
        AvroReader[AvroTypeProviderTest15]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro16Test extends Specification {

  "A case class with an `Long` field in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest16(1L, 2L)

      val filename = "AvroTypeProviderTest16.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest16](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest16] =
        AvroReader[AvroTypeProviderTest16]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro17Test extends Specification {

  "A case class with an `Double` field in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest17(1D, 2D)

      val filename = "AvroTypeProviderTest17.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest17](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest17] =
        AvroReader[AvroTypeProviderTest17]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro18Test extends Specification {

  "A case class with an `Boolean` field in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest18(true, false)

      val filename = "AvroTypeProviderTest18.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest18](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest18] =
        AvroReader[AvroTypeProviderTest18]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro19Test extends Specification {

  "A case class with an `String` field in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest19("1", "2")

      val filename = "AvroTypeProviderTest19.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest19](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest19] =
        AvroReader[AvroTypeProviderTest19]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro20Test extends Specification {

  "A case class with an `Null` field in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest20(null, null)

      val filename = "AvroTypeProviderTest20.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest20](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest20] =
        AvroReader[AvroTypeProviderTest20]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro24Test extends Specification {

  "A case class with an `List[String]` field in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest24(List("mekka", "lekka", "hi"), List("mekka", "hiney", "ho"))

      val filename = "AvroTypeProviderTest24.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest24](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest24] =
        AvroReader[AvroTypeProviderTest24]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro25Test extends Specification {

  "A case class with an `List[Int]` field in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest25(List(1, 2), List(3,4))

      val filename = "AvroTypeProviderTest25.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest25](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest25] =
        AvroReader[AvroTypeProviderTest25]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro26Test extends Specification {

  "A case class with an `Option[String]` field in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest26(Some("sun"), Some("moon"))

      val filename = "AvroTypeProviderTest26.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest26](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest26] =
        AvroReader[AvroTypeProviderTest26]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro27Test extends Specification {

  "A case class with an `Option[Int]` field in the second position" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest27(Some(1), Some(2))

      val filename = "AvroTypeProviderTest27.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest27](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest27] =
        AvroReader[AvroTypeProviderTest27]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}



class ScavroMap04Test extends Specification {

  "A case class with two Map[Int, Int] fields" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTestMap04(Map("Gorgonzola"->2), Map("Cheddar"->4))

      val filename = "AvroTypeProviderTestMap04.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTestMap04](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTestMap04] =
        AvroReader[AvroTypeProviderTestMap04]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class ScavroMap05Test extends Specification {

  "A case class with two Map[Int, String] fields" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTestMap05(Map("Havana"->"Cuba"), Map("Blue World"->"series"))

      val filename = "AvroTypeProviderTestMap05.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTestMap05](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTestMap05] =
        AvroReader[AvroTypeProviderTestMap05]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class ScavroMap06Test extends Specification {

  "A case class with two Map[String, Option[List[Int]]] fields" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTestMap06(Map("Olala"->Some(List(1,4))), Map("Rumpole"->None))

      val filename = "AvroTypeProviderTestMap06.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTestMap06](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTestMap06] =
        AvroReader[AvroTypeProviderTestMap06]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}
