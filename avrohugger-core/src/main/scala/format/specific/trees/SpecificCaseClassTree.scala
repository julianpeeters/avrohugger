package avrohugger
package format
package specific
package trees

import format.specific.methods.{_}
import avrohugger.generators.ScalaDocGenerator
import avrohugger.matchers.{DefaultParamMatcher, DefaultValueMatcher, TypeMatcher}
import avrohugger.stores.ClassStore
import treehugger.forest._
import definitions._
import org.apache.avro.Schema
import treehuggerDSL._

import scala.collection.JavaConverters._

object SpecificCaseClassTree {

  def toCaseClassDef(
    classStore: ClassStore,
    namespace: Option[String],
    schema: Schema,
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]],
    restrictedFields: Boolean) = {

    val classSymbol = RootClass.newClass(schema.getName)
    val avroFields = schema.getFields.asScala.toList

    val shouldGenerateSimpleClass = restrictedFields && avroFields.size > 22

    // generate list of constructor parameters
    val params: List[ValDef] = avroFields.map { f =>
      val fieldName = FieldRenamer.rename(f.name)
      val fieldType = typeMatcher.toScalaType(classStore, namespace, f.schema)
      val defaultValue = DefaultValueMatcher.getDefaultValue(
        classStore,
        namespace,
        f,
        typeMatcher)
      VAR(fieldName, fieldType) := defaultValue
    }

    // extension
    val baseClassName = "org.apache.avro.specific.SpecificRecordBase"
    val baseClass = RootClass.newClass(baseClassName)

    // no-arg constructor: make arbitrary default if none is provided
    val defaultParams: List[Tree] = avroFields.zip(params).map(f => {
      val (avroField, defaultValue) = (f._1, f._2.rhs)
      if (defaultValue == EmptyTree)
        DefaultParamMatcher.asDefaultParam(classStore, avroField.schema, typeMatcher)
      else
        defaultValue
    })
    val defThis = DEFTHIS.withParams(PARAM("")).tree := {
      THIS APPLY(defaultParams:_*)
    }

    // methods - first add an index the the schema's fields
    val indexedFields = avroFields.zipWithIndex.map(p => {
      val avroField = p._1
      val index = p._2
      IndexedField(avroField, index)
    })
    val defGetSchema = GetSchemaGenerator(classSymbol).toDef
    val defGet = GetGenerator.toDef(indexedFields, classSymbol, typeMatcher)
    val defPut = PutGenerator.toDef(
      classStore,
      namespace,
      indexedFields,
      typeMatcher,
      classSymbol)

    val maybeFlagsWithCaseClassFinal =
      if (shouldGenerateSimpleClass) maybeFlags
      else maybeFlags.map { flags =>
        if (flags.contains(Flags.FINAL)) flags
        else flags :+ Flags.FINAL.toLong
      }


    // define the class def with the members previously defined
    // There could be base traits, flags, or both, and could have no fields
    val caseClassDef = (maybeBaseTrait, maybeFlagsWithCaseClassFinal) match {
      case (Some(baseTrait), Some(flags)) =>
        if (shouldGenerateSimpleClass) {
          CLASSDEF(classSymbol)
            .withFlags(flags:_*)
            .withParams(params)
            .withParents(baseClass)
            .withParents(baseTrait)
        }
        else if (avroFields.nonEmpty) {
          CASECLASSDEF(classSymbol)
            .withFlags(flags:_*)
            .withParams(params)
            .withParents(baseClass)
            .withParents(baseTrait)
        }
        else { // for "empty" records: empty params and no no-arg ctor
          CASECLASSDEF(classSymbol)
            .withFlags(flags:_*)
            .withParams(PARAM(""))
            .withParents(baseClass)
            .withParents(baseTrait)
        }
      case (Some(baseTrait), None) =>
        if (!avroFields.isEmpty) {
          CASECLASSDEF(classSymbol)
            .withParams(params)
            .withFlags(Flags.FINAL)
            .withParents(baseClass)
            .withParents(baseTrait)
        }
        else { // for "empty" records: empty params and no no-arg ctor
          CASECLASSDEF(classSymbol)
            .withParams(PARAM(""))
            .withFlags(Flags.FINAL)
            .withParents(baseClass)
            .withParents(baseTrait)
        }
      case (None, Some(flags)) =>
        if (shouldGenerateSimpleClass) {
          CLASSDEF(classSymbol)
            .withFlags(flags:_*)
            .withParams(params)
            .withParents(baseClass)
            .withParents("Serializable")
        }
        else if (avroFields.nonEmpty) {
          CASECLASSDEF(classSymbol)
            .withFlags(flags:_*)
            .withParams(params)
            .withParents(baseClass)
        }
        else { // for "empty" records: empty params and no no-arg ctor
          CASECLASSDEF(classSymbol)
            .withFlags(flags:_*)
            .withParams(PARAM(""))
            .withParents(baseClass)
        }
      case (None, None) =>
        if (shouldGenerateSimpleClass) {
          CLASSDEF(classSymbol)
            .withParams(params)
            .withParents(baseClass)
            .withParents("Serializable")
        }
        else if (!avroFields.isEmpty) {
          CASECLASSDEF(classSymbol)
            .withFlags(Flags.FINAL)
            .withParams(params)
            .withParents(baseClass)
        }
        else { // for "empty" records: empty params and no no-arg ctor
          CASECLASSDEF(classSymbol)
            .withParams(PARAM(""))
            .withParents(baseClass)
        }
    }

    val caseClassTree = {
      // for "empty" records: empty params and no no-arg ctor
      if (!avroFields.isEmpty) caseClassDef := BLOCK(
        defThis,
        defGet,
        defPut,
        defGetSchema)
     else caseClassDef := BLOCK(
        defGet,
        defPut,
        defGetSchema)
    }

    val treeWithScalaDoc = ScalaDocGenerator.docToScalaDoc(
      Left(schema),
      caseClassTree)

    treeWithScalaDoc

  }

}
