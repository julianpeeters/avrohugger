/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.logical

import scala.annotation.switch

final case class LogicalSql(var data: BigDecimal, var ts: java.sql.Timestamp, var dt: java.sql.Date, var dataBig: BigDecimal) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(scala.math.BigDecimal(0), new java.sql.Timestamp(0L), new java.sql.Date(0L), scala.math.BigDecimal(0))
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        val schema = getSchema.getFields().get(field$).schema()
        val decimalType = schema.getLogicalType().asInstanceOf[org.apache.avro.LogicalTypes.Decimal]
        val scale = decimalType.getScale()
        val scaledValue = data.setScale(scale)
        val bigDecimal = scaledValue.bigDecimal
        LogicalSql.decimalConversion.toBytes(bigDecimal, schema, decimalType)
      }.asInstanceOf[AnyRef]
      case 1 => {
        ts.getTime()
      }.asInstanceOf[AnyRef]
      case 2 => {
        dt.getTime()./(86400000)
      }.asInstanceOf[AnyRef]
      case 3 => {
        val schema = getSchema.getFields().get(field$).schema()
        val decimalType = schema.getLogicalType().asInstanceOf[org.apache.avro.LogicalTypes.Decimal]
        val scale = decimalType.getScale()
        val scaledValue = dataBig.setScale(scale)
        val bigDecimal = scaledValue.bigDecimal
        LogicalSql.decimalConversion.toBytes(bigDecimal, schema, decimalType)
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.data = {
        value match {
          case (buffer: java.nio.ByteBuffer) => {
            val schema = getSchema.getFields().get(field$).schema()
            val decimalType = schema.getLogicalType().asInstanceOf[org.apache.avro.LogicalTypes.Decimal]
            BigDecimal(LogicalSql.decimalConversion.fromBytes(buffer, schema, decimalType))
          }
        }
      }.asInstanceOf[BigDecimal]
      case 1 => this.ts = {
        value match {
          case (l: Long) => {
            new java.sql.Timestamp(l)
          }
        }
      }.asInstanceOf[java.sql.Timestamp]
      case 2 => this.dt = {
        value match {
          case (i: Integer) => {
            new java.sql.Date(i.toLong.*(86400000L))
          }
        }
      }.asInstanceOf[java.sql.Date]
      case 3 => this.dataBig = {
        value match {
          case (buffer: java.nio.ByteBuffer) => {
            val schema = getSchema.getFields().get(field$).schema()
            val decimalType = schema.getLogicalType().asInstanceOf[org.apache.avro.LogicalTypes.Decimal]
            BigDecimal(LogicalSql.decimalConversion.fromBytes(buffer, schema, decimalType))
          }
        }
      }.asInstanceOf[BigDecimal]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = LogicalSql.SCHEMA$
}

object LogicalSql {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"LogicalSql\",\"namespace\":\"example.logical\",\"fields\":[{\"name\":\"data\",\"type\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":9,\"scale\":2}},{\"name\":\"ts\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}},{\"name\":\"dt\",\"type\":{\"type\":\"int\",\"logicalType\":\"date\"}},{\"name\":\"dataBig\",\"type\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":20,\"scale\":12}}]}")
  val decimalConversion = new org.apache.avro.Conversions.DecimalConversion
}