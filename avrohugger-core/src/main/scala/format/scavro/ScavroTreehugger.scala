package avrohugger
package format
package scavro

import trees.{ ScavroCaseClassTree, ScavroObjectTree, ScavroTraitTree }
import input.reflectivecompilation.schemagen._

import avrohugger.input.DependencyInspector._
import avrohugger.input.NestedSchemaExtractor._

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Field
import org.apache.avro.Schema.Type.{ENUM, RECORD}
import treehugger.forest._
import definitions._
import treehuggerDSL._

import scala.collection.JavaConversions._

object ScavroTreehugger {

	def asScalaCodeString(
		classStore: ClassStore,
    schemaOrProtocol: Either[Schema, Protocol],
		namespace: Option[String],
    typeMatcher: TypeMatcher,
	  schemaStore: SchemaStore): String = {
			
		val imports: List[Import] = schemaOrProtocol match {
			case Left(schema) => 
			  if(schema.getType == RECORD) getImports(schema, namespace, schemaStore)
				else List.empty
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

    // wrap the imports and classdef in a block with a comment and a package
		val tree = {
      val blockContent = imports ++ topLevelDefs
      if (namespace.isDefined) BLOCK(blockContent).inPackage(namespace.get)
      else BLOCK(blockContent:_*).withoutPackage
    }.withDoc("MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY")
		
    // SpecificCompiler can't return a tree for Java enums, so return
    // a String here for a consistent api vis a vis *ToFile and *ToStrings
    treeToString(tree)
  }

  def isRecord(schema: Schema): Boolean = ( schema.getType == RECORD )

  def getImports(
		schema: Schema,
		currentNamespace: Option[String],
		schemaStore: SchemaStore): List[Import] = {
			
	  val SchemaClass = RootClass.newClass("org.apache.avro.Schema")
		val ScavroPackage = RootClass.newPackage("org.oedura.scavro")

		val schemaImport = IMPORT(SchemaClass)
		val scavroImport = IMPORT(
			ScavroPackage,
			"AvroMetadata",
			"AvroReader",
			"AvroSerializeable")
			
		if (isRecord(schema)) {
			val javaClassRename = "J" + schema.getName
			val renameImport = schema.getNamespace match {
				case null =>
				  sys.error("Scavro does not support schemas without namespaces.")
				case _ =>
				  IMPORT(schema.getNamespace,RENAME(schema.getName) ==> javaClassRename)
			}
	    val topLevelSchemas: List[Schema] =
				schema::(getNestedSchemas(schema, schemaStore))
	    val recordImports: List[Import] = topLevelSchemas.filter(isRecord)
			  .flatMap(s => s.getFields)
	      .filter(field => getReferredNamespace(field.schema).isDefined)
	      .filter(field => getReferredNamespace(field.schema) != currentNamespace)
	      .distinct
	      .groupBy(field => getReferredNamespace(field.schema).get)
				.toList
	      .map { _ match { case(packageName, fields) =>
	        IMPORT(packageName, fields.map( field => {
						val renamedType = "J" + getReferredTypeName(field)
	          RENAME(getReferredTypeName(field)) ==> renamedType
	        } ).distinct )
	      }}
				
			// if the record has an array, or a map or union that may have an array
			// value type, we need an extra javaConversions import
			val fieldTypes = schema.getFields.map(f => f.schema.getType)
			val hasArrayField = fieldTypes.contains(Schema.Type.ARRAY)
			val hasMapField = fieldTypes.contains(Schema.Type.MAP)
			val hasUnionField = fieldTypes.contains(Schema.Type.UNION)
			val javaConversionsImport = IMPORT("scala.collection.JavaConversions._")
			if( hasArrayField || hasMapField || hasUnionField) {
				List(schemaImport, scavroImport, renameImport, javaConversionsImport) ++
				  recordImports
			}
			else List(schemaImport, scavroImport, renameImport) ++ recordImports

		}
		else List(schemaImport, scavroImport)
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
			case Left(schema) =>
				// register classes
				val ScalaClass = RootClass.newClass(schema.getName)
				val JavaClass = RootClass.newClass("J" + schema.getName)
			  registerType(schema, classStore)
				schema.getType match {
		      case RECORD =>
						val caseClassDef = ScavroCaseClassTree.toCaseClassDef(
							classStore,
							namespace,
							schema,
							ScalaClass,
							JavaClass,
							typeMatcher,
						  maybeBaseTrait,
						  maybeFlags)
						val companionDef = ScavroObjectTree.toCompanionDef(
							classStore,
							schema,
							ScalaClass,
							JavaClass,
							typeMatcher,
						  maybeFlags)
		        List(caseClassDef, companionDef)
		      case ENUM =>
					  val objectDef = ScavroObjectTree.toObjectDef(
							classStore,
							schema,
							maybeBaseTrait,
						  maybeFlags)
					  List(objectDef)
		      case _ =>
					  sys.error("Only RECORD and ENUM can be top-level definitions")
				}
			case Right(protocol) =>
				val name: String = protocol.getName
				val ns: String = protocol.getNamespace
				val allTypes = protocol.getTypes.toList
				allTypes.foreach(schema => registerType(schema, classStore))
				def isTopLevelNamespace(schema: Schema) = schema.getNamespace == ns
				val maybeNewBaseTrait = Some(name)
				val maybeFlags = Some(List(Flags.FINAL.toLong))
				val traitDef = ScavroTraitTree.toADTRootDef(protocol)
				val localSubTypes = allTypes.filter(isTopLevelNamespace)
				if (localSubTypes.length > 1) {
					traitDef +: localSubTypes.flatMap(schema =>
						asTopLevelDef(
							classStore,
							namespace,
							Left(schema),
							typeMatcher,
							maybeNewBaseTrait,
						  maybeFlags))
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
						asTopLevelDef(
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
