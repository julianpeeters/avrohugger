package test

import java.io.File

import org.apache.avro.io.{DecoderFactory, EncoderFactory}
import org.apache.avro.generic.{ GenericDatumReader, GenericRecord}
import org.apache.avro.specific.{
  SpecificDatumReader,
  SpecificDatumWriter,
  SpecificRecordBase
}
import org.apache.avro.Schema
import org.apache.avro.file.{ DataFileReader, DataFileWriter }

import org.specs2.mutable.Specification

object SpecificTestUtil extends Specification {

  def write[T <: SpecificRecordBase](file: File, record: T ) = {
    val userDatumWriter = new SpecificDatumWriter[T]
    val dataFileWriter = new DataFileWriter[T](userDatumWriter)
      dataFileWriter.create(record.getSchema(), file);
      dataFileWriter.append(record);
      dataFileWriter.close();
  }

  def writeThenRead[T <: SpecificRecordBase](record: T) = {
    val fileName = s"${record.getClass.getName}"
    val fileEnding = "avro"
    val file = File.createTempFile(fileName, fileEnding)
    file.deleteOnExit()

    write(file, record)

    val dummyRecord = new GenericDatumReader[GenericRecord]
    val schema = new DataFileReader(file, dummyRecord).getSchema
    val userDatumReader = new SpecificDatumReader[T](schema)
    val dataFileReader = new DataFileReader[T](file, userDatumReader)
    dataFileReader.next()
  }

  def verifyWriteAndRead[T <: SpecificRecordBase](record: T) = {
    val sameRecord = writeThenRead(record)
    sameRecord must ===(record)
  }

  def verifyEncodeDecode[T <: SpecificRecordBase](record: T) = {
    val schema = record.getSchema
    val writer = new SpecificDatumWriter[T](schema)
    val out = new java.io.ByteArrayOutputStream()
    val encoder = EncoderFactory.get().binaryEncoder(out, null)
    writer.write(record, encoder)
    encoder.flush
    val ba = out.toByteArray
    ba.size must ===(1)
    ba(0) must ===(0)
    out.close
    val reader = new SpecificDatumReader[T](schema)
    val decoder = DecoderFactory.get().binaryDecoder(ba, null)
    val decoded = reader.read(record, decoder)
    decoded must ===(record)
  }

}
