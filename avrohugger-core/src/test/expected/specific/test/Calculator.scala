/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package test

import scala.annotation.switch

sealed trait Calculator extends org.apache.avro.specific.SpecificRecordBase with Product with Serializable

final case class Added(var value: Int) extends org.apache.avro.specific.SpecificRecordBase with Calculator {
  def this() = this(0)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        value
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.value = {
        value
      }.asInstanceOf[Int]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = test.Added.SCHEMA$
}

final object Added {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Added\",\"namespace\":\"test\",\"fields\":[{\"name\":\"value\",\"type\":\"int\"}]}")
}

final case class Subtracted(var value: Int) extends org.apache.avro.specific.SpecificRecordBase with Calculator {
  def this() = this(0)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        value
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.value = {
        value
      }.asInstanceOf[Int]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = test.Subtracted.SCHEMA$
}

final object Subtracted {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Subtracted\",\"namespace\":\"test\",\"fields\":[{\"name\":\"value\",\"type\":\"int\"}]}")
}

final case class Divided(var value: Int) extends org.apache.avro.specific.SpecificRecordBase with Calculator {
  def this() = this(0)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        value
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.value = {
        value
      }.asInstanceOf[Int]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = test.Divided.SCHEMA$
}

final object Divided {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Divided\",\"namespace\":\"test\",\"fields\":[{\"name\":\"value\",\"type\":\"int\"}]}")
}

final case class Multiplied(var value: Int) extends org.apache.avro.specific.SpecificRecordBase with Calculator {
  def this() = this(0)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        value
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.value = {
        value
      }.asInstanceOf[Int]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = test.Multiplied.SCHEMA$
}

final object Multiplied {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Multiplied\",\"namespace\":\"test\",\"fields\":[{\"name\":\"value\",\"type\":\"int\"}]}")
}

final case class Reset() extends org.apache.avro.specific.SpecificRecordBase with Calculator {
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = test.Reset.SCHEMA$
}

final object Reset {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Reset\",\"namespace\":\"test\",\"fields\":[]}")
}