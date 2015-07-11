/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package avro.examples.baseball

case class Player(var number: Int, var first_name: String, var last_name: String, var nicknames: List[Nickname]) extends org.apache.avro.specific.SpecificRecordBase {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Player\",\"namespace\":\"avro.examples.baseball\",\"fields\":[{\"name\":\"number\",\"type\":\"int\"},{\"name\":\"first_name\",\"type\":\"string\"},{\"name\":\"last_name\",\"type\":\"string\"},{\"name\":\"nicknames\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"Nickname\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"}]}}}]}")
  def this() = this(1, "", "", List(new Nickname))
  def get(field: Int): AnyRef = {
    field match {
      case pos if pos == 0 => {
        number
      }.asInstanceOf[AnyRef]
      case pos if pos == 1 => {
        first_name
      }.asInstanceOf[AnyRef]
      case pos if pos == 2 => {
        last_name
      }.asInstanceOf[AnyRef]
      case pos if pos == 3 => {
        java.util.Arrays.asList(({
          nicknames
        }: _*))
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field: Int, value: Any): Unit = {
    field match {
      case pos if pos == 0 => this.number = {
        value
      }.asInstanceOf[Int]
      case pos if pos == 1 => this.first_name = {
        value match {
          case (value: org.apache.avro.util.Utf8) => value.toString
          case _ => value
        }
      }.asInstanceOf[String]
      case pos if pos == 2 => this.last_name = {
        value match {
          case (value: org.apache.avro.util.Utf8) => value.toString
          case _ => value
        }
      }.asInstanceOf[String]
      case pos if pos == 3 => this.nicknames = {
        value match {
          case null => null
          case (array: org.apache.avro.generic.GenericData.Array[_]) => {
            scala.collection.JavaConversions.asScalaIterator(array.iterator).toList
          }
        }
      }.asInstanceOf[List[Nickname]]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = SCHEMA$
}