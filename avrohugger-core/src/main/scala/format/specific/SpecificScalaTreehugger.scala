package avrohugger
package format
package specific

import trees.{ SpecificCaseClassTree, SpecificObjectTree, SpecificTraitTree }

import avrohugger.input.DependencyInspector._
import avrohugger.input.NestedSchemaExtractor._
import avrohugger.input.reflectivecompilation.schemagen.SchemaStore

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Field

import treehugger.forest._
import definitions._
import treehuggerDSL._

import scala.collection.JavaConversions._

object SpecificScalaTreehugger {
  
  def asScalaCodeString(
    classStore: ClassStore, 
    namespace: Option[String], 
    schemaOrProtocol: Either[Schema, Protocol],
    typeMatcher: TypeMatcher,
    schemaStore: SchemaStore): String = {
      
    // imports in case a field type is from a different namespace
    val imports: List[Import] = schemaOrProtocol match {
      case Left(schema) => getImports(schema, namespace, schemaStore)
      case Right(protocol) => protocol.getTypes.toList.flatMap(schema => 
        getImports(schema, namespace, schemaStore))
    }

    val topLevelDefs: List[Tree] =
      asTopLevelDef(
        classStore,
        namespace,
        schemaOrProtocol,
        typeMatcher,
        None,
        None)
    
    // wrap the definitions in a block with a comment and a package
    val tree = {
      val blockContent = imports ++ topLevelDefs
      if (namespace.isDefined) BLOCK(blockContent:_*).inPackage(namespace.get)
      else BLOCK(blockContent:_*).withoutPackage
    }.withDoc("MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY")
    //TODO: move all docs into format.doc package
    val codeString = treeToString(tree)
    codeString
  }
  
  def isRecord(schema: Schema): Boolean = (schema.getType == Schema.Type.RECORD)
  
  def getImports(
    schema: Schema,
    currentNamespace: Option[String],
    schemaStore: SchemaStore): List[Import] = {
    if (isRecord(schema)) {
      val topLevelSchemas: List[Schema] =
        schema::(getNestedSchemas(schema, schemaStore)) 
      topLevelSchemas.filter(isRecord).flatMap(s => s.getFields)
        .filter(field => getReferredNamespace(field.schema).isDefined)
        .filter(field => getReferredNamespace(field.schema) != currentNamespace)
        .distinct
        .groupBy(field => getReferredNamespace(field.schema).get )
        .toList
        .map { _ match { case(packageName, fields) =>
            IMPORT(packageName, fields.map( getReferredTypeName ).distinct )
          }
        }
    }
    else List.empty
  }
  
  def registerType(schema: Schema, classStore: ClassStore): Unit = {
    val classSymbol = RootClass.newClass(schema.getName)
    classStore.accept(schema, classSymbol)
  } 
  
  def asTopLevelDef(
    classStore: ClassStore,
    namespace: Option[String],
    schemaOrProtocol: Either[Schema, Protocol],
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]]): List[Tree] = {
    
    schemaOrProtocol match {
      case Left(schema) => {
        registerType(schema, classStore)
        val caseClassDef = SpecificCaseClassTree.toCaseClassDef(
          classStore,
          namespace,
          schema,
          typeMatcher,
          maybeBaseTrait,
          maybeFlags)
        val companionDef = SpecificObjectTree.toCompanionDef(schema, maybeFlags)
        List(caseClassDef, companionDef)
      }
      case Right(protocol) => {
        val name: String = protocol.getName
        val ns: String = protocol.getNamespace
        val allTypes = protocol.getTypes.toList
        allTypes.foreach(schema => registerType(schema, classStore))
        val messages = protocol.getMessages.toMap
        def isEnum(schema: Schema) = schema.getType == Schema.Type.ENUM
        def isTopLevelNamespace(schema: Schema) = schema.getNamespace == ns
        if (messages.isEmpty) {
          val maybeNewBaseTrait = Some(name)
          val maybeFlags = Some(List(Flags.FINAL.toLong))
          val sealedTraitDef = SpecificTraitTree.toADTRootDef(protocol)
          val localSubTypes = allTypes.filter(isTopLevelNamespace)
          sealedTraitDef +: localSubTypes.filterNot(isEnum).flatMap(schema =>
  					asTopLevelDef(
              classStore,
              namespace,
              Left(schema),
              typeMatcher,
              maybeNewBaseTrait,
              maybeFlags))
        }
        else {
          val traitDef = SpecificTraitTree.toTraitDef(
            classStore,
            namespace,
            protocol,
            typeMatcher)
          val companionDef = SpecificObjectTree.toCompanionDef(protocol)
          List(traitDef, companionDef)
        }
      }
    }
    
  }

}


