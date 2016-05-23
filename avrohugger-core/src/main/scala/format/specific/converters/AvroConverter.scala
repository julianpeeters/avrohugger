package avrohugger
package format
package specific
package converters

import trees.{ SpecificCaseClassTree, SpecificObjectTree, SpecificTraitTree }
import docs.ScalaDocGen

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.{ Protocol, Schema }

import scala.language.postfixOps
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
    val messages = protocol.getMessages.toMap
    val maybeProtocolDoc = Option(protocol.getDoc)
    def isEnum(schema: Schema) = schema.getType == Schema.Type.ENUM
    allTypes.foreach(schema => 
      SpecificTypeRegistrar.registerType(schema, classStore))
    if (messages.isEmpty) {
      val localSubtypes = SpecificRecord.getLocalSubtypes(protocol)
      // protocols with only 1 schema are ADTs (Java Enums don't count)
      if (localSubtypes.filterNot(isEnum).length > 1) {
        val maybeNewBaseTrait = Some(protocol.getName)
        val maybeNewFlags = Some(List(Flags.FINAL.toLong))
        val sealedTraitDef = SpecificTraitTree.toADTRootDef(protocol)
        sealedTraitDef +: localSubtypes.filterNot(isEnum).flatMap(schema =>
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
        docTrees ::: localSubtypes.filterNot(isEnum).flatMap(schema =>
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
  

  def schemaToTrees(
    classStore: ClassStore,
    namespace: Option[String],
    schema: Schema,
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]]): List[Tree] = {
    
    SpecificTypeRegistrar.registerType(schema, classStore)
    
    val caseClassDef = SpecificCaseClassTree.toCaseClassDef(
      classStore,
      namespace,
      schema,
      typeMatcher,
      maybeBaseTrait,
      maybeFlags)
      
    val companionDef = SpecificObjectTree.toCompanionDef(schema, maybeFlags)
    
    List(caseClassDef, companionDef)
  }

}
