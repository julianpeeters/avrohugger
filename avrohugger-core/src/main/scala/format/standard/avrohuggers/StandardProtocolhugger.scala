package avrohugger
package format
package standard
package avrohuggers

import avrohugger.format.abstractions.avrohuggers.Protocolhugger
import avrohugger.format.standard.trees.StandardTraitTree
import avrohugger.generators.ScalaDocGenerator
import avrohugger.matchers.TypeMatcher
import avrohugger.stores.ClassStore
import avrohugger.types._
import org.apache.avro.Protocol
import treehugger.forest._


object StandardProtocolhugger extends Protocolhugger {

  def toTrees(
    classStore: ClassStore,
    namespace: Option[String],
    protocol: Protocol,
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]],
    restrictedFields: Boolean,
    targetScalaPartialVersion: String
  ): List[Tree] = {

    val name: String = protocol.getName
    val localSubTypes = getLocalSubtypes(protocol)

    val adtSubTypes = typeMatcher.avroScalaTypes.`enum` match {
      case JavaEnum => localSubTypes.filterNot(isEnum)
      case ScalaCaseObjectEnum => localSubTypes
      case Scala3Enum => localSubTypes
      case ScalaEnumeration => localSubTypes
      case EnumAsScalaString => localSubTypes.filterNot(isEnum)
    }

    if (adtSubTypes.length > 1 && typeMatcher.avroScalaTypes.protocol == types.ScalaADT) {
      val maybeNewBaseTrait = Some(name)
      val maybeNewFlags = Some(List(Flags.FINAL.toLong))
      val traitDef = StandardTraitTree.toADTRootDef(protocol, typeMatcher)
      traitDef +: adtSubTypes.flatMap(schema => {
        StandardSchemahugger.toTrees(classStore, namespace, schema, typeMatcher, maybeNewBaseTrait, maybeNewFlags, restrictedFields, targetScalaPartialVersion)
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
        StandardSchemahugger.toTrees(classStore, namespace, schema, typeMatcher, maybeBaseTrait, maybeFlags, restrictedFields, targetScalaPartialVersion)
      })
    }
  }

}
