package avrohugger
package format
package specific
package methods

import treehugger.forest._
import definitions._
import treehuggerDSL._

case class GetSchemaGenerator(companionObjectName: Symbol) {
    def toDef = {
      val SchemaClass = RootClass.newClass("org.apache.avro.Schema")
      DEF("getSchema", SchemaClass) := REF(companionObjectName) DOT "SCHEMA$"
    }
}