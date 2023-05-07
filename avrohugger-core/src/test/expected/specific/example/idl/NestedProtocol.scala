/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl

import scala.annotation.switch

sealed trait NestedProtocol extends org.apache.avro.specific.SpecificRecordBase with Product with Serializable

final case class Level2(var name: String) extends org.apache.avro.specific.SpecificRecordBase with NestedProtocol {
  def this() = this("")
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        name
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.name = {
        value.toString
      }.asInstanceOf[String]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = Level2.SCHEMA$
}

final object Level2 {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Level2\",\"namespace\":\"example.idl\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"}]}")
}

final case class Level1(var level2: example.idl.Level2) extends org.apache.avro.specific.SpecificRecordBase with NestedProtocol {
  def this() = this(new Level2)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        level2
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.level2 = {
        value
      }.asInstanceOf[example.idl.Level2]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = Level1.SCHEMA$
}

final object Level1 {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Level1\",\"namespace\":\"example.idl\",\"fields\":[{\"name\":\"level2\",\"type\":{\"type\":\"record\",\"name\":\"Level2\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"}]}}]}")
}

final case class Level0(var level1: example.idl.Level1) extends org.apache.avro.specific.SpecificRecordBase with NestedProtocol {
  def this() = this(new Level1)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        level1
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.level1 = {
        value
      }.asInstanceOf[example.idl.Level1]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = Level0.SCHEMA$
}

final object Level0 {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Level0\",\"namespace\":\"example.idl\",\"fields\":[{\"name\":\"level1\",\"type\":{\"type\":\"record\",\"name\":\"Level1\",\"fields\":[{\"name\":\"level2\",\"type\":{\"type\":\"record\",\"name\":\"Level2\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"}]}}]}}]}")
}