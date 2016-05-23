package avrohugger
package format
package specific
package avrohuggers

import trees.{ SpecificObjectTree, SpecificTraitTree }
import docs.ScalaDocGen

import org.apache.avro.{ Protocol, Schema }

import treehugger.forest._
import definitions._
import treehuggerDSL._

import scala.collection.JavaConversions._

object Protocolhugger {
  
  def toTrees(
    classStore: ClassStore,
    namespace: Option[String],
    protocol: Protocol,
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]]): List[Tree] = {
      
    val name: String = protocol.getName
    val ns: String = protocol.getNamespace
    val allTypes = protocol.getTypes.toList
    val messages = protocol.getMessages.toMap
    val maybeProtocolDoc = Option(protocol.getDoc)
    allTypes.foreach(schema => 
      SpecificTypeRegistrar.registerType(schema, classStore))
    def isEnum(schema: Schema) = schema.getType == Schema.Type.ENUM
    def isTopLevelNamespace(schema: Schema) = schema.getNamespace == ns
    if (messages.isEmpty) {
      val localSubTypes = allTypes.filter(isTopLevelNamespace)
      // protocols with only 1 schema are ADTs (Java Enums don't count)
      if (localSubTypes.filterNot(isEnum).length > 1) {
        val maybeNewBaseTrait = Some(name)
        val maybeNewFlags = Some(List(Flags.FINAL.toLong))
        val sealedTraitDef = SpecificTraitTree.toADTRootDef(protocol)
        sealedTraitDef +: localSubTypes.filterNot(isEnum).flatMap(schema =>
          SpecificScalaTreehugger.asTopLevelDef(
            classStore,
            namespace,
            Left(schema),
            typeMatcher,
            maybeNewBaseTrait,
            maybeNewFlags))
      }
      // if only one Scala type is defined, then don't generate sealed trait
      else {
        // no sealed trait tree, but could still need a top-level doc
        val docTrees = {
          Option(protocol.getDoc) match {
            case Some(doc) => 
              List(ScalaDocGen.docToScalaDoc(Right(protocol), EmptyTree))
            case None => List.empty
          }	
        } 
        docTrees ::: localSubTypes.filterNot(isEnum).flatMap(schema =>
        SpecificScalaTreehugger.asTopLevelDef(
          classStore,
          namespace,
          Left(schema),
          typeMatcher,
          maybeBaseTrait,
          maybeFlags))
        }
    }
    else {
      val traitDef = SpecificTraitTree.toTraitDef(
        classStore,
        namespace,
        protocol,
        typeMatcher)
      val companionDef = SpecificObjectTree.toCompanionDef(protocol)
      List(traitDef, companionDef)
    }
  }
  
}