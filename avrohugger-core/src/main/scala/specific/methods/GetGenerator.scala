package avrohugger
package specific
package methods

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.Schema

import scala.collection.JavaConversions._

object GetGenerator {
  def toDef(indexedFields: List[IndexedField]) = {
    def asGetCase(field: IndexedField) = {
		  // Recursive definition takes a field's schema, and a tree that represents the source code to be written.
		  // The initial tree that is passed in is a REF("fieldName"), which is wrapped in a pattern match tree (e.g.,
		  // to sort None and Some(x) if the field is a union). A Schema is passed in order to get access to the field's type
		  // parameters while the tree is built up.
		  def convertToJava(schema: Schema, tree: Tree): Tree  = {
		    schema.getType match {
		      case Schema.Type.UNION => {
		        // check if it's the kind of union that we support (i.e. nullable fields)
		        if (schema.getTypes.length != 2 || 
		           !schema.getTypes.map(x => x.getType).contains(Schema.Type.NULL) || 
		            schema.getTypes.filterNot(x => x.getType == Schema.Type.NULL).length != 1) {
		          sys.error("Unions beyond nullable fields are not supported")
		        }
		        else {
		          val maybeType = schema.getTypes.find(x => x.getType != Schema.Type.NULL)
		          if (maybeType.isDefined) {
		          val conversionCases = List(
		            CASE(SOME(ID("x"))) ==> convertToJava(maybeType.get, REF("x")),
		            CASE(NONE)          ==> NULL
		          )
		          tree MATCH(conversionCases:_*)
		          }
		          else sys.error("There was no type in this union")
		        }
		      }
		      case Schema.Type.ARRAY => {
		      	val applyParam = {
	      	    schema.getElementType.getType match {
                // contents that that need converting
                case Schema.Type.UNION | Schema.Type.ARRAY => {
                  BLOCK(tree MAP(LAMBDA(PARAM("x")) ==> BLOCK(
				      			convertToJava(schema.getElementType, REF("x"))
				      		)))
                }
                // types that aren't yet supported
                case Schema.Type.FIXED | 
                  Schema.Type.BYTES | 
                  Schema.Type.MAP => {
                  sys.error("unsupported type")
                }
								// contents that don't need converting
                case _ => BLOCK(tree)
              }
            }
		      	REF("java.util.Arrays.asList") APPLY(applyParam withType(TYPE_REF(SEQ_WILDCARD)))
		      }
	        case Schema.Type.FIXED    => sys.error("the FIXED datatype is not yet supported")
          case Schema.Type.MAP      => sys.error("the MAP datatype is not yet supported")
          case Schema.Type.BYTES    => sys.error("the BYTES datatype is not yet supported")
		      case _ => tree
		    }
		  }
		        
		  CASE (ID("pos"), IF(REF("pos") INT_== LIT(field.idx))) ==> {
	      BLOCK(convertToJava(field.avroField.schema, REF(field.avroField.name))).AS(AnyRefClass)
		  }
		}

		val errorCase = CASE(WILDCARD) ==> NEW("org.apache.avro.AvroRuntimeException", LIT("Bad index"))
    val casesGet = indexedFields.map(field => asGetCase(field)):+errorCase

    DEF("get", AnyRefClass) withParams(PARAM("field", IntClass)) := BLOCK(
      REF("field") MATCH(casesGet:_*)
    )  
  }
}
