package avrohugger
package format
package specific
package methods

import avrohugger.format.specific.conversions.DateConversion
import avrohugger.format.specific.methods.ConverterGenerator.hasLogical
import org.apache.avro.{Conversion, LogicalType, Schema}
import treehugger.forest._
import definitions._
import org.apache.avro.LogicalTypes.{Date, Decimal, TimestampMillis}
import treehugger.forest
import treehuggerDSL._

import scala.collection.JavaConverters._


object ConverterGenerator {

  def toListDef(schema: Schema): Option[Tree] = {
    if (hasLogical(schema)) {
      val list = converterList(schema)
      Some(VAL("conversions", RootClass.newAliasType("List[org.apache.avro.Conversion[_]]")) withFlags(Flags.PRIVATE) := LIST(list: _*))
    } else None
  }

  def toDef(schema: Schema): Option[Tree] = {
    if (hasLogical(schema)) {
      Some(DEF("getConversion", "org.apache.avro.Conversion[_]") withFlags (Flags.OVERRIDE) withParams (PARAM("field$", IntClass)) := BLOCK(
        REF("conversions") APPLY (REF("field$"))
      ))
    } else None
  }

  private def hasLogical(schema: Schema): Boolean = {
    schema.getFields.iterator().asScala.foldLeft(false)((result, field) => result || field.schema().getLogicalType != null)
  }

  private def fieldLogicalType(field: Schema.Field): LogicalType = field.schema().getType match {
    case Schema.Type.UNION =>
      field.schema().getTypes.asScala.foldLeft(null.asInstanceOf[LogicalType]) { (result, schema) =>
        if (schema.getLogicalType != null) schema.getLogicalType else result
      }
    case _ =>
      field.schema().getLogicalType
  }

  private def converterList(schema: Schema): List[Tree] = {
    schema.getFields.iterator().asScala.map(field => fieldLogicalType(field) match {
      case x: Date => REF("avrohugger.format.specific.conversions.DateConversion")
      case x: TimestampMillis => REF("avrohugger.format.specific.conversions.DateTimeConversion")
      case x: Decimal => NEW("org.apache.avro.Conversions.DecimalConversion")
      case _ => NULL
    })
  }.toList
}

