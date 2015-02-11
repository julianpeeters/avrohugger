
package avrohugger
package specific
package methods

import treehugger.forest._
import definitions._
import treehuggerDSL._


object GetSchemaGenerator {
    def toDef = {
    	val SchemaClass = RootClass.newClass("org.apache.avro.Schema")
        DEF("getSchema", SchemaClass) := REF("SCHEMA$")
    }
}