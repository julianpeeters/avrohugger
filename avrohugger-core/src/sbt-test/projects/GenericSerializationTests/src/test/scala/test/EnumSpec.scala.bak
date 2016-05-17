
package test
// Specs2
import org.specs2.mutable.Specification

import java.io.File

import com.gensler.scalavro.types.AvroType
import com.gensler.scalavro.io.AvroTypeIO

import scala.util.{ Try, Success, Failure }

import Direction._

import test._

class EnumSpec extends Specification {
  "A generated case class with an `scala.Enumeration` field" should {
    "serialize and deserialize correctly" in {

      val compassIO = AvroType[Compass].io

      val outputStream = new java.io.ByteArrayOutputStream
      val record = Compass(Direction.NORTH)
      compassIO.write(record, outputStream)

      val bytes = outputStream.toByteArray
      val inputStream = new java.io.ByteArrayInputStream(bytes)
      val Success(sameRecord) = compassIO.read(inputStream)

      sameRecord must ===(record)

    }
  }
}
