package avrohugger
package format
package scavro
package trees

import converters.ScalaConverter

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.Schema

import scala.collection.JavaConversions._

object ScavroObjectTree {

  // for generating enums
  def toObjectDef(
    classStore: ClassStore, 
    schema: Schema) = {
    // register new type
    val classSymbol = RootClass.newClass(schema.getName + ".Value")
    classStore.accept(schema, classSymbol)

    OBJECTDEF(schema.getName) withParents("Enumeration") := BLOCK(
      TYPEVAR(schema.getName) := REF("Value"),
      VAL(schema.getEnumSymbols.mkString(", ")) := REF("Value")
    )
  }


  // for generating companion objects of case class records 
  def toCompanionDef(
    classStore: ClassStore, 
    schema: Schema,
    ScalaClass: Symbol,
    JavaClass: Symbol,
    typeMatcher: TypeMatcher) = {

    val AvroMetadata = TYPE_REF(REF("AvroMetadata")) APPLYTYPE(ScalaClass, JavaClass)
    val Class = TYPE_REF(REF("Class")) APPLYTYPE(JavaClass)
    val AvroReader = TYPE_REF(REF("AvroReader")) APPLYTYPE(ScalaClass)
    val toAvroType = TYPE_REF(LAMBDA(PARAM("j", JavaClass)) ==> TYPE_REF(ScalaClass))

    val javaClassAccessors: List[Tree] = schema.getFields.toList.map(avroField => {
      val getterTree = REF("j") DOT ("get" + avroField.name.head.toUpper + avroField.name.tail)
      val scalaConverter = new ScalaConverter(typeMatcher)
      scalaConverter.convertFromJava(avroField.schema, getterTree)
    })

    // adapted from https://github.com/oysterbooks/scavro/blob/code_generation/src/main/scala/oyster/scavro/plugin/ScalaCodegen.scala
    val objectTree = OBJECTDEF(ScalaClass) := BLOCK (
      DEF("reader") withFlags(Flags.IMPLICIT) := NEW(ANONDEF(AvroReader) := BLOCK(
        TYPEVAR("J") withFlags(Flags.OVERRIDE) := REF(JavaClass)
      )),
      VAL("metadata", TYPE_REF(AvroMetadata))
        withFlags(Flags.IMPLICIT) := NEW(ANONDEF(AvroMetadata) := BLOCK(
          VAL("avroClass", TYPE_REF(Class)) 
            withFlags(Flags.OVERRIDE) := REF("classOf") APPLYTYPE(JavaClass),
          VAL("schema") withFlags(Flags.OVERRIDE) withType("Schema") := 
            TYPE_REF(JavaClass) DOT "getClassSchema" APPLY(),
          VAL("fromAvro", TYPE_REF(PAREN(TYPE_REF(JavaClass))) TYPE_=> ScalaClass)
            withFlags(Flags.OVERRIDE) := BLOCK(
              LAMBDA(PARAM("j", JavaClass)) ==> TYPE_REF(ScalaClass) APPLY(javaClassAccessors:_*)
            )
        )
      )
    )

    val treeWithScalaDoc = ScalaDocGen.docToScalaDoc(schema, objectTree)
    treeWithScalaDoc
  
  }
  
}
