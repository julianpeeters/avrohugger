/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
import scala.annotation.switch

/**
 * Auto-generated schema
 * @param x Auto-Generated Field
 */
final case class AvroTypeProviderTestNoNamespace(var x: Int) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(0)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        x
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.x = {
        value
      }.asInstanceOf[Int]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = AvroTypeProviderTestNoNamespace.SCHEMA$
}

object AvroTypeProviderTestNoNamespace {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"AvroTypeProviderTestNoNamespace\",\"doc\":\"Auto-generated schema\",\"fields\":[{\"name\":\"x\",\"type\":\"int\",\"doc\":\"Auto-Generated Field\"}]}")
}