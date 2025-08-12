/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl

import other.ns.scala3enum.{ExternalDependency, Suit}

final case class DependentRecord(dependency: ExternalDependency, number: Int)
final case class DependentRecord2(dependency: Suit, name: String)
final case class DependentRecord3(dependency: Embedded, value: Boolean)

