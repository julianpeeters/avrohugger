/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl

import scala.annotation.switch

case class LogicalIdl(var dec: BigDecimal = scala.math.BigDecimal("8888.88"), var maybeDec: Option[BigDecimal] = Some(scala.math.BigDecimal("9999.99")), var ts: java.time.LocalDateTime = java.time.LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(1526573732000L), java.util.TimeZone.getDefault().toZoneId()), var dt: java.time.LocalDate = java.time.LocalDate.ofEpochDay(600L)) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(scala.math.BigDecimal("8888.88"), Some(scala.math.BigDecimal("9999.99")), java.time.LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(1526573732000L), java.util.TimeZone.getDefault().toZoneId()), java.time.LocalDate.ofEpochDay(600L))
  private val conversions: List[org.apache.avro.Conversion[_]] = List(new org.apache.avro.Conversions.DecimalConversion, new org.apache.avro.Conversions.DecimalConversion, avrohugger.format.specific.conversions.DateTimeConversion, avrohugger.format.specific.conversions.DateConversion)
  override def getConversion(field$: Int): org.apache.avro.Conversion[_] = {
    conversions(field$)
  }
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        java.nio.ByteBuffer.wrap(dec)
      }.asInstanceOf[AnyRef]
      case 1 => {
        maybeDec match {
          case Some(x) => java.nio.ByteBuffer.wrap(x)
          case None => null
        }
      }.asInstanceOf[AnyRef]
      case 2 => {
        ts
      }.asInstanceOf[AnyRef]
      case 3 => {
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
      case 1 => this.maybeDec = {
        value match {
          case null => None
          case _ => Some(value match {
            case (buffer: java.nio.ByteBuffer) => {
              buffer.array()
            }
          })
        }
      }.asInstanceOf[Option[BigDecimal]]
      case 2 => this.ts = {
        value
      }.asInstanceOf[java.time.LocalDateTime]
      case 3 => this.dt = {
        value
      }.asInstanceOf[java.time.LocalDate]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = LogicalIdl.SCHEMA$
}

object LogicalIdl {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"LogicalIdl\",\"namespace\":\"example.idl\",\"fields\":[{\"name\":\"dec\",\"type\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":9,\"scale\":2},\"default\":8888.88},{\"name\":\"maybeDec\",\"type\":[{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":9,\"scale\":2},\"null\"],\"default\":9999.99},{\"name\":\"ts\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"},\"default\":1526573732000},{\"name\":\"dt\",\"type\":{\"type\":\"int\",\"logicalType\":\"date\"},\"default\":600}]}")
}