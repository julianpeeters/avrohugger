/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl

import scala.annotation.switch

case class Event1() extends org.apache.avro.specific.SpecificRecordBase {
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
  def getSchema: org.apache.avro.Schema = Event1.SCHEMA$
}

object Event1 {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Event1\",\"namespace\":\"example.idl\",\"fields\":[]}")
}

case class Event2() extends org.apache.avro.specific.SpecificRecordBase {
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
  def getSchema: org.apache.avro.Schema = Event2.SCHEMA$
}

object Event2 {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Event2\",\"namespace\":\"example.idl\",\"fields\":[]}")
}

case class Event3() extends org.apache.avro.specific.SpecificRecordBase {
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
  def getSchema: org.apache.avro.Schema = Event3.SCHEMA$
}

object Event3 {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Event3\",\"namespace\":\"example.idl\",\"fields\":[]}")
}

case class Event4() extends org.apache.avro.specific.SpecificRecordBase {
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
  def getSchema: org.apache.avro.Schema = Event4.SCHEMA$
}

object Event4 {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Event4\",\"namespace\":\"example.idl\",\"fields\":[]}")
}

final case class ShouldRenderAsOption(var value: Option[Event1]) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(None)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        value match {
          case Some(x) => x
          case None => null
        }
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.value = {
        value match {
          case null => None
          case _ => Some(value)
        }
      }.asInstanceOf[Option[Event1]]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = ShouldRenderAsOption.SCHEMA$
}

object ShouldRenderAsOption {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"ShouldRenderAsOption\",\"namespace\":\"example.idl\",\"fields\":[{\"name\":\"value\",\"type\":[\"null\",{\"type\":\"record\",\"name\":\"Event1\",\"fields\":[]}]}]}")
}

final case class ShouldRenderAsOption2(var value: Option[Event1]) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(None)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        value match {
          case Some(x) => x
          case None => null
        }
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.value = {
        value match {
          case null => None
          case _ => Some(value)
        }
      }.asInstanceOf[Option[Event1]]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = ShouldRenderAsOption2.SCHEMA$
}

object ShouldRenderAsOption2 {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"ShouldRenderAsOption2\",\"namespace\":\"example.idl\",\"fields\":[{\"name\":\"value\",\"type\":[{\"type\":\"record\",\"name\":\"Event1\",\"fields\":[]},\"null\"]}]}")
}

final case class ShouldRenderAsOptionEither(var value: Option[Either[Event1, Event2]]) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(None)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        value match {
          case Some(x) => x
          case None => null
        }
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.value = {
        value match {
          case null => None
          case _ => Some(value)
        }
      }.asInstanceOf[Option[Either[Event1, Event2]]]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = ShouldRenderAsOptionEither.SCHEMA$
}

object ShouldRenderAsOptionEither {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"ShouldRenderAsOptionEither\",\"namespace\":\"example.idl\",\"fields\":[{\"name\":\"value\",\"type\":[\"null\",{\"type\":\"record\",\"name\":\"Event1\",\"fields\":[]},{\"type\":\"record\",\"name\":\"Event2\",\"fields\":[]}]}]}")
}

final case class ShouldRenderAsOptionEither2(var value: Option[Either[Event1, Event2]]) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(None)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        value match {
          case Some(x) => x
          case None => null
        }
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.value = {
        value match {
          case null => None
          case _ => Some(value)
        }
      }.asInstanceOf[Option[Either[Event1, Event2]]]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = ShouldRenderAsOptionEither2.SCHEMA$
}

object ShouldRenderAsOptionEither2 {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"ShouldRenderAsOptionEither2\",\"namespace\":\"example.idl\",\"fields\":[{\"name\":\"value\",\"type\":[{\"type\":\"record\",\"name\":\"Event1\",\"fields\":[]},\"null\",{\"type\":\"record\",\"name\":\"Event2\",\"fields\":[]}]}]}")
}

final case class ShouldRenderAsOptionEither3(var value: Option[Either[Event1, Event2]]) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(None)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        value match {
          case Some(x) => x
          case None => null
        }
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.value = {
        value match {
          case null => None
          case _ => Some(value)
        }
      }.asInstanceOf[Option[Either[Event1, Event2]]]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = ShouldRenderAsOptionEither3.SCHEMA$
}

object ShouldRenderAsOptionEither3 {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"ShouldRenderAsOptionEither3\",\"namespace\":\"example.idl\",\"fields\":[{\"name\":\"value\",\"type\":[{\"type\":\"record\",\"name\":\"Event1\",\"fields\":[]},{\"type\":\"record\",\"name\":\"Event2\",\"fields\":[]},\"null\"]}]}")
}

final case class ShouldRenderAsEither(var value: Either[Event1, Event2]) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(Left(new Event1))
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
      }.asInstanceOf[Either[Event1, Event2]]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = ShouldRenderAsEither.SCHEMA$
}

object ShouldRenderAsEither {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"ShouldRenderAsEither\",\"namespace\":\"example.idl\",\"fields\":[{\"name\":\"value\",\"type\":[{\"type\":\"record\",\"name\":\"Event1\",\"fields\":[]},{\"type\":\"record\",\"name\":\"Event2\",\"fields\":[]}]}]}")
}