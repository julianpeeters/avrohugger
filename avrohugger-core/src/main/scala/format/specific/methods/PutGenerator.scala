package avrohugger
package format
package specific
package methods

import converters.ScalaConverter
import matchers.TypeMatcher
import stores.ClassStore

import treehugger.forest._
import definitions._
import treehuggerDSL._

object PutGenerator {

  private def asPutCase(classStore: ClassStore,
    namespace: Option[String],
    field: IndexedField,
    typeMatcher: TypeMatcher,
    classSymbol: ClassSymbol,
    targetScalaPartialVersion: String) = {
    CASE(LIT(field.idx)) ==> {
      THIS DOT FieldRenamer.rename(field.avroField.name) :=
        BLOCK(ScalaConverter.convertFromJava(
          classStore,
          namespace,
          field.avroField.schema,
          REF("getSchema").DOT("getFields").APPLY().DOT("get").APPLY(REF("field$")).DOT("schema").APPLY(),
          false,
          REF("value"),
          typeMatcher,
          classSymbol,
          targetScalaPartialVersion))
          .AS(typeMatcher.toScalaType(classStore, namespace, field.avroField.schema))
    }
  }

  def toDef(
    classStore: ClassStore,
    namespace: Option[String],
    indexedFields: List[IndexedField],
    typeMatcher: TypeMatcher,
    classSymbol: ClassSymbol,
    targetScalaPartialVersion: String) = {

    val errorCase = CASE(WILDCARD) ==> THROW(NEW("org.apache.avro.AvroRuntimeException", LIT("Bad index")))
    val casesPut = indexedFields.map(field => asPutCase(classStore,
      namespace,
      field,
      typeMatcher,
      classSymbol,
      targetScalaPartialVersion)) :+ errorCase

    DEF("put", UnitClass) withParams(PARAM("field$", IntClass), PARAM("value", AnyClass)) := BLOCK(
      REF("field$") withAnnots (ANNOT("switch")) MATCH (casesPut), UNIT
    )
  }

}
