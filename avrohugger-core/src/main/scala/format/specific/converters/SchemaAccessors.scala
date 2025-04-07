package avrohugger
package format
package specific
package converters

import treehugger.forest._
import treehuggerDSL._

object SchemaAccessors {
  def arrayAccessor(tree: Tree): Tree = tree.DOT("getElementType")

  def mapAccessor(tree: Tree): Tree = tree.DOT("getValueType")

  def unionAccessor(tree: Tree, memberName: String, asScalaBufferConverter: String): Tree =
    REF(asScalaBufferConverter).APPLY(
      tree.DOT("getTypes")
    ).DOT("asScala").DOT("toList").DOT("find").APPLY(
      REF("_").DOT("getFullName").INFIX("==").APPLY(LIT(memberName))
    ).DOT("get")
}