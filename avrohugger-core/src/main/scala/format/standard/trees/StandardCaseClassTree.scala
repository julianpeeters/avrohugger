package avrohugger
package format
package standard
package trees

import docs.ScalaDocGen

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.Schema

import scala.collection.JavaConversions._

object StandardCaseClassTree {

  def toCaseClassDef(
    classStore: ClassStore, 
    namespace: Option[String], 
    schema: Schema,
    typeMatcher: TypeMatcher,
    maybeBaseTrait: Option[String],
    maybeFlags: Option[List[Long]]) = {
      
    // register new type
    val classSymbol = RootClass.newClass(schema.getName)
    
    val params: List[ValDef] = schema.getFields.toList.map(f => {
      val fieldName = f.name
      val fieldType = typeMatcher.toScalaType(classStore, namespace, f.schema)
      PARAM(fieldName, fieldType): ValDef
    })
    
    // There could be base traits, flags, or both, and could have no fields
    val caseClassDef = (maybeBaseTrait, maybeFlags) match {
      case (Some(baseTrait), Some(flags)) => {
        if (!schema.getFields.toList.isEmpty) {
          CASECLASSDEF(classSymbol)
            .withFlags(flags:_*)
            .withParams(params)
            .withParents(baseTrait)
        }
        else { // in case of empty record
          CASECLASSDEF(classSymbol)
            .withFlags(flags:_*)
            .withParams(PARAM(""))
            .withParents(baseTrait)
        }
      }
      case (Some(baseTrait), None) => {
        if (!schema.getFields.toList.isEmpty) {
          CASECLASSDEF(classSymbol)
            .withParams(params)
            .withParents(baseTrait)
        }
        else { // in case of empty record
          CASECLASSDEF(classSymbol)
            .withParams(PARAM(""))
            .withParents(baseTrait)
        }
      }
      case (None, Some(flags)) => {
        if (!schema.getFields.toList.isEmpty) {
          CASECLASSDEF(classSymbol)
            .withFlags(flags:_*)
            .withParams(params)
        }
        else { // in case of empty record
          CASECLASSDEF(classSymbol)
            .withFlags(flags:_*)
            .withParams(PARAM(""))
        }
      }
      case (None, None) => {
        if (!schema.getFields.toList.isEmpty) {
          CASECLASSDEF(classSymbol)
            .withParams(params)
        }
        else { // in case of empty record
          CASECLASSDEF(classSymbol)
            .withParams(PARAM(""))
        }
      }
    }
    
    val classTree = caseClassDef.tree


    val treeWithScalaDoc = ScalaDocGen.docToScalaDoc(Left(schema), classTree)
    treeWithScalaDoc
  }

}
