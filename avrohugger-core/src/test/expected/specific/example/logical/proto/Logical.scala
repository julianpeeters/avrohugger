/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.logical.proto

import scala.annotation.switch

case class Logical(var dec: BigDecimal, var ts: java.time.Instant, var dt: java.time.LocalDate, var uuid: java.util.UUID) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(scala.math.BigDecimal(0), java.time.Instant.now, java.time.LocalDate.now, java.util.UUID.randomUUID)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        val schema = getSchema.getFields().get(field$).schema()
        val decimalType = schema.getLogicalType().asInstanceOf[org.apache.avro.LogicalTypes.Decimal]
        val scale = decimalType.getScale()
        val scaledValue = dec.setScale(scale)
        val bigDecimal = scaledValue.bigDecimal
        Logical.decimalConversion.toBytes(bigDecimal, schema, decimalType)
      }.asInstanceOf[AnyRef]
      case 1 => {
        ts.toEpochMilli
      }.asInstanceOf[AnyRef]
      case 2 => {
        dt.toEpochDay.toInt
      }.asInstanceOf[AnyRef]
      case 3 => {
        uuid.toString
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.dec = {
        value match {
          case (buffer: java.nio.ByteBuffer) => {
            val schema = getSchema.getFields().get(field$).schema()
            val decimalType = schema.getLogicalType().asInstanceOf[org.apache.avro.LogicalTypes.Decimal]
            BigDecimal(Logical.decimalConversion.fromBytes(buffer, schema, decimalType))
          }
        }
      }.asInstanceOf[BigDecimal]
      case 1 => this.ts = {
        value match {
          case (l: Long) => {
            java.time.Instant.ofEpochMilli(l)
          }
        }
      }.asInstanceOf[java.time.Instant]
      case 2 => this.dt = {
        value match {
          case (i: Integer) => {
            java.time.LocalDate.ofEpochDay(i.toInt)
          }
        }
      }.asInstanceOf[java.time.LocalDate]
      case 3 => this.uuid = {
        value match {
          case (str: String) => {
            java.util.UUID.fromString(str)
          }
        }
      }.asInstanceOf[java.util.UUID]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = Logical.SCHEMA$
}

object Logical {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Logical\",\"namespace\":\"example.logical.proto\",\"fields\":[{\"name\":\"dec\",\"type\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":9,\"scale\":2},\"logicalType\":\"decimal\",\"precision\":9,\"scale\":2},{\"name\":\"ts\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}},{\"name\":\"dt\",\"type\":{\"type\":\"int\",\"logicalType\":\"date\"}},{\"name\":\"uuid\",\"type\":{\"type\":\"string\",\"logicalType\":\"uuid\"}}]}")
  val decimalConversion = new org.apache.avro.Conversions.DecimalConversion
}