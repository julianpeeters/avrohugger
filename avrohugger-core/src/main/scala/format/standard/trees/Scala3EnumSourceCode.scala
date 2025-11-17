package avrohugger.format.standard.trees

import treehugger.forest.Tree

/**
 * treehugger does not natively support generating Scala 3 enums, which have a new syntax
 * and semantics compared to Scala 2 sealed trait hierarchies. By encapsulating raw Scala 3
 * enum code as a Tree, we can bypass treehugger's lack of enum support and inject the
 * required enum directly into the generated source output.
 */
final case class Scala3EnumSourceCode(code: String) extends Tree

