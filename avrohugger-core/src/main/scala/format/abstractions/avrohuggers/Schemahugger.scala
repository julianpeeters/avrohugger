package avrohugger
package format
package abstractions
package avrohuggers

import stores.ClassStore
import matchers.TypeMatcher

import org.apache.avro.Schema

import treehugger.forest.Tree

trait Schemahugger {

  def toTrees(
    classStore: ClassStore,
    namespace: Option[String],
    schema: Schema,
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]],
    restrictedFields: Boolean): List[Tree]

}
