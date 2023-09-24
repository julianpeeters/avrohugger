package avrohugger
package matchers

import avrohugger.matchers.custom.CustomDefaultParamMatcher
import avrohugger.stores.ClassStore
import avrohugger.types._
import org.apache.avro.Schema
import treehugger.forest._
import definitions._
import treehugger.forest
import treehuggerDSL._

import scala.jdk.CollectionConverters._

object DefaultParamMatcher {

  // for SpecificRecord
  def asDefaultParam(
    classStore: ClassStore,
    avroSchema: Schema,
    typeMatcher: TypeMatcher): Tree  = {

    avroSchema.getType match {

      case Schema.Type.BOOLEAN => FALSE
      case Schema.Type.INT     =>
        LogicalType.foldLogicalTypes[Tree](
          schema = avroSchema,
          default = LIT(0)) {
          case Date =>
            CustomDefaultParamMatcher.checkCustomDateType(
              typeMatcher.avroScalaTypes.date)
          case TimeMillis =>
            CustomDefaultParamMatcher.checkCustomTimeMillisType(
              typeMatcher.avroScalaTypes.timeMillis
            )
        }
      case Schema.Type.LONG    =>
        LogicalType.foldLogicalTypes[Tree](
          schema = avroSchema,
          default = LIT(0L)) {
          case TimestampMillis =>
            CustomDefaultParamMatcher.checkCustomTimestampMillisType(
              typeMatcher.avroScalaTypes.timestampMillis)
        }
      case Schema.Type.FLOAT   => LIT(0F)
      case Schema.Type.DOUBLE  => LIT(0D)
      case Schema.Type.STRING  =>
        LogicalType.foldLogicalTypes[Tree](
          schema = avroSchema,
          default = LIT("")) {
          case UUID => REF("java.util.UUID.randomUUID")
        }
      case Schema.Type.NULL    => NULL
      case Schema.Type.FIXED   =>
        REF(classStore.generatedClasses(avroSchema)).APPLY(CustomDefaultParamMatcher.checkCustomDecimalType(
          decimalType = typeMatcher.avroScalaTypes.decimal,
          schema = avroSchema,
          default = ArrayClass.APPLY()))
      case Schema.Type.ENUM    =>
        CustomDefaultParamMatcher.checkCustomEnumType(typeMatcher.avroScalaTypes.`enum`)
      case Schema.Type.BYTES   =>
        CustomDefaultParamMatcher.checkCustomDecimalType(
          decimalType = typeMatcher.avroScalaTypes.decimal,
          schema = avroSchema,
          default = ArrayClass.APPLY())
      case Schema.Type.RECORD  => NEW(classStore.generatedClasses(avroSchema))
      case Schema.Type.UNION =>
        val schemas = avroSchema.getTypes.asScala.toList
        if (avroSchema.isNullable) NONE
        else if (schemas.size == 2 && typeMatcher.avroScalaTypes.union.useEitherForTwoNonNullTypes)
          LEFT(asDefaultParam(classStore, schemas.head, typeMatcher))
        else COPRODUCT(asDefaultParam(classStore, schemas.head, typeMatcher), schemas.map(_.getName))
      case Schema.Type.ARRAY   =>
        CustomDefaultParamMatcher.checkCustomArrayType(typeMatcher.avroScalaTypes.array) DOT "empty"
      case Schema.Type.MAP     =>
        MAKE_MAP(LIT("") ANY_-> asDefaultParam(classStore, avroSchema.getValueType, typeMatcher))
    }
  }

  private def COPRODUCT(defaultParam: Tree, tp: List[String]): Tree =  {
      val copTypes: List[String] = tp :+ "CNil"
      val chain: forest.Tree = INFIX_CHAIN(":+:", copTypes.map(t => Ident(t.safeToString)))
      val chainedS = treeToString(chain)
      val copType = typeRef(RootClass.newClass(newTypeName(chainedS)))
      REF("shapeless.Coproduct") APPLYTYPE copType APPLY defaultParam
    }

}
