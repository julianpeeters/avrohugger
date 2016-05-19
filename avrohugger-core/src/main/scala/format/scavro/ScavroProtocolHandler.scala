package avrohugger
package format
package scavro

import trees.ScavroTraitTree
import util.ScalaDocGen

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Type.{ ENUM, RECORD }

import treehugger.forest._
import definitions._
import treehuggerDSL._

import scala.collection.JavaConversions._

object ScavroProtocolHandler {
  
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
    allTypes.foreach(schema =>
      ScavroTypeRegistrar.registerType(schema, classStore))
    def isTopLevelNamespace(schema: Schema) = schema.getNamespace == ns
    val maybeNewBaseTrait = Some(name)
    val maybeNewFlags = Some(List(Flags.FINAL.toLong))
    val traitDef = ScavroTraitTree.toADTRootDef(protocol)
    val localSubTypes = allTypes.filter(isTopLevelNamespace)
    if (localSubTypes.length > 1) {
      traitDef +: localSubTypes.flatMap(schema =>
        ScavroTreehugger.asTopLevelDef(
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
      docTrees ::: localSubTypes.flatMap(schema =>
        ScavroTreehugger.asTopLevelDef(
          classStore,
          namespace,
          Left(schema),
          typeMatcher,
          maybeBaseTrait,
          maybeFlags))
    }
  }
  
}