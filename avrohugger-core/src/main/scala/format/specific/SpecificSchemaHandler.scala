package avrohugger
package format
package specific

import trees.{ SpecificCaseClassTree, SpecificObjectTree }

import org.apache.avro.Schema

import treehugger.forest._
import definitions._
import treehuggerDSL._

object SpecificSchemaHandler {
  
  
  def toTrees(
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