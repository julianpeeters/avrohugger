/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl

import shapeless.{:+:, CNil, Coproduct}

case class Event1(index: Int)

case class Event2()

case class Event3()

case class Event4(index: Int)

case class Event5()

case class Event6()

case class Event7(index: Int)

case class Event8()

case class Event9()

case class ShouldRenderAsOption(value: Option[Event1 :+: CNil] = None)

case class ShouldRenderAsOption2(value: Option[Event1 :+: CNil] = Some(Coproduct[Event1 :+: CNil](new Event1(0))))

case class ShouldRenderAsOption3(value: Option[Event1 :+: Event2 :+: CNil] = None)

case class ShouldRenderAsOption4(value: Option[Event1 :+: Event2 :+: CNil] = Some(Coproduct[Event1 :+: Event2 :+: CNil](new Event1(1))))

case class ShouldRenderAsOption5(value: Option[Event1 :+: Event2 :+: CNil] = Some(Coproduct[Event1 :+: Event2 :+: CNil](new Event1(2))))

case class ShouldRenderAsOptionCoproduct(value: Option[Event1 :+: Event2 :+: Event3 :+: CNil] = None)

case class ShouldRenderAsOptionCoproduct2(value: Option[Event1 :+: Event2 :+: Event3 :+: CNil] = Some(Coproduct[Event1 :+: Event2 :+: Event3 :+: CNil](new Event1(3))))

case class ShouldRenderAsOptionCoproduct3(value: Option[Event1 :+: Event2 :+: Event3 :+: CNil] = Some(Coproduct[Event1 :+: Event2 :+: Event3 :+: CNil](new Event1(4))))

case class ShouldRenderAsCoproduct1(value: Event1 :+: Event2 :+: CNil = Coproduct[Event1 :+: Event2 :+: CNil](new Event1(5)))

case class ShouldRenderAsCoproduct2(value: Event1 :+: Event2 :+: Event3 :+: Event4 :+: CNil = Coproduct[Event1 :+: Event2 :+: Event3 :+: Event4 :+: CNil](new Event1(6)))

case class CopX(value: Event1 :+: Event2 :+: Event3 :+: CNil = Coproduct[Event1 :+: Event2 :+: Event3 :+: CNil](new Event1(7)))

case class CopY(value: Event4 :+: Event5 :+: Event6 :+: CNil = Coproduct[Event4 :+: Event5 :+: Event6 :+: CNil](new Event4(8)))

case class CopZ(value: Event7 :+: Event8 :+: Event9 :+: CNil = Coproduct[Event7 :+: Event8 :+: Event9 :+: CNil](new Event7(9)))

case class ShouldRenderAsCoproductOfCoproduct(value: CopX :+: CopY :+: CopZ :+: CNil = Coproduct[CopX :+: CopY :+: CopZ :+: CNil](new CopX(Coproduct[Event1 :+: Event2 :+: Event3 :+: CNil](new Event1(10)))))