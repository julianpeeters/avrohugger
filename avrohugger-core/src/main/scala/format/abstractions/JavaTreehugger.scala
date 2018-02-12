package avrohugger
package format
package abstractions

import stores.ClassStore

import org.apache.avro.Schema

/** Parent to all ouput format treehuggers
  * Note: Java required for generating Enums (Specific API requires Java enums)
  * _ABSTRACT MEMBERS_: to be implemented by a subclass
  * asJavaCodeString
  */
trait JavaTreehugger {

  def asJavaCodeString(
    classStore: ClassStore,
    namespace: Option[String],
    schema: Schema): String
  
}
