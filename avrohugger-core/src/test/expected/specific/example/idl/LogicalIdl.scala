/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl

import scala.annotation.switch

import shapeless.tag.@@

case class LogicalIdl(var dec: scala.math.BigDecimal @@ (shapeless.Nat._9, shapeless.Nat._2) = shapeless.tag[(shapeless.Nat._9, shapeless.Nat._2)][scala.math.BigDecimal](scala.math.BigDecimal("8888.88")), var maybeDec: Option[scala.math.BigDecimal @@ (shapeless.Nat._9, shapeless.Nat._2)] = Some(shapeless.tag[(shapeless.Nat._9, shapeless.Nat._2)][scala.math.BigDecimal](scala.math.BigDecimal("9999.99"))), var ts: java.time.Instant = java.time.Instant.ofEpochMilli(1526573732000L), var dt: java.time.LocalDate = java.time.LocalDate.ofEpochDay(600L), var decBig: scala.math.BigDecimal @@ ((shapeless.Nat._2, shapeless.Nat._0), (shapeless.Nat._1, shapeless.Nat._2)) = shapeless.tag[((shapeless.Nat._2, shapeless.Nat._0), (shapeless.Nat._1, shapeless.Nat._2))][scala.math.BigDecimal](scala.math.BigDecimal("7777.77"))) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(shapeless.tag[(shapeless.Nat._9, shapeless.Nat._2)][scala.math.BigDecimal](scala.math.BigDecimal("8888.88")), Some(shapeless.tag[(shapeless.Nat._9, shapeless.Nat._2)][scala.math.BigDecimal](scala.math.BigDecimal("9999.99"))), java.time.Instant.ofEpochMilli(1526573732000L), java.time.LocalDate.ofEpochDay(600L), shapeless.tag[((shapeless.Nat._2, shapeless.Nat._0), (shapeless.Nat._1, shapeless.Nat._2))][scala.math.BigDecimal](scala.math.BigDecimal("7777.77")))
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        val schema = getSchema.getFields().get(field$).schema()
        val decimalType = schema.getLogicalType().asInstanceOf[org.apache.avro.LogicalTypes.Decimal]
        val scale = decimalType.getScale()
        val scaledValue = dec.setScale(scale)
        val bigDecimal = scaledValue.bigDecimal
        LogicalIdl.decimalConversion.toBytes(bigDecimal, schema, decimalType)
      }.asInstanceOf[AnyRef]
      case 1 => {
        maybeDec match {
          case Some(x) => {
            val schema = getSchema.getFields().get(field$).schema()
            val decimalType = schema.getLogicalType().asInstanceOf[org.apache.avro.LogicalTypes.Decimal]
            val scale = decimalType.getScale()
            val scaledValue = x.setScale(scale)
            val bigDecimal = scaledValue.bigDecimal
            LogicalIdl.decimalConversion.toBytes(bigDecimal, schema, decimalType)
          }
          case None => null
        }
      }.asInstanceOf[AnyRef]
      case 2 => {
        ts.toEpochMilli
      }.asInstanceOf[AnyRef]
      case 3 => {
        dt.toEpochDay.toInt
      }.asInstanceOf[AnyRef]
      case 4 => {
        val schema = getSchema.getFields().get(field$).schema()
        val decimalType = schema.getLogicalType().asInstanceOf[org.apache.avro.LogicalTypes.Decimal]
        val scale = decimalType.getScale()
        val scaledValue = decBig.setScale(scale)
        val bigDecimal = scaledValue.bigDecimal
        LogicalIdl.decimalConversion.toBytes(bigDecimal, schema, decimalType)
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
            shapeless.tag[(shapeless.Nat._9, shapeless.Nat._2)][scala.math.BigDecimal](scala.math.BigDecimal(LogicalIdl.decimalConversion.fromBytes(buffer, schema, decimalType)))
          }
        }
      }.asInstanceOf[scala.math.BigDecimal @@ (shapeless.Nat._9, shapeless.Nat._2)]
      case 1 => this.maybeDec = {
        value match {
          case null => None
          case _ => Some(value match {
            case (buffer: java.nio.ByteBuffer) => {
              val schema = getSchema.getFields().get(field$).schema()
              val decimalType = schema.getLogicalType().asInstanceOf[org.apache.avro.LogicalTypes.Decimal]
              shapeless.tag[(shapeless.Nat._9, shapeless.Nat._2)][scala.math.BigDecimal](scala.math.BigDecimal(LogicalIdl.decimalConversion.fromBytes(buffer, schema, decimalType)))
            }
          })
        }
      }.asInstanceOf[Option[scala.math.BigDecimal @@ (shapeless.Nat._9, shapeless.Nat._2)]]
      case 2 => this.ts = {
        value match {
          case (l: Long) => {
            java.time.Instant.ofEpochMilli(l)
          }
        }
      }.asInstanceOf[java.time.Instant]
      case 3 => this.dt = {
        value match {
          case (i: Integer) => {
            java.time.LocalDate.ofEpochDay(i.toInt)
          }
        }
      }.asInstanceOf[java.time.LocalDate]
      case 4 => this.decBig = {
        value match {
          case (buffer: java.nio.ByteBuffer) => {
            val schema = getSchema.getFields().get(field$).schema()
            val decimalType = schema.getLogicalType().asInstanceOf[org.apache.avro.LogicalTypes.Decimal]
            shapeless.tag[((shapeless.Nat._2, shapeless.Nat._0), (shapeless.Nat._1, shapeless.Nat._2))][scala.math.BigDecimal](scala.math.BigDecimal(LogicalIdl.decimalConversion.fromBytes(buffer, schema, decimalType)))
          }
        }
      }.asInstanceOf[scala.math.BigDecimal @@ ((shapeless.Nat._2, shapeless.Nat._0), (shapeless.Nat._1, shapeless.Nat._2))]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = LogicalIdl.SCHEMA$
}

object LogicalIdl {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"LogicalIdl\",\"namespace\":\"example.idl\",\"fields\":[{\"name\":\"dec\",\"type\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":9,\"scale\":2},\"default\":8888.88},{\"name\":\"maybeDec\",\"type\":[{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":9,\"scale\":2},\"null\"],\"default\":9999.99},{\"name\":\"ts\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"},\"default\":1526573732000},{\"name\":\"dt\",\"type\":{\"type\":\"int\",\"logicalType\":\"date\"},\"default\":600},{\"name\":\"decBig\",\"type\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":20,\"scale\":12},\"default\":7777.77}]}")
  val decimalConversion = new org.apache.avro.Conversions.DecimalConversion
}