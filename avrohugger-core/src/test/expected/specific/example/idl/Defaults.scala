/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl

import scala.annotation.switch

sealed trait Defaults extends org.apache.avro.specific.SpecificRecordBase with Product with Serializable

final case class Embedded(var inner: Int) extends org.apache.avro.specific.SpecificRecordBase with Defaults {
  def this() = this(0)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        inner
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.inner = {
        value
      }.asInstanceOf[Int]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = Embedded.SCHEMA$
}

final object Embedded {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Embedded\",\"namespace\":\"example.idl\",\"fields\":[{\"name\":\"inner\",\"type\":\"int\"}]}")
}

final case class DefaultTest(var suit: DefaultEnum = DefaultEnum.SPADES, var number: Int = 0, var str: String = "str", var optionString: Option[String] = None, var optionStringValue: Option[String] = Some("default"), var embedded: Embedded = new Embedded(1), var defaultArray: Seq[Int] = Seq(1, 3, 4, 5), var optionalEnum: Option[DefaultEnum] = None, var defaultMap: Map[String, String] = Map("Hello" -> "world", "Merry" -> "Christmas"), var byt: Array[Byte] = Array[Byte](-61, -65)) extends org.apache.avro.specific.SpecificRecordBase with Defaults {
  def this() = this(DefaultEnum.SPADES, 0, "str", None, Some("default"), new Embedded(1), Seq(1, 3, 4, 5), None, Map("Hello" -> "world", "Merry" -> "Christmas"), Array[Byte](-61, -65))
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        suit
      }.asInstanceOf[AnyRef]
      case 1 => {
        number
      }.asInstanceOf[AnyRef]
      case 2 => {
        str
      }.asInstanceOf[AnyRef]
      case 3 => {
        optionString match {
          case Some(x) => x
          case None => null
        }
      }.asInstanceOf[AnyRef]
      case 4 => {
        optionStringValue match {
          case Some(x) => x
          case None => null
        }
      }.asInstanceOf[AnyRef]
      case 5 => {
        embedded
      }.asInstanceOf[AnyRef]
      case 6 => {
        scala.collection.JavaConverters.bufferAsJavaListConverter({
          defaultArray map { x =>
            x
          }
        }.toBuffer).asJava
      }.asInstanceOf[AnyRef]
      case 7 => {
        optionalEnum match {
          case Some(x) => x
          case None => null
        }
      }.asInstanceOf[AnyRef]
      case 8 => {
        val map: java.util.HashMap[String, Any] = new java.util.HashMap[String, Any]
        defaultMap foreach { kvp =>
          val key = kvp._1
          val value = kvp._2
          map.put(key, value)
        }
        map
      }.asInstanceOf[AnyRef]
      case 9 => {
        java.nio.ByteBuffer.wrap(byt)
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.suit = {
        value
      }.asInstanceOf[DefaultEnum]
      case 1 => this.number = {
        value
      }.asInstanceOf[Int]
      case 2 => this.str = {
        value.toString
      }.asInstanceOf[String]
      case 3 => this.optionString = {
        value match {
          case null => None
          case _ => Some(value.toString)
        }
      }.asInstanceOf[Option[String]]
      case 4 => this.optionStringValue = {
        value match {
          case null => None
          case _ => Some(value.toString)
        }
      }.asInstanceOf[Option[String]]
      case 5 => this.embedded = {
        value
      }.asInstanceOf[Embedded]
      case 6 => this.defaultArray = {
        value match {
          case (array: java.util.List[_]) => {
            scala.collection.JavaConverters.asScalaIteratorConverter(array.iterator).asScala.map({ x =>
              x
            }).toSeq
          }
        }
      }.asInstanceOf[Seq[Int]]
      case 7 => this.optionalEnum = {
        value match {
          case null => None
          case _ => Some(value)
        }
      }.asInstanceOf[Option[DefaultEnum]]
      case 8 => this.defaultMap = {
        value match {
          case (map: java.util.Map[_,_]) => {
            scala.collection.JavaConverters.mapAsScalaMapConverter(map).asScala.toMap map { kvp =>
              val key = kvp._1.toString
              val value = kvp._2
              (key, value.toString)
            }
          }
        }
      }.asInstanceOf[Map[String, String]]
      case 9 => this.byt = {
        value match {
          case (buffer: java.nio.ByteBuffer) => {
            buffer.array()
          }
        }
      }.asInstanceOf[Array[Byte]]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = DefaultTest.SCHEMA$
}

final object DefaultTest {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"DefaultTest\",\"namespace\":\"example.idl\",\"fields\":[{\"name\":\"suit\",\"type\":{\"type\":\"enum\",\"name\":\"DefaultEnum\",\"symbols\":[\"SPADES\",\"DIAMONDS\",\"CLUBS\",\"HEARTS\"]},\"default\":\"SPADES\"},{\"name\":\"number\",\"type\":\"int\",\"default\":0},{\"name\":\"str\",\"type\":\"string\",\"default\":\"str\"},{\"name\":\"optionString\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"optionStringValue\",\"type\":[\"string\",\"null\"],\"default\":\"default\"},{\"name\":\"embedded\",\"type\":{\"type\":\"record\",\"name\":\"Embedded\",\"fields\":[{\"name\":\"inner\",\"type\":\"int\"}]},\"default\":{\"inner\":1}},{\"name\":\"defaultArray\",\"type\":{\"type\":\"array\",\"items\":\"int\"},\"default\":[1,3,4,5]},{\"name\":\"optionalEnum\",\"type\":[\"null\",\"DefaultEnum\"],\"default\":null},{\"name\":\"defaultMap\",\"type\":{\"type\":\"map\",\"values\":\"string\"},\"default\":{\"Hello\":\"world\",\"Merry\":\"Christmas\"}},{\"name\":\"byt\",\"type\":\"bytes\",\"default\":\"ÿ\"}]}")
}