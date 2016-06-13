/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import other.ns.model.{ExternalDependency, Suit}

import example.idl.{DependentRecord => JDependentRecord, DependentRecord2 => JDependentRecord2, DependentRecord3 => JDependentRecord3, Embedded => JEmbedded}

import other.ns.{ExternalDependency => JExternalDependency, Suit => JSuit}

sealed trait ImportProtocol extends AvroSerializeable with Product with Serializable

final case class DependentRecord(dependency: ExternalDependency, number: Int) extends AvroSerializeable with ImportProtocol {
  type J = JDependentRecord
  override def toAvro: JDependentRecord = {
    new JDependentRecord(dependency match {
      case ExternalDependency(number) => new JExternalDependency(number)
    }, number)
  }
}

final object DependentRecord {
  implicit def reader = new AvroReader[DependentRecord] {
    override type J = JDependentRecord
  }
  implicit val metadata: AvroMetadata[DependentRecord, JDependentRecord] = new AvroMetadata[DependentRecord, JDependentRecord] {
    override val avroClass: Class[JDependentRecord] = classOf[JDependentRecord]
    override val schema: Schema = JDependentRecord.getClassSchema()
    override val fromAvro: (JDependentRecord) => DependentRecord = {
      (j: JDependentRecord) => DependentRecord(ExternalDependency(j.getDependency.getNumber.toInt), j.getNumber.toInt)
    }
  }
}

final case class DependentRecord2(dependency: Suit.Value, name: String) extends AvroSerializeable with ImportProtocol {
  type J = JDependentRecord2
  override def toAvro: JDependentRecord2 = {
    new JDependentRecord2(dependency match {
      case Suit.SPADES => JSuit.SPADES
      case Suit.DIAMONDS => JSuit.DIAMONDS
      case Suit.CLUBS => JSuit.CLUBS
      case Suit.HEARTS => JSuit.HEARTS
    }, name)
  }
}

final object DependentRecord2 {
  implicit def reader = new AvroReader[DependentRecord2] {
    override type J = JDependentRecord2
  }
  implicit val metadata: AvroMetadata[DependentRecord2, JDependentRecord2] = new AvroMetadata[DependentRecord2, JDependentRecord2] {
    override val avroClass: Class[JDependentRecord2] = classOf[JDependentRecord2]
    override val schema: Schema = JDependentRecord2.getClassSchema()
    override val fromAvro: (JDependentRecord2) => DependentRecord2 = {
      (j: JDependentRecord2) => DependentRecord2(j.getDependency match {
        case JSuit.SPADES => Suit.SPADES
        case JSuit.DIAMONDS => Suit.DIAMONDS
        case JSuit.CLUBS => Suit.CLUBS
        case JSuit.HEARTS => Suit.HEARTS
      }, j.getName.toString)
    }
  }
}

final case class DependentRecord3(dependency: Embedded, value: Boolean) extends AvroSerializeable with ImportProtocol {
  type J = JDependentRecord3
  override def toAvro: JDependentRecord3 = {
    new JDependentRecord3(dependency match {
      case Embedded(inner) => new JEmbedded(inner)
    }, value)
  }
}

final object DependentRecord3 {
  implicit def reader = new AvroReader[DependentRecord3] {
    override type J = JDependentRecord3
  }
  implicit val metadata: AvroMetadata[DependentRecord3, JDependentRecord3] = new AvroMetadata[DependentRecord3, JDependentRecord3] {
    override val avroClass: Class[JDependentRecord3] = classOf[JDependentRecord3]
    override val schema: Schema = JDependentRecord3.getClassSchema()
    override val fromAvro: (JDependentRecord3) => DependentRecord3 = {
      (j: JDependentRecord3) => DependentRecord3(Embedded(j.getDependency.getInner.toInt), j.getValue)
    }
  }
}