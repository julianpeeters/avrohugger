package avrohugger
package format
package standard

import trees.{ StandardCaseClassTree, StandardObjectTree, StandardTraitTree }

import avrohugger.input.DependencyInspector._
import avrohugger.input.NestedSchemaExtractor._
import avrohugger.input.reflectivecompilation.schemagen.SchemaStore

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Field
import org.apache.avro.Schema.Type.{ ENUM, RECORD }

import treehugger.forest._
import definitions._
import treehuggerDSL._

import scala.collection.JavaConversions._

object StandardTreehugger {

  def asScalaCodeString(
		classStore: ClassStore,
    schemaOrProtocol: Either[Schema, Protocol], 
		namespace: Option[String],
    typeMatcher: TypeMatcher,
    schemaStore: SchemaStore): String = {
      
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
      
    // wrap the imports and class definition in a block with comment and package
    val tree = {
      val blockContent = imports ++ topLevelDefs
      if (namespace.isDefined) BLOCK(blockContent).inPackage(namespace.get)
      else BLOCK(blockContent:_*).withoutPackage
    }.withDoc("MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY")

    // SpecificCompiler can't return a tree for Java enums, so return
    // a string here for a consistent api vis a vis *ToFile and *ToStrings
    treeToString(tree)
  }

  def isRecord(schema: Schema): Boolean = ( schema.getType == RECORD )

  def getImports(
    schema: Schema,
    currentNamespace: Option[String],
    schemaStore: SchemaStore): List[Import] = {
    if (isRecord(schema)) {
      val topLevelSchemas = schema::(getNestedSchemas(schema, schemaStore)) 
      topLevelSchemas.filter(isRecord).flatMap(s => s.getFields)
        .filter(field => getReferredNamespace(field.schema).isDefined)
        .filter(field => getReferredNamespace(field.schema) != currentNamespace)
        .distinct
        .groupBy( field => getReferredNamespace(field.schema).get )
        .toList
        .map { _ match { case(packageName, fields) =>
          IMPORT(packageName, fields.map( getReferredTypeName ).distinct )
        }
      }
    }
    else List.empty
  }
  
  def registerType(schema: Schema, classStore: ClassStore): Unit = {
    schema.getType match {
      case RECORD => {
        val scalaClassSymbol = RootClass.newClass(schema.getName)
        classStore.accept(schema, scalaClassSymbol)
      }
      case ENUM => {
        val scalaEnumSymbol = RootClass.newClass(schema.getName + ".Value")
        classStore.accept(schema, scalaEnumSymbol)
      }
      case _ => sys.error("Only RECORD and ENUM can be top-level definitions")
    }  
  }
  
  def asTopLevelDef(
    classStore: ClassStore,
    namespace: Option[String],
    schemaOrProtocol: Either[Schema, Protocol],
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]]): List[Tree] = {

    schemaOrProtocol match {
      case Left(schema) => { // as case class definition
        StandardTypeRegistrar.registerType(schema, classStore)
        schema.getType match {
          case RECORD => 
            val classDef = StandardCaseClassTree.toCaseClassDef(
              classStore,
              namespace,
              schema,
              typeMatcher,
              maybeBaseTrait,
              maybeFlags)
            List(classDef)
          case ENUM => 
            val objectDef = StandardObjectTree.toObjectDef(
              classStore,
              schema,
              maybeBaseTrait,
              maybeFlags)
            List(objectDef)
          case _ => sys.error("Only RECORD or ENUM can be toplevel definitions")
        }
      }
      case Right(protocol) => {
        val name: String = protocol.getName
        val ns: String = protocol.getNamespace
        val allTypes = protocol.getTypes.toList
        allTypes.foreach(schema => 
          StandardTypeRegistrar.registerType(schema, classStore))
        def isTopLevelNamespace(schema: Schema) = schema.getNamespace == ns
        val maybeNewBaseTrait = Some(name)
        val maybeNewFlags = Some(List(Flags.FINAL.toLong))
        val traitDef = StandardTraitTree.toADTRootDef(protocol)
        val localSubTypes = allTypes.filter(isTopLevelNamespace)
        if (localSubTypes.length > 1) {
          traitDef +: localSubTypes.flatMap(schema =>
            StandardTreehugger.asTopLevelDef(
              classStore,
              namespace,
              Left(schema),
              typeMatcher,
              maybeNewBaseTrait,
              maybeNewFlags))
        }
        // if only one Scala type is defined, then don't generate sealed trait
        else {
          // no sealed trait tree, but could still need a top-level doc
          val docTrees = {
            Option(protocol.getDoc) match {
              case Some(doc) => 
                List(ScalaDocGen.docToScalaDoc(Right(protocol), EmptyTree))
              case None => List.empty
            }	
          } 
          docTrees ::: localSubTypes.flatMap(schema =>
            StandardTreehugger.asTopLevelDef(
              classStore,
              namespace,
              Left(schema),
              typeMatcher,
              None,
              None))
        }
      }
    }
    
    
  }
  
}
