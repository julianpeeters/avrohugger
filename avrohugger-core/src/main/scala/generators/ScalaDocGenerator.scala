package avrohugger
package generators

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Field
import org.apache.avro.Schema.Type.{ ENUM, FIXED, RECORD }

import scala.language.postfixOps
import scala.jdk.CollectionConverters._

object ScalaDocGenerator {

  private def aFieldHasDoc(schema: Schema): Boolean = {
    schema.getFields().asScala.exists(field => {
      val maybeFieldDoc = Option(field.doc)
      isDoc(maybeFieldDoc)
    })
  }

  private def topLevelHasDoc(schema: Schema): Boolean = {
    val maybeSchemaDoc = Option(schema.getDoc)
    isDoc(maybeSchemaDoc)
  }

  private def isDoc(maybeDoc: Option[String]): Boolean = {
    maybeDoc match {
      case Some(doc) => true
      case None => false
    }
  }

  // Need arbitrary number of fields, so can't use DocTags, must return String
  private def getFieldFauxDocTags(schema: Schema): List[String] = {
    val docStrings = schema.getFields().asScala.toList.map(field => {
      val fieldName = field.name
      val fieldDoc = Option(field.doc).getOrElse("")
      s"@param $fieldName $fieldDoc"
    })
    docStrings
  }

  private def wrapClassWithDoc(schema: Schema, tree: Tree, docs: List[String]) = {
    if (topLevelHasDoc(schema) || aFieldHasDoc(schema)) tree.withDoc(docs)
    else tree
  }

  private def wrapValueClassWithDoc(schema: Schema, tree: Tree, docs: List[String]) = {
    if (topLevelHasDoc(schema)) tree.withDoc(docs)
    else tree
  }

  private def wrapEnumWithDoc(schema: Schema, tree: Tree, docs: List[String]) = {
    if (topLevelHasDoc(schema)) tree.withDoc(docs)
    else tree
  }

  private def wrapTraitWithDoc(protocol: Protocol, tree: Tree, docs: List[String]) = {
    if (isDoc(Option(protocol.getDoc))) tree.withDoc(docs)
    else tree
  }

  def docToScalaDoc(
    schemaOrProtocol: Either[Schema, Protocol],
    tree: Tree): Tree = {
    val docStrings: List[String] = schemaOrProtocol match {
      case Left(schema) => Option(schema.getDoc).toList
      case Right(protocol) => Option(protocol.getDoc).toList
    }

    schemaOrProtocol match {
      case Left(schema) => schema.getType match {
        case RECORD =>
          val paramDocs = getFieldFauxDocTags(schema)
          wrapClassWithDoc(schema, tree, docStrings ::: paramDocs)
        case ENUM =>
          wrapEnumWithDoc(schema, tree, docStrings)
        case FIXED =>
          wrapValueClassWithDoc(schema, tree, docStrings)
        case _ =>
          sys.error("Error generating ScalaDoc from Avro doc. Not FIXED/ENUM/RECORD")
      }
      case Right(protocol) => wrapTraitWithDoc(protocol, tree, docStrings)
    }

  }

}
