package avrohugger
package format
package specific
package methods

import converters.ScalaConverter.convertFromJava

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.Schema

import scala.collection.JavaConversions._
import scala.language.postfixOps

object PutGenerator {

    def toDef(
      classStore: ClassStore, 
      namespace: Option[String], 
      indexedFields: List[IndexedField],
      typeMatcher: TypeMatcher) = {

      def asPutCase(field: IndexedField) = {
        CASE (ID("pos"), IF(REF("pos") INT_== LIT(field.idx))) ==> {
          THIS DOT field.avroField.name := 
            BLOCK(convertFromJava(classStore, namespace, field.avroField.schema, REF("value"), typeMatcher))
            .AS(typeMatcher.toScalaType(classStore, namespace, field.avroField.schema))
        }
      }
      
      val errorCase = CASE(WILDCARD) ==> NEW("org.apache.avro.AvroRuntimeException", LIT("Bad index"))
      val casesPut = indexedFields.map(field => asPutCase(field)):+ errorCase

      DEF("put", UnitClass) withParams(PARAM("field$", IntClass), PARAM("value", AnyClass)) := BLOCK(
        REF("field$") MATCH(casesPut:_*), UNIT
      ) 
    }

}
