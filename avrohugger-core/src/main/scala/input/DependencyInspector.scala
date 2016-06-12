package avrohugger
package input

import org.apache.avro.Schema
import org.apache.avro.Schema.Field

import scala.collection.JavaConversions._


object DependencyInspector {
  import Schema.Type._
  def getReferredNamespace(schema: Schema): Option[String] = schema.getType match {
    case ARRAY =>
      getReferredNamespace(schema.getElementType)
    case UNION =>
      schema.getTypes.find( innerType => innerType.getType != NULL ) flatMap getReferredNamespace
    case MAP =>
      getReferredNamespace(schema.getValueType)
    case RECORD | ENUM =>
      Option(schema.getNamespace)
    case _ => None

  }

  def getReferredTypeName(schema: Schema): String = schema.getType match {
    case ARRAY =>
      getReferredTypeName(schema.getElementType)
    case UNION =>
      schema.getTypes.find( innerType => innerType.getType != NULL ).map( getReferredTypeName ).getOrElse("")
    case MAP =>
      getReferredTypeName(schema.getValueType)
    case _ =>
      schema.getName
  }

}
