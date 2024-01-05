/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package fixedtwo.two

final case class fixed() extends org.apache.avro.specific.SpecificFixed {
  def getSchema: org.apache.avro.Schema = fixedtwo.two.fixed.SCHEMA$
  def readExternal(in: java.io.ObjectInput): Unit = {
    fixedtwo.two.fixed.READER$.read(this, org.apache.avro.specific.SpecificData.getDecoder(in))
    ()
  }
  def writeExternal(out: java.io.ObjectOutput): Unit = {
    fixedtwo.two.fixed.WRITER$.write(this, org.apache.avro.specific.SpecificData.getEncoder(out))
  }
}

object fixed {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"fixed\",\"name\":\"fixed\",\"namespace\":\"fixedtwo.two\",\"size\":16}")
  val READER$ = new org.apache.avro.specific.SpecificDatumReader[fixed](fixedtwo.two.fixed.SCHEMA$)
  val WRITER$ = new org.apache.avro.specific.SpecificDatumWriter[fixed](fixedtwo.two.fixed.SCHEMA$)
  def apply(data: Array[Byte]): fixed = {
    val fixed = new fixedtwo.two.fixed()
    fixed.bytes(data)
    fixed
  }
}