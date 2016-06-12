package avrohugger
package format
package standard
package avrohuggers

import trees.StandardTraitTree
import matchers.TypeMatcher
import stores.ClassStore

import org.apache.avro.{ Protocol, Schema }

import treehugger.forest._
import definitions._
import treehuggerDSL._

import format.abstractions.avrohuggers.Protocolhugger

import scala.collection.JavaConversions._

object StandardProtocolhugger extends Protocolhugger {
  
  def toTrees(
    classStore: ClassStore,
    namespace: Option[String],
    protocol: Protocol,
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]]): List[Tree] = {
      
    val name: String = protocol.getName
    
    val localSubTypes = Standard.getLocalSubtypes(protocol)
    
    if (localSubTypes.length > 1) {
      val maybeNewBaseTrait = Some(name)
      val maybeNewFlags = Some(List(Flags.FINAL.toLong))
      val traitDef = StandardTraitTree.toADTRootDef(protocol)
      traitDef +: localSubTypes.flatMap(schema =>
        StandardScalaTreehugger.asTopLevelDefs(
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
        StandardScalaTreehugger.asTopLevelDefs(
          classStore,
          namespace,
          Left(schema),
          typeMatcher,
          maybeBaseTrait,
          maybeFlags))
    }
  }
  
}