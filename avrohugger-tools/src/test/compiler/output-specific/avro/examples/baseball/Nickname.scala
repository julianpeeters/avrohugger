/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package avro.examples.baseball

case class Nickname(var name: String) extends org.apache.avro.specific.SpecificRecordBase {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Nickname\",\"namespace\":\"avro.examples.baseball\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"}]}")
  def this() = this("")
  def get(field: Int): AnyRef = {
    field match {
      case pos if pos == 0 => {
        name
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field: Int, value: Any): Unit = {
    field match {
      case pos if pos == 0 => this.name = {
        value match {
          case (value: org.apache.avro.util.Utf8) => value.toString
          case _ => value
        }
      }.asInstanceOf[String]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = SCHEMA$
}