package avrohugger
package inputformat
package schemagen

import scala.reflect.runtime.universe._
import scala.reflect.runtime._

import org.codehaus.jackson.JsonNode
import org.codehaus.jackson.node._

import collection.JavaConversions._
 
object JsonMatcher {

  def toJsonNode(namespace: Option[Name], dv: Tree) : JsonNode = {

    def getFullName(nme: String): String = {
      namespace match {
        case Some(ns) => ns + "+" + nme
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
      case Apply(Ident(NameTag("Some")), List(x))      => toJsonNode(namespace, x)
      case Apply(Ident(NameTag("List")), xs)           => {
        val jsonArray = jsonNodeFactory.arrayNode
        xs.map(x => toJsonNode(namespace, x)).map(v => jsonArray.add(v))
        jsonArray
      }
      case Apply(Ident(NameTag("Map")), kvps)          => {
        val jsonObject = jsonNodeFactory.objectNode
        kvps.foreach(kvp => kvp match {
          case Apply(Select(Literal(Constant(key: String)), NameTag(tn)), List(x)) =>  {
            jsonObject.put(key, toJsonNode(namespace, x))
          }
        })
        jsonObject
      }
      // if the default value is another (i.e. nested) record/case class
      case Apply(Ident(NameTag(name)), xs) if SchemaStore.schemas.contains(getFullName(name.toString)) => {
        val jsonObject = jsonNodeFactory.objectNode
        xs.zipWithIndex.map( x => {
          val value = x._1
          val index = x._2
          val nestedRecordField = SchemaStore.schemas(getFullName(name.toString)).getFields()(index)
          // values from the tree, field names from cross referencing tree's pos with schema field pos
          // (they always correspond since the schema is defined based on the fields in a class def)
          jsonObject.put(nestedRecordField.name, toJsonNode(namespace, value))
        })
        jsonObject
      }
      case x => sys.error("Could not extract default value. Found: " + x + ", " + showRaw(x))
    }
  } 
}