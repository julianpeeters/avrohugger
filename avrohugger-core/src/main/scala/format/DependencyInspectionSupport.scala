package avrohugger
package format

import org.apache.avro.Schema
import org.apache.avro.Schema.Field

import scala.collection.JavaConversions._


object DependencyInspectionSupport {
  import Schema.Type._
  def getSchemaReferredNamespace(schema: Schema): Option[String] = schema.getType match {
    case ARRAY =>
      getSchemaReferredNamespace(schema.getElementType)
    case UNION =>
      schema.getTypes.find( innerType => innerType.getType != NULL ) flatMap getSchemaReferredNamespace
    case MAP =>
      getSchemaReferredNamespace(schema.getValueType)
    case RECORD =>
      Option(schema.getNamespace)
    case _ => None

  }


  def getFieldReferredNamespace(field: Field): Option[String] = getSchemaReferredNamespace(field.schema)

  def getReferredTypeName(schema: Schema): String = schema.getType match {
    case ARRAY =>
      schema.getElementType.getName
    case UNION =>
      schema.getTypes.find( innerType => innerType.getType != NULL ).map( _.getName ).getOrElse("")
    case MAP =>
      schema.getValueType.getName
    case _ =>
      schema.getName
  }

  def getReferredTypeName(field: Field): String = getReferredTypeName(field.schema)

}
