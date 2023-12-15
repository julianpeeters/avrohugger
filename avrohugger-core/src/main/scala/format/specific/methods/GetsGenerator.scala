package avrohugger
package format
package specific
package methods

import converters.JavaConverter
import avrohugger.matchers.TypeMatcher
import treehugger.forest._
import definitions._
import treehugger.forest
import treehuggerDSL._

/**
 * This generator is an alternative to GetGenerator. with these differences
 * It is used for schemas with more than 254 fields which cannot have a constructor and have class members instead.
 * A further issue with large numbers of fields is that the case class produced by GetGenerator.toDef
 * can exceed the JVM limit of 65536 bytes resulting in a "Method too large" compilation error.
 * This class generates a smaller get method by delegating to private methods
 * Thus generator differs from GetGenerator in the following respects
 * 1. It generates a list of function definitions
 * 2. It generates a private method for each field that converts to an AnyRef
 * 3. The match expression public def get(field$: Int): AnyRef delegates to the appropriate private method
 */
object GetsGenerator {
  def toDefs(
    indexedFields: List[IndexedField],
    classSymbol: ClassSymbol,
    typeMatcher: TypeMatcher,
    targetScalaPartialVersion: String) :List[forest.DefDef]= {

    def fieldGetMethodName(field: IndexedField) = s"get${field.avroField.name}AnyRef"
    def asGetMethod(
                   field: IndexedField,
                   classSymbol: ClassSymbol,
                   typeMatcher: TypeMatcher,
                   targetScalaPartialVersion: String)  = {

      DEFINFER(fieldGetMethodName(field)).withType(AnyRefClass).withFlags(Flags.PRIVATE):=  {
        BLOCK(JavaConverter.convertToJava(
          field.avroField.schema,
          REF("getSchema").DOT("getFields").APPLY().DOT("get").APPLY(LIT(field.idx)).DOT("schema").APPLY(),
          false,
          REF(FieldRenamer.rename(field.avroField.name)),
          classSymbol,
          typeMatcher,
          targetScalaPartialVersion)).AS(AnyRefClass)
      }
    }

    def asGetCase(field: IndexedField) = {
      CASE (LIT(field.idx)) ==> REF(fieldGetMethodName(field))
    }

    val errorCase = CASE(WILDCARD) ==> NEW("org.apache.avro.AvroRuntimeException", LIT("Bad index"))
    val casesGet = indexedFields.map(field => asGetCase(field)):+errorCase
    val methodsDef: List[forest.DefDef] = indexedFields.map(field => asGetMethod(field, classSymbol, typeMatcher, targetScalaPartialVersion))

    val getDef = DEF("get", AnyRefClass) withParams(PARAM("field$", IntClass)) := BLOCK(
      REF("field$") withAnnots(ANNOT("switch")) MATCH(casesGet)
    )
    getDef:: methodsDef
  }
}
