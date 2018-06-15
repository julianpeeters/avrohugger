package avrohugger
package format
package specific
package conversions

import java.nio.ByteBuffer

import org.apache.avro._
import org.apache.avro.generic.GenericFixed

import scala.math.BigDecimal

object DecimalConversion extends Conversion[BigDecimal] {

  private val avroConverter = new org.apache.avro.Conversions.DecimalConversion

  override def getConvertedType: Class[BigDecimal] = classOf[BigDecimal]

  override def getLogicalTypeName = "decimal"

  override def fromBytes(value: ByteBuffer, schema: Schema, `type`: LogicalType): BigDecimal =
    avroConverter.fromBytes(value, schema, `type`)

  override def toBytes(value: BigDecimal, schema: Schema, `type`: LogicalType): ByteBuffer =
    avroConverter.toBytes(value.bigDecimal, schema, `type`)

  override def fromFixed(value: GenericFixed, schema: Schema, `type`: LogicalType): BigDecimal =
    avroConverter.fromFixed(value, schema, `type`)

  override def toFixed(value: BigDecimal, schema: Schema, `type`: LogicalType): GenericFixed =
    avroConverter.toFixed(value.bigDecimal, schema, `type`)
}

