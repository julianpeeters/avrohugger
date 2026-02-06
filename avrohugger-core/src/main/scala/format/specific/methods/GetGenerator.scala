package avrohugger
package format
package specific
package methods

import converters.JavaConverter
import avrohugger.matchers.TypeMatcher
import treehugger.forest._
import definitions._
import treehuggerDSL._

object GetGenerator {
  def toDef(
    indexedFields: List[IndexedField],
    classSymbol: ClassSymbol,
    typeMatcher: TypeMatcher,
    targetScalaPartialVersion: String) = {
    def asGetCase(
      field: IndexedField,
      classSymbol: ClassSymbol,
      typeMatcher: TypeMatcher,
      targetScalaPartialVersion: String) = {

      CASE (LIT(field.idx)) ==> {
        BLOCK(JavaConverter.convertToJava(
          field.avroField.schema,
          REF("getSchema").DOT("getFields").APPLY().DOT("get").APPLY(REF("field$")).DOT("schema").APPLY(),
          false,
          REF(FieldRenamer.rename(field.avroField.name)),
          classSymbol,
          typeMatcher,
          targetScalaPartialVersion)).AS(AnyRefClass)
      }
    }

    val errorCase = CASE(WILDCARD) ==> THROW(NEW("org.apache.avro.AvroRuntimeException", LIT("Bad index")))
    val casesGet = indexedFields.map(field => asGetCase(field, classSymbol, typeMatcher, targetScalaPartialVersion)):+errorCase

    DEF("get", AnyRefClass) withParams(PARAM("field$", IntClass)) := BLOCK(
      REF("field$") withAnnots(ANNOT("switch")) MATCH(casesGet)
    )  
  }
}
