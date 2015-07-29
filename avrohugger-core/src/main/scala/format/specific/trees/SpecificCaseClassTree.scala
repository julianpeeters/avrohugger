package avrohugger
package format
package specific
package trees
import methods._

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.Schema

import scala.collection.JavaConversions._

object SpecificCaseClassTree {

	def toCaseClassDef(
    classStore: ClassStore, 
    namespace: Option[String], 
    schema: Schema) = {

    // register new type
    val classSymbol = RootClass.newClass(schema.getName)
    classStore.accept(schema, classSymbol)

    // generate list of constructor parameters
    val params: List[ValDef] = schema.getFields.toList.map { field =>
      val fieldName = field.name
      val fieldType = TypeMatcher.toType(classStore, namespace, field.schema)
      VAR(fieldName, fieldType) := DefaultValueMatcher.getDefaultValue(field)
    }

    // extension
    val baseClass = RootClass.newClass("org.apache.avro.specific.SpecificRecordBase")

    // no-arg constructor: make arbitrary default if none is provided
    val defaultParams: List[Tree] = schema.getFields.toList.zip(params).map(f => {
      val (avroField, defaultValue) = (f._1, f._2.rhs)
      if (defaultValue == EmptyTree) DefaultParamMatcher.asDefaultParam(classStore, avroField.schema)
      else defaultValue
    })
    val defThis = DEFTHIS.withParams(PARAM("")).tree := THIS APPLY(defaultParams:_*)

    // methods - first add an index the the schema's fields
    val indexedFields = schema.getFields.toList.zipWithIndex.map(p => IndexedField(p._1, p._2))
    val defGet = GetGenerator.toDef(indexedFields)
    val defPut = PutGenerator.toDef(classStore, namespace, indexedFields)
    val defGetSchema = GetSchemaGenerator(classSymbol).toDef

    // define the class def with the members previously defined
    CASECLASSDEF(classSymbol).withParams(params).withParents(baseClass) := BLOCK(
      defThis,
      defGet,
      defPut,
      defGetSchema
    )
	}

}
