package avrohugger
package format
package abstractions
package avrohuggers

import avrohugger.matchers.TypeMatcher
import avrohugger.stores.ClassStore
import org.apache.avro.{ Protocol, Schema }
import treehugger.forest.Tree

import scala.jdk.CollectionConverters._

trait Protocolhugger {

  def toTrees(
    classStore: ClassStore,
    namespace: Option[String],
    protocol: Protocol,
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]],
    restrictedFields: Boolean,
    targetScalaPartialVersion: String
  ): List[Tree]


  def getLocalSubtypes(protocol: Protocol): List[Schema] = {
    val protocolNS = protocol.getNamespace
    val types = protocol.getTypes().asScala.toList
    types.filter(_.getNamespace == protocolNS)
  }

  def isEnum(schema: Schema): Boolean = schema.getType == Schema.Type.ENUM

}
