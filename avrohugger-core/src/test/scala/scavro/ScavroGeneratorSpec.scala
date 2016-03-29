
import java.io.File

import avrohugger._
import avrohugger.format.Scavro
import org.specs2._

class ScavroGeneratorSpec extends mutable.Specification {

  "a ScavroGenerator" should {

    "correctly generate a case class definition in a package" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/mail.avpr")
      val gen = new Generator(Scavro)
      val outDir = gen.defaultOutputDir + "/scavro/"
      gen.fileToFile(infile, outDir)
      val source = scala.io.Source.fromFile(s"$outDir/example/proto/model/Message.scala").mkString
      source ===
        """|/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
            |package example.proto.model
            |
            |import org.apache.avro.Schema
            |
            |import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}
            |
            |import example.proto.{Message => JMessage}
            |
            |case class Message(to: String, from: String, body: String) extends AvroSerializeable {
            |  type J = JMessage
            |  override def toAvro: JMessage = {
            |    new JMessage(to, from, body)
            |  }
            |}
            |
            |object Message {
            |  implicit def reader = new AvroReader[Message] {
            |    override type J = JMessage
            |  }
            |  implicit val metadata: AvroMetadata[Message, JMessage] = new AvroMetadata[Message, JMessage] {
            |    override val avroClass: Class[JMessage] = classOf[JMessage]
            |    override val schema: Schema = JMessage.getClassSchema()
            |    override val fromAvro: (JMessage) => Message = {
            |      (j: JMessage) => Message(j.getTo.toString, j.getFrom.toString, j.getBody.toString)
            |    }
            |  }
            |}""".stripMargin
    }

    "correctly generate a case class with bytes" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/bytes.avpr")
      val gen = new Generator(Scavro)
      val outDir = gen.defaultOutputDir + "/scavro/"
      gen.fileToFile(infile, outDir)
      val source = scala.io.Source.fromFile(s"$outDir/example/proto/model/Binary.scala").mkString
      source ===
        """|/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
            |package example.proto.model
            |
            |import org.apache.avro.Schema
            |
            |import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}
            |
            |import example.proto.{Binary => JBinary}
            |
            |case class Binary(data: Array[Byte]) extends AvroSerializeable {
            |  type J = JBinary
            |  override def toAvro: JBinary = {
            |    new JBinary(java.nio.ByteBuffer.wrap(data))
            |  }
            |}
            |
            |object Binary {
            |  implicit def reader = new AvroReader[Binary] {
            |    override type J = JBinary
            |  }
            |  implicit val metadata: AvroMetadata[Binary, JBinary] = new AvroMetadata[Binary, JBinary] {
            |    override val avroClass: Class[JBinary] = classOf[JBinary]
            |    override val schema: Schema = JBinary.getClassSchema()
            |    override val fromAvro: (JBinary) => Binary = {
            |      (j: JBinary) => Binary(j.getData match {
            |        case (buffer: java.nio.ByteBuffer) => {
            |          buffer.array()
            |        }
            |      })
            |    }
            |  }
            |}""".stripMargin
    }

  }

}
