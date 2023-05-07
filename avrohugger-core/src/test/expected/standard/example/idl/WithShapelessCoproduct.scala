/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl

import shapeless.{:+:, CNil}

final case class Event1()

final case class Event2()

final case class Event3()

final case class Event4()

final case class ShouldRenderAsOption(value: Option[example.idl.Event1])

final case class ShouldRenderAsOption2(value: Option[example.idl.Event1])

final case class ShouldRenderAsOptionEither(value: Option[Either[example.idl.Event1, example.idl.Event2]])

final case class ShouldRenderAsOptionEither2(value: Option[Either[example.idl.Event1, example.idl.Event2]])

final case class ShouldRenderAsOptionEither3(value: Option[Either[example.idl.Event1, example.idl.Event2]])

final case class ShouldRenderAsOptionCoproduct(value: Option[example.idl.Event1 :+: example.idl.Event2 :+: example.idl.Event3 :+: CNil])

final case class ShouldRenderAsOptionCoproduct2(value: Option[example.idl.Event1 :+: example.idl.Event2 :+: example.idl.Event3 :+: CNil])

final case class ShouldRenderAsOptionCoproduct3(value: Option[example.idl.Event1 :+: example.idl.Event2 :+: example.idl.Event3 :+: CNil])

final case class ShouldRenderAsEither(value: Either[example.idl.Event1, example.idl.Event2])

final case class ShouldRenderAsCoproduct(value: example.idl.Event1 :+: example.idl.Event2 :+: example.idl.Event3 :+: example.idl.Event4 :+: CNil)

final case class ShouldRenderAsCoproduct2(value: example.idl.Event1 :+: example.idl.Event2 :+: example.idl.Event3 :+: example.idl.Event4 :+: CNil)