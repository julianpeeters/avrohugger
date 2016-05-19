package avrohugger
package format
package standard

import org.apache.avro.Schema
import org.apache.avro.Schema.Type.{ ENUM, RECORD }

import treehugger.forest._
import definitions._
import treehuggerDSL._

object StandardTypeRegistrar {
  
  def registerType(schema: Schema, classStore: ClassStore): Unit = {
    schema.getType match {
      case RECORD => {
        val scalaClassSymbol = RootClass.newClass(schema.getName)
        classStore.accept(schema, scalaClassSymbol)
      }
      case ENUM => {
        val scalaEnumSymbol = RootClass.newClass(schema.getName + ".Value")
        classStore.accept(schema, scalaEnumSymbol)
      }
      case _ => sys.error("Only RECORD and ENUM can be top-level definitions")
    }  
  }
  
}