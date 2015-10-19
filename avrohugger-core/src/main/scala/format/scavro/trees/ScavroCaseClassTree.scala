package avrohugger
package format
package scavro
package trees

import converters.JavaConverter

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
    typeMatcher: TypeMatcher) = {

    classStore.accept(schema, ScalaClass)

    val mixin = TYPE_REF(REF("AvroSerializeable"))

    val scalaClassParams: List[ValDef] = schema.getFields.toList.map { field =>
      val fieldName = field.name
      val fieldType = typeMatcher.toScalaType(classStore, namespace, field.schema)
      PARAM(fieldName, fieldType): ValDef
    }

    val scalaClassAccessors: List[Tree] = schema.getFields.toList.map(avroField => {
      val javaConverter = new JavaConverter(classStore, namespace, typeMatcher)
      javaConverter.convertToJava(avroField.schema, REF(avroField.name))
    })

    // Defines case class, adapted from https://github.com/oysterbooks/scavro/blob/code_generation/src/main/scala/oyster/scavro/plugin/ScalaCodegen.scala
    val caseClassTree = (
      CASECLASSDEF(ScalaClass) withParams(scalaClassParams) withParents(mixin)
    ) := BLOCK (
      TYPEVAR("J") := REF(JavaClass),
      DEF("toAvro", JavaClass) withFlags(Flags.OVERRIDE) := BLOCK(
        NEW(JavaClass, scalaClassAccessors: _*)
      )
    ) : Tree

    caseClassTree
	}

}
