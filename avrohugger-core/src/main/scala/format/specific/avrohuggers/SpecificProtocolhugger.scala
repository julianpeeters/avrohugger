package avrohugger
package format
package specific
package avrohuggers

import format.abstractions.avrohuggers.Protocolhugger
import generators.ScalaDocGenerator
import trees.{ SpecificObjectTree, SpecificTraitTree }
import matchers.TypeMatcher
import stores.ClassStore

import org.apache.avro.{ Protocol, Schema }

import treehugger.forest._
import definitions._
import treehuggerDSL._

import scala.collection.JavaConverters._

object SpecificProtocolhugger extends Protocolhugger {

  def toTrees(
    classStore: ClassStore,
    namespace: Option[String],
    protocol: Protocol,
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]],
    restrictedFields: Boolean): List[Tree] = {

    val name: String = protocol.getName
    val messages = protocol.getMessages.asScala.toMap
    val maybeProtocolDoc = Option(protocol.getDoc)

    if (messages.isEmpty) {
      val localSubTypes = getLocalSubtypes(protocol)
      // protocols with more than 1 schema defined (Java Enums don't count) and
      // without messages are generated as ADTs
      val localNonEnums = localSubTypes.filterNot(isEnum)

      if (localNonEnums.length > 1) {
        val maybeNewBaseTrait = Some(name)
        val maybeNewFlags = Some(List(Flags.FINAL.toLong))
        val sealedTraitDef = SpecificTraitTree.toADTRootDef(protocol)
        val subTypeDefs = localNonEnums.flatMap(schema => {
          SpecificSchemahugger.toTrees(
            classStore,
            namespace,
            schema,
            typeMatcher,
            maybeNewBaseTrait,
            maybeNewFlags,
            restrictedFields)
        })
        sealedTraitDef +: subTypeDefs
      }
      // if only one Scala type is defined, then don't generate sealed trait
      else {
        // no sealed trait tree, but could still need a protocol doc at top
        val docTrees = {
          Option(protocol.getDoc) match {
            case Some(doc) =>
              List(ScalaDocGenerator.docToScalaDoc(Right(protocol), EmptyTree))
            case None => List.empty
          }
        }
        docTrees ::: localNonEnums.flatMap(schema => {
          SpecificSchemahugger.toTrees(
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
    else {
      val rpcTraitDef = SpecificTraitTree.toRPCTraitDef(
        classStore,
        namespace,
        protocol,
        typeMatcher)
      val companionDef = SpecificObjectTree.toTraitCompanionDef(protocol)
      List(rpcTraitDef, companionDef)
    }
  }

}
