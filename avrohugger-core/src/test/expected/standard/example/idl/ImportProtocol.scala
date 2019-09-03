/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl

import other.ns.{ExternalDependency, Suit}

final case class DependentRecord(dependency: ExternalDependency, number: Int)

final case class DependentRecord2(dependency: Suit.Value, name: String)

final case class DependentRecord3(dependency: Embedded, value: Boolean)