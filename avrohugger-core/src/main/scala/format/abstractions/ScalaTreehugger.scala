package avrohugger
package format
package abstractions

import matchers.TypeMatcher
import stores.{ ClassStore, SchemaStore }
import avrohuggers.{ Schemahugger, Protocolhugger }

import org.apache.avro.{ Protocol, Schema }

import treehugger.forest.Tree

/** Parent to all ouput format treehuggers
  *
  * _ABSTRACT MEMBERS_: to be implemented by a subclass
  * asScalaCodeString
  * importer
  * protocolhugger
  * schemahugger
  *
  * _CONCRETE MEMBERS_: implementations to be inherited by a subclass
  * asTopLevelDefs
  */
trait ScalaTreehugger {

  ///////////////////////////// abstract members ///////////////////////////////  
  def asScalaCodeString(
    classStore: ClassStore,
    namespace: Option[String],
    schemaOrProtocol: Either[Schema, Protocol],
    typeMatcher: TypeMatcher,
    schemaStore: SchemaStore): String

  val importer: Importer

  val protocolhugger: Protocolhugger

  val schemahugger: Schemahugger
      
  ////////////////////////////// concrete members //////////////////////////////
  def asTopLevelDefs(
    classStore: ClassStore,
    namespace: Option[String],
    schemaOrProtocol: Either[Schema, Protocol],
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]]): List[Tree] = {
      
    schemaOrProtocol match {
      case Left(schema) => schemahugger.toTrees(
        classStore,
        namespace,
        schema,
        typeMatcher,
        maybeBaseTrait,
        maybeFlags
      )
      case Right(protocol) => protocolhugger.toTrees(
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