package avrohugger
package format
package scavro
package trees

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.Protocol

object ScavroTraitTree {

  def toADTRootDef(protocol: Protocol) = {
    val traitTree =
      TRAITDEF(protocol.getName)
        .withFlags(Flags.SEALED)
        .withParents("AvroSerializeable")
        .withParents("Product")
        .withParents("Serializable")
    val treeWithScalaDoc = ScalaDocGenerator.docToScalaDoc(
      Right(protocol),
      traitTree)
    
    treeWithScalaDoc
  }
  
}
