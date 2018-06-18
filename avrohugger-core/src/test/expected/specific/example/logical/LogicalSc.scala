/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.logical

import scala.annotation.switch

case class LogicalSc(var data: BigDecimal, var ts: java.time.LocalDateTime, var dt: java.time.LocalDate) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(scala.math.BigDecimal(0), java.time.LocalDateTime.now, java.time.LocalDate.now)
  private val conversions: List[org.apache.avro.Conversion[_]] = List(avrohugger.format.specific.conversions.DecimalConversion, avrohugger.format.specific.conversions.DateTimeConversion, avrohugger.format.specific.conversions.DateConversion)
  override def getConversion(field$: Int): org.apache.avro.Conversion[_] = {
    conversions(field$)
  }
  private val encoder = new org.apache.avro.message.BinaryMessageEncoder[LogicalSc](LogicalSc.MODEL$, getSchema)
  def toByteBuffer: java.nio.ByteBuffer = {
    encoder.encode(this)
  }
  private val WRITER$ = LogicalSc.MODEL$.createDatumWriter(getSchema).asInstanceOf[org.apache.avro.io.DatumWriter[LogicalSc]]
  override def writeExternal(out: java.io.ObjectOutput) {
    WRITER$.write(this, org.apache.avro.specific.SpecificData.getEncoder(out))
  }
  private val READER$ = LogicalSc.MODEL$.createDatumReader(getSchema).asInstanceOf[org.apache.avro.io.DatumReader[LogicalSc]]
  override def readExternal(in: java.io.ObjectInput) {
    READER$.read(this, org.apache.avro.specific.SpecificData.getDecoder(in))
  }
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        data
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
      case 0 => this.data = {
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
  def getSchema: org.apache.avro.Schema = LogicalSc.SCHEMA$
}

object LogicalSc {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"LogicalSc\",\"namespace\":\"example.logical\",\"fields\":[{\"name\":\"data\",\"type\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":9,\"scale\":2}},{\"name\":\"ts\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}},{\"name\":\"dt\",\"type\":{\"type\":\"int\",\"logicalType\":\"date\"}}]}")
  private val MODEL$ = new org.apache.avro.specific.SpecificData
  private val decoder = new org.apache.avro.message.BinaryMessageDecoder[LogicalSc](MODEL$, SCHEMA$)
  def getDecoder: org.apache.avro.message.BinaryMessageDecoder[LogicalSc] = {
    decoder
  }
  def fromByteBuffer(b: java.nio.ByteBuffer): LogicalSc = {
    decoder.decode(b)
  }
}