package avrohugger
package format

import treehugger.forest._
import treehuggerDSL._
import definitions._
import org.apache.avro.generic.GenericData.StringType

object AvroString {

  private var stringType: StringType = _

  def setType(newType: StringType): Unit = {
    stringType = newType
  }

  def getType(): Type = stringType match {
    case StringType.Utf8 => RootClass.newClass(nme.createNameType("org.apache.avro.util.Utf8"))
    case _ => TYPE_REF("String")
  }

  def useUtf8() = StringType.Utf8 == stringType

}
