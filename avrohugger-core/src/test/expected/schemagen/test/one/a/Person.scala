/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
  package test.one.a
  
  import scala.annotation.switch
  
  /**
   * Auto-Generated Schema
   * @param pet Auto-Generated Field
   */
  case class Person(var pet: Pet) extends org.apache.avro.specific.SpecificRecordBase {
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
    def getSchema: org.apache.avro.Schema = Person.SCHEMA$
  }
  
  object Person {
    val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Person\",\"namespace\":\"test.one.a\",\"doc\":\"Auto-Generated Schema\",\"fields\":[{\"name\":\"pet\",\"type\":{\"type\":\"record\",\"name\":\"Pet\",\"doc\":\"Auto-Generated Schema\",\"fields\":[{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Auto-Generated Field\"}]},\"doc\":\"Auto-Generated Field\"}]}")
  }