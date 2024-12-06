package avrohugger
package input

import avrohugger.matchers.TypeMatcher
import stores.SchemaStore
import types.EnumAsScalaString

import org.apache.avro.Schema
import org.apache.avro.Schema.Type.{ARRAY, ENUM, FIXED, MAP, RECORD, UNION}

import scala.jdk.CollectionConverters._

object NestedSchemaExtractor {
  // if a record is found, extract nested RECORDs and ENUMS (i.e. top-level types) 
  def getNestedSchemas(
    schema: Schema,
    schemaStore: SchemaStore,
    typeMatcher: TypeMatcher): Set[Schema] = {
    def extract(
      schema: Schema,
      fieldPath: List[String] = List.empty): Set[Schema] = {

      schema.getType match {
        case RECORD =>
          val fields: Set[Schema.Field] = schema.getFields().asScala.toList.toSet
          val fieldSchemas: Set[Schema] = fields.map(field => field.schema)
          def flattenSchema(fieldSchema: Schema): Set[Schema] = {
            fieldSchema.getType match {
              case ARRAY => flattenSchema(fieldSchema.getElementType)
              case MAP => flattenSchema(fieldSchema.getValueType)
              case RECORD => {
                // if the field schema is one that has already been stored, use that one
                if (schemaStore.schemas.contains(fieldSchema.getFullName)) Set()
                // if we've already seen this schema (recursive schemas) don't traverse further
                else if (fieldPath.contains(fieldSchema.getFullName)) Set()
                else extract(fieldSchema, fieldSchema.getFullName :: fieldPath) + fieldSchema
              }
              case UNION => fieldSchema.getTypes().asScala.toList.flatMap(flattenSchema).toSet
              case ENUM => {
                // if the field schema is one that has already been stored, use that one
                if (schemaStore.schemas.contains(fieldSchema.getFullName)) Set()
                else Set(fieldSchema)
              }
              case FIXED => {
                // if the field schema is one that has already been stored, use that one
                if (schemaStore.schemas.contains(fieldSchema.getFullName)) Set()
                else Set(fieldSchema)
              }
              case _ => Set(fieldSchema)
            }
          }
          val flatSchemas = fieldSchemas.flatMap(fieldSchema => flattenSchema(fieldSchema))
          def topLevelTypes(schema: Schema) = {
            if (typeMatcher.avroScalaTypes.`enum` == EnumAsScalaString) (schema.getType == RECORD | schema.getType == FIXED)
            else (schema.getType == RECORD | schema.getType == ENUM | schema.getType == FIXED)
          }
          val nestedTopLevelSchemas = flatSchemas.filter(topLevelTypes)
          nestedTopLevelSchemas
        case ENUM => Set(schema)
        case FIXED => Set(schema)
        case _ => Set()
      } 
    }

    extract(schema) + schema
  }
}

