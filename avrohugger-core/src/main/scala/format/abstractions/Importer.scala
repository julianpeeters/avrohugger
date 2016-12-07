package avrohugger
package format
package abstractions

import avrohugger.input.DependencyInspector.{
  getReferredNamespace,
  getReferredTypeName
}
import avrohugger.input.NestedSchemaExtractor
import matchers.{ CustomNamespaceMatcher, TypeMatcher }
import stores.SchemaStore

import org.apache.avro.{ Schema, Protocol }
import org.apache.avro.Schema.Type.{ ENUM, RECORD, UNION, MAP, ARRAY }

import treehugger.forest._
import definitions.RootClass
import treehuggerDSL._

import scala.collection.JavaConverters._

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
    schemaStore: SchemaStore,
    typeMatcher: TypeMatcher): List[Import]
  
  ////////////////////////////// concrete members //////////////////////////////
  def getFieldSchemas(schema: Schema): List[Schema] = {
    schema.getFields.asScala.toList.map(field => field.schema)
  }

  def getTypeSchemas(schema: Schema): List[Schema] = {
    schema.getTypes.asScala.toList
  }
    
  def getRecordImports(
    recordSchemas: List[Schema],
    namespace: Option[String],
    typeMatcher: TypeMatcher): List[Import] = {
    
    
    def checkNamespace(schema: Schema): Option[String] = {
      val maybeCustom = typeMatcher.customNamespaceMap.get(schema.getNamespace)
      getReferredNamespace(schema) match {
        case Some(ns) => CustomNamespaceMatcher.checkCustomNamespace(
          maybeCustom, Some(ns))
        case None => None
      }
    }

    recordSchemas
      .filter(schema => isRecord(schema) || isEnum(schema))
      .filter(schema => checkNamespace(schema).isDefined)
      .filter(schema => checkNamespace(schema) != namespace)
      .groupBy(schema => checkNamespace(schema).get)
      .toList.map(group => group match {
        case(packageName, fields) => {
          val maybeCustomNS = typeMatcher.customNamespaceMap.get(packageName)
          val Some(updatedPkg) = CustomNamespaceMatcher.checkCustomNamespace(
            maybeCustomNS, Some(packageName))
          val importedPackageSym = RootClass.newClass(updatedPkg)
          val importedTypes = fields.map(field => getReferredTypeName(field))
          IMPORT(importedPackageSym, importedTypes)
        }
      })
  }
  
  // gets record schemas, excluding the root schema, which may be dependencies
  def getRecordSchemas(topLevelSchemas: List[Schema]): List[Schema] = {
    def nextSchemas(s: Schema) = getRecordSchemas(List(s))
    topLevelSchemas
      .flatMap(schema => {
        schema.getType match {
          case RECORD => {
            val fields = getFieldSchemas(schema)
              .flatMap(nextSchemas).toSeq
            Seq(schema) ++ fields
          }
          case UNION => {
            schema.getTypes.asScala
              .flatMap(nextSchemas).toSeq
          }
          case MAP => {
            Seq(schema.getValueType)
              .flatMap(nextSchemas).toSeq
          }
          case ARRAY => {
            Seq(schema.getElementType)
              .flatMap(nextSchemas).toSeq
          }
          case _ => Seq.empty[Schema]
        }
      })
      .filter(schema => isEnum(schema) || isRecord(schema))
      .distinct
      .toList
  }
  
  def getTopLevelSchemas(
    schemaOrProtocol: Either[Schema,  Protocol],
    schemaStore: SchemaStore): List[Schema] = {
    schemaOrProtocol match {
      case Left(schema) =>
        schema::(NestedSchemaExtractor.getNestedSchemas(schema, schemaStore))
      case Right(protocol) => protocol.getTypes.asScala.toList.flatMap(schema => {
        schema::(NestedSchemaExtractor.getNestedSchemas(schema, schemaStore))
      })
    }
          
  }

  def isEnum(schema: Schema): Boolean = ( schema.getType == ENUM )

  def isRecord(schema: Schema): Boolean = ( schema.getType == RECORD )

}
