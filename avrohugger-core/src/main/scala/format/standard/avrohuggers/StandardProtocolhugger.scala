package avrohugger
package format
package standard
package avrohuggers

import generators.ScalaDocGenerator
import trees.StandardTraitTree
import matchers.TypeMatcher
import stores.ClassStore
import types._
import org.apache.avro.{ Protocol, Schema }

import treehugger.forest._
import definitions._
import treehuggerDSL._

import format.abstractions.avrohuggers.Protocolhugger


object StandardProtocolhugger extends Protocolhugger {

  def toTrees(
    classStore: ClassStore,
    namespace: Option[String],
    protocol: Protocol,
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]],
    restrictedFields: Boolean): List[Tree] = {

    val name: String = protocol.getName

    val localSubTypes = getLocalSubtypes(protocol)

    val adtSubTypes = typeMatcher.avroScalaTypes.enum match {
      case JavaEnum => localSubTypes.filterNot(isEnum)
      case ScalaCaseObjectEnum => localSubTypes
      case ScalaEnumeration => localSubTypes
    }

    if (adtSubTypes.length > 1) {
      val maybeNewBaseTrait = Some(name)
      val maybeNewFlags = Some(List(Flags.FINAL.toLong))
      val traitDef = StandardTraitTree.toADTRootDef(protocol, typeMatcher)
      traitDef +: adtSubTypes.flatMap(schema => {
        StandardSchemahugger.toTrees(
          classStore,
          namespace,
          schema,
          typeMatcher,
          maybeNewBaseTrait,
          maybeNewFlags,
          restrictedFields)
      })
    }
    // if only one Scala type is defined, then don't generate sealed trait
    else {
      // no sealed trait tree, but could still need a top-level doc
      val docTrees = {
        Option(protocol.getDoc) match {
          case Some(doc) =>
            List(ScalaDocGenerator.docToScalaDoc(Right(protocol), EmptyTree))
          case None => List.empty
        }
      }
      docTrees ::: localSubTypes.flatMap(schema => {
        StandardSchemahugger.toTrees(
          classStore,
          namespace,
          schema,
          typeMatcher,
          maybeBaseTrait,
          maybeFlags,
          restrictedFields)
      })
    }
  }

}
