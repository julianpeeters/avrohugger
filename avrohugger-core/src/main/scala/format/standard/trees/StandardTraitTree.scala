package avrohugger
package format
package standard
package trees

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.Protocol
import org.apache.avro.Schema.Type.RECORD

import scala.collection.JavaConversions._

object StandardTraitTree {

  def toADTRootDef(protocol: Protocol) = {
    val sealedTraitTree =  TRAITDEF(protocol.getName).withFlags(Flags.SEALED)
    val adtRootTree = {
      if (protocol.getTypes.toList.forall(schema => schema.getType == RECORD)) {
        sealedTraitTree
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
