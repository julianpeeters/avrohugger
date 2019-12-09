package avrohugger
package format
package scavro
package trees

import generators.ScalaDocGenerator
import converters.ScalaConverter
import matchers.TypeMatcher
import stores.ClassStore

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.Schema

import scala.collection.JavaConverters._

object ScavroObjectTree {

  // for generating enums
  def toScalaEnumDef(
    classStore: ClassStore, 
    schema: Schema,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]]) = {
      
    val objectDef = (maybeBaseTrait, maybeFlags) match {
      case (Some(baseTrait), Some(flags)) =>
        OBJECTDEF(schema.getName)
          .withFlags(flags:_*)
          .withParents("Enumeration")
          .withParents(baseTrait) 
      case (Some (baseTrait), None) => 
        OBJECTDEF(schema.getName)
          .withParents(baseTrait) 
          .withParents("Enumeration")
      case (None, Some(flags)) =>
        OBJECTDEF(schema.getName)
          .withFlags(flags:_*)
          .withParents("Enumeration")
      case (None, None) => 
        OBJECTDEF(schema.getName)
          .withParents("Enumeration")
    }
    
    val objectTree = objectDef := BLOCK(
      TYPEVAR(schema.getName) := REF("Value"),
      VAL(schema.getEnumSymbols.asScala.mkString(", ")) := REF("Value")
    )
    
    val treeWithScalaDoc = ScalaDocGenerator.docToScalaDoc(
      Left(schema),
      objectTree)
      
    treeWithScalaDoc
  }

  // for generating companion objects of case class records 
  def toCompanionDef(
    classStore: ClassStore, 
    schema: Schema,
    ScalaClass: Symbol,
    JavaClass: Symbol,
    typeMatcher: TypeMatcher,
    maybeFlags: Option[List[Long]]) = {

    val AvroMetadata =
      TYPE_REF(REF("AvroMetadata")) APPLYTYPE(ScalaClass, JavaClass)
    val Class =
      TYPE_REF(REF("Class")) APPLYTYPE(JavaClass)
    val AvroReader =
      TYPE_REF(REF("AvroReader")) APPLYTYPE(ScalaClass)
    val toAvroType =
      TYPE_REF(LAMBDA(PARAM("j", JavaClass)) ==> TYPE_REF(ScalaClass))

    val javaClassAccessors: List[Tree] =
      schema.getFields.asScala.toList.map(avroField => {
        val nameGet = ScavroMethodRenamer.generateMethodName(schema, avroField, "get", "")
        val getterTree = REF("j") DOT nameGet
        val scalaConverter = new ScalaConverter(typeMatcher)
        scalaConverter.convertFromJava(avroField.schema, getterTree)
      })

    val objectDef = maybeFlags match {
      case Some(flags) => OBJECTDEF(ScalaClass).withFlags(flags:_*)
      case None => OBJECTDEF(ScalaClass)
    }
   
    // adapted from https://github.com/oysterbooks/scavro/blob/code_generation/src/main/scala/oyster/scavro/plugin/ScalaCodegen.scala
    val objectTree = {
      val anonReaderDef = ANONDEF(AvroReader)
      val avroType = TYPE_REF(Class)
      val javaType = TYPE_REF(JavaClass)
      val scalaTypeApplied = TYPE_REF(ScalaClass) APPLY(javaClassAccessors:_*)
      objectDef := BLOCK (
        DEF("reader") withFlags(Flags.IMPLICIT) := NEW(anonReaderDef := BLOCK(
          TYPEVAR("J") withFlags(Flags.OVERRIDE) := REF(JavaClass)
        )),
        VAL("metadata", TYPE_REF(AvroMetadata))
          withFlags(Flags.IMPLICIT) := NEW(ANONDEF(AvroMetadata) := BLOCK(
            VAL("avroClass", avroType) 
              withFlags(Flags.OVERRIDE) := REF("classOf") APPLYTYPE(JavaClass),
            VAL("schema") withFlags(Flags.OVERRIDE) withType("Schema") := 
              javaType DOT "getClassSchema" APPLY(Nil),
            VAL("fromAvro", TYPE_REF(PAREN(javaType)) TYPE_=> ScalaClass)
              withFlags(Flags.OVERRIDE) := BLOCK(
                LAMBDA(PARAM("j", JavaClass)) ==> scalaTypeApplied
              )
          )
        )
      )
    }

    objectTree
  
  }
  
}
