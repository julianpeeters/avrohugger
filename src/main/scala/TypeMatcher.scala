package avrohugger

import treehugger.forest._
import treehuggerDSL._
import definitions._
import org.apache.avro.Schema 
import org.apache.avro.Schema.{Type => AvroType} 
import scala.collection.JavaConversions._

object TypeMatcher {
  def toType(classStore: ClassStore, namespace: Option[String], schema: org.apache.avro.Schema): Type = {

    // May contain nested schemas that will use the same namespace as the top-level schema. 
    // Thus, when a field is parsed, the namespace is passed in once while and nested classes 
    def matchType(schema: org.apache.avro.Schema): Type = {
      schema.getType match { 
        case Schema.Type.ARRAY    => listType(toType(classStore, namespace, schema.getElementType))
        case Schema.Type.BOOLEAN  => BooleanClass
        //case Schema.Type.BYTES    => //TODO
        case Schema.Type.DOUBLE   => DoubleClass
        //case Schema.Type.FIXED    => //TODO
        case Schema.Type.FLOAT    => FloatClass
        case Schema.Type.LONG     => LongClass
        case Schema.Type.INT      => IntClass
        //case Schema.Type.MAP      => //TODO
        case Schema.Type.NULL     => NullClass
        case Schema.Type.STRING   => StringClass
        case Schema.Type.RECORD   => classStore.generatedClasses(schema) 
        case Schema.Type.UNION    => { 
          val unionSchemas = schema.getTypes.toList
          // unions are represented as Scala Option[T], and thus unions must be of two types, one of them NULL
          if (unionSchemas.length == 2 && unionSchemas.exists(schema => schema.getType == Schema.Type.NULL)) {
            val maybeSchema = unionSchemas.find(schema => schema.getType != Schema.Type.NULL)
            if (maybeSchema.isDefined ) optionType(matchType(maybeSchema.get))
            else error("no avro type found in this union")  
          }
          else error("unions not yet supported beyond nullable fields")
        }
        case x => error( x +  "is not yet supported or is not a valid Avro type")
      }
    }
    matchType(schema)
  }
}