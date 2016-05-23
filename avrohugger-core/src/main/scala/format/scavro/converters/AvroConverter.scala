package avrohugger
package format
package scavro
package converters

import trees.{ ScavroCaseClassTree, ScavroObjectTree, ScavroTraitTree }
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
      ScavroTypeRegistrar.registerType(schema, classStore))
    val localSubtypes = Scavro.getLocalSubtypes(protocol)
    if (localSubtypes.length > 1) {
      val maybeNewBaseTrait = Some(protocol.getName)
      val maybeNewFlags = Some(List(Flags.FINAL.toLong))
      val traitDef = ScavroTraitTree.toADTRootDef(protocol)
      traitDef +: localSubtypes.flatMap(schema =>
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
      docTrees ::: localSubtypes.flatMap(schema =>
        ScavroTreehugger.asTopLevelDef(
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
    // register classes
    val ScalaClass = RootClass.newClass(schema.getName)
    val JavaClass = RootClass.newClass("J" + schema.getName)
    ScavroTypeRegistrar.registerType(schema, classStore)
    schema.getType match {
      case RECORD =>
        val caseClassDef = ScavroCaseClassTree.toCaseClassDef(
          classStore,
          namespace,
          schema,
          ScalaClass,
          JavaClass,
          typeMatcher,
          maybeBaseTrait,
          maybeFlags)
        val companionDef = ScavroObjectTree.toCompanionDef(
          classStore,
          schema,
          ScalaClass,
          JavaClass,
          typeMatcher,
          maybeFlags)
        List(caseClassDef, companionDef)
      case ENUM =>
        val objectDef = ScavroObjectTree.toObjectDef(
          classStore,
          schema,
          maybeBaseTrait,
          maybeFlags)
        List(objectDef)
      case _ =>
        sys.error("Only RECORD and ENUM can be top-level definitions")
    }
  }
  
  
}