package avrohugger
package matchers

import stores.SchemaStore

import scala.reflect.runtime.universe._
import scala.reflect.runtime._

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node._

object JsonMatcher {

  def toJsonNode(namespace: Option[Name], dv: Tree, schemaStore: SchemaStore) : JsonNode = {

    def getFullName(nme: String): String = {
      namespace match {
        case Some(ns) => s"$ns+$nme"
        case None => nme
      }
    }

    lazy val jsonNodeFactory = JsonNodeFactory.instance

    dv match {
      // use of null here is for Java interop, builds Avro FieldConstructor w/o default value
      case EmptyTree                                   => null
      case Literal(Constant(x: Unit))                  => jsonNodeFactory.nullNode
      case Literal(Constant(x: Boolean))               => jsonNodeFactory.booleanNode(x)
      case Literal(Constant(x: Int))                   => jsonNodeFactory.numberNode(x)
      case Literal(Constant(x: Long))                  => jsonNodeFactory.numberNode(x)
      case Literal(Constant(x: Float))                 => jsonNodeFactory.numberNode(x)
      case Literal(Constant(x: Double))                => jsonNodeFactory.numberNode(x)
      case Literal(Constant(x: String))                => jsonNodeFactory.textNode(x)
      case Literal(Constant(null))                     => jsonNodeFactory.nullNode
      case Ident(NameTag("None"))                      => jsonNodeFactory.nullNode
      case Apply(Ident(NameTag("Some")), List(x))      => toJsonNode(namespace, x, schemaStore)
      case Apply(Ident(NameTag("List")), xs)           => {
        val jsonArray = jsonNodeFactory.arrayNode
        xs.map(x => toJsonNode(namespace, x, schemaStore)).map(v => jsonArray.add(v))
        jsonArray
      }
      case Apply(Ident(NameTag("Map")), kvps)          => {
        val jsonObject = jsonNodeFactory.objectNode
        kvps.foreach(kvp => kvp match {
          case Apply(Select(Literal(Constant(key: String)), NameTag(tn)), List(x)) =>  {
            jsonObject.put(key, toJsonNode(namespace, x, schemaStore))
          }
        })
        jsonObject
      }
      // if the default value is another (i.e. nested) record/case class
      case Apply(Ident(NameTag(name)), xs) if schemaStore.schemas.contains(getFullName(name.toString)) => {
        val jsonObject = jsonNodeFactory.objectNode
        xs.zipWithIndex.map( x => {
          val value = x._1
          val index = x._2
          val nestedRecordField = schemaStore.schemas(getFullName(name.toString)).getFields.get(index)
          // values from the tree, field names from cross referencing tree's pos with schema field pos
          // (they always correspond since the schema is defined based on the fields in a class def)
          jsonObject.set(nestedRecordField.name, toJsonNode(namespace, value, schemaStore))
        })
        jsonObject
      }
      // enum
      case Select(Ident(NameTag(enum)), NameTag(enumValue)) => jsonNodeFactory.textNode(enumValue.toString())
      case x => sys.error(s"Could not extract default value. Found: $x, ${showRaw(x)}")
    }
  }
}
