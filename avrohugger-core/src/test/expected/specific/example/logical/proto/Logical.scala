/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.logical.proto

import scala.annotation.switch

case class Logical(var dec: BigDecimal, var ts: java.time.LocalDateTime, var dt: java.time.LocalDate) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(null, 0L, 0)
  private val conversions: List[org.apache.avro.Conversion[_]] = List(new org.apache.avro.Conversions.DecimalConversion, avrohugger.format.specific.conversions.DateTimeConversion, avrohugger.format.specific.conversions.DateConversion)
  override def getConversion(field$: Int): org.apache.avro.Conversion[_] = {
    conversions(field$)
  }
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        java.nio.ByteBuffer.wrap(dec)
      }.asInstanceOf[AnyRef]
      case 1 => {
        ts
      }.asInstanceOf[AnyRef]
      case 2 => {
        dt
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.dec = {
        value match {
          case (buffer: java.nio.ByteBuffer) => {
            buffer.array()
          }
        }
      }.asInstanceOf[BigDecimal]
      case 1 => this.ts = {
        value
      }.asInstanceOf[java.time.LocalDateTime]
      case 2 => this.dt = {
        value
      }.asInstanceOf[java.time.LocalDate]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = Logical.SCHEMA$
}

object Logical {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Logical\",\"namespace\":\"example.logical.proto\",\"fields\":[{\"name\":\"dec\",\"type\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":9,\"scale\":2},\"logicalType\":\"decimal\",\"precision\":9,\"scale\":2},{\"name\":\"ts\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}},{\"name\":\"dt\",\"type\":{\"type\":\"int\",\"logicalType\":\"date\"}}]}")
}