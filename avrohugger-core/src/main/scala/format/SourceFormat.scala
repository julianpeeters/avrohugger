package avrohugger
package format

import avrohugger.input.reflectivecompilation.schemagen.SchemaStore

import org.apache.avro.Protocol
import org.apache.avro.Schema

import scala.collection.JavaConversions._

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

  def getName(schemaOrProtocol: Either[Schema, Protocol]): String
  
  
  // concrete member
  def getLocalSubtypes(protocol: Protocol): List[Schema] = {
    val ns = protocol.getNamespace
    val allTypes = protocol.getTypes.toList
    def isTopLevelNamespace(schema: Schema) = schema.getNamespace == ns
    allTypes.filter(isTopLevelNamespace)
  }
  
}
