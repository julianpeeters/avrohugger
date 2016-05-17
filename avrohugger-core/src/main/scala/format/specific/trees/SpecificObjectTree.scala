package avrohugger
package format
package specific
package trees

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.{ Protocol, Schema }

import scala.collection.JavaConversions._

object SpecificObjectTree {

  // Companion to case classes
  def toCompanionDef(schema: Schema, maybeFlags: Option[List[Long]]) = {
    val ParserClass = RootClass.newClass("org.apache.avro.Schema.Parser")
    val objectDef = maybeFlags match {
      case Some(flags) => OBJECTDEF(schema.getName).withFlags(flags:_*)
      case None => OBJECTDEF(schema.getName)
    }
    // companion object definition
    objectDef := BLOCK(
      VAL(REF("SCHEMA$")) := {
        (NEW(ParserClass)) APPLY() DOT "parse" APPLY(LIT(schema.toString))
      }
    )
  }
  
  // Companion to traits that have messages
  def toCompanionDef(protocol: Protocol) = {
    val ProtocolClass = RootClass.newClass("org.apache.avro.Protocol")
    // companion object definition
    OBJECTDEF(protocol.getName) := BLOCK(
      VAL("PROTOCOL", ProtocolClass).withFlags(Flags.FINAL) := {
        REF(ProtocolClass) DOT "parse" APPLY(LIT(protocol.toString))
      }
    )
  }
  

}
