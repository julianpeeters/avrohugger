/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl

sealed trait NestedProtocol extends Product with Serializable

final case class Level2(name: String) extends NestedProtocol

final case class Level1(level2: Level2) extends NestedProtocol

final case class Level0(level1: Level1) extends NestedProtocol