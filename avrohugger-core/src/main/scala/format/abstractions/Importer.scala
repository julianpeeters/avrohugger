package avrohugger
package format
package abstractions

import avrohugger.input.DependencyInspector.{
  getReferredNamespace,
  getReferredTypeName
}
import avrohugger.input.NestedSchemaExtractor
import stores.SchemaStore

import org.apache.avro.{ Schema, Protocol }
import org.apache.avro.Schema.Type.{ ENUM, RECORD }

import treehugger.forest._
import definitions.RootClass
import treehuggerDSL._

import scala.collection.JavaConversions._

/** Parent to all ouput formats' importers
  *
  * _ABSTRACT MEMBERS_: to be implemented by a subclass
  * getImports
  *
  * _CONCRETE MEMBERS_: implementations to be inherited by a subclass
  * getFieldSchemas
  * getRecordImports
  * getRecordSchemas
  * getTopLevelSchemas
  * isEnum
  * isRecord
  */
trait Importer {
  
  ///////////////////////////// abstract members ///////////////////////////////
  def getImports(
    schemaOrProtocol: Either[Schema, Protocol],
    currentNamespace: Option[String],
    schemaStore: SchemaStore): List[Import]
  
  ////////////////////////////// concrete members //////////////////////////////
  def getFieldSchemas(schema: Schema): List[Schema] = {
    schema.getFields.toList.map(field => field.schema)
  }
    
  def getRecordImports(
    recordSchemas: List[Schema],//schemaOrProtocol: Either[Schema, Protocol],
    namespace: Option[String]): List[Import] = {
    recordSchemas
      .filter(schema => getReferredNamespace(schema).isDefined)
      .filter(schema => getReferredNamespace(schema) != namespace)
      .groupBy(schema => getReferredNamespace(schema).get)
      .toList.map(group => group match {
        case(packageName, fields) => {
          val importedPackageSym = RootClass.newClass(packageName)
          val importedTypes = fields.map(field => getReferredTypeName(field))
          IMPORT(importedPackageSym, importedTypes)
        }
      })
  }
  
  // gets record schemas, excluding the root schema, which may be dependencies
  def getRecordSchemas(topLevelSchemas: List[Schema]): List[Schema] = {
    topLevelSchemas
      .filter(isRecord)
      .flatMap(schema => getFieldSchemas(schema)
      .filter(schema => isEnum(schema) || isRecord(schema)))
      .distinct
  }
  
  def getTopLevelSchemas(
    schemaOrProtocol: Either[Schema,  Protocol],
    schemaStore: SchemaStore): List[Schema] = {
    schemaOrProtocol match {
      case Left(schema) =>
        schema::(NestedSchemaExtractor.getNestedSchemas(schema, schemaStore))
      case Right(protocol) => protocol.getTypes.toList.flatMap(schema => {
        schema::(NestedSchemaExtractor.getNestedSchemas(schema, schemaStore))
      })
    }
          
  }

  def isEnum(schema: Schema): Boolean = ( schema.getType == ENUM )

  def isRecord(schema: Schema): Boolean = ( schema.getType == RECORD )

}