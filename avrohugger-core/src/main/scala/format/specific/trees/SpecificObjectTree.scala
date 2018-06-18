package avrohugger
package format
package specific
package trees

import avrohugger.format.specific.methods.LogicalTypesGenerator
import generators.ScalaDocGenerator
import org.apache.avro.{Protocol, Schema}
import treehugger.forest._
import definitions._
import treehuggerDSL._

// only companions, so no doc generation is required here
object SpecificObjectTree {

  // Companion to case classes
  def toCaseCompanionDef(schema: Schema, maybeFlags: Option[List[Long]]) = {
    val ParserClass = RootClass.newClass("org.apache.avro.Schema.Parser")
    val objectDef = maybeFlags match {
      case Some(flags) => OBJECTDEF(schema.getName).withFlags(flags:_*)
      case None => OBJECTDEF(schema.getName)
    }

    val schemaDef = VAL(REF("SCHEMA$")) := {
      (NEW(ParserClass)) APPLY() DOT "parse" APPLY(LIT(schema.toString))
    }
    val encoderDef = LogicalTypesGenerator.toObjectDef(schema)
    val parts = List(schemaDef) ++ encoderDef
    // companion object definition
    objectDef := BLOCK(parts: _*)
  }
  
  // Companion to traits that have messages
  def toTraitCompanionDef(protocol: Protocol) = {
    val ProtocolClass = RootClass.newClass("org.apache.avro.Protocol")
    // companion object definition
    OBJECTDEF(protocol.getName) := BLOCK(
      VAL("PROTOCOL", ProtocolClass).withFlags(Flags.FINAL) := {
        REF(ProtocolClass) DOT "parse" APPLY(LIT(protocol.toString))
      }
    )
  }
  
}
