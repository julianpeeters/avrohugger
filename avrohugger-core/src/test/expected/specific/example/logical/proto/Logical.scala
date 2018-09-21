/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.logical.proto

import scala.annotation.switch

import shapeless.tag.@@

case class Logical(var dec: scala.math.BigDecimal @@ (shapeless.Nat._9, shapeless.Nat._2), var ts: java.time.Instant, var dt: java.time.LocalDate, var uuid: java.util.UUID, var decBig: scala.math.BigDecimal @@ ((shapeless.Nat._2, shapeless.Nat._0), (shapeless.Nat._1, shapeless.Nat._2))) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(shapeless.tag[(shapeless.Nat._9, shapeless.Nat._2)][scala.math.BigDecimal](0), java.time.Instant.now, java.time.LocalDate.now, java.util.UUID.randomUUID, shapeless.tag[((shapeless.Nat._2, shapeless.Nat._0), (shapeless.Nat._1, shapeless.Nat._2))][scala.math.BigDecimal](0))
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
      case 4 => {
        val schema = getSchema.getFields().get(field$).schema()
        val decimalType = schema.getLogicalType().asInstanceOf[org.apache.avro.LogicalTypes.Decimal]
        val scale = decimalType.getScale()
        val scaledValue = decBig.setScale(scale)
        val bigDecimal = scaledValue.bigDecimal
        Logical.decimalConversion.toBytes(bigDecimal, schema, decimalType)
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
            shapeless.tag[(shapeless.Nat._9, shapeless.Nat._2)][scala.math.BigDecimal](scala.math.BigDecimal(Logical.decimalConversion.fromBytes(buffer, schema, decimalType)))
          }
        }
      }.asInstanceOf[scala.math.BigDecimal @@ (shapeless.Nat._9, shapeless.Nat._2)]
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
          case (chars: java.lang.CharSequence) => {
            java.util.UUID.fromString(chars.toString)
          }
        }
      }.asInstanceOf[java.util.UUID]
      case 4 => this.decBig = {
        value match {
          case (buffer: java.nio.ByteBuffer) => {
            val schema = getSchema.getFields().get(field$).schema()
            val decimalType = schema.getLogicalType().asInstanceOf[org.apache.avro.LogicalTypes.Decimal]
            shapeless.tag[((shapeless.Nat._2, shapeless.Nat._0), (shapeless.Nat._1, shapeless.Nat._2))][scala.math.BigDecimal](scala.math.BigDecimal(Logical.decimalConversion.fromBytes(buffer, schema, decimalType)))
          }
        }
      }.asInstanceOf[scala.math.BigDecimal @@ ((shapeless.Nat._2, shapeless.Nat._0), (shapeless.Nat._1, shapeless.Nat._2))]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = Logical.SCHEMA$
}

object Logical {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Logical\",\"namespace\":\"example.logical.proto\",\"fields\":[{\"name\":\"dec\",\"type\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":9,\"scale\":2},\"logicalType\":\"decimal\",\"precision\":9,\"scale\":2},{\"name\":\"ts\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}},{\"name\":\"dt\",\"type\":{\"type\":\"int\",\"logicalType\":\"date\"}},{\"name\":\"uuid\",\"type\":{\"type\":\"string\",\"logicalType\":\"uuid\"}},{\"name\":\"decBig\",\"type\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":20,\"scale\":12},\"logicalType\":\"decimal\",\"precision\":20,\"scale\":12}]}")
  val decimalConversion = new org.apache.avro.Conversions.DecimalConversion
}