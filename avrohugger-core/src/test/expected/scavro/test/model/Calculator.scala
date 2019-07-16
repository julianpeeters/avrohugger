/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package test.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import test.{Added => JAdded, Divided => JDivided, Multiplied => JMultiplied, Reset => JReset, Subtracted => JSubtracted}

sealed trait Calculator extends AvroSerializeable with Product with Serializable

final case class Added(value: Int) extends AvroSerializeable with Calculator {
  type J = JAdded
  override def toAvro: JAdded = {
    new JAdded(value)
  }
}

final object Added {
  implicit def reader = new AvroReader[Added] {
    override type J = JAdded
  }
  implicit val metadata: AvroMetadata[Added, JAdded] = new AvroMetadata[Added, JAdded] {
    override val avroClass: Class[JAdded] = classOf[JAdded]
    override val schema: Schema = JAdded.getClassSchema()
    override val fromAvro: (JAdded) => Added = {
      (j: JAdded) => Added(j.getValue)
    }
  }
}

final case class Subtracted(value: Int) extends AvroSerializeable with Calculator {
  type J = JSubtracted
  override def toAvro: JSubtracted = {
    new JSubtracted(value)
  }
}

final object Subtracted {
  implicit def reader = new AvroReader[Subtracted] {
    override type J = JSubtracted
  }
  implicit val metadata: AvroMetadata[Subtracted, JSubtracted] = new AvroMetadata[Subtracted, JSubtracted] {
    override val avroClass: Class[JSubtracted] = classOf[JSubtracted]
    override val schema: Schema = JSubtracted.getClassSchema()
    override val fromAvro: (JSubtracted) => Subtracted = {
      (j: JSubtracted) => Subtracted(j.getValue)
    }
  }
}

final case class Divided(value: Int) extends AvroSerializeable with Calculator {
  type J = JDivided
  override def toAvro: JDivided = {
    new JDivided(value)
  }
}

final object Divided {
  implicit def reader = new AvroReader[Divided] {
    override type J = JDivided
  }
  implicit val metadata: AvroMetadata[Divided, JDivided] = new AvroMetadata[Divided, JDivided] {
    override val avroClass: Class[JDivided] = classOf[JDivided]
    override val schema: Schema = JDivided.getClassSchema()
    override val fromAvro: (JDivided) => Divided = {
      (j: JDivided) => Divided(j.getValue)
    }
  }
}

final case class Multiplied(value: Int) extends AvroSerializeable with Calculator {
  type J = JMultiplied
  override def toAvro: JMultiplied = {
    new JMultiplied(value)
  }
}

final object Multiplied {
  implicit def reader = new AvroReader[Multiplied] {
    override type J = JMultiplied
  }
  implicit val metadata: AvroMetadata[Multiplied, JMultiplied] = new AvroMetadata[Multiplied, JMultiplied] {
    override val avroClass: Class[JMultiplied] = classOf[JMultiplied]
    override val schema: Schema = JMultiplied.getClassSchema()
    override val fromAvro: (JMultiplied) => Multiplied = {
      (j: JMultiplied) => Multiplied(j.getValue)
    }
  }
}

final case class Reset() extends AvroSerializeable with Calculator {
  type J = JReset
  override def toAvro: JReset = {
    new JReset
  }
}

final object Reset {
  implicit def reader = new AvroReader[Reset] {
    override type J = JReset
  }
  implicit val metadata: AvroMetadata[Reset, JReset] = new AvroMetadata[Reset, JReset] {
    override val avroClass: Class[JReset] = classOf[JReset]
    override val schema: Schema = JReset.getClassSchema()
    override val fromAvro: (JReset) => Reset = {
      (j: JReset) => Reset()
    }
  }
}