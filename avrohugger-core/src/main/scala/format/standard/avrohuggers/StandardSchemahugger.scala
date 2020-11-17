package avrohugger
package format
package standard
package avrohuggers

import format.abstractions.avrohuggers.Schemahugger
import trees.{ StandardCaseClassTree, StandardObjectTree, StandardTraitTree }
import matchers.TypeMatcher
import stores.{ClassStore, SchemaStore}
import types._

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Type.{ ENUM, FIXED, RECORD }

import treehugger.forest._
import definitions._
import treehuggerDSL._

object StandardSchemahugger extends Schemahugger {

  def toTrees(
    schemaStore: SchemaStore,
    classStore: ClassStore,
    namespace: Option[String],
    schema: Schema,
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]],
    restrictedFields: Boolean): List[Tree] = { // as case class definition

    schema.getType match {
      case RECORD =>
        val classDef = StandardCaseClassTree.toCaseClassDef(
          classStore,
          namespace,
          schema,
          typeMatcher,
          maybeBaseTrait,
          maybeFlags,
          restrictedFields)
        val companionDef = StandardObjectTree.toCaseCompanionDef(
          schema,
          maybeFlags)
        typeMatcher.avroScalaTypes.record match {
          case ScalaCaseClass => List(classDef)
          case ScalaCaseClassWithSchema => List(classDef, companionDef)
        }
      case ENUM => typeMatcher.avroScalaTypes.enum match {
        case JavaEnum =>
          List.empty
        case ScalaCaseObjectEnum =>
          StandardTraitTree.toCaseObjectEnumDef(schema, maybeBaseTrait)
        case ScalaEnumeration =>
          val objectDef = StandardObjectTree.toScalaEnumDef(
            classStore,
            schema,
            maybeBaseTrait,
            maybeFlags)
          List(objectDef)
        case EnumAsScalaString => List.empty
      }
      case FIXED => List.empty
      case _ => sys.error("Only RECORD or ENUM or FIXED can be toplevel definitions")
    }
  }

}
