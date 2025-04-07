package avrohugger
package format
package abstractions
package avrohuggers

import stores.{ ClassStore, SchemaStore }
import matchers.TypeMatcher

import org.apache.avro.{ Protocol, Schema }

import treehugger.forest.Tree

import scala.jdk.CollectionConverters._

trait Protocolhugger {

  def toTrees(
    schemaStore: SchemaStore,
    classStore: ClassStore,
    namespace: Option[String],
    protocol: Protocol,
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]],
    restrictedFields: Boolean,
    targetScalaPartialVersion: String): List[Tree]


  def getLocalSubtypes(protocol: Protocol): List[Schema] = {
    val protocolNS = protocol.getNamespace
    val types = protocol.getTypes().asScala.toList
    types.filter(isTopLevelNamespace(_, protocolNS))
  }

  def isEnum(schema: Schema): Boolean = schema.getType == Schema.Type.ENUM

  private def isTopLevelNamespace(schema: Schema, protocolNS: String) = schema.getNamespace == protocolNS

}
