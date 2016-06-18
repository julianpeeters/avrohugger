package avrohugger
package format
package standard
package trees

import matchers.TypeMatcher

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Type.{ ENUM, RECORD }

import scala.collection.JavaConversions._

object StandardTraitTree {

  def toADTRootDef(protocol: Protocol, typeMatcher: TypeMatcher) = {
    def isEnum(schema: Schema) = schema.getType == ENUM
    val sealedTraitTree = TRAITDEF(protocol.getName).withFlags(Flags.SEALED)
    val adtRootTree = {
      val adtSubTypes = typeMatcher.customEnumStyleMap.get("enum") match {
        case Some("java enum") => protocol.getTypes.toList.filterNot(isEnum)
        case _ => protocol.getTypes.toList
      }
      if (adtSubTypes.forall(schema => schema.getType == RECORD)) {
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
