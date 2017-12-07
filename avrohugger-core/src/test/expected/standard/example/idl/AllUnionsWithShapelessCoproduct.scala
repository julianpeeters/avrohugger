/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl

import shapeless.{:+:, CNil, Coproduct}

sealed trait AllUnionsWithShapelessCoproduct extends Product with Serializable

final case class Event1() extends AllUnionsWithShapelessCoproduct

final case class Event2() extends AllUnionsWithShapelessCoproduct

final case class Event3() extends AllUnionsWithShapelessCoproduct

final case class Event4() extends AllUnionsWithShapelessCoproduct

final case class ShouldRenderAsCoproduct(value: Option[Event1 :+: CNil]) extends AllUnionsWithShapelessCoproduct

final case class ShouldRenderAsCoproduct2(value: Option[Event1 :+: CNil]) extends AllUnionsWithShapelessCoproduct

final case class ShouldRenderAsCoproduct3(value: Option[Event1 :+: Event2 :+: CNil]) extends AllUnionsWithShapelessCoproduct

final case class ShouldRenderAsCoproduct4(value: Option[Event1 :+: Event2 :+: CNil]) extends AllUnionsWithShapelessCoproduct

final case class ShouldRenderAsCoproduct5(value: Option[Event1 :+: Event2 :+: CNil]) extends AllUnionsWithShapelessCoproduct

final case class ShouldRenderAsCoproduct6(value: Option[Event1 :+: Event2 :+: Event3 :+: CNil]) extends AllUnionsWithShapelessCoproduct

final case class ShouldRenderAsCoproduct7(value: Option[Event1 :+: Event2 :+: Event3 :+: CNil]) extends AllUnionsWithShapelessCoproduct

final case class ShouldRenderAsCoproduct8(value: Option[Event1 :+: Event2 :+: Event3 :+: CNil]) extends AllUnionsWithShapelessCoproduct

final case class ShouldRenderAsCoproduct9(value: Event1 :+: Event2 :+: CNil) extends AllUnionsWithShapelessCoproduct

final case class ShouldRenderAsCoproduct10(value: Event1 :+: Event2 :+: Event3 :+: Event4 :+: CNil) extends AllUnionsWithShapelessCoproduct

final case class ShouldRenderAsCoproduct11(value: Event1 :+: Event2 :+: Event3 :+: Event4 :+: CNil) extends AllUnionsWithShapelessCoproduct