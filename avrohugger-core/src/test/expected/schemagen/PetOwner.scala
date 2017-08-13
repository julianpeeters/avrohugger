/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
import scala.annotation.switch

case class PetOwner(var pet: Pet) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(new Pet)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        pet
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.pet = {
        value
      }.asInstanceOf[Pet]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = PetOwner.SCHEMA$
}

object PetOwner {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"PetOwner\",\"fields\":[{\"name\":\"pet\",\"type\":{\"type\":\"record\",\"name\":\"Pet\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"}]}}]}")
}