package avrohugger
package format
package standard
package trees

import generators.ScalaDocGenerator
import matchers.DefaultValueMatcher
import matchers.TypeMatcher
import stores.ClassStore

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.Schema

import scala.collection.JavaConverters._

object StandardCaseClassTree {

  def toCaseClassDef(
    classStore: ClassStore,
    namespace: Option[String],
    schema: Schema,
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]],
    restrictedFields: Boolean) = {

    // register new type
    val classSymbol = RootClass.newClass(schema.getName)
    val avroFields = schema.getFields.asScala.toList

    val shouldGenerateSimpleClass = restrictedFields && avroFields.size > 22

    val params: List[ValDef] = avroFields.map(f => {
      val fieldName = FieldRenamer.rename(f.name)
      val isMangled = FieldRenamer.isMangled(f.name())
      val fieldType = typeMatcher.toScalaType(classStore, namespace, f.schema)
      val defaultValue = DefaultValueMatcher.getDefaultValue(
        classStore,
        namespace,
        f,
        typeMatcher)
        PARAM(fieldName, fieldType) := defaultValue
    })

    // There could be base traits, flags, or both, and could have no fields
    val caseClassDef = (maybeBaseTrait, maybeFlags) match {
      case (Some(baseTrait), Some(flags)) => {
        if (shouldGenerateSimpleClass) {
          CLASSDEF(classSymbol)
            .withFlags(flags:_*)
            .withParams(params)
            .withParents(baseTrait)
        }
        else if (!avroFields.isEmpty) {
          CASECLASSDEF(classSymbol)
            .withFlags(flags:_*)
            .withParams(params)
            .withParents(baseTrait)
        }
        else { // in case of empty record
          CASECLASSDEF(classSymbol)
            .withFlags(flags:_*)
            .withParams(PARAM(""))
            .withParents(baseTrait)
        }
      }
      case (Some(baseTrait), None) => {
        if (shouldGenerateSimpleClass) {
          CLASSDEF(classSymbol)
            .withParams(params)
            .withParents(baseTrait)
        }
        else if (!avroFields.isEmpty) {
          CASECLASSDEF(classSymbol)
            .withParams(params)
            .withParents(baseTrait)
        }
        else { // in case of empty record
          CASECLASSDEF(classSymbol)
            .withParams(PARAM(""))
            .withParents(baseTrait)
        }
      }
      case (None, Some(flags)) => {
        if (shouldGenerateSimpleClass) {
          CLASSDEF(classSymbol)
            .withFlags(flags:_*)
            .withParams(params)
            .withParents("Serializable")
        }
        else if (!avroFields.isEmpty) {
          CASECLASSDEF(classSymbol)
            .withFlags(flags:_*)
            .withParams(params)
        }
        else { // in case of empty record
          CASECLASSDEF(classSymbol)
            .withFlags(flags:_*)
            .withParams(PARAM(""))
        }
      }
      case (None, None) => {
        if (shouldGenerateSimpleClass) {
          CLASSDEF(classSymbol)
            .withParams(params)
            .withParents("Serializable")
        }
        else if (!avroFields.isEmpty) {
          CASECLASSDEF(classSymbol)
            .withParams(params)
        }
        else { // in case of empty record
          CASECLASSDEF(classSymbol)
            .withParams(PARAM(""))
        }
      }
    }

    val mangledClass = if(avroFields.count(f => FieldRenamer.isMangled(f.name())) > 0) {
      caseClassDef := BLOCK(
        avroFields.flatMap { field =>
          if(FieldRenamer.isMangled(field.name()))
            Some(VAL(FieldRenamer.backtick(field.name()), typeMatcher.toScalaType(classStore, namespace, field.schema)) := REF(FieldRenamer.mangle(field.name())))
          else None
        }
      )
    } else caseClassDef.tree

    val classTree = mangledClass

    val treeWithScalaDoc = ScalaDocGenerator.docToScalaDoc(
      Left(schema),
      classTree)

    treeWithScalaDoc
  }

}
