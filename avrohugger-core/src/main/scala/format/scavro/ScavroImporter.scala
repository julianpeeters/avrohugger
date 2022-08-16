package avrohugger
package format
package scavro

import format.abstractions.Importer
import avrohugger.input.{DependencyInspector, NestedSchemaExtractor}
import avrohugger.matchers.TypeMatcher
import avrohugger.matchers.custom.CustomNamespaceMatcher
import avrohugger.stores.SchemaStore
import avrohugger.types._

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Field
import org.apache.avro.Schema.Type.RECORD

import treehugger.forest._
import definitions._
import treehuggerDSL._

import scala.jdk.CollectionConverters._

object ScavroImporter extends Importer {

  def asRenamedImportTree(imported: Import) = {
    val packageSym = imported.expr
    val typeNames = imported.selectors.map(s => treeToString(s.name))
    val renames = typeNames.distinct.sorted.map(typeName => {
      val renamedType = "J" + typeName
      RENAME(typeName) ==> renamedType
    })
    IMPORT(packageSym, renames)
  }

  def getImports(
    schemaOrProtocol: Either[Schema, Protocol],
    currentNamespace: Option[String],
    schemaStore: SchemaStore,
    typeMatcher: TypeMatcher): List[Import] = {

    def checkJavaConversions(schemaOrProtocol: Either[Schema, Protocol]): Option[Import] = {

      def checkForArrays(schema: Schema, used: List[Schema] = List.empty): List[Schema] = {
        schema.getType match {
          case Schema.Type.MAP =>
            checkForArrays(schema.getValueType, used)
          case Schema.Type.RECORD =>
            getFieldSchemas(schema).flatMap(s => {
              if (used.contains(s)) List(s)
              else checkForArrays(s, used :+ s)
            })
          case Schema.Type.UNION =>
            val types = schema.getTypes().asScala
            if (types.length != 2 ||
               !types.map(x => x.getType).contains(Schema.Type.NULL) ||
                types.filterNot(x => x.getType == Schema.Type.NULL).length != 1) {
              sys.error("Unions beyond nullable fields are not supported")
            }
            else {
              val maybeType = types.find(x => x.getType != Schema.Type.NULL)
              maybeType match {
                case Some(s) => checkForArrays(s, used)
                case None => sys.error("There was no type in this union")
              }
            }
          case _ => List(schema)
        }
      }

      val schemas: List[Schema] = schemaOrProtocol match {
        case Left(schema) => checkForArrays(schema)
        case Right(protocol) => {
          protocol.getTypes().asScala.toList
            .filter(schema => isRecord(schema))
            .flatMap(schema => checkForArrays(schema))
        }
      }

      val hasArrayField: Boolean =
        schemas.map(schema => schema.getType).contains(Schema.Type.ARRAY)

      val convPackage = RootClass.newClass("scala.jdk.CollectionConverters")
      val javaConvertersImport = IMPORT(convPackage, "_")

      if(hasArrayField) Some(javaConvertersImport)
      else None
    }

    lazy val SchemaClass = RootClass.newClass("org.apache.avro.Schema")
    lazy val ScavroPackage = RootClass.newPackage("org.oedura.scavro")

    lazy val schemaImport = IMPORT(SchemaClass)
    lazy val scavroImport = IMPORT(
      ScavroPackage,
      "AvroMetadata",
      "AvroReader",
      "AvroSerializeable")

    lazy val baseImports: List[Import] =
      List(schemaImport, scavroImport)
    lazy val maybeJavaConversionsImport: Option[Import] =
      checkJavaConversions(schemaOrProtocol)

    // gets all record schemas, including the root schema, which need renaming
    def getAllRecordSchemas(topLevelSchemas: List[Schema]): List[Schema] = {
      topLevelSchemas
        .filter(isRecord)
        .flatMap(schema => schema +: getFieldSchemas(schema))
        .distinct
    }

    // gets imported Java model classes, returning them as Import trees
    def getJavaRecordImports(
      recordSchemas: List[Schema],
      namespace: Option[String],
      typeMatcher: TypeMatcher): List[Import] = {

      def asImportDef(packageName: String, fields: List[Schema]): Import = {
        val importedPackageSym = RootClass.newClass(packageName)
        val importedTypes =
          fields.map(field => DependencyInspector.getReferredTypeName(field))
        IMPORT(importedPackageSym, importedTypes)
      }

      def requiresImportDef(
        schema: Schema,
        typeMatcher: TypeMatcher): Boolean = {
        def isTopLevel(schema: Schema) = typeMatcher.avroScalaTypes.enum match {
          case EnumAsScalaString =>
            isRecord(schema)
          case JavaEnum | ScalaEnumeration | ScalaCaseObjectEnum =>
            (isRecord(schema) || (isEnum(schema)))
        }
        isTopLevel(schema)  && Option(schema.getNamespace) != namespace
      }

      recordSchemas
        .filter(schema => requiresImportDef(schema, typeMatcher))
        .groupBy(schema => Option(schema.getNamespace).getOrElse(""))
        .toList
        .map(group => group match {
          case(packageName, fields) => asImportDef(packageName, fields)
        })
    }

    // gets imported Scavro model classes, returning them as Import trees
    def getTopLevelImports(
      recordSchemas: List[Schema],
      namespace: Option[String]): List[Import] = {
      recordSchemas
        .filter(schema => DependencyInspector.getReferredNamespace(schema).isDefined)
        .filter(schema => {
          val renamedNamespace = ScavroNamespaceRenamer.renameNamespace(
            DependencyInspector.getReferredNamespace(schema),
            Left(schema),
            typeMatcher)
          renamedNamespace != namespace
        })
        .groupBy(schema => {
          val renamedNamespace = ScavroNamespaceRenamer.renameNamespace(
            DependencyInspector.getReferredNamespace(schema),
            Left(schema),
            typeMatcher)
          renamedNamespace.get
        })
        .toList.map(group => group match {
          case(packageName, fields) => {
            val importedPackageSym = RootClass.newClass(packageName)
            val importedTypes = fields.map(field => DependencyInspector.getReferredTypeName(field))
            IMPORT(importedPackageSym, importedTypes)
          }
        })
    }
    val topLevelSchemas = getTopLevelSchemas(schemaOrProtocol, schemaStore, typeMatcher)

    val allRecordSchemas = getAllRecordSchemas(topLevelSchemas)
    val scalaRecordImports = getUserDefinedImports(
      allRecordSchemas,
      currentNamespace,
      typeMatcher)

    val javaRecordImports = getJavaRecordImports(
      allRecordSchemas,
      currentNamespace,
      typeMatcher)

    val renamedJavaImports = javaRecordImports.map(asRenamedImportTree)
    val scalaRecords = getRecordSchemas(topLevelSchemas)
    val enumSchemas = getEnumSchemas(topLevelSchemas)
    val scalaImports = getTopLevelImports(scalaRecords ++ enumSchemas, currentNamespace)

    val recordImports = (scalaImports ++ renamedJavaImports).distinct

    if (allRecordSchemas.isEmpty) List.empty
    else baseImports ++ recordImports ++ maybeJavaConversionsImport
  }

}
