package avrohugger
package format
package standard
package trees

import generators.ScalaDocGenerator
import matchers.DefaultValueMatcher
import matchers.TypeMatcher
import matchers.custom.CustomTypeMatcher
import stores.ClassStore

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.Schema

import scala.jdk.CollectionConverters._
import org.apache.avro.LogicalTypes

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
    val avroFields = schema.getFields().asScala.toList

    val shouldGenerateSimpleClass = restrictedFields && avroFields.size > 22

    val params: List[ValDef] = avroFields.map(f => {
      val fieldName = FieldRenamer.rename(f.name)
      val fieldType = typeMatcher.toScalaType(classStore, namespace, f.schema, fieldName == f.schema.getName())
      val defaultValue = DefaultValueMatcher.getDefaultValue(
        classStore,
        namespace,
        f,
        typeMatcher,
        fieldName == f.schema.getName()
      )
      PARAM(fieldName, fieldType) := defaultValue
    })

    val maybeFlagsWithCaseClassFinal =
      if (shouldGenerateSimpleClass) maybeFlags
      else maybeFlags.map { flags =>
        if (flags.contains(Flags.FINAL)) flags
        else flags :+ Flags.FINAL.toLong
      }

    // There could be base traits, flags, or both, and could have no fields
    val caseClassDef = (maybeBaseTrait, maybeFlagsWithCaseClassFinal) match {
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
            .withFlags(Flags.FINAL)
            .withParents(baseTrait)
        }
        else { // in case of empty record
          CASECLASSDEF(classSymbol)
            .withParams(PARAM(""))
            .withFlags(Flags.FINAL)
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
            .withFlags(Flags.FINAL)
        }
        else { // in case of empty record
          CASECLASSDEF(classSymbol)
            .withParams(PARAM(""))
            .withFlags(Flags.FINAL)
        }
      }
    }

    val classTree = caseClassDef.tree

    val treeWithScalaDoc = ScalaDocGenerator.docToScalaDoc(
      Left(schema),
      classTree)

    treeWithScalaDoc
  }


  def toFixedClassDef(
    classStore: ClassStore,
    namespace: Option[String],
    schema: Schema,
    typeMatcher: TypeMatcher) = {

    // register new type
    val classSymbol = RootClass.newClass(schema.getName)

    val params: List[ValDef] = {
      val fieldName = schema.getLogicalType() match {
        case _ : LogicalTypes.Decimal => "bigDecimal"
        case _ => "bytes"
      }
      val fieldType = CustomTypeMatcher.checkCustomDecimalType(typeMatcher.avroScalaTypes.decimal, schema)
      val field = PARAM(fieldName, fieldType)
      List(field)
    }

    val caseClassDef =
      CASECLASSDEF(classSymbol)
        .withParams(params)
        .withFlags(Flags.FINAL)

    val classTree = caseClassDef.tree

    val treeWithScalaDoc = ScalaDocGenerator.docToScalaDoc(
      Left(schema),
      classTree)

    treeWithScalaDoc
  }


}
