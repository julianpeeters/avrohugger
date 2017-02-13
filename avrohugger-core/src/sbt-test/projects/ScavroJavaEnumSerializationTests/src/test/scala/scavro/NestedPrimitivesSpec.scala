package scavro

import org.specs2.mutable.Specification

import org.oedura.scavro.{AvroReader, AvroWriter}

import test.scavro._

class Scavro28Test extends Specification {

  "A case class with a `List[List[String]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest28(List(List("blackbird", "grackle")))

      val filename = "AvroTypeProviderTest28.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest28](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest28] =
        AvroReader[AvroTypeProviderTest28]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro29Test extends Specification {

  "A case class with a `List[List[Int]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest29(List(List(1, 2)))

      val filename = "AvroTypeProviderTest29.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest29](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest29] =
        AvroReader[AvroTypeProviderTest29]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro30Test extends Specification {

  "A case class with an `Option[List[String]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest30(Some(List("starling", "oriole")))

      val filename = "AvroTypeProviderTest30.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest30](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest30] =
        AvroReader[AvroTypeProviderTest30]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro31Test extends Specification {

  "A case class with an `Option[List[Int]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest31(Some(List(5, 6)))

     val filename = "AvroTypeProviderTest31.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest31](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest31] =
        AvroReader[AvroTypeProviderTest31]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro32Test extends Specification {

  "A case class with a `List[Option[String]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest32(List(Some("cowbird")))

     val filename = "AvroTypeProviderTest32.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest32](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest32] =
        AvroReader[AvroTypeProviderTest32]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro33Test extends Specification {

  "A case class with a `List[Option[Int]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest33(List(Some(1)))

      val filename = "AvroTypeProviderTest33.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest33](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest33] =
        AvroReader[AvroTypeProviderTest33]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}


class Scavro34Test extends Specification {

  "A case class with a `Option[List[Option[String]]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest34(Some(List(Some("cowbird"), None)))

      val filename = "AvroTypeProviderTest34.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest34](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest34] =
        AvroReader[AvroTypeProviderTest34]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro35Test extends Specification {

  "A case class with a `Option[List[Option[Int]]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest35(Some(List(Some(1), None)))

      val filename = "AvroTypeProviderTest35.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest35](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest35] =
        AvroReader[AvroTypeProviderTest35]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}


class Scavro36Test extends Specification {

  "A case class with a `List[Option[List[Option[String]]]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest36(List(None, Some(List(Some("cowbird"), None))))

      val filename = "AvroTypeProviderTest36.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest36](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest36] =
        AvroReader[AvroTypeProviderTest36]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class Scavro37Test extends Specification {

  "A case class with a `List[Option[List[Option[Int]]]]` field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTest37(List(None, Some(List(Some(1), None))))

      val filename = "AvroTypeProviderTest37.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTest37](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTest37] =
        AvroReader[AvroTypeProviderTest37]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}


class ScavroMap07Test extends Specification {

  "A case class with a Map[Int, Map[Int, Int]] field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTestMap07(Map("art"->Map("explode"->4)))

      val filename = "AvroTypeProviderTestMap07.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTestMap07](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTestMap07] =
        AvroReader[AvroTypeProviderTestMap07]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}


class ScavroMap08Test extends Specification {

  "A case class with a List[Map[String, Map[Int, String]]] field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTestMap08(List(Map("hare"->Map("serpent"->"eagle"))))

      val filename = "AvroTypeProviderTestMap08.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTestMap08](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTestMap08] =
        AvroReader[AvroTypeProviderTestMap08]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}

class ScavroMap09Test extends Specification {

  "A case class with a Option[Map[String, Option[List[String]]]] field" should {
    "serialize and deserialize correctly" in {

      val record = AvroTypeProviderTestMap09(Some(Map("Eje"->None)))

      val filename = "AvroTypeProviderTestMap09.avro"
      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[AvroTypeProviderTestMap09](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[AvroTypeProviderTestMap09] =
        AvroReader[AvroTypeProviderTestMap09]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)
    }
  }
}
