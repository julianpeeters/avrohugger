package avrohugger
package format
package abstractions

import avrohugger.input.DependencyInspector
import avrohugger.input.NestedSchemaExtractor
import avrohugger.matchers.TypeMatcher
import avrohugger.matchers.custom.CustomNamespaceMatcher
import avrohugger.stores.SchemaStore

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
  * getEnumSchemas
  * getFieldSchemas
  * getUserDefinedImports
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
  // gets enum schemas which may be dependencies
  def getEnumSchemas(
    topLevelSchemas: List[Schema],
    alreadyImported: List[Schema] = List.empty[Schema]): List[Schema] = {
    def nextSchemas(s: Schema, us: List[Schema]) = getRecordSchemas(List(s), us)
    topLevelSchemas
      .flatMap(schema => {
        schema.getType match {
          case RECORD =>
            val fieldSchemasWithChildSchemas = getFieldSchemas(schema).toSeq
              .filter(s => alreadyImported.contains(s))
              .flatMap(s => nextSchemas(s, alreadyImported :+ s))
            Seq(schema) ++ fieldSchemasWithChildSchemas
          case ENUM =>
            Seq(schema)
          case UNION =>
            schema.getTypes.asScala
              .find(s => s.getType != NULL).toSeq
              .filter(s => alreadyImported.contains(s))
              .flatMap(s => nextSchemas(schema, alreadyImported :+ s))
          case MAP =>
            Seq(schema.getValueType)
              .filter(s => alreadyImported.contains(s))
              .flatMap(s => nextSchemas(schema, alreadyImported :+ s))
          case ARRAY =>
            Seq(schema.getElementType)
              .filter(s => alreadyImported.contains(s))
              .flatMap(s => nextSchemas(schema, alreadyImported :+ s))
          case _ =>
            Seq.empty[Schema]
        }
      })
      .filter(schema => schema.getType == ENUM)
      .distinct
      .toList
  }
  
  def getFieldSchemas(schema: Schema): List[Schema] = {
    schema.getFields.asScala.toList.map(field => field.schema)
  }

  def getTypeSchemas(schema: Schema): List[Schema] = {
    schema.getTypes.asScala.toList
  }

  def getUserDefinedImports(
    recordSchemas: List[Schema],
    namespace: Option[String],
    typeMatcher: TypeMatcher): List[Import] = {

    def checkNamespace(schema: Schema): Option[String] = {
      val maybeReferredNamespace =
        DependencyInspector.getReferredNamespace(schema)
      CustomNamespaceMatcher.checkCustomNamespace(
        maybeReferredNamespace,
        typeMatcher,
        maybeDefaultNamespace = maybeReferredNamespace)
    }

    def asImportDef(packageName: String, fields: List[Schema]): Import = {
      val maybeUpdatedPackageName = CustomNamespaceMatcher.checkCustomNamespace(
        Some(packageName),
        typeMatcher,
        maybeDefaultNamespace = Some(packageName))
      val updatedPkg = maybeUpdatedPackageName.getOrElse(packageName)
      val importedPackageSym = RootClass.newClass(updatedPkg)
      val importedTypes =
        fields.map(field => DependencyInspector.getReferredTypeName(field))
      IMPORT(importedPackageSym, importedTypes)
    }

    def requiresImportDef(schema: Schema): Boolean = {
      (isRecord(schema) || isEnum(schema)) &&
      checkNamespace(schema).isDefined     &&
      checkNamespace(schema) != namespace
    }

    recordSchemas
      .filter(schema => requiresImportDef(schema))
      .groupBy(schema => checkNamespace(schema).getOrElse(schema.getNamespace))
      .toList
      .map(group => group match {
        case(packageName, fields) => asImportDef(packageName, fields)
      })
  }

  // gets record schemas which may be dependencies
  def getRecordSchemas(
    topLevelSchemas: List[Schema],
    alreadyImported: List[Schema] = List.empty[Schema]): List[Schema] = {
    def nextSchemas(s: Schema, us: List[Schema]) = getRecordSchemas(List(s), us)
    topLevelSchemas
      .flatMap(schema => {
        schema.getType match {
          case RECORD =>
            val fieldSchemasWithChildSchemas = getFieldSchemas(schema).toSeq
              .filter(s => alreadyImported.contains(s))
              .flatMap(s => nextSchemas(s, alreadyImported :+ s))
            Seq(schema) ++ fieldSchemasWithChildSchemas
          case ENUM =>
            Seq(schema)
          case UNION =>
            schema.getTypes.asScala
              .find(s => s.getType != NULL).toSeq
              .filter(s => alreadyImported.contains(s))
              .flatMap(s => nextSchemas(schema, alreadyImported :+ s))
          case MAP =>
            Seq(schema.getValueType)
              .filter(s => alreadyImported.contains(s))
              .flatMap(s => nextSchemas(schema, alreadyImported :+ s))
          case ARRAY =>
            Seq(schema.getElementType)
              .filter(s => alreadyImported.contains(s))
              .flatMap(s => nextSchemas(schema, alreadyImported :+ s))
          case _ =>
            Seq.empty[Schema]
        }
      })
      .filter(schema => isRecord(schema))
      .distinct
      .toList
  }

  def getTopLevelSchemas(
    schemaOrProtocol: Either[Schema,  Protocol],
    schemaStore: SchemaStore,
    typeMatcher: TypeMatcher): List[Schema] = {
    schemaOrProtocol match {
      case Left(schema) =>
        schema::(NestedSchemaExtractor.getNestedSchemas(schema, schemaStore, typeMatcher))
      case Right(protocol) => protocol.getTypes.asScala.toList.flatMap(schema => {
        schema::(NestedSchemaExtractor.getNestedSchemas(schema, schemaStore, typeMatcher))
      })
    }

  }

  def isEnum(schema: Schema): Boolean = ( schema.getType == ENUM )

  def isRecord(schema: Schema): Boolean = ( schema.getType == RECORD )

}
