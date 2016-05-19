package avrohugger
package format
package specific

import org.apache.avro.Schema

import treehugger.forest._
import definitions._
import treehuggerDSL._

object SpecificTypeRegistrar {
  
  def registerType(schema: Schema, classStore: ClassStore): Unit = {
    val classSymbol = RootClass.newClass(schema.getName)
    classStore.accept(schema, classSymbol)
  } 
  
}