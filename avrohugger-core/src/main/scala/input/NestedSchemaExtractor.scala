package avrohugger
package input

import avrohugger.matchers.TypeMatcher
import stores.SchemaStore
import types.EnumAsScalaString

import org.apache.avro.Schema
import org.apache.avro.Schema.Type.{ARRAY, ENUM, MAP, RECORD, UNION}

import scala.collection.JavaConverters._

object NestedSchemaExtractor {
  // if a record is found, extract nested RECORDs and ENUMS (i.e. top-level types) 
  def getNestedSchemas(
    schema: Schema,
    schemaStore: SchemaStore,
    typeMatcher: TypeMatcher): List[Schema] = {
    def extract(
      schema: Schema,
      fieldPath: List[String] = List.empty): List[Schema] = {

      schema.getType match {
        case RECORD =>
          val fields: List[Schema.Field] = schema.getFields.asScala.toList
          val fieldSchemas: List[Schema] = fields.map(field => field.schema)
          def flattenSchema(fieldSchema: Schema): List[Schema] = {
            fieldSchema.getType match {
              case ARRAY => flattenSchema(fieldSchema.getElementType)
              case MAP => flattenSchema(fieldSchema.getValueType)
              case RECORD => {
                // if the field schema is one that has already been stored, use that one
                if (schemaStore.schemas.contains(fieldSchema.getFullName)) List()
                // if we've already seen this schema (recursive schemas) don't traverse further
                else if (fieldPath.contains(fieldSchema.getFullName)) List()
                else fieldSchema :: extract(fieldSchema, fieldSchema.getFullName :: fieldPath)
              }
              case UNION => fieldSchema.getTypes.asScala.toList.flatMap(x => flattenSchema(x))
              case ENUM => {
                // if the field schema is one that has already been stored, use that one
                if (schemaStore.schemas.contains(fieldSchema.getFullName)) List()
                else List(fieldSchema)
              }
              case _ => List(fieldSchema)
            }
          }
          val flatSchemas = fieldSchemas.flatMap(fieldSchema => flattenSchema(fieldSchema))
          def topLevelTypes(schema: Schema) = {
            if (typeMatcher.avroScalaTypes.enum == EnumAsScalaString) schema.getType == RECORD
            else (schema.getType == RECORD | schema.getType == ENUM)
          }
          val nestedTopLevelSchemas = flatSchemas.filter(topLevelTypes)
          nestedTopLevelSchemas
        case ENUM => List(schema)
        case _ => Nil
      } 
    }

    schema::extract(schema)
  }
}

