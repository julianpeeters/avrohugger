/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package fixedtwo.one

final case class fixed() extends org.apache.avro.specific.SpecificFixed {
  def getSchema: org.apache.avro.Schema = fixedtwo.one.fixed.SCHEMA$
  def readExternal(in: java.io.ObjectInput): Unit = {
    fixedtwo.one.fixed.READER$.read(this, org.apache.avro.specific.SpecificData.getDecoder(in))
    ()
  }
  def writeExternal(out: java.io.ObjectOutput): Unit = {
    fixedtwo.one.fixed.WRITER$.write(this, org.apache.avro.specific.SpecificData.getEncoder(out))
  }
}

object fixed {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"fixed\",\"name\":\"fixed\",\"namespace\":\"fixedtwo.one\",\"size\":16}")
  val READER$ = new org.apache.avro.specific.SpecificDatumReader[fixed](fixedtwo.one.fixed.SCHEMA$)
  val WRITER$ = new org.apache.avro.specific.SpecificDatumWriter[fixed](fixedtwo.one.fixed.SCHEMA$)
  def apply(data: Array[Byte]): fixed = {
    val fixed = new fixedtwo.one.fixed()
    fixed.bytes(data)
    fixed
  }
}