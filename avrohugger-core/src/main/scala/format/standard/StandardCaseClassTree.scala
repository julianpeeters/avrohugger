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
    schema: Schema) = {
		// register new type
    val classSymbol = RootClass.newClass(schema.getName)
    classStore.accept(schema, classSymbol)
    val params: List[ValDef] = schema.getFields.toList.map { field =>
      val fieldName = field.name
      val fieldType = TypeMatcher.toType(classStore, namespace, field.schema)
      PARAM(fieldName, fieldType): ValDef
    }
    // generate class definition
    CASECLASSDEF(classSymbol).withParams(params).tree
	}

}
