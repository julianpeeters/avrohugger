package avrohugger
package format
package abstractions

import matchers.TypeMatcher
import stores.{ ClassStore, SchemaStore }
import avrohuggers.{ Schemahugger, Protocolhugger }

import org.apache.avro.{ Protocol, Schema }

/** Parent to all ouput format treehuggers
  *
  * _ABSTRACT MEMBERS_: to be implemented by a subclass
  * asScalaCodeString
  * importer
  * protocolhugger
  * schemahugger
  */
trait ScalaTreehugger {

  ///////////////////////////// abstract members ///////////////////////////////
  def asScalaCodeString(
    classStore: ClassStore,
    namespace: Option[String],
    schemaOrProtocol: Either[Schema, Protocol],
    typeMatcher: TypeMatcher,
    schemaStore: SchemaStore,
    restrictedFields: Boolean,
    targetScalaPartialVersion: String): String

  val importer: Importer

  val protocolhugger: Protocolhugger

  val schemahugger: Schemahugger



}
