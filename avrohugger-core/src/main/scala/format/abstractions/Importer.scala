package avrohugger
package format
package abstractions

import avrohugger.input.DependencyInspector
import avrohugger.input.NestedSchemaExtractor
import avrohugger.matchers.TypeMatcher
import avrohugger.matchers.custom.CustomNamespaceMatcher
import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Type.{ ARRAY, ENUM, FIXED, MAP, RECORD, UNION }
import treehugger.forest._
import definitions.RootClass
import org.apache.avro.Schema.Type
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
  def getImports(schemaOrProtocol: Either[Schema, Protocol], currentNamespace: Option[String], typeMatcher: TypeMatcher): List[Import]

  private def nextSchemas(s: Schema, us: Set[Schema]) = getRecordSchemas(List(s), us)

  ////////////////////////////// concrete members //////////////////////////////
  // gets enum schemas which may be dependencies
  def getEnumSchemas(
    topLevelSchemas: List[Schema],
    alreadyImported: Set[Schema] = Set.empty[Schema]): List[Schema] = {

    topLevelSchemas
      .flatMap(schema => {
        schema.getType match {
          case RECORD =>
            val fieldSchemasWithChildSchemas = getFieldSchemas(schema).toSet
              .intersect(alreadyImported)
              .flatMap(s => nextSchemas(s, alreadyImported + s))
            Seq(schema) ++ fieldSchemasWithChildSchemas
          case ENUM =>
            Seq(schema)
          case UNION =>
            schema.getTypes().asScala
              .find(s => s.getType != Type.NULL).toSet
              .intersect(alreadyImported)
              .flatMap(s => nextSchemas(schema, alreadyImported + s))
          case MAP =>
            Set(schema.getValueType)
              .intersect(alreadyImported)
              .flatMap(s => nextSchemas(schema, alreadyImported + s))
          case ARRAY =>
            Set(schema.getElementType)
              .intersect(alreadyImported)
              .flatMap(s => nextSchemas(schema, alreadyImported + s))
          case _ =>
            Seq.empty[Schema]
        }
      })
      .filter(schema => schema.getType == ENUM)
      .distinct
  }

  def getFixedSchemas(topLevelSchemas: List[Schema]): List[Schema] =
    topLevelSchemas
      .flatMap(schema => {
        schema.getType match {
          case FIXED => Seq(schema)
          case _ => Seq.empty[Schema]
        }
      })
      .filter(_.getType == FIXED)
      .distinct

  def getFieldSchemas(schema: Schema): List[Schema] = {
    schema.getFields().asScala.toList.map(_.schema)
  }

  private def checkNamespace(schema: Schema, typeMatcher: TypeMatcher): Option[String] = {
    val maybeReferredNamespace =
      DependencyInspector.getReferredNamespace(schema)
    CustomNamespaceMatcher.checkCustomNamespace(
      maybeReferredNamespace,
      typeMatcher,
      maybeDefaultNamespace = maybeReferredNamespace)
  }

  private def asImportDef(packageName: String, fields: List[Schema], typeMatcher: TypeMatcher): Import = {
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

  private def requiresImportDef(schema: Schema, namespace: Option[String], typeMatcher: TypeMatcher): Boolean = {
    (isRecord(schema) || isEnum(schema) || isFixed(schema)) &&
      checkNamespace(schema, typeMatcher).isDefined &&
      checkNamespace(schema, typeMatcher) != namespace
  }

  def getUserDefinedImports(
    recordSchemas: List[Schema],
    namespace: Option[String],
    typeMatcher: TypeMatcher): List[Import] = {

    recordSchemas
      .filter(schema => requiresImportDef(schema, namespace, typeMatcher))
      .groupBy(schema => checkNamespace(schema, typeMatcher).getOrElse(schema.getNamespace))
      .toList
      .map {
        case (packageName, fields) => asImportDef(packageName, fields, typeMatcher)
      }
  }

  // gets record schemas which may be dependencies
  def getRecordSchemas(
    topLevelSchemas: List[Schema],
    alreadyImported: Set[Schema] = Set.empty[Schema]): List[Schema] = {
    def nextSchemas(s: Schema, us: Set[Schema]) = getRecordSchemas(List(s), us)

    topLevelSchemas
      .flatMap(schema => {
        schema.getType match {
          case RECORD =>
            val fieldSchemasWithChildSchemas = getFieldSchemas(schema).toSet
              .intersect(alreadyImported)
              .flatMap(s => nextSchemas(s, alreadyImported + s))
            Seq(schema) ++ fieldSchemasWithChildSchemas
          case ENUM =>
            Seq(schema)
          case UNION =>
            schema.getTypes().asScala
              .find(s => s.getType != Type.NULL).toSet
              .intersect(alreadyImported)
              .flatMap(s => nextSchemas(schema, alreadyImported + s))
          case MAP =>
            Set(schema.getValueType)
              .intersect(alreadyImported)
              .flatMap(s => nextSchemas(schema, alreadyImported + s))
          case ARRAY =>
            Set(schema.getElementType)
              .intersect(alreadyImported)
              .flatMap(s => nextSchemas(schema, alreadyImported + s))
          case _ =>
            Seq.empty[Schema]
        }
      })
      .filter(isRecord)
      .distinct
  }

  def getTopLevelSchemas(
    schemaOrProtocol: Either[Schema, Protocol],
    typeMatcher: TypeMatcher): List[Schema] = {
    schemaOrProtocol match {
      case Left(schema) =>
        schema :: NestedSchemaExtractor.getNestedSchemas(schema, typeMatcher)
      case Right(protocol) => protocol.getTypes().asScala.toList.flatMap { schema =>
        schema :: NestedSchemaExtractor.getNestedSchemas(schema, typeMatcher)
      }
    }

  }

  def isFixed(schema: Schema): Boolean = schema.getType == FIXED

  def isEnum(schema: Schema): Boolean = schema.getType == ENUM

  def isRecord(schema: Schema): Boolean = schema.getType == RECORD

}
