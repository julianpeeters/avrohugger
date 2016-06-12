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
import org.apache.avro.Schema.Type.RECORD

import treehugger.forest._
import definitions.RootClass

import scala.collection.JavaConversions._

/** Parent to all ouput format importers
  *
  * _ABSTRACT MEMBERS_: to be implemented by a subclass
  * asImportTree
  * getImports
  * getRecordSchemas
  *
  * _CONCRETE MEMBERS_: implementations to be inherited by a subclass
  * getFieldSchemas
  * getRecordImports
  * getTopLevelSchemas
  * isRecord
  */
trait Importer {
  
  ///////////////////////////// abstract members ///////////////////////////////
  def asImportTree(packageSym: Symbol, typeNames: List[String]): Import
  
  def getImports(
    schemaOrProtocol: Either[Schema, Protocol],
    currentNamespace: Option[String],
    schemaStore: SchemaStore): List[Import]
    
  def getRecordSchemas(topLevelSchemas: List[Schema]): List[Schema]

  ////////////////////////////// concrete members //////////////////////////////
  def getFieldSchemas(schema: Schema): List[Schema] = {
    schema.getFields.toList.map(field => field.schema)
  }
  
  def getRecordImports(
    recordSchemas: List[Schema],
    namespace: Option[String]): List[Import] = {
    recordSchemas
      .filter(schema => getReferredNamespace(schema).isDefined)
      .filter(schema => getReferredNamespace(schema) != namespace)
      .groupBy(schema => getReferredNamespace(schema).get)
      .toList.map(group => group match {
        case(packageName, fields) => {
          val importedPackage = RootClass.newClass(packageName)
          val importedTypes = fields.map( field => getReferredTypeName(field))
          asImportTree(importedPackage, importedTypes)
        }
      })
  }
  
  def getTopLevelSchemas(
    schema: Schema,
    schemaStore: SchemaStore): List[Schema] = {
    schema::(NestedSchemaExtractor.getNestedSchemas(schema, schemaStore))      
  }

  def isRecord(schema: Schema): Boolean = ( schema.getType == RECORD )

}