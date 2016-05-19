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

  def write[T <: SpecificRecordBase](file: File, records: List[T]) = {
    val userDatumWriter = new SpecificDatumWriter[T]
    val dataFileWriter = new DataFileWriter[T](userDatumWriter)
    dataFileWriter.create(records.head.getSchema, file);
    records.foreach(record => dataFileWriter.append(record))
    dataFileWriter.close();
  }

  def read[T <: SpecificRecordBase](file: File, records: List[T]) = {
    val dummyRecord = new GenericDatumReader[GenericRecord]
    val schema = new DataFileReader(file, dummyRecord).getSchema
    val userDatumReader = new SpecificDatumReader[T](schema)
    val dataFileReader = new DataFileReader[T](file, userDatumReader)
    // Adapted from: https://github.com/tackley/avrohugger-list-issue/blob/master/src/main/scala/net/tackley/Reader.scala
    // This isn't great scala, but represents how org.apache.avro.mapred.AvroInputFormat
    // (via org.apache.avro.file.DataFileStream) interacts with the SpecificDatumReader.
    var record: T = null.asInstanceOf[T]
    var sameRecord: T = null.asInstanceOf[T]
    val recordIter = records.iterator
    while (dataFileReader.hasNext) {
      sameRecord = dataFileReader.next(sameRecord)
      record = recordIter.next
    }
    dataFileReader.close()
    sameRecord must ===(record)
  }

  def verifyWriteAndRead[T <: SpecificRecordBase](records: List[T]) = {
    val fileName = s"${records.head.getClass.getName}"
    val fileEnding = "avro"
    val file = File.createTempFile(fileName, fileEnding)
    file.deleteOnExit()
    write(file, records)
    read(file, records)
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
