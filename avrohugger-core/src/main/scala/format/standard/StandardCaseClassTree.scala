package avrohugger
package format
package standard

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
    typeMatcher: TypeMatcher) = {
      
    // register new type
    val classSymbol = RootClass.newClass(schema.getName)
    classStore.accept(schema, classSymbol)
    
    val params: List[ValDef] = schema.getFields.toList.map { field =>
      val fieldName = field.name
      val fieldType = typeMatcher.toScalaType(classStore, namespace, field.schema)
      PARAM(fieldName, fieldType): ValDef
    }
    
    val caseClassTree = { // generate class definition
      if (!schema.getFields.toList.isEmpty) {
        CASECLASSDEF(classSymbol).withParams(params).tree
      }
      else { // in case of empty record
        CASEOBJECTDEF(classSymbol).tree
      }
    }

    val treeWithScalaDoc = ScalaDocGen.docToScalaDoc(schema, caseClassTree)
    treeWithScalaDoc
  }

}
