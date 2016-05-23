package avrohugger
package format
package standard

import avrohuggers.{ Protocolhugger, Schemahugger }
import input.DependencyInspector._
import input.NestedSchemaExtractor._
import input.reflectivecompilation.schemagen.SchemaStore

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Field
import org.apache.avro.Schema.Type.{ RECORD }

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
      case Left(schema) => {
        StandardImports.getImports(schema, namespace, schemaStore)
      }
      case Right(protocol) => protocol.getTypes.toList.flatMap(schema => 
        StandardImports.getImports(schema, namespace, schemaStore))
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
  
  def asTopLevelDef(
    classStore: ClassStore,
    namespace: Option[String],
    schemaOrProtocol: Either[Schema, Protocol],
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]]): List[Tree] = {

    schemaOrProtocol match {
      case Left(schema) => Schemahugger.toTrees(
        classStore,
        namespace,
        schema,
        typeMatcher,
        maybeBaseTrait,
        maybeFlags
      )
      case Right(protocol) => Protocolhugger.toTrees(
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
