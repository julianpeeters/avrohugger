package avrohugger
package format
package specific
package methods

import converters.JavaConverter.convertToJava

import treehugger.forest._
import definitions._
import treehuggerDSL._

object GetGenerator {
  def toDef(indexedFields: List[IndexedField]) = {
    def asGetCase(field: IndexedField) = {
            
      CASE (LIT(field.idx)) ==> {
        BLOCK(convertToJava(field.avroField.schema, REF(field.avroField.name))).AS(AnyRefClass)
      }
    }

    val errorCase = CASE(WILDCARD) ==> NEW("org.apache.avro.AvroRuntimeException", LIT("Bad index"))
    val casesGet = indexedFields.map(field => asGetCase(field)):+errorCase

    DEF("get", AnyRefClass) withParams(PARAM("field$", IntClass)) := BLOCK(
      REF("field$") withAnnots(ANNOT("switch")) MATCH(casesGet:_*)
    )  
  }
}
