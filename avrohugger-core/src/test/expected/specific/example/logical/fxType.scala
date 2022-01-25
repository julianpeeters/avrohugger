/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.logical

final case class fxType() extends org.apache.avro.specific.SpecificFixed {
  def getSchema: org.apache.avro.Schema = fxType.SCHEMA$
  def bigDecimal: BigDecimal =
    java.nio.ByteBuffer.wrap(bytes) match {
      case (buffer: java.nio.ByteBuffer) => {
        val schema = getSchema
        val decimalType = schema.getLogicalType().asInstanceOf[org.apache.avro.LogicalTypes.Decimal]
        BigDecimal(fxType.decimalConversion.fromBytes(buffer, schema, decimalType))
      }
    }
  def readExternal(in: java.io.ObjectInput): Unit = {
    example.logical.fxType.READER$.read(this, org.apache.avro.specific.SpecificData.getDecoder(in))
    ()
  }
  def writeExternal(out: java.io.ObjectOutput): Unit = {
    example.logical.fxType.WRITER$.write(this, org.apache.avro.specific.SpecificData.getEncoder(out))
  }
}

object fxType {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"fixed\",\"name\":\"fxType\",\"namespace\":\"example.logical\",\"size\":2,\"logicalType\":\"decimal\",\"precision\":2,\"scale\":1}")
  val READER$ = new org.apache.avro.specific.SpecificDatumReader[fxType](example.logical.fxType.SCHEMA$)
  val WRITER$ = new org.apache.avro.specific.SpecificDatumWriter[fxType](example.logical.fxType.SCHEMA$)
  val decimalConversion = new org.apache.avro.Conversions.DecimalConversion
  def apply(data: BigDecimal): fxType = {
    val schema = SCHEMA$
    val decimalType = schema.getLogicalType().asInstanceOf[org.apache.avro.LogicalTypes.Decimal]
    val scale = decimalType.getScale()
    val scaledValue = data.setScale(scale)
    val bigDecimal = scaledValue.bigDecimal
    val fixed = new example.logical.fxType()
    fixed.bytes(decimalConversion.toBytes(bigDecimal, schema, decimalType).array())
    fixed
  }
}