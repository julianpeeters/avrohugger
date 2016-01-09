package avrohugger
package format
package specific
package matchers

import org.apache.avro.Schema
import org.apache.avro.Schema.Type

import treehugger.forest._
import definitions._
import treehuggerDSL._

import scala.collection.JavaConversions._


object DefaultParamMatcher {

  def asDefaultParam(classStore: ClassStore, avroSchema: Schema): Tree  = {

    avroSchema.getType match {

      case Type.BOOLEAN => FALSE
      case Type.INT     => LIT(0)
      case Type.LONG    => LIT(0L)
      case Type.FLOAT   => LIT(0F)
      case Type.DOUBLE  => LIT(0D)
      case Type.STRING  => LIT("")
      case Type.NULL    => NULL
      case Type.ARRAY   => NIL
      case Type.MAP     => MAKE_MAP(LIT("") ANY_-> asDefaultParam(classStore, avroSchema.getValueType))
      case Type.FIXED   => sys.error("the FIXED datatype is not yet supported")
      case Type.ENUM    => NULL // TODO Take first enum value?
      case Type.BYTES   => NULL
      case Type.RECORD  => NEW(classStore.generatedClasses(avroSchema))
      case Type.UNION   => NONE
    }
  }

}
