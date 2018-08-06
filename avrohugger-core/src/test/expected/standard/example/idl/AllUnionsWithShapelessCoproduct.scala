/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl

import shapeless.{:+:, CNil}

case class Event1()

case class Event2()

case class Event3()

case class Event4()

case class ShouldRenderAsCoproduct(value: Option[Event1 :+: CNil])

case class ShouldRenderAsCoproduct2(value: Option[Event1 :+: CNil])

case class ShouldRenderAsCoproduct3(value: Option[Event1 :+: Event2 :+: CNil])

case class ShouldRenderAsCoproduct4(value: Option[Event1 :+: Event2 :+: CNil])

case class ShouldRenderAsCoproduct5(value: Option[Event1 :+: Event2 :+: CNil])

case class ShouldRenderAsCoproduct6(value: Option[Event1 :+: Event2 :+: Event3 :+: CNil])

case class ShouldRenderAsCoproduct7(value: Option[Event1 :+: Event2 :+: Event3 :+: CNil])

case class ShouldRenderAsCoproduct8(value: Option[Event1 :+: Event2 :+: Event3 :+: CNil])

case class ShouldRenderAsCoproduct9(value: Event1 :+: Event2 :+: CNil)

case class ShouldRenderAsCoproduct10(value: Event1 :+: Event2 :+: Event3 :+: Event4 :+: CNil)

case class ShouldRenderAsCoproduct11(value: Event1 :+: Event2 :+: Event3 :+: Event4 :+: CNil)