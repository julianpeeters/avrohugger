/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.logical.proto

import scala.annotation.switch

case class Logical(var dec: BigDecimal, var ts: java.time.LocalDateTime, var dt: java.time.LocalDate) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(scala.math.BigDecimal(0), java.time.LocalDateTime.now, java.time.LocalDate.now)
  private val conversions: List[org.apache.avro.Conversion[_]] = List(avrohugger.format.specific.conversions.DecimalConversion, avrohugger.format.specific.conversions.DateTimeConversion, avrohugger.format.specific.conversions.DateConversion)
  override def getConversion(field$: Int): org.apache.avro.Conversion[_] = {
    conversions(field$)
  }
  private val encoder = new org.apache.avro.message.BinaryMessageEncoder[Logical](Logical.MODEL$, getSchema)
  def toByteBuffer: java.nio.ByteBuffer = {
    encoder.encode(this)
  }
  private val WRITER$ = Logical.MODEL$.createDatumWriter(getSchema).asInstanceOf[org.apache.avro.io.DatumWriter[Logical]]
  override def writeExternal(out: java.io.ObjectOutput) {
    WRITER$.write(this, org.apache.avro.specific.SpecificData.getEncoder(out))
  }
  private val READER$ = Logical.MODEL$.createDatumReader(getSchema).asInstanceOf[org.apache.avro.io.DatumReader[Logical]]
  override def readExternal(in: java.io.ObjectInput) {
    READER$.read(this, org.apache.avro.specific.SpecificData.getDecoder(in))
  }
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        dec
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
        value
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
  private val MODEL$ = new org.apache.avro.specific.SpecificData
  private val decoder = new org.apache.avro.message.BinaryMessageDecoder[Logical](MODEL$, SCHEMA$)
  def getDecoder: org.apache.avro.message.BinaryMessageDecoder[Logical] = {
    decoder
  }
  def fromByteBuffer(b: java.nio.ByteBuffer): Logical = {
    decoder.decode(b)
  }
}