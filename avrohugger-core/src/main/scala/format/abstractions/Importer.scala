package avrohugger
package format
package abstractions

import avrohugger.input.DependencyInspector
import avrohugger.input.NestedSchemaExtractor
import avrohugger.matchers.TypeMatcher
import avrohugger.matchers.custom.CustomNamespaceMatcher
import avrohugger.stores.SchemaStore

import org.apache.avro.{Schema, Protocol}
import org.apache.avro.Schema.Type.{ENUM, RECORD, UNION, MAP, ARRAY, FIXED}

import treehugger.forest._
import definitions.RootClass
import treehuggerDSL._

import scala.jdk.CollectionConverters._

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
                  typeMatcher: TypeMatcher): Set[Import]

  ////////////////////////////// concrete members //////////////////////////////
  // gets enum schemas which may be dependencies
  def getEnumSchemas(
                      topLevelSchemas: Set[Schema],
                      alreadyImported: Set[Schema] = Set.empty[Schema]): Set[Schema] = {
    def nextSchemas(s: Schema, us: Set[Schema]) = getRecordSchemas(Set(s), us)

    topLevelSchemas
      .flatMap(schema => {
        schema.getType match {
          case RECORD =>
            val fieldSchemasWithChildSchemas = getFieldSchemas(schema).toSeq
              .filter(s => alreadyImported.contains(s))
              .flatMap(s => nextSchemas(s, alreadyImported + s))
            Seq(schema) ++ fieldSchemasWithChildSchemas
          case ENUM =>
            Seq(schema)
          case UNION =>
            schema.getTypes().asScala
              .find(s => s.getType != NULL).toSeq
              .filter(s => alreadyImported.contains(s))
              .flatMap(s => nextSchemas(schema, alreadyImported + s))
          case MAP =>
            Seq(schema.getValueType)
              .filter(s => alreadyImported.contains(s))
              .flatMap(s => nextSchemas(schema, alreadyImported + s))
          case ARRAY =>
            Seq(schema.getElementType)
              .filter(s => alreadyImported.contains(s))
              .flatMap(s => nextSchemas(schema, alreadyImported + s))
          case _ =>
            Seq.empty[Schema]
        }
      })
      .filter(schema => schema.getType == ENUM)
  }

  def getFixedSchemas(topLevelSchemas: Set[Schema]): Set[Schema] =
    topLevelSchemas
      .flatMap(schema => {
        schema.getType match {
          case FIXED => Seq(schema)
          case _ => Seq.empty[Schema]
        }
      })
      .filter(_.getType == FIXED)

  def getFieldSchemas(schema: Schema): Set[Schema] = {
    schema.getFields().asScala.toList.map(_.schema).toSet
  }

  def getUserDefinedImports(
                             recordSchemas: Set[Schema],
                             namespace: Option[String],
                             typeMatcher: TypeMatcher): Set[Import] = {

    def checkNamespace(schema: Schema): Option[String] = {
      val maybeReferredNamespace =
        DependencyInspector.getReferredNamespace(schema)
      CustomNamespaceMatcher.checkCustomNamespace(
        maybeReferredNamespace,
        typeMatcher,
        maybeDefaultNamespace = maybeReferredNamespace)
    }

    def asImportDef(packageName: String, fields: Set[Schema]): Import = {
      val maybeUpdatedPackageName = CustomNamespaceMatcher.checkCustomNamespace(
        Some(packageName),
        typeMatcher,
        maybeDefaultNamespace = Some(packageName))
      val updatedPkg = maybeUpdatedPackageName.getOrElse(packageName)
      val importedPackageSym = RootClass.newClass(updatedPkg)
      val importedTypes = fields.map(DependencyInspector.getReferredTypeName)
      IMPORT(importedPackageSym, importedTypes)
    }

    def requiresImportDef(schema: Schema): Boolean = {
      (isRecord(schema) || isEnum(schema) || isFixed(schema)) &&
        checkNamespace(schema).isDefined &&
        checkNamespace(schema) != namespace
    }

    recordSchemas
      .filter(schema => requiresImportDef(schema))
      .groupBy(schema => checkNamespace(schema).getOrElse(schema.getNamespace))
      .map {
        case (packageName: String, fields: Set[Schema]) => asImportDef(packageName, fields)
      }
      .toSet
  }

  // gets record schemas which may be dependencies
  def getRecordSchemas(
                        topLevelSchemas: Set[Schema],
                        alreadyImported: Set[Schema] = Set.empty[Schema]): Set[Schema] = {
    def nextSchemas(s: Schema, us: Set[Schema]) = getRecordSchemas(Set(s), us)

    topLevelSchemas
      .flatMap(schema => {
        schema.getType match {
          case RECORD =>
            val fieldSchemasWithChildSchemas = getFieldSchemas(schema).toSeq
              .filter(s => alreadyImported.contains(s))
              .flatMap(s => nextSchemas(s, alreadyImported + s))
            Seq(schema) ++ fieldSchemasWithChildSchemas
          case ENUM =>
            Seq(schema)
          case UNION =>
            schema.getTypes().asScala
              .find(s => s.getType != NULL).toSeq
              .filter(s => alreadyImported.contains(s))
              .flatMap(s => nextSchemas(schema, alreadyImported + s))
          case MAP =>
            Seq(schema.getValueType)
              .filter(s => alreadyImported.contains(s))
              .flatMap(s => nextSchemas(schema, alreadyImported + s))
          case ARRAY =>
            Seq(schema.getElementType)
              .filter(s => alreadyImported.contains(s))
              .flatMap(s => nextSchemas(schema, alreadyImported + s))
          case _ =>
            Seq.empty[Schema]
        }
      })
      .filter(isRecord)
  }

  def getTopLevelSchemas(
                          schemaOrProtocol: Either[Schema, Protocol],
                          schemaStore: SchemaStore,
                          typeMatcher: TypeMatcher): Set[Schema] = {
    schemaOrProtocol match {
      case Left(schema) =>
        NestedSchemaExtractor.getNestedSchemas(schema, schemaStore, typeMatcher) + schema
      case Right(protocol) => protocol.getTypes().asScala.toList.flatMap(schema => {
        NestedSchemaExtractor.getNestedSchemas(schema, schemaStore, typeMatcher) + schema
      }).toSet
    }

  }

  def isFixed(schema: Schema): Boolean = schema.getType == FIXED

  def isEnum(schema: Schema): Boolean = schema.getType == ENUM

  def isRecord(schema: Schema): Boolean = schema.getType == RECORD

}
