package avrohugger
package format

import avrohugger.input.reflectivecompilation.schemagen.SchemaStore

import org.apache.avro.Protocol
import org.apache.avro.Schema

trait SourceFormat {

  // abstract members to be implemented by a subclass
  val toolName: String

  val toolShortDescription: String

  def fileExt(schemaOrProtocol: Either[Schema, Protocol]): String

  val typeMatcher: TypeMatcher

  def asDefinitionString(
  	classStore: ClassStore, 
    namespace: Option[String], 
    schemaOrProtocol: Either[Schema, Protocol],
    schemaStore: SchemaStore): String

  def writeToFile(
    classStore: ClassStore, 
    namespace: Option[String], 
    schemaOrProtocol: Either[Schema, Protocol],
    outDir: String,
    schemaStore: SchemaStore): Unit


  // concrete member
  def getName(schemaOrProtocol: Either[Schema, Protocol]): String = {
    schemaOrProtocol match {
      case Left(schema) => schema.getName
      case Right(protocol) => protocol.getName
    }
  }
  
}
