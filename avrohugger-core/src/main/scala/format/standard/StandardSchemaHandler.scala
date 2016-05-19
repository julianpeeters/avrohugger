package avrohugger
package format
package standard

import trees.{ StandardCaseClassTree, StandardObjectTree }

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Type.{ ENUM, RECORD }

import treehugger.forest._
import definitions._
import treehuggerDSL._

object StandardSchemaHandler {
  
  def toTrees(
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