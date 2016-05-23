package avrohugger
package format
package specific

import converters.AvroConverter
import input.reflectivecompilation.schemagen.SchemaStore
import util.ScalaDocGen

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Field
import org.apache.avro.Schema.Type.RECORD

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
      
    val switchAnnotSymbol = RootClass.newClass("scala.annotation.switch")
    val switchImport = IMPORT(switchAnnotSymbol)
    
    // imports in case a field type is from a different namespace
    val imports: List[Import] = schemaOrProtocol match {
      case Left(schema) => {
        val deps = SpecificImports.getImports(schema, namespace, schemaStore)
        if (schema.getType == RECORD) switchImport :: deps
        else deps
      }
      case Right(protocol) => {
        val types = protocol.getTypes.toList
        val messages = protocol.getMessages.toMap
        val deps = types.flatMap(schema =>
          SpecificImports.getImports(schema, namespace, schemaStore))
        if (messages.isEmpty) switchImport :: deps // for ADT
        else List.empty // for RPC
      }
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
      case Left(schema) => AvroConverter.schemaToTrees(
        classStore,
        namespace,
        schema,
        typeMatcher,
        maybeBaseTrait,
        maybeFlags
      )
      case Right(protocol) => AvroConverter.protocolToTrees(
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


