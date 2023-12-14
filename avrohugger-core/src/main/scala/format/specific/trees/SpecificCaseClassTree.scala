package avrohugger
package format
package specific
package trees

import avrohugger.format.specific.methods._
import avrohugger.generators.ScalaDocGenerator
import avrohugger.matchers.{DefaultParamMatcher, DefaultValueMatcher, TypeMatcher}
import avrohugger.stores._
import treehugger.forest._
import definitions._
import org.apache.avro.{LogicalTypes, Schema}
import treehugger.forest
import treehuggerDSL._

import scala.jdk.CollectionConverters._

object SpecificCaseClassTree {

  def toCaseClassDef(
    classStore: ClassStore,
    namespace: Option[String],
    schema: Schema,
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]],
    restrictedFields: Boolean,
    targetScalaPartialVersion: String) = {

    val classSymbol = RootClass.newClass(schema.getName)
    val avroFields = schema.getFields().asScala.toList

    val shouldGenerateSimpleClass = restrictedFields && avroFields.size > 22

    // generate list of constructor parameters
    val params: List[ValDef] = avroFields.map { f =>
      val fieldName = FieldRenamer.rename(f.name)
      val fieldType = typeMatcher.toScalaType(classStore, namespace, f.schema)
      val defaultValue = DefaultValueMatcher.getDefaultValue(
        classStore,
        namespace,
        f,
        typeMatcher,
        fieldName == fieldType.safeToString)
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
      THIS APPLY(defaultParams)
    }

    // methods - first add an index the the schema's fields
    val indexedFields = avroFields.zipWithIndex.map(p => {
      val avroField = p._1
      val index = p._2
      IndexedField(avroField, index)
    })
    val defGetSchema = namespace.fold(GetSchemaGenerator(classSymbol).toDef)(ns => GetSchemaGenerator(RootClass.newClass(s"$ns.${classSymbol}")).toDef)
    val defGet = GetGenerator.toDef(indexedFields, classSymbol, typeMatcher, targetScalaPartialVersion)
    val defPut = PutGenerator.toDef(
      classStore,
      namespace,
      indexedFields,
      typeMatcher,
      classSymbol,
      targetScalaPartialVersion)

    val maybeFlagsWithCaseClassFinal =
      if (shouldGenerateSimpleClass) maybeFlags
      else maybeFlags.map { flags =>
        if (flags.contains(Flags.FINAL)) flags
        else flags :+ Flags.FINAL.toLong
      }

    val tooManyParams = params.length > 254
    val (constructorParams, fields) =  if(params.length > 254) (Nil, params) else(params, Nil)

    // define the class def with the members previously defined
    // There could be base traits, flags, or both, and could have no fields
    val caseClassDef = (maybeBaseTrait, maybeFlagsWithCaseClassFinal) match {
      case (Some(baseTrait), Some(flags)) =>
        if (tooManyParams) {
          CLASSDEF(classSymbol)
            .withFlags(flags: _*)
            .withParents(baseClass)
            .withParents(baseTrait)
        } else if (shouldGenerateSimpleClass) {
          CLASSDEF(classSymbol)
            .withFlags(flags:_*)
            .withParams(constructorParams)
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
        if (tooManyParams) {
          CLASSDEF(classSymbol)
            .withFlags(flags: _*)
            .withParents(baseClass)
        } else if (shouldGenerateSimpleClass) {
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
        if (tooManyParams) {
          CLASSDEF(classSymbol)
            .withParents(baseClass)
            .withParents("Serializable")
        } else  if (shouldGenerateSimpleClass) {
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


  def toFixedDef(
    schema: Schema,
    namespace: Option[String],
    maybeFlags: Option[List[Long]],
    schemaStore: SchemaStore,
    typeMatcher: TypeMatcher,
    classStore: ClassStore,
    targetScalaPartialVersion: String
  ) = {
    val classSymbol = RootClass.newClass(schema.getName)
    val defGetSchema = namespace.fold(GetSchemaGenerator(classSymbol).toDef)(ns => GetSchemaGenerator(RootClass.newClass(s"$ns.${classSymbol}")).toDef)
    val defReadExternal = DEF("readExternal", UnitClass).withParams(PARAM("in", TYPE_REF("java.io.ObjectInput"))) := BLOCK(
      REF(s"${schema.getFullName()}.READER$$").DOT("read").APPLY(THIS, REF("org.apache.avro.specific.SpecificData.getDecoder(in)")),
      PAREN()
    )
    val defWriteExternal = DEF("writeExternal", UnitClass).withParams(PARAM("out", TYPE_REF("java.io.ObjectOutput"))) := BLOCK(
      REF(s"${schema.getFullName()}.WRITER$$").DOT("write").APPLY(THIS, REF("org.apache.avro.specific.SpecificData.getEncoder(out)"))
    )
    val defBigDecimal = DEF("bigDecimal", TYPE_REF("BigDecimal")) := {
      val JavaBuffer = RootClass.newClass("java.nio.ByteBuffer")
      val resultExpr = schema.getLogicalType match {
        case decimal: LogicalTypes.Decimal => {
          val Decimal = RootClass.newClass("org.apache.avro.LogicalTypes.Decimal")
          Block(
            VAL("schema") := REF("getSchema"),
            VAL("decimalType") := REF("schema").DOT("getLogicalType").APPLY().AS(Decimal),
            REF("BigDecimal").APPLY(classSymbol.DOT("decimalConversion").DOT("fromBytes").APPLY(REF("buffer"),REF("schema"),REF("decimalType")))
          )
        }
        case _ => Block(
          VAL("dup") := REF("buffer").DOT("duplicate").APPLY(),
          VAL("array") := NEW("Array[Byte]", REF("dup").DOT("remaining")),
          REF("dup") DOT "get" APPLY(REF("array")),
          REF(schema.getFullName()).APPLY(REF("array"))
        )
      }
      val bufferConversion = CASE(ID("buffer") withType (JavaBuffer)) ==> resultExpr
      REF("java.nio.ByteBuffer").DOT("wrap").APPLY(REF("bytes")) MATCH bufferConversion
    }

    val baseClass = RootClass.newClass("org.apache.avro.specific.SpecificFixed")

    schema.getLogicalType() match {
      case decimal: LogicalTypes.Decimal =>
        CASECLASSDEF(schema.getName)
          .withFlags(Flags.FINAL)
          .withParams()
          .withParents(baseClass) := BLOCK(
            // defCtor,
            // defNoArgCtor,
            defGetSchema,
            defBigDecimal,
            defReadExternal,
            defWriteExternal
          )
      case _ =>
        CASECLASSDEF(schema.getName)
          .withFlags(Flags.FINAL)
          .withParams()
          .withParents(baseClass) := BLOCK(
            // defCtorDefault,
            // defNoArgCtor,
            defGetSchema,
            defReadExternal,
            defWriteExternal
          )
    }

  
  }

}
