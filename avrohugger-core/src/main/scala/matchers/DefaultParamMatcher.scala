package avrohugger
package matchers

import avrohugger.matchers.custom.CustomDefaultParamMatcher
import avrohugger.stores.ClassStore
import avrohugger.types._

import org.apache.avro.Schema
import org.apache.avro.Schema.Type

import treehugger.forest._
import definitions._
import treehuggerDSL._


object DefaultParamMatcher {

  // for SpecificRecord
  def asDefaultParam(
    classStore: ClassStore,
    avroSchema: Schema,
    typeMatcher: TypeMatcher): Tree  = {

    avroSchema.getType match {

      case Type.BOOLEAN => FALSE
      case Type.INT     =>
        LogicalType.foldLogicalTypes[Tree](
          schema = avroSchema,
          default = LIT(0)) {
          case Date =>
            CustomDefaultParamMatcher.checkCustomDateType(
              typeMatcher.avroScalaTypes.date)
        }
      case Type.LONG    =>
        LogicalType.foldLogicalTypes[Tree](
          schema = avroSchema,
          default = LIT(0L)) {
          case TimestampMillis =>
            CustomDefaultParamMatcher.checkCustomTimestampMillisType(
              typeMatcher.avroScalaTypes.timestampMillis)
        }
      case Type.FLOAT   => LIT(0F)
      case Type.DOUBLE  => LIT(0D)
      case Type.STRING  => LIT("")
      case Type.NULL    => NULL
      case Type.FIXED   => sys.error("the FIXED datatype is not yet supported")
      case Type.ENUM    =>
        CustomDefaultParamMatcher.checkCustomEnumType(typeMatcher.avroScalaTypes.enum)
      case Type.BYTES   =>
        LogicalType.foldLogicalTypes[Tree](
          schema = avroSchema,
          default = NULL) {
          case Decimal => REF("scala.math.BigDecimal") APPLY LIT(0)
        }
      case Type.RECORD  => NEW(classStore.generatedClasses(avroSchema))
      case Type.UNION   => NONE
      case Type.ARRAY   =>
        CustomDefaultParamMatcher.checkCustomArrayType(typeMatcher.avroScalaTypes.array) DOT "empty"
      case Type.MAP     =>
        MAKE_MAP(LIT("") ANY_-> asDefaultParam(classStore, avroSchema.getValueType, typeMatcher))
      
    }
    
    
  }

}
