package avrohugger
package input
package reflectivecompilation
package schemagen

import parsers.ScalaDocParser
import java.util.{Arrays => JArrays}
import org.apache.avro.Schema.Field
import org.apache.avro.Schema

import scala.collection.JavaConverters._

import scala.reflect.runtime.universe._

object EnumSchemaGenerator  {

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
