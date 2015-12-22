package avrohugger
package format

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.Schema
import org.apache.avro.Schema.Field
import org.apache.avro.Schema.Type.{ ENUM, RECORD }

import scala.language.postfixOps
import scala.collection.JavaConversions._

object ScalaDocGen {

  def docToScalaDoc(schema: Schema, tree: Tree): Tree = {

    def aFieldHasDoc(schema: Schema): Boolean = {
      schema.getFields.exists(field => isDoc(field.doc))
    }

    def topLevelHasDoc(schema: Schema): Boolean = {
      isDoc(schema.getDoc)
    }

    def isDoc(maybeDoc: String): Boolean = {
      maybeDoc match {
        case null => false
        case _ => true
      }
    }

    def avroDocToScalaString(doc: String) = doc match {
      case null => ""
      case docString => docString
    }

    // Need arbitrary number of fields, so can't use DocTags, must return String
    def getFieldFauxDocTags(schema: Schema): List[String] = {
      val docStrings = schema.getFields.toList.map(field => {
        val fieldName = field.name
        val fieldDoc = avroDocToScalaString(field.doc)
        s"@param $fieldName $fieldDoc"
      })
      docStrings
    }

    def wrapClassWithDoc(schema: Schema, tree: Tree, docs: List[String]) = {
      if (topLevelHasDoc(schema) || aFieldHasDoc(schema)) tree.withDoc(docs)
      else tree
    }

    def wrapEnumWithDoc(schema: Schema, tree: Tree, docs: List[String]) = {
      if (topLevelHasDoc(schema)) tree.withDoc(docs)
      else tree
    }

    val schemaDocString = avroDocToScalaString(schema.getDoc)

    schema.getType match {
      case RECORD => wrapClassWithDoc(schema, tree, schemaDocString::getFieldFauxDocTags(schema))
      case ENUM => wrapEnumWithDoc(schema, tree, List(schemaDocString))
      case _ => sys.error("Error generating ScalaDoc from Avro doc. Not an ENUM or RECORD")
    }

  }

}
