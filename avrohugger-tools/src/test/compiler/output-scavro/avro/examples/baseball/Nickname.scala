/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package avro.examples.baseball.model

import org.apache.avro.Schema

import org.oedura.scavro.{AvroMetadata, AvroReader, AvroSerializeable}

import avro.examples.baseball.{Nickname => JNickname}

final case class Nickname(name: String) extends AvroSerializeable {
  type J = JNickname
  override def toAvro: JNickname = {
    new JNickname(name)
  }
}

object Nickname {
  implicit def reader = new AvroReader[Nickname] {
    override type J = JNickname
  }
  implicit val metadata: AvroMetadata[Nickname, JNickname] = new AvroMetadata[Nickname, JNickname] {
    override val avroClass: Class[JNickname] = classOf[JNickname]
    override val schema: Schema = JNickname.getClassSchema()
    override val fromAvro: (JNickname) => Nickname = {
      (j: JNickname) => Nickname(j.getName.toString)
    }
  }
}