
package test
// Specs2
import org.specs2.mutable.Specification

import java.io.File

import com.gensler.scalavro.types.AvroType
import com.gensler.scalavro.io.AvroTypeIO

import scala.util.{ Try, Success, Failure }

import Direction._

import test._

class RecordSpec extends Specification {
  
  "A generated case class with a record field" should {
    "serialize and deserialize correctly" in {

      val compassIO = AvroType[AvroTypeProviderTest00].io

      val outputStream = new java.io.ByteArrayOutputStream
      val record = AvroTypeProviderTest00(1)
      compassIO.write(record, outputStream)

      val bytes = outputStream.toByteArray
      val inputStream = new java.io.ByteArrayInputStream(bytes)
      val Success(sameRecord) = compassIO.read(inputStream)

      sameRecord must ===(record)

    }
  }
    
    
  
  "A generated case class with an empty record field" should {
    "serialize and deserialize correctly" in {

      val resetIO = AvroType[Reset].io

      val outputStream = new java.io.ByteArrayOutputStream
      val record = Reset()
      resetIO.write(record, outputStream)

      val bytes = outputStream.toByteArray
      val inputStream = new java.io.ByteArrayInputStream(bytes)
      val Success(sameRecord) = resetIO.read(inputStream)

      sameRecord must ===(record)

    }
  }
  
}
