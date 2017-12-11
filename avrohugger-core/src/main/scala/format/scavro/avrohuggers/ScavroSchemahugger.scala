package avrohugger
package format
package scavro
package avrohuggers

import format.abstractions.avrohuggers.Schemahugger
import trees.{ ScavroCaseClassTree, ScavroObjectTree, ScavroTraitTree }
import matchers.TypeMatcher
import stores.ClassStore
import types._

import org.apache.avro.Schema
import org.apache.avro.Schema.Type.{ ENUM, RECORD }

import treehugger.forest._
import definitions._
import treehuggerDSL._

object ScavroSchemahugger extends Schemahugger{

  def toTrees(
    classStore: ClassStore,
    namespace: Option[String],
    schema: Schema,
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]],
    restrictedFields: Boolean): List[Tree] = {
    val ScalaClass = RootClass.newClass(schema.getName)
    val JavaClass = RootClass.newClass("J" + schema.getName)
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
          maybeFlags,
          restrictedFields)
        val companionDef = ScavroObjectTree.toCompanionDef(
          classStore,
          schema,
          ScalaClass,
          JavaClass,
          typeMatcher,
          maybeFlags)
        List(caseClassDef, companionDef)
      case ENUM => typeMatcher.avroScalaTypes.enum match {
        case JavaEnum =>
          List.empty
        case ScalaCaseObjectEnum =>
          ScavroTraitTree.toCaseObjectEnumDef(schema, maybeBaseTrait)
        case ScalaEnumeration =>
          val objectDef = ScavroObjectTree.toScalaEnumDef(
            classStore,
            schema,
            maybeBaseTrait,
            maybeFlags)
          List(objectDef)
      }
      case _ =>
        sys.error("Only RECORD and ENUM can be top-level definitions")
    }
  }

}
