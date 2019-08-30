/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl

import shapeless.{:+:, CNil}

final case class Event1()

final case class Event2()

final case class Event3()

final case class Event4()

final case class ShouldRenderAsOption1(value: Option[Event1])

final case class ShouldRenderAsOption2(value: Option[Event1])

final case class ShouldRenderAsCoproduct3(value: Option[Event1 :+: Event2 :+: CNil])

final case class ShouldRenderAsCoproduct4(value: Option[Event1 :+: Event2 :+: CNil])

final case class ShouldRenderAsCoproduct5(value: Option[Event1 :+: Event2 :+: CNil])

final case class ShouldRenderAsCoproduct6(value: Option[Event1 :+: Event2 :+: Event3 :+: CNil])

final case class ShouldRenderAsCoproduct7(value: Option[Event1 :+: Event2 :+: Event3 :+: CNil])

final case class ShouldRenderAsCoproduct8(value: Option[Event1 :+: Event2 :+: Event3 :+: CNil])

final case class ShouldRenderAsCoproduct9(value: Event1 :+: Event2 :+: CNil)

final case class ShouldRenderAsCoproduct10(value: Event1 :+: Event2 :+: Event3 :+: Event4 :+: CNil)

final case class ShouldRenderAsCoproduct11(value: Event1 :+: Event2 :+: Event3 :+: Event4 :+: CNil)