package avrohugger
package specific

import org.apache.avro.Schema.Field
import org.apache.avro.Schema.Type
import treehugger.forest._
import definitions._
import treehuggerDSL._

object DefaultParamMatcher {

  def asDefaultParam(avroField: Field) = {

    avroField.schema.getType match {

/*
 //case Schema.Type.ARRAY    => listType(toType(classStore, namespace, schema.getElementType))
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
*/

      case Type.BOOLEAN => TRUE
      case Type.INT     => LIT(1)
      case Type.LONG    => LIT(1L)
      case Type.FLOAT   => LIT(1F)
      case Type.DOUBLE  => LIT(1D)
      case Type.STRING  => LIT("")
      case Type.NULL    => NULL
      case Type.ARRAY   => sys.error("the ARRAY datatype is not yet supported")
      case Type.FIXED   => sys.error("the FIXED datatype is not yet supported")
      case Type.ENUM    => sys.error("the ENUM datatype is not yet supported")
      case Type.MAP     => sys.error("the MAP datatype is not yet supported")
      case Type.BYTES   => sys.error("the BYTES datatype is not yet supported")
      case Type.UNION   => sys.error("the UNION datatype is not yet supported")
      case Type.RECORD  => sys.error("the RECORD datatype is not yet supported")
     // case x         => q"""${newTermName(x)}(..${ApplyParamMatcher.getApplyParams(x, c)})"""
    }
  }
/*
  def asParameterizedDefaultParam(fieldTypeName: String) : Tree = {

    fieldTypeName match {
      //List
      case  l: String if l.startsWith("List[") => {
        if (getBoxed(l).endsWith("]")) q"""scala.List(${asParameterizedDefaultParam(getBoxed(l), c)})"""
        else q"""List(${asDefaultParam(getBoxed(l), c)})"""
      }
      //Option
      case  o: String if o.startsWith("Option[") => { 
        if(getBoxed(o).endsWith("]")) q"""Some(${asParameterizedDefaultParam(getBoxed(o), c)})"""
        else q"""Some(${asDefaultParam(getBoxed(o), c)})"""
      }
      //User-Defined
      case  x: String if x.startsWith(x + "[") => { 
        if(getBoxed(x).endsWith("]")) q"""${newTermName(x)}(${asParameterizedDefaultParam(getBoxed(x), c)})"""
        else q"""${newTermName(x)}(${asDefaultParam(getBoxed(x), c)})"""
      }
    }
  }

  def getBoxed(typeName: String) = {
    typeName.dropWhile( c => (c != '[') ).drop(1).dropRight(1)
  }
*/
}
