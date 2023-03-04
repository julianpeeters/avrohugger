/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import example.idl.{DefaultEnum => JDefaultEnum, DefaultTest => JDefaultTest, Embedded => JEmbedded}

import scala.jdk.CollectionConverters._

sealed trait Defaults

final object DefaultEnum extends Enumeration with Defaults {
  type DefaultEnum = Value
  val SPADES, DIAMONDS, CLUBS, HEARTS = Value
}

final case class Embedded(inner: Int) extends AvroSerializeable with Defaults {
  type J = JEmbedded
  override def toAvro: JEmbedded = {
    new JEmbedded(inner)
  }
}

final object Embedded {
  implicit def reader = new AvroReader[Embedded] {
    override type J = JEmbedded
  }
  implicit val metadata: AvroMetadata[Embedded, JEmbedded] = new AvroMetadata[Embedded, JEmbedded] {
    override val avroClass: Class[JEmbedded] = classOf[JEmbedded]
    override val schema: Schema = JEmbedded.getClassSchema()
    override val fromAvro: (JEmbedded) => Embedded = {
      (j: JEmbedded) => Embedded(j.getInner.toInt)
    }
  }
}

final case class DefaultTest(suit: DefaultEnum.Value = DefaultEnum.SPADES, number: Int = 0, str: String = "str", optionString: Option[String] = None, optionStringValue: Option[String] = Some("default"), embedded: example.idl.Embedded = new Embedded(1), defaultArray: Array[Int] = Array(1, 3, 4, 5), optionalEnum: Option[DefaultEnum.Value] = None, defaultMap: Map[String, String] = Map("Hello" -> "world", "Merry" -> "Christmas"), byt: Array[Byte] = Array[Byte](-61, -65)) extends AvroSerializeable with Defaults {
  type J = JDefaultTest
  override def toAvro: JDefaultTest = {
    new JDefaultTest(suit match {
      case DefaultEnum.SPADES => JDefaultEnum.SPADES
      case DefaultEnum.DIAMONDS => JDefaultEnum.DIAMONDS
      case DefaultEnum.CLUBS => JDefaultEnum.CLUBS
      case DefaultEnum.HEARTS => JDefaultEnum.HEARTS
    }, number, str, optionString match {
      case Some(x) => x
      case None => null
    }, optionStringValue match {
      case Some(x) => x
      case None => null
    }, embedded.toAvro, {
      val array: java.util.List[java.lang.Integer] = new java.util.ArrayList[java.lang.Integer]
      defaultArray foreach { element =>
        array.add(element)
      }
      array
    }, optionalEnum match {
      case Some(x) => x match {
        case DefaultEnum.SPADES => JDefaultEnum.SPADES
        case DefaultEnum.DIAMONDS => JDefaultEnum.DIAMONDS
        case DefaultEnum.CLUBS => JDefaultEnum.CLUBS
        case DefaultEnum.HEARTS => JDefaultEnum.HEARTS
      }
      case None => null
    }, {
      val map: java.util.Map[CharSequence, CharSequence] = new java.util.HashMap[CharSequence, CharSequence]
      defaultMap foreach { kvp =>
        val key = kvp._1
        val value = kvp._2
        map.put(key, value)
      }
      map
    }, java.nio.ByteBuffer.wrap(byt))
  }
}

final object DefaultTest {
  implicit def reader = new AvroReader[DefaultTest] {
    override type J = JDefaultTest
  }
  implicit val metadata: AvroMetadata[DefaultTest, JDefaultTest] = new AvroMetadata[DefaultTest, JDefaultTest] {
    override val avroClass: Class[JDefaultTest] = classOf[JDefaultTest]
    override val schema: Schema = JDefaultTest.getClassSchema()
    override val fromAvro: (JDefaultTest) => DefaultTest = {
      (j: JDefaultTest) => DefaultTest(j.getSuit match {
        case JDefaultEnum.SPADES => DefaultEnum.SPADES
        case JDefaultEnum.DIAMONDS => DefaultEnum.DIAMONDS
        case JDefaultEnum.CLUBS => DefaultEnum.CLUBS
        case JDefaultEnum.HEARTS => DefaultEnum.HEARTS
      }, j.getNumber.toInt, j.getStr.toString, j.getOptionString match {
        case null => None
        case _ => Some(j.getOptionString.toString)
      }, j.getOptionStringValue match {
        case null => None
        case _ => Some(j.getOptionStringValue.toString)
      }, Embedded.metadata.fromAvro(j.getEmbedded), Array((j.getDefaultArray.asScala: _*)) map { x =>
        x.toInt
      }, j.getOptionalEnum match {
        case null => None
        case _ => Some(j.getOptionalEnum match {
          case JDefaultEnum.SPADES => DefaultEnum.SPADES
          case JDefaultEnum.DIAMONDS => DefaultEnum.DIAMONDS
          case JDefaultEnum.CLUBS => DefaultEnum.CLUBS
          case JDefaultEnum.HEARTS => DefaultEnum.HEARTS
        })
      }, j.getDefaultMap match {
        case (map: java.util.Map[_,_]) => {
          scala.jdk.CollectionConverters.mapAsScalaMapConverter(map).asScala.toMap map { kvp =>
            val key = kvp._1.toString
            val value = kvp._2
            (key, value.toString)
          }
        }
      }, j.getByt match {
        case (buffer: java.nio.ByteBuffer) => {
          buffer.array()
        }
      })
    }
  }
}