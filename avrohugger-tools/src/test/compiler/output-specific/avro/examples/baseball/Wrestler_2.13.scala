/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package avro.examples.baseball

import scala.annotation.switch

final case class Wrestler(var number: Int, var first_name: String, var last_name: String, var nicknames: Seq[avro.examples.baseball.Mascot]) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(0, "", "", Seq.empty)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        number
      }.asInstanceOf[AnyRef]
      case 1 => {
        first_name
      }.asInstanceOf[AnyRef]
      case 2 => {
        last_name
      }.asInstanceOf[AnyRef]
      case 3 => {
        scala.jdk.CollectionConverters.BufferHasAsJava({
          nicknames map { x =>
            x
          }
        }.toBuffer).asJava
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.number = {
        value
      }.asInstanceOf[Int]
      case 1 => this.first_name = {
        value.toString
      }.asInstanceOf[String]
      case 2 => this.last_name = {
        value.toString
      }.asInstanceOf[String]
      case 3 => this.nicknames = {
        value match {
          case (array: java.util.List[_]) => {
            scala.jdk.CollectionConverters.IteratorHasAsScala(array.iterator).asScala.map({ x =>
              x
            }).toSeq
          }
          case _ => new org.apache.avro.AvroRuntimeException("expected array with type java.util.List[_]")
        }
      }.asInstanceOf[Seq[avro.examples.baseball.Mascot]]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = avro.examples.baseball.Wrestler.SCHEMA$
}

object Wrestler {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Wrestler\",\"namespace\":\"avro.examples.baseball\",\"fields\":[{\"name\":\"number\",\"type\":\"int\"},{\"name\":\"first_name\",\"type\":\"string\"},{\"name\":\"last_name\",\"type\":\"string\"},{\"name\":\"nicknames\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"Mascot\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"}]}}}]}")
}