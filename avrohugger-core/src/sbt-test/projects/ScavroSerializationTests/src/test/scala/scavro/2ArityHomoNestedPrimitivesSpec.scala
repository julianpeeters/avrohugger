package scavro

import scavrotest._

import org.specs2.mutable.Specification

import java.io.File
import org.oedura.scavro.{AvroReader, AvroWriter}


class Scavro38Test extends Specification {

  "A case class with more than one `List[List[String]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest38(
        List(
          List("hi", "bye"),
          List("yay", "nay")
        ),
        List(
          List("one", "two"),
          List("three", "four")
        )
      )

      val filename = "AvroTypeProviderTest38.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest38](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest38] =
        AvroReader[AvroTypeProviderTest38]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro39Test extends Specification {

  "A case class with more than one `List[List[Int]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest39(
        List(
          List(1, 2),
          List(3, 4)
        ),
        List(
          List(5, 6),
          List(7, 8)
        )
      )

      val filename = "AvroTypeProviderTest39.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest39](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest39] =
        AvroReader[AvroTypeProviderTest39]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro40Test extends Specification {

  "A case class with more than one `Option[List[String]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest40(Some(List("up", "down")), Some(List("left", "right")))

      val filename = "AvroTypeProviderTest40.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest40](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest40] =
        AvroReader[AvroTypeProviderTest40]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro41Test extends Specification {

  "A case class with more than one `Option[List[Int]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest41(Some(List(1, 2)), Some(List(3, 4)))

      val filename = "AvroTypeProviderTest41.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest41](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest41] =
        AvroReader[AvroTypeProviderTest41]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro42Test extends Specification {

  "A case class with more than one `List[Option[String]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest42(List(None, Some("red")), List(Some("blue"), None))

      val filename = "AvroTypeProviderTest42.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest42](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest42] =
        AvroReader[AvroTypeProviderTest42]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro43Test extends Specification {

  "A case class with more than one `List[Option[Int]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest43(List(Some(1), None), List(Some(3), None))

      val filename = "AvroTypeProviderTest43.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest43](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest43] =
        AvroReader[AvroTypeProviderTest43]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro44Test extends Specification {

  "A case class with more than one `Option[List[Option[String]]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest44(Some(List(Some("gold"), None)), Some(List(Some("silver"), None)))

      val filename = "AvroTypeProviderTest44.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest44](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest44] =
        AvroReader[AvroTypeProviderTest44]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro45Test extends Specification {

  "A case class with more than one `Option[List[Option[Int]]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest45(Some(List(Some(8), None)), Some(List(Some(10), None)))

      val filename = "AvroTypeProviderTest45.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest45](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest45] =
        AvroReader[AvroTypeProviderTest45]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro46Test extends Specification {

  "A case class with more than one `List[Option[List[String]]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest46(List(None, Some(List(Some("green"), None))), List(None, Some(List(None, Some("yellow")))) )

      val filename = "AvroTypeProviderTest46.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest46](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest46] =
        AvroReader[AvroTypeProviderTest46]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro47Test extends Specification {

  "A case class with more than one `List[Option[List[Int]]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest47(List(None, Some(List(Some(2), None))), List(None, Some(List(None, Some(4)))) )

      val filename = "AvroTypeProviderTest47.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest47](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest47] =
        AvroReader[AvroTypeProviderTest47]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class ScavroMap10Test extends Specification {

  "A case class with two Map[String, Map[String, Int]] fields" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTestMap10(Map("glory"->Map("kitty"->3)), Map("pride"->Map("doggy"->4)))

      val filename = "AvroTypeProviderTestMap10.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTestMap10](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTestMap10] =
        AvroReader[AvroTypeProviderTestMap10]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}
