package avrohugger
package format
package specific
package methods

import converters.JavaConverter.convertToJava

import treehugger.forest._
import definitions._
import treehuggerDSL._

object GetGenerator {
  def toDef(indexedFields: List[IndexedField], classSymbol: ClassSymbol) = {
    def asGetCase(field: IndexedField, classSymbol: ClassSymbol) = {
            
      CASE (LIT(field.idx)) ==> {
        BLOCK(convertToJava(field.avroField.schema, REF(field.avroField.name), classSymbol)).AS(AnyRefClass)
      }
    }

    val errorCase = CASE(WILDCARD) ==> NEW("org.apache.avro.AvroRuntimeException", LIT("Bad index"))
    val casesGet = indexedFields.map(field => asGetCase(field, classSymbol)):+errorCase

    DEF("get", AnyRefClass) withParams(PARAM("field$", IntClass)) := BLOCK(
      REF("field$") withAnnots(ANNOT("switch")) MATCH(casesGet:_*)
    )  
  }
}
