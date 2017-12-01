/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl

sealed trait WithoutShapelessCoproduct extends Product with Serializable

final case class Event1() extends WithoutShapelessCoproduct

final case class Event2() extends WithoutShapelessCoproduct

final case class Event3() extends WithoutShapelessCoproduct

final case class Event4() extends WithoutShapelessCoproduct

final case class ShouldRenderAsOption(value: Option[Event1]) extends WithoutShapelessCoproduct

final case class ShouldRenderAsOption2(value: Option[Event1]) extends WithoutShapelessCoproduct

final case class ShouldRenderAsOptionEither(value: Option[Either[Event1, Event2]]) extends WithoutShapelessCoproduct

final case class ShouldRenderAsOptionEither2(value: Option[Either[Event1, Event2]]) extends WithoutShapelessCoproduct

final case class ShouldRenderAsOptionEither3(value: Option[Either[Event1, Event2]]) extends WithoutShapelessCoproduct

final case class ShouldRenderAsEither(value: Either[Event1, Event2]) extends WithoutShapelessCoproduct