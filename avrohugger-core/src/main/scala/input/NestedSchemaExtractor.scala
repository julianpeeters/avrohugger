package avrohugger
package input

import avrohugger.matchers.TypeMatcher
import stores.SchemaStore
import types.EnumAsScalaString

import org.apache.avro.Schema
import org.apache.avro.Schema.Type.{ ARRAY, ENUM, FIXED, MAP, RECORD, UNION }

import scala.jdk.CollectionConverters._

object NestedSchemaExtractor {
  // if a record is found, extract nested RECORDs and ENUMS (i.e. top-level types) 
  def getNestedSchemas(
    schema: Schema,
    schemaStore: SchemaStore,
    typeMatcher: TypeMatcher): List[Schema] = {
    var visitedSchemas = Set.empty[String]

    def extract(schema: Schema): List[Schema] = {
      if (visitedSchemas.contains(schema.getFullName))
        List()
      else {
        visitedSchemas += schema.getFullName
        schema.getType match {
          case RECORD =>
            val fields: List[Schema.Field] = schema.getFields().asScala.toList
            val fieldSchemas: List[Schema] = fields.map(field => field.schema)

            def flattenSchema(fieldSchema: Schema): List[Schema] = {
              fieldSchema.getType match {
                case ARRAY => flattenSchema(fieldSchema.getElementType)
                case MAP => flattenSchema(fieldSchema.getValueType)
                case RECORD => extract(fieldSchema) :+ fieldSchema
                case UNION => fieldSchema.getTypes().asScala.toList.flatMap(x => flattenSchema(x))
                case ENUM => List(fieldSchema)
                case FIXED => List(fieldSchema)
                case _ => List(fieldSchema)
              }
            }

            fieldSchemas
              .flatMap(flattenSchema)
              .filter { schema =>
                if (typeMatcher.avroScalaTypes.`enum` == EnumAsScalaString) {
                  schema.getType == RECORD | schema.getType == FIXED
                }
                else {
                  schema.getType == RECORD | schema.getType == ENUM | schema.getType == FIXED
                }
              }
          case ENUM => List(schema)
          case FIXED => List(schema)
          case _ => Nil
        }
      }
    }
    // most-nested schemas should be compiled first 
    extract(schema) :+ schema
  }
}

