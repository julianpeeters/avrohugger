package avrohugger
package format
package standard
package trees

import generators.ScalaDocGenerator
import matchers.TypeMatcher
import types._
import org.apache.avro.Schema.Type.{ ENUM, RECORD }
import org.apache.avro.{ Protocol, Schema }
import treehugger.forest._
import treehuggerDSL._

import scala.jdk.CollectionConverters._

object StandardTraitTree {

  def toScala3EnumDef(schema: Schema): List[Tree] = {
    val enumName = schema.getName
    val enumCases = schema.getEnumSymbols.asScala.map(_.toString).mkString(", ")
    val enumSource = s"enum $enumName { case $enumCases }\n"
    List(Scala3EnumSourceCode(enumSource))
  }

  def toADTRootDef(protocol: Protocol, typeMatcher: TypeMatcher) = {
    def isEnum(schema: Schema) = schema.getType == ENUM

    val sealedTraitTree = TRAITDEF(protocol.getName).withFlags(Flags.SEALED)
    val adtRootTree = {
      val adtSubTypes = typeMatcher.avroScalaTypes.`enum` match {
        case JavaEnum => protocol.getTypes().asScala.toList.filterNot(isEnum)
        case ScalaCaseObjectEnum => protocol.getTypes().asScala.toList
        case Scala3Enum => protocol.getTypes().asScala.toList
        case ScalaEnumeration => protocol.getTypes().asScala.toList
        case EnumAsScalaString => protocol.getTypes().asScala.filterNot(isEnum)
      }
      if (adtSubTypes.forall(schema => schema.getType == RECORD)) {
        sealedTraitTree
          .withParents("Product")
          .withParents("Serializable")
      }
      else sealedTraitTree
    }
    ScalaDocGenerator.docToScalaDoc(
      Right(protocol),
      adtRootTree)
  }

  def toCaseObjectEnumDef(schema: Schema,
    maybeBaseTrait: Option[String]): List[Tree] = {
    val adtRootTree: Tree = maybeBaseTrait match {
      case Some(baseTrait) =>
        TRAITDEF(schema.getName).withFlags(Flags.SEALED).withParents(baseTrait)
      case None =>
        TRAITDEF(schema.getName).withFlags(Flags.SEALED)
    }
    val adtSubTypes: List[Tree] = schema.getEnumSymbols.asScala
      .map(enumSymbol => enumSymbol.toString)
      .map(enumSymbolString => {
        (CASEOBJECTDEF(enumSymbolString).withParents(schema.getName): Tree)
      }).toList
    val objectTree = OBJECTDEF(schema.getName) := Block(adtSubTypes: _*)
    val adtRootTreeWithScalaDoc: Tree = ScalaDocGenerator.docToScalaDoc(
      Left(schema),
      adtRootTree)
    List(adtRootTreeWithScalaDoc, objectTree)
  }
}
