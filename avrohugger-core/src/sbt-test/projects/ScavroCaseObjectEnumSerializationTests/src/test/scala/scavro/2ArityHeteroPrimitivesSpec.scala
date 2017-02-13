package scavro

import test.scavro._

import org.specs2.mutable.Specification

import org.oedura.scavro.{AvroReader, AvroWriter}


class Scavro48Test extends Specification {

  "A case class with an `Int` field coexisting with a non-`Int` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest48(1, "bonjour")

      val filename = "AvroTypeProviderTest48.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest48](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest48] =
        AvroReader[AvroTypeProviderTest48]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro49Test extends Specification {

  "A case class with an `String` field coexisting with a non-`Int` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest49("bueno", 2)

      val filename = "AvroTypeProviderTest49.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest49](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest49] =
        AvroReader[AvroTypeProviderTest49]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro50Test extends Specification {

  "A case class with an `Option[String]` field coexisting with an `Option[Int]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest50(Some("tropics"), Some(3))

      val filename = "AvroTypeProviderTest50.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest50](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest50] =
        AvroReader[AvroTypeProviderTest50]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro51Test extends Specification {

  "A case class with an `Option[Int]` field coexisting with an `Option[String]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest51(Some(4), Some("level"))

      val filename = "AvroTypeProviderTest51.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest51](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest51] =
        AvroReader[AvroTypeProviderTest51]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro52Test extends Specification {

  "A case class with a `List[String]` field coexisting with a `List[Int]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest52(List("am", "pm"), List(5,6))

      val filename = "AvroTypeProviderTest52.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest52](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest52] =
        AvroReader[AvroTypeProviderTest52]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro53Test extends Specification {

  "A case class with an `List[Int]` field coexisting with a `List[String]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest53(List(7, 8), List("bon", "sois"))

      val filename = "AvroTypeProviderTest53.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest53](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest53] =
        AvroReader[AvroTypeProviderTest53]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro54Test extends Specification {

  "A case class with an `Option[List[Option[String]]]` field coexisting with a `Option[List[Option[Int]]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest54(Some(List(Some("bronco"), None)), Some(List(Some(9), None)))

      val filename = "AvroTypeProviderTest54.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest54](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest54] =
        AvroReader[AvroTypeProviderTest54]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro55Test extends Specification {

  "A case class with an `Option[List[Option[Int]]]` field coexisting with a `Option[List[Option[String]]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest55(Some(List(Some(10), None)), Some(List(Some("bronca"), None)))

      val filename = "AvroTypeProviderTest55.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest55](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest55] =
        AvroReader[AvroTypeProviderTest55]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro56Test extends Specification {

  "A case class with an `List[Option[List[Option[String]]]]` field coexisting with a `List[Option[List[Option[Int]]]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest56(List(Some(List(Some("tibetan"), None)), None), List(Some(List(Some(11), None)), None))

      val filename = "AvroTypeProviderTest56.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest56](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest56] =
        AvroReader[AvroTypeProviderTest56]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro57Test extends Specification {

  "A case class with an `Int` field coexisting with a non-`Int` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest57(List(Some(List(Some(12), None)), None), List(Some(List(Some("fire"), None)), None))

      val filename = "AvroTypeProviderTest57.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest57](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest57] =
        AvroReader[AvroTypeProviderTest57]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class ScavroMap11Test extends Specification {

  "A case class with two differing nested Map fields" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTestMap11(Map("one"->Map("two"->3)), List(Map("state"->Map("knowledge"->"power"))))

      val filename = "AvroTypeProviderTestMap11.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTestMap11](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTestMap11] =
        AvroReader[AvroTypeProviderTestMap11]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}
