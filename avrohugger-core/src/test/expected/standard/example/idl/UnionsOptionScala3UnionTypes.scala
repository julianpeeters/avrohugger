/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.idl

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

final case class ShouldRenderAsOption3(value: Option[(example.idl.Event1| example.idl.Event2)] = None)

final case class ShouldRenderAsOption4(value: Option[(example.idl.Event1| example.idl.Event2)] = Some(new Event1(1)))

final case class ShouldRenderAsOption5(value: Option[(example.idl.Event1| example.idl.Event2)] = Some(new Event1(2)))

final case class ShouldRenderAsOptionCoproduct(value: Option[(example.idl.Event1| example.idl.Event2| example.idl.Event3)] = None)

final case class ShouldRenderAsOptionCoproduct2(value: Option[(example.idl.Event1| example.idl.Event2| example.idl.Event3)] = Some(new Event1(3)))

final case class ShouldRenderAsOptionCoproduct3(value: Option[(example.idl.Event1| example.idl.Event2| example.idl.Event3)] = Some(new Event1(4)))

final case class ShouldRenderAsCoproduct1(value: (example.idl.Event1| example.idl.Event2) = new Event1(5))

final case class ShouldRenderAsCoproduct2(value: (example.idl.Event1| example.idl.Event2| example.idl.Event3| example.idl.Event4) = new Event1(6))

final case class CopX(value: (example.idl.Event1| example.idl.Event2| example.idl.Event3) = new Event1(7))

final case class CopY(value: (example.idl.Event4| example.idl.Event5| example.idl.Event6) = new Event4(8))

final case class CopZ(value: (example.idl.Event7| example.idl.Event8| example.idl.Event9) = new Event7(9))

final case class ShouldRenderAsCoproductOfCoproduct(value: (example.idl.CopX| example.idl.CopY| example.idl.CopZ) = new CopX(new Event1(10)))