package avrohugger
package format
package specific

import trees.{ SpecificCaseClassTree, SpecificObjectTree, SpecificTraitTree }
import util.ScalaDocGen
import input.reflectivecompilation.schemagen.SchemaStore

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
      case Left(schema) => {
        SpecificImports.getImports(schema, namespace, schemaStore)
      }
      case Right(protocol) => protocol.getTypes.toList.flatMap(schema => 
        SpecificImports.getImports(schema, namespace, schemaStore))
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

    val codeString = treeToString(tree)
    codeString
  }
  
  def asTopLevelDef(
    classStore: ClassStore,
    namespace: Option[String],
    schemaOrProtocol: Either[Schema, Protocol],
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]]): List[Tree] = {
    
    schemaOrProtocol match {
      case Left(schema) => SpecificSchemaHandler.toTrees(
        classStore,
        namespace,
        schema,
        typeMatcher,
        maybeBaseTrait,
        maybeFlags
      )
      case Right(protocol) => SpecificProtocolHandler.toTrees(
        classStore,
        namespace,
        protocol,
        typeMatcher,
        maybeBaseTrait,
        maybeFlags
      )
    }
    
  }

}


