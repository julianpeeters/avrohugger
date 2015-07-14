package test

import org.specs2.mutable.Specification

import java.io.File

import org.apache.avro.io.{DecoderFactory, EncoderFactory}
import org.apache.avro.generic.GenericData.Record
import org.apache.avro.generic._
import org.apache.avro.specific._
import org.apache.avro.Schema
import org.apache.avro.Schema.{Type => AvroType}
import org.apache.avro.file._


class EncoderTest extends Specification {



  "A case class " should {
    "serialize and deserialize correctly via encoder and decoder" in {

      val record = AvroTypeProviderTest00(0)
      val schema = AvroTypeProviderTest00.SCHEMA$

      val w = new SpecificDatumWriter[AvroTypeProviderTest00](schema)

      val out = new java.io.ByteArrayOutputStream()
      val encoder = EncoderFactory.get().binaryEncoder(out, null)

      w.write(record, encoder)

      encoder.flush

      val ba = out.toByteArray

      ba.size must ===(1)
      ba(0) must ===(0)

      out.close

      val reader = new SpecificDatumReader[AvroTypeProviderTest00](schema)
   
      val decoder = DecoderFactory.get().binaryDecoder(ba, null)
      val decoded = reader.read(null, decoder)

      decoded must ===(record)

    }
  }





}