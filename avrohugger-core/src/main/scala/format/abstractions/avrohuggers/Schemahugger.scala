package avrohugger
package format
package abstractions
package avrohuggers

import avrohugger.matchers.TypeMatcher
import avrohugger.stores.ClassStore
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
    restrictedFields: Boolean,
    targetScalaPartialVersion: String
  ): List[Tree]

}
