/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl

import other.ns.{ExternalDependency, Suit}

case class DependentRecord(dependency: ExternalDependency, number: Int)

case class DependentRecord2(dependency: Suit.Value, name: String)

case class DependentRecord3(dependency: Embedded, value: Boolean)