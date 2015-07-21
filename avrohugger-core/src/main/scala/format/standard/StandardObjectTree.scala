package avrohugger
package format
package standard

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.Schema

import scala.collection.JavaConversions._

object StandardObjectTree {

  def toObjectDef(
    classStore: ClassStore, 
    schema: Schema) = {

    // register new type
    val classSymbol = RootClass.newClass(schema.getName)
    classStore.accept(schema, classSymbol)
    OBJECTDEF(schema.getName) withParents("Enumeration") := BLOCK(
      TYPEVAR(schema.getName) := REF("Value"),
      VAL(schema.getEnumSymbols.mkString(", ")) := REF("Value")
    )
  }
  
}
