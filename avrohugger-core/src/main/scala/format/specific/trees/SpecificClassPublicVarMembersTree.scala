package avrohugger
package format
package specific
package trees

import avrohugger.format.specific.methods._
import avrohugger.matchers.{DefaultValueMatcher, TypeMatcher}
import avrohugger.stores._
import treehugger.forest._
import definitions._
import org.apache.avro.Schema
import treehugger.forest
import treehuggerDSL._

import scala.jdk.CollectionConverters._

object SpecificClassPublicVarMembersTree {

  def toTooManyFieldsForAConstructorClassDef(
    classStore: ClassStore,
    namespace: Option[String],
    schema: Schema,
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]],
    targetScalaPartialVersion: String): forest.ClassDef = {

    val classSymbol = RootClass.newClass(schema.getName)
    val avroFields = schema.getFields().asScala.toList


    // generate list of memberVars
    val memberVars: List[ValDef] = avroFields.map { f =>
      val fieldName = FieldRenamer.rename(f.name)
      val fieldType = typeMatcher.toScalaType(classStore, namespace, f.schema)
      val defaultValue = DefaultValueMatcher.getDefaultValue(
        classStore,
        namespace,
        f,
        typeMatcher,
        fieldName == fieldType.safeToString)
      val rhs = if (defaultValue.isEmpty) WILDCARD else defaultValue
      VAR(fieldName, fieldType) := rhs
    }

    // extension
    val baseClassName = "org.apache.avro.specific.SpecificRecordBase"
    val baseClass = RootClass.newClass(baseClassName)

    // methods - first add an index the the schema's fields
    val indexedFields = avroFields.zipWithIndex.map(p => {
      val avroField = p._1
      val index = p._2
      IndexedField(avroField, index)
    })
    val defGetSchema = namespace.fold(GetSchemaGenerator(classSymbol).toDef)(ns => GetSchemaGenerator(RootClass.newClass(s"$ns.${classSymbol}")).toDef)
    val defGets = GetsGenerator.toDefs(indexedFields, classSymbol, typeMatcher, targetScalaPartialVersion)
    val defPut = PutGenerator.toDef(
      classStore,
      namespace,
      indexedFields,
      typeMatcher,
      classSymbol,
      targetScalaPartialVersion)

    val flagsWithClassFinal: List[Long] =  {
      val flags = maybeFlags.toList.flatten
        if (flags.contains(Flags.FINAL)) flags
        else flags :+ Flags.FINAL.toLong
      }

    val baseTrait = maybeBaseTrait.getOrElse("Serializable")

    val classDef = CLASSDEF(classSymbol)
            .withFlags(flagsWithClassFinal: _*)
            .withParents(baseClass)
            .withParents(baseTrait)
     classDef := BLOCK((memberVars ++ defGets ++ Seq(defPut, defGetSchema)):_*)
  }

}
