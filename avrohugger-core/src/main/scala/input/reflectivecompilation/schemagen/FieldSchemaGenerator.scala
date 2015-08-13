package avrohugger
package input
package reflectivecompilation
package schemagen

import collection.JavaConversions._
import java.util.{Arrays => JArrays}
import org.apache.avro.Schema
import org.apache.avro.Schema.Field
import org.apache.avro.Schema.{Type => AvroType}
import org.apache.avro.util.Utf8

import reflect.runtime.universe._
import scala.tools.reflect.ToolBox

object FieldSchemaGenerator {
 
  def toAvroField(namespace: Option[Name], nme: TermName, tpe: Type, dv: Tree) = {

    //map is adapted from https://github.com/radlab/avro-scala-compiler-plugin/blob/master/src/main/scala/plugin/SchemaGen.scala
    def createSchema(tpe: Type) : Schema = {

      tpe match {
        case x @ TypeRef(pre, symbol, args) if (x <:< typeOf[Int] && args.length == 0)  => {
          Schema.create(AvroType.INT)
        }
        case x @ TypeRef(pre, symbol, args) if (x <:< typeOf[Float] && args.length == 0)  => {
          Schema.create(AvroType.FLOAT)
        }
        case x @ TypeRef(pre, symbol, args) if (x <:< typeOf[Long] && args.length == 0)  => {
          Schema.create(AvroType.LONG)
        }
        case x @ TypeRef(pre, symbol, args) if (x <:< typeOf[Double] && args.length == 0)  => {
          Schema.create(AvroType.DOUBLE)
        }      
        case x @ TypeRef(pre, symbol, args) if (x <:< typeOf[Boolean] && args.length == 0)  => {
          Schema.create(AvroType.BOOLEAN)
        }
        case x @ TypeRef(pre, symbol, args) if (x <:< typeOf[String] && args.length == 0)  => {
          Schema.create(AvroType.STRING)
        }      case x @ TypeRef(pre, symbol, args) if (x <:< typeOf[Null] && args.length == 0)  => {
          Schema.create(AvroType.NULL)
        }
        case x @ TypeRef(pre, symbol, args) if (x <:< typeOf[java.nio.ByteBuffer] && args.length == 0)  => {
          Schema.create(AvroType.BYTES)
        }      case x @ TypeRef(pre, symbol, args) if (x <:< typeOf[Utf8] && args.length == 0)  => {
          Schema.create(AvroType.STRING)
        }
        case x @ TypeRef(pre, symbol, args) if (x <:< typeOf[List[Any]] && args.length == 1)  => {
          Schema.createArray(createSchema(args.head))
        }
        case x @ TypeRef(pre, symbol, args) if (x <:< typeOf[Option[Any]] && args.length == 1) => {
          if (args.head <:< typeOf[Option[Any]]) { 
            throw new UnsupportedOperationException("Implementation limitation: Cannot immediately nest Option types")
          } 
          else Schema.createUnion(JArrays.asList(Array(createSchema(typeOf[Null]), createSchema(args.head)):_*))
        }
        case x @ TypeRef(pre, symbol, args) if (x <:< typeOf[Map[String, Any]] && args.length == 2)  => {
          Schema.createMap(createSchema(args(1)))
        }
        case x @ TypeRef(pre, symbol, args) if (x <:< typeOf[Product with Serializable] ) => {
          // if a case class (a nested record) is found, reuse the schema that was made 
          // and stored when its macro was expanded. alternatives: reflectively getting 
          // the schema from its companion, or regenerating the schema.
          try {
            // reflective compilation can't see the package name, so we add it explicitly
            namespace match {
              case Some(ns) => SchemaStore.schemas(s"$ns.${x.toString}")
              case None => SchemaStore.schemas(x.toString)
            }
          }
          catch {
            case t: Throwable => {
              sys.error(s"""Found field with type $tpe, but $tpe has not yet been 
                |expanded. Due to limitations of reflective compilation, please define 
                |your top-level class first, nested records following.""".stripMargin)
            }
          }
        }
        case x => throw new UnsupportedOperationException("Could not generate schema. Cannot support yet: " + x )
      }
    } 

    new Field(
      nme.toString.trim, 
      createSchema(tpe),
      "Auto-Generated Field", 
      JsonMatcher.toJsonNode(namespace, dv)
    )
  }
}