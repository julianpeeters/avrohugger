package avrohugger
package format
package specific
package trees
import methods._

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.Schema

import scala.collection.JavaConversions._

object SpecificObjectTree {

  def toObjectDef(classStore: ClassStore, schema: Schema) = {
    // register  types
    val classSymbol = RootClass.newClass(schema.getName)
    classStore.accept(schema, classSymbol)
    val ParserClass = RootClass.newClass("org.apache.avro.Schema.Parser")

    // new val
    val valSchema = VAL(REF("SCHEMA$")) := (NEW(ParserClass)) APPLY() DOT "parse" APPLY(LIT(schema.toString))
    // companion object definion
    OBJECTDEF(schema.getName) := BLOCK(
      valSchema
    )
  }

}
