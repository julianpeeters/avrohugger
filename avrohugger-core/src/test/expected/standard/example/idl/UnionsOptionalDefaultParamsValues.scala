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

final case class ShouldRenderAsOption(value: Option[Event1 :+: CNil] = None)

final case class ShouldRenderAsOption2(value: Option[Event1 :+: CNil] = Some(Coproduct[Event1 :+: CNil](new Event1(0))))

final case class ShouldRenderAsOption3(value: Option[Event1 :+: Event2 :+: CNil] = None)

final case class ShouldRenderAsOption4(value: Option[Event1 :+: Event2 :+: CNil] = Some(Coproduct[Event1 :+: Event2 :+: CNil](new Event1(1))))

final case class ShouldRenderAsOption5(value: Option[Event1 :+: Event2 :+: CNil] = Some(Coproduct[Event1 :+: Event2 :+: CNil](new Event1(2))))

final case class ShouldRenderAsOptionCoproduct(value: Option[Event1 :+: Event2 :+: Event3 :+: CNil] = None)

final case class ShouldRenderAsOptionCoproduct2(value: Option[Event1 :+: Event2 :+: Event3 :+: CNil] = Some(Coproduct[Event1 :+: Event2 :+: Event3 :+: CNil](new Event1(3))))

final case class ShouldRenderAsOptionCoproduct3(value: Option[Event1 :+: Event2 :+: Event3 :+: CNil] = Some(Coproduct[Event1 :+: Event2 :+: Event3 :+: CNil](new Event1(4))))

final case class ShouldRenderAsCoproduct1(value: Event1 :+: Event2 :+: CNil = Coproduct[Event1 :+: Event2 :+: CNil](new Event1(5)))

final case class ShouldRenderAsCoproduct2(value: Event1 :+: Event2 :+: Event3 :+: Event4 :+: CNil = Coproduct[Event1 :+: Event2 :+: Event3 :+: Event4 :+: CNil](new Event1(6)))

final case class CopX(value: Event1 :+: Event2 :+: Event3 :+: CNil = Coproduct[Event1 :+: Event2 :+: Event3 :+: CNil](new Event1(7)))

final case class CopY(value: Event4 :+: Event5 :+: Event6 :+: CNil = Coproduct[Event4 :+: Event5 :+: Event6 :+: CNil](new Event4(8)))

final case class CopZ(value: Event7 :+: Event8 :+: Event9 :+: CNil = Coproduct[Event7 :+: Event8 :+: Event9 :+: CNil](new Event7(9)))

final case class ShouldRenderAsCoproductOfCoproduct(value: CopX :+: CopY :+: CopZ :+: CNil = Coproduct[CopX :+: CopY :+: CopZ :+: CNil](new CopX(Coproduct[Event1 :+: Event2 :+: Event3 :+: CNil](new Event1(10)))))