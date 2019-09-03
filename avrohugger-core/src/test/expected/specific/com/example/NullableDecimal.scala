/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.example

import scala.annotation.switch

final case class NullableDecimal(var ciao: Option[BigDecimal] = None) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(None)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        ciao match {
          case Some(x) => {
            val schema = scala.collection.JavaConverters.asScalaBufferConverter(getSchema.getFields().get(field$).schema().getTypes).asScala.toList.find(_.getFullName == "bytes").get
            val decimalType = schema.getLogicalType().asInstanceOf[org.apache.avro.LogicalTypes.Decimal]
            val scale = decimalType.getScale()
            val scaledValue = x.setScale(scale)
            val bigDecimal = scaledValue.bigDecimal
            NullableDecimal.decimalConversion.toBytes(bigDecimal, schema, decimalType)
          }
          case None => null
        }
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.ciao = {
        value match {
          case null => None
          case _ => Some(value match {
            case (buffer: java.nio.ByteBuffer) => {
              val schema = scala.collection.JavaConverters.asScalaBufferConverter(getSchema.getFields().get(field$).schema().getTypes).asScala.toList.find(_.getFullName == "bytes").get
              val decimalType = schema.getLogicalType().asInstanceOf[org.apache.avro.LogicalTypes.Decimal]
              BigDecimal(NullableDecimal.decimalConversion.fromBytes(buffer, schema, decimalType))
            }
          })
        }
      }.asInstanceOf[Option[BigDecimal]]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = NullableDecimal.SCHEMA$
}

object NullableDecimal {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"NullableDecimal\",\"namespace\":\"com.example\",\"fields\":[{\"name\":\"ciao\",\"type\":[\"null\",{\"type\":\"bytes\",\"scale\":5,\"precision\":64,\"logicalType\":\"decimal\"}],\"default\":null}]}")
  val decimalConversion = new org.apache.avro.Conversions.DecimalConversion
}