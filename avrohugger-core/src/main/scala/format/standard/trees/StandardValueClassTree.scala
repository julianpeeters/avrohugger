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

object StandardValueClassTree {

  def toValueClassDef(
    classStore: ClassStore,
    namespace: Option[String],
    schema: Schema,
    typeMatcher: TypeMatcher) = {

    // register new type
    val classSymbol = RootClass.newClass(schema.getName)
    val baseTrait = RootClass.newClass("AnyVal")

    val params: List[ValDef] = {
      val fieldName = "bytes"
      val fieldType = TYPE_ARRAY(ByteClass)
      val field = PARAM(fieldName, fieldType)
      List(field)
    }

    val caseClassDef =
      CASECLASSDEF(classSymbol)
        .withParams(params)
        .withFlags(Flags.FINAL)
        .withParents(baseTrait)

    val classTree = caseClassDef.tree

    val treeWithScalaDoc = ScalaDocGenerator.docToScalaDoc(
      Left(schema),
      classTree)

    treeWithScalaDoc
  }

}
