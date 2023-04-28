/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl.array

import scala.annotation.switch

final case class ArrayIdl(var data: Array[Int]) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(Array.empty)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        scala.collection.JavaConverters.bufferAsJavaListConverter({
          data map { x =>
            x
          }
        }.toBuffer).asJava
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.data = {
        value match {
          case (array: java.util.List[_]) => {
            scala.collection.JavaConverters.asScalaIteratorConverter(array.iterator).asScala.map({ x =>
              x
            }).toArray(scala.reflect.ClassTag(classOf[Int])).asInstanceOf[Array[Int]]
          }
        }
      }.asInstanceOf[Array[Int]]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = ArrayIdl.SCHEMA$
}

object ArrayIdl {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"ArrayIdl\",\"namespace\":\"example.idl.array\",\"fields\":[{\"name\":\"data\",\"type\":{\"type\":\"array\",\"items\":\"int\"}}]}")
}
