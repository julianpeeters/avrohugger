/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl

import shapeless.{:+:, CNil, Coproduct}

final case class Event1(index: Int)

final case class Event2()

final case class Event3()

final case class Event4(index: Int)

final case class Event5()

final case class Event6()

final case class Event7(index: Int)

final case class Event8()

final case class Event9()

final case class ShouldRenderAsOption(value: Option[example.idl.Event1] = None)

final case class ShouldRenderAsOption2(value: Option[example.idl.Event1] = Some(new Event1(0)))

final case class ShouldRenderAsOptionEither(value: Option[Either[example.idl.Event1, example.idl.Event2]] = None)

final case class ShouldRenderAsOptionEither2(value: Option[Either[example.idl.Event1, example.idl.Event2]] = Some(Left(new Event1(1))))

final case class ShouldRenderAsOptionEither3(value: Option[Either[example.idl.Event1, example.idl.Event2]] = Some(Left(new Event1(2))))

final case class ShouldRenderAsOptionCoproduct(value: Option[example.idl.Event1 :+: example.idl.Event2 :+: example.idl.Event3 :+: CNil] = None)

final case class ShouldRenderAsOptionCoproduct2(value: Option[example.idl.Event1 :+: example.idl.Event2 :+: example.idl.Event3 :+: CNil] = Some(Coproduct[example.idl.Event1 :+: example.idl.Event2 :+: example.idl.Event3 :+: CNil](new Event1(3))))

final case class ShouldRenderAsOptionCoproduct3(value: Option[example.idl.Event1 :+: example.idl.Event2 :+: example.idl.Event3 :+: CNil] = Some(Coproduct[example.idl.Event1 :+: example.idl.Event2 :+: example.idl.Event3 :+: CNil](new Event1(4))))

final case class ShouldRenderAsEither(value: Either[example.idl.Event1, example.idl.Event2] = Left(new Event1(5)))

final case class ShouldRenderAsCoproduct(value: example.idl.Event1 :+: example.idl.Event2 :+: example.idl.Event3 :+: example.idl.Event4 :+: CNil = Coproduct[example.idl.Event1 :+: example.idl.Event2 :+: example.idl.Event3 :+: example.idl.Event4 :+: CNil](new Event1(6)))

final case class CopX(value: example.idl.Event1 :+: example.idl.Event2 :+: example.idl.Event3 :+: CNil = Coproduct[example.idl.Event1 :+: example.idl.Event2 :+: example.idl.Event3 :+: CNil](new Event1(7)))

final case class CopY(value: example.idl.Event4 :+: example.idl.Event5 :+: example.idl.Event6 :+: CNil = Coproduct[example.idl.Event4 :+: example.idl.Event5 :+: example.idl.Event6 :+: CNil](new Event4(8)))

final case class CopZ(value: example.idl.Event7 :+: example.idl.Event8 :+: example.idl.Event9 :+: CNil = Coproduct[example.idl.Event7 :+: example.idl.Event8 :+: example.idl.Event9 :+: CNil](new Event7(9)))

final case class ShouldRenderAsCoproductOfCoproduct(value: example.idl.CopX :+: example.idl.CopY :+: example.idl.CopZ :+: CNil = Coproduct[example.idl.CopX :+: example.idl.CopY :+: example.idl.CopZ :+: CNil](new CopX(Coproduct[example.idl.Event1 :+: example.idl.Event2 :+: example.idl.Event3 :+: CNil](new Event1(10)))))