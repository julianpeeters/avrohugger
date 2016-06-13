package avrohugger
package format
package scavro
package trees

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.Schema.Type.RECORD
import org.apache.avro.Protocol

import scala.collection.JavaConversions._


object ScavroTraitTree {

  def toADTRootDef(protocol: Protocol) = {
    val sealedTraitTree =  TRAITDEF(protocol.getName).withFlags(Flags.SEALED)
    val adtRootTree = {
      if (protocol.getTypes.toList.forall(schema => schema.getType == RECORD)) {
        sealedTraitTree
          .withParents("AvroSerializeable")
          .withParents("Product")
          .withParents("Serializable")
      }
      else sealedTraitTree
    }
    val treeWithScalaDoc = ScalaDocGenerator.docToScalaDoc(
      Right(protocol),
      adtRootTree)
    
    treeWithScalaDoc
  }
  
}
