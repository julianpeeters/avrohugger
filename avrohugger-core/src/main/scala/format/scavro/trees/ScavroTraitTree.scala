package avrohugger
package format
package scavro
package trees

import matchers.TypeMatcher

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.Schema.Type.{ ENUM, RECORD }
import org.apache.avro.{ Protocol, Schema }

import scala.collection.JavaConversions._


object ScavroTraitTree {

  def toADTRootDef(protocol: Protocol, typeMatcher: TypeMatcher) = {
    def isEnum(schema: Schema) = schema.getType == ENUM
    val sealedTraitTree =  TRAITDEF(protocol.getName).withFlags(Flags.SEALED)
    val adtRootTree = {
      val adtSubTypes = typeMatcher.customEnumStyleMap.get("enum") match {
        case Some("java enum") => protocol.getTypes.toList.filterNot(isEnum)
        case _ => protocol.getTypes.toList
      }
      if (adtSubTypes.forall(schema => schema.getType == RECORD)) {
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
