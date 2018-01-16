package avrohugger
package matchers

import stores.ClassStore
import types._

import org.apache.avro.Schema
import org.apache.avro.Schema.Type

import treehugger.forest._
import definitions._
import treehuggerDSL._


object DefaultParamMatcher {
  
  
  def checkCustomArrayType(arrayType: AvroScalaArrayType) = {
    arrayType match {
      case ScalaArray  => ArrayClass
      case ScalaList   => ListClass
      case ScalaVector => VectorClass
    }
  }
  
  def checkCustomEnumType(enumType: AvroScalaEnumType) = {
    enumType match {
      case JavaEnum            => NULL // TODO Take first enum value?
      case ScalaEnumeration    => NULL // TODO Take first enum value?
      case ScalaCaseObjectEnum => NULL // TODO Take first enum value?
      case EnumAsScalaString   => LIT("")
    }
  }
  

  // for SpecificRecord
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
      case Type.ENUM    =>
        checkCustomEnumType(typeMatcher.avroScalaTypes.enum)
      case Type.BYTES   => NULL
      case Type.RECORD  => NEW(classStore.generatedClasses(avroSchema))
      case Type.UNION   => NONE
      case Type.ARRAY   =>
        checkCustomArrayType(typeMatcher.avroScalaTypes.array) DOT "empty"
      case Type.MAP     =>
        MAKE_MAP(LIT("") ANY_-> asDefaultParam(classStore, avroSchema.getValueType, typeMatcher))
      
    }
    
    
  }

}
