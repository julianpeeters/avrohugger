package avrohugger
package format
package scavro

import org.apache.avro.Schema
import org.apache.avro.Schema.Field
import org.apache.avro.compiler.specific.SpecificCompiler
import org.apache.avro.specific.SpecificData

import scala.jdk.CollectionConverters._

object ScavroMethodRenamer {

  /** Adapted from: https://github.com/apache/avro/blob/master/lang/java/compiler/src/main/java/org/apache/avro/compiler/specific/SpecificCompiler.java#L867
    * Generates a method name from a field name.
    * @param schema the schema in which the field is defined.
    * @param field the field for which to generate the accessor name.
    * @param prefix method name prefix, e.g. "get" or "set".
    * @param postfix method name postfix, e.g. "" or "Builder".
    * @return the generated method name.
    */
  def generateMethodName(
    schema: Schema,
    field: Field,
    prefix: String,
    postfix: String): String = {
      
    /* Reserved words for accessor/mutator methods */
    var ACCESSOR_MUTATOR_RESERVED_WORDS: java.util.Set[String] =
      ((Set("class", "schema", "classSchema") ++ Scavro.RESERVED_WORDS)).asJava
        
    /* Reserved words for error types */
    val ERROR_RESERVED_WORDS: java.util.Set[String] =
      (Set("message", "cause") ++ ACCESSOR_MUTATOR_RESERVED_WORDS.asScala).asJava

    // Check for the special case in which the schema defines two fields whose
    // names are identical except for the case of the first character:
    val firstChar: Char = field.name().charAt(0)
    val conflictingFieldName: String = {
      val s1 = if (Character.isLowerCase(firstChar)) Character.toUpperCase(firstChar) else Character.toLowerCase(firstChar)
      val s2 = if (field.name().length() > 1) field.name().substring(1) else ""
      s"$s1$s2"
    }
    val fieldNameConflict: Boolean = Option(schema.getField(conflictingFieldName)).isDefined

    val methodBuilder: StringBuilder = new StringBuilder(prefix)
    val fieldName: String =
      SpecificCompiler.mangle(
        field.name(),
        if(schema.isError()) ERROR_RESERVED_WORDS else ACCESSOR_MUTATOR_RESERVED_WORDS,
        true)

    var nextCharToUpper: Boolean = true
    (0 until fieldName.length).map(ii => {
      if (fieldName.charAt(ii) == '_') {
        nextCharToUpper = true
      }
      else if (nextCharToUpper) {
        methodBuilder.append(Character.toUpperCase(fieldName.charAt(ii)))
        nextCharToUpper = false
      }
      else {
        methodBuilder.append(fieldName.charAt(ii))
      }
    })
    methodBuilder.append(postfix)

    // If there is a field name conflict append $0 or $1
    if (fieldNameConflict) {
      if (methodBuilder.charAt(methodBuilder.length - 1) != '$') {
        methodBuilder.append('$')
      }
      methodBuilder.append(if(Character.isLowerCase(firstChar))'0' else '1')
    }

    methodBuilder.toString()
  }
  

  
}