package test

import java.io.File

import org.apache.avro.file.{ DataFileReader, DataFileWriter }
import org.apache.avro.generic.{
  GenericRecord,
  GenericDatumReader,
  GenericDatumWriter }


import org.specs2.mutable.Specification

object StandardTestUtil extends Specification {

  def write(file: File, records: List[GenericRecord]) = {
  
    val userDatumWriter = new GenericDatumWriter[GenericRecord]
    val dataFileWriter = new DataFileWriter[GenericRecord](userDatumWriter)
    dataFileWriter.create(records.head.getSchema, file);
    records.foreach(record => dataFileWriter.append(record))
    dataFileWriter.close();
   

  }

  def read(file: File, records: List[GenericRecord]) = {
    
    val dummyRecord = new GenericDatumReader[GenericRecord]
    val schema = new DataFileReader(file, dummyRecord).getSchema
    val userDatumReader = new GenericDatumReader[GenericRecord](schema)
    val dataFileReader = new DataFileReader[GenericRecord](file, userDatumReader)
    // Adapted from: https://github.com/tackley/avrohugger-list-issue/blob/master/src/main/scala/net/tackley/Reader.scala
    // This isn't great scala, but represents how org.apache.avro.mapred.AvroInputFormat
    // (via org.apache.avro.file.DataFileStream) interacts with the StandardDatumReader.
    var record: GenericRecord = null.asInstanceOf[GenericRecord]
    var sameRecord: GenericRecord = null.asInstanceOf[GenericRecord]
    val recordIter = records.iterator
    while (dataFileReader.hasNext) {
      sameRecord = dataFileReader.next(sameRecord)
      record = recordIter.next
    }
    dataFileReader.close()
    sameRecord must ===(record)
  
  }

  def verifyWriteAndRead(records: List[GenericRecord]) = {
    val fileName = s"${records.head.getClass.getName}"
    val fileEnding = "avro"
    val file = File.createTempFile(fileName, fileEnding)
    file.deleteOnExit()
    write(file, records)
    read(file, records)
  }
/*
  def verifyEncodeDecode[T <: StandardRecordBase](record: T) = {
    val schema = record.getSchema
    val writer = new StandardDatumWriter[T](schema)
    val out = new java.io.ByteArrayOutputStream()
    val encoder = EncoderFactory.get().binaryEncoder(out, null)
    writer.write(record, encoder)
    encoder.flush
    val ba = out.toByteArray
    ba.size must ===(1)
    ba(0) must ===(0)
    out.close
    val reader = new StandardDatumReader[T](schema)
    val decoder = DecoderFactory.get().binaryDecoder(ba, null)
    val decoded = reader.read(record, decoder)
    decoded must ===(record)
  }
*/
}
