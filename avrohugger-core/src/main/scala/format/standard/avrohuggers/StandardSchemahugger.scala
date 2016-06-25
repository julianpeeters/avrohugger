package avrohugger
package format
package standard
package avrohuggers

import format.abstractions.avrohuggers.Schemahugger
import trees.{ StandardCaseClassTree, StandardObjectTree, StandardTraitTree }
import matchers.TypeMatcher
import stores.ClassStore

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Type.{ ENUM, RECORD }

import treehugger.forest._
import definitions._
import treehuggerDSL._

object StandardSchemahugger extends Schemahugger {
  
  def toTrees(
    classStore: ClassStore,
    namespace: Option[String],
    schema: Schema,
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]]): List[Tree] = { // as case class definition

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
      case ENUM => typeMatcher.customEnumStyleMap.get("enum") match {
        case Some("java enum") =>
          List.empty
        case Some("case object") =>
          StandardTraitTree.toCaseObjectEnumDef(schema, maybeBaseTrait)
        case _ =>
          val objectDef = StandardObjectTree.toScalaEnumDef(
            classStore,
            schema,
            maybeBaseTrait,
            maybeFlags)
          List(objectDef)
      }
      case _ => sys.error("Only RECORD or ENUM can be toplevel definitions")
    }
  }
  
}