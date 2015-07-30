package avrohugger
package format
package standard

import avrohugger.format.DependencyInspectionSupport._
import org.apache.avro.Schema
import org.apache.avro.Schema.Field
import org.apache.avro.Schema.Type.{ENUM, RECORD}
import treehugger.forest._
import treehuggerDSL._

import scala.collection.JavaConversions._

object StandardTreehugger {

	def asScalaCodeString(
		classStore: ClassStore,
		schema: Schema, 
		namespace: Option[String]): String = {

    val topLevelDef = schema.getType match {
      case RECORD => StandardCaseClassTree.toCaseClassDef(classStore, namespace, schema)
      case ENUM => StandardObjectTree.toObjectDef(classStore, schema)
      case _ => sys.error("Only RECORD and ENUM can be top-level definitions")
    }

    
    val imports = if( isRecord(schema) ) getImports(schema, namespace) else List.empty
      

    // wrap the class definition in a block with a comment and a package
    val tree = {
      val blockContent = imports ++ List(topLevelDef)

      if (namespace.isDefined) BLOCK(blockContent).inPackage(namespace.get)
      else BLOCK(blockContent)
    }.withDoc("MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY")

    // SpecificCompiler can't return a tree for Java enums, so return
    // a string here for a consistent api vis a vis *ToFile and *ToStrings
    treeToString(tree)
  }

  def isRecord(schema: Schema): Boolean = scala.util.Try( schema.getFields ).isSuccess

  def getImports(schema: Schema, currentNamespace: Option[String]): Iterable[Import] = {
    schema
      .getFields.toList
      .filter( getFieldReferredNamespace(_).isDefined )
      .filter { field => getFieldReferredNamespace(field) != currentNamespace }
      .distinct
      .groupBy( getFieldReferredNamespace(_).get )
      .map { _ match { case(packageName, fields) =>
        IMPORT(packageName, fields.map( getReferredTypeName ) )
      }
    }
  }
}
