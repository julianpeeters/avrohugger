/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl

import shapeless.{:+:, CNil}

case class Event1()

case class Event2()

case class Event3()

case class Event4()

case class ShouldRenderAsOption(value: Option[Event1])

case class ShouldRenderAsOption2(value: Option[Event1])

case class ShouldRenderAsOptionEither(value: Option[Either[Event1, Event2]])

case class ShouldRenderAsOptionEither2(value: Option[Either[Event1, Event2]])

case class ShouldRenderAsOptionEither3(value: Option[Either[Event1, Event2]])

case class ShouldRenderAsOptionCoproduct(value: Option[Event1 :+: Event2 :+: Event3 :+: CNil])

case class ShouldRenderAsOptionCoproduct2(value: Option[Event1 :+: Event2 :+: Event3 :+: CNil])

case class ShouldRenderAsOptionCoproduct3(value: Option[Event1 :+: Event2 :+: Event3 :+: CNil])

case class ShouldRenderAsEither(value: Either[Event1, Event2])

case class ShouldRenderAsCoproduct(value: Event1 :+: Event2 :+: Event3 :+: Event4 :+: CNil)

case class ShouldRenderAsCoproduct2(value: Event1 :+: Event2 :+: Event3 :+: Event4 :+: CNil)