package avrohugger
package format
package standard
package converters

import trees.{ StandardCaseClassTree, StandardObjectTree, StandardTraitTree }
import docs.ScalaDocGen

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Type.{ ENUM, RECORD }

import treehugger.forest._
import definitions._
import treehuggerDSL._

import scala.collection.JavaConversions._

object AvroConverter {
  
  def protocolToTrees(
    classStore: ClassStore,
    namespace: Option[String],
    protocol: Protocol,
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]]): List[Tree] = {
      
    val allTypes = protocol.getTypes.toList
    allTypes.foreach(schema => 
      StandardTypeRegistrar.registerType(schema, classStore))
    val localSubTypes = Standard.getLocalSubtypes(protocol)
    if (localSubTypes.length > 1) {
      val maybeNewBaseTrait = Some(protocol.getName)
      val maybeNewFlags = Some(List(Flags.FINAL.toLong))
      val traitDef = StandardTraitTree.toADTRootDef(protocol)
      traitDef +: localSubTypes.flatMap(schema =>
        StandardTreehugger.asTopLevelDef(
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
        StandardTreehugger.asTopLevelDef(
          classStore,
          namespace,
          Left(schema),
          typeMatcher,
          maybeBaseTrait,
          maybeFlags))
    }
  }
  
  
  def schemaToTrees(
    classStore: ClassStore,
    namespace: Option[String],
    schema: Schema,
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]]): List[Tree] = { // as case class definition
      
    StandardTypeRegistrar.registerType(schema, classStore)
    
    schema.getType match {
      case RECORD => 
        val classDef = StandardCaseClassTree.toCaseClassDef(
          classStore,
          namespace,
          schema,
          typeMatcher,
          maybeBaseTrait,
          maybeFlags)
        List(classDef)
      case ENUM => 
        val objectDef = StandardObjectTree.toObjectDef(
          classStore,
          schema,
          maybeBaseTrait,
          maybeFlags)
        List(objectDef)
      case _ => sys.error("Only RECORD or ENUM can be toplevel definitions")
    }
  }
  
  
  
}