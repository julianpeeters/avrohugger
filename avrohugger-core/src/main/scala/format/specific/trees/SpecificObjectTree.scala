package avrohugger
package format
package specific
package trees

import avrohugger.matchers.TypeMatcher
import avrohugger.matchers.custom.CustomTypeMatcher
import avrohugger.stores.SchemaStore
import avrohugger.types._
import org.apache.avro.{LogicalTypes, Protocol, Schema}
import treehugger.forest._
import definitions._
import treehuggerDSL._

import java.nio.charset.StandardCharsets
import scala.jdk.CollectionConverters._

// only companions, so no doc generation is required here
object SpecificObjectTree {

  val DecimalConversion = RootClass.newClass("org.apache.avro.Conversions.DecimalConversion")
  val decimalConversionDef = VAL(REF("decimalConversion")) := NEW(DecimalConversion)

  def schemaContainsDecimal(
    schema: Schema,
    schemaStore: SchemaStore,
    typeMatcher: TypeMatcher
  ): Boolean = {
    def getNestedSchemas(s: Schema): List[Schema] = s.getType match {
      case Schema.Type.ARRAY => getNestedSchemas(s.getElementType)
      case Schema.Type.MAP => getNestedSchemas(s.getValueType)
      case Schema.Type.UNION => s.getTypes().asScala.toList.flatMap(getNestedSchemas)
      case _ => List(s)
    }
    val topLevelSchemas = SpecificImporter.getTopLevelSchemas(Left(schema), schemaStore, typeMatcher)
    val recordSchemas = SpecificImporter.getRecordSchemas(topLevelSchemas).filter(s => s.getType == Schema.Type.RECORD)
    val fieldSchemas = recordSchemas.flatMap(_.getFields().asScala.map(_.schema()))
    fieldSchemas.flatMap(getNestedSchemas).exists(s => Option(s.getLogicalType()) match {
      case Some(logicalType) => logicalType.getName == "decimal"
      case None => false
    })
  }

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
      NEW(ParserClass) APPLY (Nil) DOT "parse" APPLY (schema.toString match {
        case schemaString if schemaString.getBytes(StandardCharsets.UTF_8).length > 65535 => LIST(schemaString.split("},\\{\"").map(LIT)) DOT "mkString" APPLY (LIT("},{\""))
        case schemaString => LIT(schemaString)
      })
    }
    val externalReader = VAL("READER$") := NEW("org.apache.avro.specific.SpecificDatumReader").APPLYTYPE(TYPE_REF(schema.getName)).APPLY(
      REF(s"${schema.getFullName()}.SCHEMA$$")
    )
    val externalWriter = VAL("WRITER$") := NEW("org.apache.avro.specific.SpecificDatumWriter").APPLYTYPE(TYPE_REF(schema.getName)).APPLY(
      REF(s"${schema.getFullName()}.SCHEMA$$")
    )
    val defCtorDefault = DEF("apply", TYPE_REF(schema.getName())).withParams(PARAM("data", TYPE_ARRAY(ByteClass))) := BLOCK(
      VAL("fixed") := NEW(schema.getFullName()).APPLY(),
      REF("fixed").DOT("bytes").APPLY(REF("data")),
      REF("fixed")
    )
    val defCtor = {
      val Decimal = RootClass.newClass("org.apache.avro.LogicalTypes.Decimal")
      val scale = REF("data").DOT("setScale").APPLY(REF("scale"))
      def scaleAndRound(roundingMode: BigDecimal.RoundingMode.Value) =
        REF("data").DOT("setScale").APPLY(REF("scale"), REF("BigDecimal.RoundingMode."+ roundingMode.toString))
      DEF("apply", TYPE_REF(schema.getName())).withParams(PARAM("data", CustomTypeMatcher.checkCustomDecimalType(typeMatcher.avroScalaTypes.decimal, schema))) := Block(
        VAL("schema") := REF("SCHEMA$"),
        VAL("decimalType") := REF("schema").DOT("getLogicalType").APPLY().AS(Decimal),
        VAL("scale") := REF("decimalType").DOT("getScale").APPLY(),
        VAL("scaledValue") := {
          typeMatcher.avroScalaTypes.decimal match {
            case ScalaBigDecimal(None) => scale
            case ScalaBigDecimal(Some(roundingMode)) => scaleAndRound(roundingMode)
            case ScalaBigDecimalWithPrecision(None) => scale
            case ScalaBigDecimalWithPrecision(Some(roundingMode)) => scaleAndRound(roundingMode)
          }
        },
        VAL("bigDecimal") := REF("scaledValue").DOT("bigDecimal"),
        VAL("fixed") := NEW(schema.getFullName()).APPLY(),
        REF("fixed").DOT("bytes").APPLY(
          REF("decimalConversion").DOT("toBytes").APPLY(REF("bigDecimal"),REF("schema"),REF("decimalType")).DOT("array").APPLY()
        ),
        REF("fixed")
      )
 
    }
    // companion object definition
    schema.getType() match {
      case Schema.Type.FIXED => Option(schema.getLogicalType()) match {
        case Some(logicalType) => logicalType match {
          case l: LogicalTypes.Decimal => objectDef := BLOCK(schemaDef, externalReader, externalWriter, decimalConversionDef, defCtor)
          case _ => objectDef :=  BLOCK(schemaDef, externalReader, externalWriter, defCtorDefault)
        }
        case None => objectDef := BLOCK(schemaDef, externalReader, externalWriter, defCtorDefault)
      }
      case Schema.Type.RECORD =>
        if (schemaContainsDecimal(schema, schemaStore, typeMatcher)) objectDef := BLOCK(schemaDef, decimalConversionDef)
        else objectDef := BLOCK(schemaDef)
      case _ => sys.error("Only FIXED and RECORD types can generate companion objects")
    }
  }

  // union acts as a blackbox, fields are not seen on root level, unpack is required
  private def collectUnionFields(sc: Schema): Iterable[Schema] = {
    sc.getTypes().asScala.toList
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
