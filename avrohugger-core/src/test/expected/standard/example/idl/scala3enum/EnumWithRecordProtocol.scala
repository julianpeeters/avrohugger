/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl

sealed trait EnumWithRecordProtocol

final case class Paint(color: example.idl.Color) extends EnumWithRecordProtocol

enum Color { case RED, GREEN }
