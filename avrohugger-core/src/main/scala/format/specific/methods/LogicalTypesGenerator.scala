package avrohugger
package format
package specific
package methods

import org.apache.avro.{LogicalType, Schema}
import treehugger.forest._
import definitions._
import org.apache.avro.LogicalTypes.{Date, Decimal, TimestampMillis}
import treehuggerDSL._

import scala.collection.JavaConverters._


object LogicalTypesGenerator {

  def toCaseClassDef(schema: Schema): List[Tree] = {
    if (hasLogicalTypes(schema)) {
      List(
        conversionListDef(schema),
        conversionGetDef(),
        encoderDef(schema),
        toByteBufferDef(),
        writerDef(schema.getName),
        writeExternalDef(),
        readerDef(schema.getName),
        readExternalDef()
      )
    } else Nil
  }

  def toObjectDef(schema: Schema): List[Tree] = {
    if (hasLogicalTypes(schema)) {
      List(
        modelDef(),
        decoderDef(schema),
        getDecoderDef(schema.getName),
        fromByteBufferDef(schema.getName)
      )
    } else Nil
  }

  private def hasLogicalTypes(schema: Schema): Boolean = {
    schema.getFields.iterator().asScala.foldLeft(false)((result, field) => result || field.schema().getLogicalType != null)
  }

  private def conversionListDef(schema: Schema): Tree = {
    val list = converterList(schema)
    VAL("conversions", RootClass.newAliasType("List[org.apache.avro.Conversion[_]]")) withFlags (Flags.PRIVATE) := LIST(list: _*)
  }

  private def converterList(schema: Schema): List[Tree] = {
    schema.getFields.iterator().asScala.map(field => fieldLogicalType(field) match {
      case x: Date => REF("avrohugger.format.specific.conversions.DateConversion")
      case x: TimestampMillis => REF("avrohugger.format.specific.conversions.DateTimeConversion")
      case x: Decimal => REF("avrohugger.format.specific.conversions.DecimalConversion")
      case _ => NULL
    })
  }.toList

  private def fieldLogicalType(field: Schema.Field): LogicalType = field.schema().getType match {
    case Schema.Type.UNION =>
      field.schema().getTypes.asScala.foldLeft(null.asInstanceOf[LogicalType]) { (result, schema) =>
        if (schema.getLogicalType != null) schema.getLogicalType else result
      }
    case _ =>
      field.schema().getLogicalType
  }

  private def conversionGetDef(): Tree =
    DEF("getConversion", "org.apache.avro.Conversion[_]") withFlags (Flags.OVERRIDE) withParams (PARAM("field$", IntClass)) := BLOCK(
      REF("conversions") APPLY (REF("field$"))
    )

  private def modelDef(): Tree = {
    VAL("MODEL$") withFlags (Flags.PRIVATE) := NEW("org.apache.avro.specific.SpecificData")
  }

  private def encoderDef(schema: Schema): Tree = {
    val className = s"org.apache.avro.message.BinaryMessageEncoder[${schema.getName}]"
    VAL("encoder") withFlags (Flags.PRIVATE) := NEW(className) APPLY(REF(schema.getName) DOT "MODEL$", REF("getSchema"))
  }

  private def toByteBufferDef(): Tree = {
    DEF("toByteBuffer", "java.nio.ByteBuffer") := BLOCK(
      REF("encoder") DOT ("encode") APPLY (THIS)
    )
  }

  private def decoderDef(schema: Schema): Tree = {
    val className = s"org.apache.avro.message.BinaryMessageDecoder[${schema.getName}]"
    VAL("decoder") withFlags (Flags.PRIVATE) := NEW(className) APPLY(REF("MODEL$"), REF("SCHEMA$"))
  }

  private def getDecoderDef(schemaName: String): Tree = {
    DEF("getDecoder", s"org.apache.avro.message.BinaryMessageDecoder[$schemaName]") := BLOCK(
      REF("decoder")
    )
  }

  private def fromByteBufferDef(schemaName: String): Tree = {
    DEF("fromByteBuffer", RootClass.newClass(s"$schemaName")) withParams (PARAM("b", "java.nio.ByteBuffer")) := BLOCK(
      REF("decoder") DOT ("decode") APPLY (REF("b"))
    )
  }

  private def writerDef(schemaName: String): Tree = {
    val className = s"org.apache.avro.io.DatumWriter[$schemaName]"
    VAL("WRITER$") withFlags (Flags.PRIVATE) := REF(schemaName) DOT "MODEL$" DOT ("createDatumWriter") APPLY (REF("getSchema")) AS (className)
  }

  private def writeExternalDef(): Tree = {
    PROC("writeExternal") withFlags (Flags.OVERRIDE) withParams (PARAM("out", RootClass.newAliasType("java.io.ObjectOutput"))) := BLOCK(
      REF("WRITER$") DOT ("write") APPLY(THIS, REF("org.apache.avro.specific.SpecificData") DOT ("getEncoder") APPLY (REF("out")))
    )
  }

  private def readerDef(schemaName: String): Tree = {
    val className = s"org.apache.avro.io.DatumReader[$schemaName]"
    VAL("READER$") withFlags (Flags.PRIVATE) := REF(schemaName) DOT "MODEL$" DOT ("createDatumReader") APPLY (REF("getSchema")) AS (className)
  }

  private def readExternalDef(): Tree = {
    PROC("readExternal") withFlags (Flags.OVERRIDE) withParams (PARAM("in", RootClass.newAliasType("java.io.ObjectInput"))) := BLOCK(
      REF("READER$") DOT ("read") APPLY(THIS, REF("org.apache.avro.specific.SpecificData") DOT ("getDecoder") APPLY (REF("in")))
    )
  }
}
