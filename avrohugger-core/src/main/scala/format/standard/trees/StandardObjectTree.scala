package avrohugger
package format
package standard
package trees

import stores.ClassStore

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.Schema

import scala.collection.JavaConversions._

object StandardObjectTree {

  def toScalaEnumDef(
    classStore: ClassStore, 
    schema: Schema,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]]) = {
      
    val objectDef = (maybeBaseTrait, maybeFlags) match {
      case (Some(baseTrait), Some(flags)) => 
        OBJECTDEF(schema.getName)
          .withFlags(flags:_*)
          .withParents("Enumeration")
          .withParents(baseTrait) 
      case (Some(baseTrait), None) =>
        OBJECTDEF(schema.getName)
          .withParents("Enumeration")
          .withParents(baseTrait)
      case (None, Some(flags)) => 
        OBJECTDEF(schema.getName)
          .withFlags(flags:_*)
          .withParents("Enumeration")
      case (None, None) =>
        OBJECTDEF(schema.getName)
          .withParents("Enumeration")
    }
    
    val objectTree = objectDef := BLOCK(
      TYPEVAR(schema.getName) := REF("Value"),
      VAL(schema.getEnumSymbols.mkString(", ")) := REF("Value")
    )

    val treeWithScalaDoc = ScalaDocGenerator.docToScalaDoc(
      Left(schema),
      objectTree)
      
    treeWithScalaDoc
  }
  
  
  
}
