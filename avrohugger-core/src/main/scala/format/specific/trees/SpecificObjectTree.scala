package avrohugger
package format
package specific
package trees

import generators.ScalaDocGenerator
import matchers.TypeMatcher
import stores.SchemaStore
import org.apache.avro.{Protocol, Schema}
import treehugger.forest._
import definitions._
import org.apache.avro.Schema.Type
import treehuggerDSL._

import scala.collection.JavaConverters._

// only companions, so no doc generation is required here
object SpecificObjectTree {

  // Companion to case classes
  def toCaseCompanionDef(
    schema: Schema,
    maybeFlags: Option[List[Long]],
    schemaStore: SchemaStore,
    typeMatcher: TypeMatcher) = {
    val ParserClass = RootClass.newClass("org.apache.avro.Schema.Parser")
    val objectDef = maybeFlags match {
      case Some(flags) => OBJECTDEF(schema.getName).withFlags(flags:_*)
      case None => OBJECTDEF(schema.getName)
    }
    val schemaDef = VAL(REF("SCHEMA$")) := {
      (NEW(ParserClass)) APPLY(Nil) DOT "parse" APPLY(LIT(schema.toString))
    }
    val DecimalConversion = RootClass.newClass("org.apache.avro.Conversions.DecimalConversion")
    val decimalConversionDef = VAL(REF("decimalConversion")) := NEW(DecimalConversion)
    def schemaContainsDecimal(schema: Schema): Boolean = {
      def getNestedSchemas(s: Schema): List[Schema] = s.getType match {
        case Schema.Type.ARRAY => getNestedSchemas(s.getElementType)
        case Schema.Type.MAP => getNestedSchemas(s.getValueType)
        case Schema.Type.UNION => s.getTypes.asScala.toList.flatMap(getNestedSchemas)
        case _ => List(s)
      }
      val topLevelSchemas = SpecificImporter.getTopLevelSchemas(Left(schema), schemaStore, typeMatcher)
      val recordSchemas = SpecificImporter.getRecordSchemas(topLevelSchemas).filter(s => s.getType == Schema.Type.RECORD)
      val fieldSchemas = recordSchemas.flatMap(_.getFields.asScala.map(_.schema()))
      fieldSchemas.flatMap(getNestedSchemas).exists(s => Option(s.getLogicalType()) match {
        case Some(logicalType) => logicalType.getName == "decimal"
        case None => false
      })
    }
    // companion object definition
    if (schemaContainsDecimal(schema)) objectDef := BLOCK(schemaDef, decimalConversionDef)
    else objectDef := BLOCK(schemaDef)
  }

  // union acts as a blackbox, fields are not seen on root level, unpack is required
  private def collectUnionFields(sc: Schema): Iterable[Schema] = {
    sc.getTypes.asScala.toList
  }
  
  // Companion to traits that have messages
  def toTraitCompanionDef(protocol: Protocol) = {
    val ProtocolClass = RootClass.newClass("org.apache.avro.Protocol")
    // companion object definition
    OBJECTDEF(protocol.getName) := BLOCK(
      VAL("PROTOCOL", ProtocolClass).withFlags(Flags.FINAL) := {
        REF(ProtocolClass) DOT "parse" APPLY(LIT(protocol.toString))
      }
    )
  }
  
}
