package avrohugger
package matchers

import stores.ClassStore

import org.apache.avro.Schema
import org.apache.avro.Schema.Type

import treehugger.forest._
import definitions._
import treehuggerDSL._

import scala.collection.JavaConversions._


object DefaultParamMatcher {
  
  
  def checkCustomArrayType(
    maybeCustomArray: Option[Class[_]],
    defaultSym: Symbol) = {
    maybeCustomArray match {
      case Some(c) if c == classOf[Array[_]]  => ArrayClass
      case Some(c) if c == classOf[List[_]]   => ListClass
      case Some(c) if c == classOf[Vector[_]] => VectorClass
      case _                                  => defaultSym
    }
  }
  

  def asDefaultParam(
    classStore: ClassStore,
    avroSchema: Schema,
    typeMatcher: TypeMatcher): Tree  = {

    avroSchema.getType match {

      case Type.BOOLEAN => FALSE
      case Type.INT     => LIT(0)
      case Type.LONG    => LIT(0L)
      case Type.FLOAT   => LIT(0F)
      case Type.DOUBLE  => LIT(0D)
      case Type.STRING  => LIT("")
      case Type.NULL    => NULL
      case Type.FIXED   => sys.error("the FIXED datatype is not yet supported")
      case Type.ENUM    => NULL // TODO Take first enum value?
      case Type.BYTES   => NULL
      case Type.RECORD  => NEW(classStore.generatedClasses(avroSchema))
      case Type.UNION   => NONE
      case Type.ARRAY   => {
        checkCustomArrayType(typeMatcher.customTypeMap.get("array"), ListClass) DOT "empty"
      }
      case Type.MAP     => {
        MAKE_MAP(LIT("") ANY_-> asDefaultParam(classStore, avroSchema.getValueType, typeMatcher))
      }
      
    }
    
    
  }

}
