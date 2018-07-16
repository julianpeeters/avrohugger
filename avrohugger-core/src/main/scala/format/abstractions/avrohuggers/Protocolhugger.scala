package avrohugger
package format
package abstractions
package avrohuggers

import stores.{ClassStore, SchemaStore}
import matchers.TypeMatcher

import org.apache.avro.{ Protocol, Schema }

import treehugger.forest.Tree

import scala.collection.JavaConverters._

trait Protocolhugger {

  def toTrees(
    schemaStore: SchemaStore,
    classStore: ClassStore,
    namespace: Option[String],
    protocol: Protocol,
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]],
    restrictedFields: Boolean): List[Tree]


  def getLocalSubtypes(protocol: Protocol): List[Schema] = {
    val protocolNS = protocol.getNamespace
    val types = protocol.getTypes.asScala.toList
    def isTopLevelNamespace(schema: Schema) = schema.getNamespace == protocolNS
    types.filter(isTopLevelNamespace)
  }

  def isEnum(schema: Schema) = schema.getType == Schema.Type.ENUM


}
