package avrohugger
package format
package scavro
package avrohuggers

import format.abstractions.avrohuggers.Protocolhugger
import matchers.TypeMatcher
import stores.ClassStore
import trees.ScavroTraitTree

import org.apache.avro.Protocol

import treehugger.forest._
import definitions._
import treehuggerDSL._

import scala.collection.JavaConversions._

object ScavroProtocolhugger extends Protocolhugger {
  
  def toTrees(
    classStore: ClassStore,
    namespace: Option[String],
    protocol: Protocol,
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]]): List[Tree] = {
      
    val name: String = protocol.getName
    val maybeNewBaseTrait = Some(name)
    val maybeNewFlags = Some(List(Flags.FINAL.toLong))
    val traitDef = ScavroTraitTree.toADTRootDef(protocol)
    val localSubTypes = getLocalSubtypes(protocol)
    if (localSubTypes.length > 1) {
      traitDef +: localSubTypes.flatMap(schema =>
        ScavroScalaTreehugger.asTopLevelDefs(
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
            List(ScalaDocGenerator.docToScalaDoc(Right(protocol), EmptyTree))
          case None => List.empty
        }	
      }
      docTrees ::: localSubTypes.flatMap(schema =>
        ScavroScalaTreehugger.asTopLevelDefs(
          classStore,
          namespace,
          Left(schema),
          typeMatcher,
          maybeBaseTrait,
          maybeFlags))
    }
  }
  
}