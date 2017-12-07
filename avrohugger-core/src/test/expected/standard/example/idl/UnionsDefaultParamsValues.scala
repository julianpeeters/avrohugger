/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl

import shapeless.{:+:, CNil, Coproduct}

sealed trait UnionsDefaultParamsValues extends Product with Serializable

final case class Event1(index: Int) extends UnionsDefaultParamsValues

final case class Event2() extends UnionsDefaultParamsValues

final case class Event3() extends UnionsDefaultParamsValues

final case class Event4(index: Int) extends UnionsDefaultParamsValues

final case class Event5() extends UnionsDefaultParamsValues

final case class Event6() extends UnionsDefaultParamsValues

final case class Event7(index: Int) extends UnionsDefaultParamsValues

final case class Event8() extends UnionsDefaultParamsValues

final case class Event9() extends UnionsDefaultParamsValues

final case class ShouldRenderAsOption(value: Option[Event1 :+: CNil] = None) extends UnionsDefaultParamsValues

final case class ShouldRenderAsOption2(value: Option[Event1 :+: CNil] = Some(new Event1(0))) extends UnionsDefaultParamsValues

final case class ShouldRenderAsOptionEither(value: Option[Event1 :+: Event2 :+: CNil] = None) extends UnionsDefaultParamsValues

final case class ShouldRenderAsOptionEither2(value: Option[Event1 :+: Event2 :+: CNil] = Some(Left(new Event1(1)))) extends UnionsDefaultParamsValues

final case class ShouldRenderAsOptionEither3(value: Option[Event1 :+: Event2 :+: CNil] = Some(Left(new Event1(2)))) extends UnionsDefaultParamsValues

final case class ShouldRenderAsOptionCoproduct(value: Option[Event1 :+: Event2 :+: Event3 :+: CNil] = None) extends UnionsDefaultParamsValues

final case class ShouldRenderAsOptionCoproduct2(value: Option[Event1 :+: Event2 :+: Event3 :+: CNil] = Some(Coproduct[Event1 :+: Event2 :+: Event3 :+: CNil](new Event1(3)))) extends UnionsDefaultParamsValues

final case class ShouldRenderAsOptionCoproduct3(value: Option[Event1 :+: Event2 :+: Event3 :+: CNil] = Some(Coproduct[Event1 :+: Event2 :+: Event3 :+: CNil](new Event1(4)))) extends UnionsDefaultParamsValues

final case class ShouldRenderAsEither(value: Event1 :+: Event2 :+: CNil = Left(new Event1(5))) extends UnionsDefaultParamsValues

final case class ShouldRenderAsCoproduct(value: Event1 :+: Event2 :+: Event3 :+: Event4 :+: CNil = Coproduct[Event1 :+: Event2 :+: Event3 :+: Event4 :+: CNil](new Event1(6))) extends UnionsDefaultParamsValues

final case class CopX(value: Event1 :+: Event2 :+: Event3 :+: CNil = Coproduct[Event1 :+: Event2 :+: Event3 :+: CNil](new Event1(7))) extends UnionsDefaultParamsValues

final case class CopY(value: Event4 :+: Event5 :+: Event6 :+: CNil = Coproduct[Event4 :+: Event5 :+: Event6 :+: CNil](new Event4(8))) extends UnionsDefaultParamsValues

final case class CopZ(value: Event7 :+: Event8 :+: Event9 :+: CNil = Coproduct[Event7 :+: Event8 :+: Event9 :+: CNil](new Event7(9))) extends UnionsDefaultParamsValues

final case class ShouldRenderAsCoproductOfCoproduct(value: CopX :+: CopY :+: CopZ :+: CNil = Coproduct[CopX :+: CopY :+: CopZ :+: CNil](new CopX(Coproduct[Event1 :+: Event2 :+: Event3 :+: CNil](new Event1(10))))) extends UnionsDefaultParamsValues