package avrohugger
package format
package abstractions
package avrohuggers

import stores.ClassStore
import matchers.TypeMatcher

import org.apache.avro.Protocol

import treehugger.forest.Tree

trait Protocolhugger {
  
  def toTrees(
    classStore: ClassStore,
    namespace: Option[String],
    protocol: Protocol,
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]]): List[Tree]
    
}