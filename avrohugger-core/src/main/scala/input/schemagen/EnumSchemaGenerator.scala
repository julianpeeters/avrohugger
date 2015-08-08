package avrohugger
package input
package schemagen

import java.util.{Arrays => JArrays}
import org.apache.avro.Schema.Field
import org.apache.avro.Schema

import scala.collection.JavaConverters._

import scala.reflect.runtime.universe._
import scala.reflect.runtime.currentMirror

object EnumSchemaGenerator  {

  def generateSchema(
  	dependencies: List[Tree],
  	className: String, 
  	namespace: Option[Name], 
  	values: List[Name]): Schema = {

    // conversion from Option to String/null is for compatibility with Apache Avro
    val ns = namespace match {
    	case Some(n) => n.toString
    	case None => null
    }

    val vals = JArrays.asList(values.map(value => value.toString).toArray:_*)

    val avroSchema = Schema.createEnum(className, "Auto-Generated Schema", ns, vals)    

    SchemaStore.accept(avroSchema)
    avroSchema
  }

}