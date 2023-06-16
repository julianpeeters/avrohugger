package avrohugger
package input
package reflectivecompilation
package schemagen

import parsers.ScalaDocParser
import stores.SchemaStore

import org.apache.avro.Schema

import java.util.{Arrays => JArrays}

import scala.reflect.runtime.universe.Name
import scala.jdk.CollectionConverters._

object EnumSchemaGenerator  {

  @deprecated("Reflective compilation will no longer be supported", "avrohugger 1.5.0")
  def generateSchema(
    className: String, 
    namespace: Option[Name], 
    values: List[Name],
    maybeScalaDoc: Option[String],
    schemaStore: SchemaStore): Schema = {

    // conversion from Option to String/null is for compatibility with Apache Avro
    val ns = namespace match {
      case Some(n) => n.toString
      case None => null
    }

    val vals = JArrays.asList(values.map(value => value.toString).toArray:_*)

    val doc = ScalaDocParser.getTopLevelDoc(maybeScalaDoc)

    val avroSchema = Schema.createEnum(className, doc, ns, vals)    

    schemaStore.accept(avroSchema)
    avroSchema
  }

}
