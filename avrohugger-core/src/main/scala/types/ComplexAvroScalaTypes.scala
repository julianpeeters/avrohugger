package avrohugger
package types

// fixed
sealed trait AvroScalaFixedType extends Product with Serializable
case object ScalaCaseClassWrapper extends AvroScalaFixedType
case object ScalaCaseClassWrapperWithSchema extends AvroScalaFixedType
// record
sealed trait AvroScalaRecordType extends Product with Serializable
case object ScalaCaseClass extends AvroScalaRecordType
case object ScalaCaseClassWithSchema extends AvroScalaRecordType
// enum
sealed trait AvroScalaEnumType extends Product with Serializable
case object ScalaEnumeration extends AvroScalaEnumType
case object JavaEnum extends AvroScalaEnumType
case object ScalaCaseObjectEnum extends AvroScalaEnumType
case object EnumAsScalaString extends AvroScalaEnumType
case object Scala3Enum extends AvroScalaEnumType
// union
sealed trait AvroScalaUnionType extends Product with Serializable {
  val useEitherForTwoNonNullTypes: Boolean = false

  val useCoproductForLoneNonNullType: Boolean = false

}

sealed trait ShapelessUnionType extends AvroScalaUnionType
case object OptionalShapelessCoproduct extends ShapelessUnionType {
  override val useCoproductForLoneNonNullType: Boolean = true
}
case object OptionShapelessCoproduct extends ShapelessUnionType
case object OptionEitherShapelessCoproduct extends ShapelessUnionType {
  override val useEitherForTwoNonNullTypes: Boolean = true
}
case object OptionScala3UnionType extends AvroScalaUnionType
// array
sealed trait AvroScalaArrayType extends Product with Serializable
case object ScalaArray extends AvroScalaArrayType
case object ScalaList extends AvroScalaArrayType
case object ScalaSeq extends AvroScalaArrayType
case object ScalaVector extends AvroScalaArrayType
// map
sealed trait AvroScalaMapType extends Product with Serializable
case object ScalaMap extends AvroScalaMapType
// protocol
sealed trait AvroScalaProtocolType extends Product with Serializable
case object ScalaADT extends AvroScalaProtocolType
case object NoTypeGenerated extends AvroScalaProtocolType
