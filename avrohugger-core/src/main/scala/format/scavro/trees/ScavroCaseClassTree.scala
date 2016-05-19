package avrohugger
package format
package scavro
package trees

import converters.JavaConverter
import util.ScalaDocGen

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.Schema

import scala.collection.JavaConversions._
import scala.language.postfixOps

object ScavroCaseClassTree {
  
  def toCaseClassDef(
    classStore: ClassStore, 
    namespace: Option[String], 
    schema: Schema,
    ScalaClass: Symbol,
    JavaClass: Symbol, 
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]]) = {

    val mixin = TYPE_REF(REF("AvroSerializeable"))
    val avroFields = schema.getFields.toList

    val scalaClassParams: List[ValDef] = avroFields.map { f =>
      val fieldName = f.name
      val fieldType = typeMatcher.toScalaType(classStore, namespace, f.schema)
      PARAM(fieldName, fieldType): ValDef
    }

    val scalaClassAccessors: List[Tree] = avroFields.map(field => {
      val javaConverter = new JavaConverter(classStore, namespace, typeMatcher)
      javaConverter.convertToJava(field.schema, REF(field.name))
    })

    // There could be base traits, flags, or both, and could have no fields
    val caseClassDef = (maybeBaseTrait, maybeFlags) match {
      case (Some(baseTrait), Some(flags)) =>
        if (!avroFields.isEmpty) {
          CASECLASSDEF(ScalaClass)
            .withFlags(flags:_*)
            .withParams(scalaClassParams)
            .withParents(mixin)
            .withParents(baseTrait)
        }
        else { // for "empty" records: empty params and no no-arg ctor
          CASECLASSDEF(ScalaClass)
            .withFlags(flags:_*)
            .withParams(PARAM(""))
            .withParents(mixin)
            .withParents(baseTrait)
        }
      case (Some(baseTrait), None) =>
        if (!avroFields.isEmpty) {
          CASECLASSDEF(ScalaClass)
            .withParams(scalaClassParams)
            .withParents(mixin)
            .withParents(baseTrait)
        }
        else { // for "empty" records: empty params and no no-arg ctor
          CASECLASSDEF(ScalaClass)
            .withParams(PARAM(""))
            .withParents(mixin)
            .withParents(baseTrait)
        }
      case (None, Some(flags)) =>
        if (!avroFields.isEmpty) {
          CASECLASSDEF(ScalaClass)
            .withFlags(flags:_*)
            .withParams(scalaClassParams)
            .withParents(mixin) 
        }
        else { // for "empty" records: empty params and no no-arg ctor
          CASECLASSDEF(ScalaClass)
            .withFlags(flags:_*)
            .withParams(PARAM(""))
            .withParents(mixin) 
        }
      case (None, None) =>
        if (!avroFields.isEmpty) {
          CASECLASSDEF(ScalaClass)
            .withParams(scalaClassParams)
            .withParents(mixin) 
        }
        else { // for "empty" records: empty params and no no-arg ctor
          CASECLASSDEF(ScalaClass)
            .withParams(PARAM(""))
            .withParents(mixin) 
        }
    }

    // Defines case class, adapted from https://github.com/oysterbooks/scavro/blob/code_generation/src/main/scala/oyster/scavro/plugin/ScalaCodegen.scala
    val caseClassTree = caseClassDef := BLOCK (
      TYPEVAR("J") := REF(JavaClass),
      DEF("toAvro", JavaClass) withFlags(Flags.OVERRIDE) := BLOCK(
        NEW(JavaClass, scalaClassAccessors: _*)
      )
    ) : Tree

    val treeWithScalaDoc = ScalaDocGen.docToScalaDoc(Left(schema),caseClassTree)
    treeWithScalaDoc
  }

}
