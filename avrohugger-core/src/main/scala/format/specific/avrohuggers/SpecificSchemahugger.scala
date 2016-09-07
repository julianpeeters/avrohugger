package avrohugger
package format
package specific
package avrohuggers

import format.abstractions.avrohuggers.Schemahugger
import trees.{ SpecificCaseClassTree, SpecificObjectTree }
import matchers.TypeMatcher
import stores.ClassStore

import org.apache.avro.Schema

import treehugger.forest.Tree

object SpecificSchemahugger extends Schemahugger {

  def toTrees(
    classStore: ClassStore,
    namespace: Option[String],
    schema: Schema,
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]],
    restrictedFields: Boolean): List[Tree] = {

    val caseClassDef = SpecificCaseClassTree.toCaseClassDef(
      classStore,
      namespace,
      schema,
      typeMatcher,
      maybeBaseTrait,
      maybeFlags,
      restrictedFields)

    val companionDef = SpecificObjectTree.toCaseCompanionDef(schema, maybeFlags)

    List(caseClassDef, companionDef)
  }

}
