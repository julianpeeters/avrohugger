package avrohugger
package matchers

import avrohugger.matchers.custom.CustomDefaultParamMatcher
import avrohugger.matchers.custom.CustomDefaultValueMatcher
import avrohugger.stores.ClassStore
import avrohugger.types._
import java.nio.charset.StandardCharsets

import treehugger.forest._
import definitions._
import org.apache.avro.Schema
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.{NullNode, ObjectNode, TextNode}
import treehugger.forest
import treehuggerDSL._
import scala.util.Try

import scala.jdk.CollectionConverters._

object DefaultValueMatcher {

  val nullNode = new TextNode("null")

  // This code was stolen from here:
  // https://github.com/julianpeeters/avro-scala-macro-annotations/blob/104fa325a00044ff6d31184fa7ff7b6852e9acd5/macros/src/main/scala/avro/scala/macro/annotations/provider/matchers/FromJsonMatcher.scala
  def getDefaultValue(
    classStore: ClassStore,
    namespace: Option[String],
    field: Schema.Field,
    typeMatcher: TypeMatcher): Tree = {

    def fromJsonNode(node: JsonNode, schema: Schema): Tree = {
      schema.getType match {
        case _ if node == null => EmptyTree //not `default=null`, but no default
        case Schema.Type.INT =>
          LogicalType.foldLogicalTypes[Tree](
            schema = schema,
            default = LIT(node.intValue())) {
            case Date =>
              CustomDefaultValueMatcher.checkCustomDateType(
                node.longValue(),
                typeMatcher.avroScalaTypes.date)
          }
        case Schema.Type.FLOAT => LIT(node.doubleValue().asInstanceOf[Float])
        case Schema.Type.LONG =>
          LogicalType.foldLogicalTypes[Tree](
            schema = schema,
            default = LIT(node.longValue())) {
            case TimestampMillis =>
            CustomDefaultValueMatcher.checkCustomTimestampMillisType(
              node.longValue(),
              typeMatcher.avroScalaTypes.timestampMillis)
          }
        case Schema.Type.DOUBLE => LIT(node.doubleValue())
        case Schema.Type.BOOLEAN => LIT(node.booleanValue())
        case Schema.Type.STRING =>
          LogicalType.foldLogicalTypes[Tree](
            schema = schema,
            default = LIT(node.textValue())) {
              case UUID => REF("java.util.UUID.fromString") APPLY LIT(node.textValue())
            }
        case Schema.Type.BYTES =>
          CustomDefaultParamMatcher.checkCustomDecimalType(
            decimalType = typeMatcher.avroScalaTypes.decimal,
            schema = schema,
            default = REF("Array[Byte]") APPLY node.textValue().getBytes.map((e: Byte) => LIT(e)),
            decimalValue =
              Try(new org.apache.avro.Conversions.DecimalConversion().fromBytes(
                java.nio.ByteBuffer.wrap(node.textValue().getBytes(StandardCharsets.UTF_8)),
                schema,
                schema.getLogicalType).toString
              ).toOption
          )
        case Schema.Type.ENUM => typeMatcher.avroScalaTypes.enum match {
          case JavaEnum => (REF(schema.getName) DOT node.textValue())
          case ScalaEnumeration => (REF(schema.getName) DOT node.textValue())
          case ScalaCaseObjectEnum => (REF(schema.getName) DOT node.textValue())
          case EnumAsScalaString => LIT(node.textValue())
        }
        case Schema.Type.NULL => LIT(null)
        case Schema.Type.UNION => {
          val unionSchemas = schema.getTypes.asScala.toList
          val result = unionDefaultArgsImpl(node, unionSchemas, fromJsonNode, typeMatcher, classStore, namespace)
          result
        }
        case Schema.Type.ARRAY => {
          val collectionType = CustomDefaultParamMatcher.checkCustomArrayType(typeMatcher.avroScalaTypes.array)
          collectionType APPLY(node.elements().asScala.toSeq.map(e => fromJsonNode(e, schema.getElementType)))
        }
        case Schema.Type.MAP => {
          val kvps = node.fields().asScala.toList.map(e => LIT(e.getKey) ANY_-> fromJsonNode(e.getValue, schema.getValueType))
          MAKE_MAP(kvps)
        }
        case Schema.Type.RECORD  => {
          val fields  = schema.getFields
          val jsObject = node match {
            case t: TextNode =>
              val mapper = new ObjectMapper();
              mapper.readValue(t.textValue(), classOf[ObjectNode])
            case o: ObjectNode => o
            case _ => throw new Exception(s"Invalid default value for field: $field, value: $node")
          }

          val fieldValues = fields.asScala.map { f =>
            fromJsonNode(jsObject.get(f.name), f.schema)
          }
          NEW(schema.getName, fieldValues: _*)
        }
        case x => throw new Exception(s"Can't extract a default field, type not yet supported: $x")
      }
    }
    val defaultValue = org.apache.avro.util.internal.Accessor.defaultValue(field)
    fromJsonNode(defaultValue, field.schema)
  }

  /**
    * Handles unions default values.
    *
    * Per the avro spec:
    * (Note that when a default value is specified for a record field whose type is a union,
    * the type of the default value must match the first element of the union)
    */
  private[this] def unionDefaultArgsImpl(node: JsonNode,
                                         unionSchemas: List[Schema],
                                         treeMatcher: (JsonNode, Schema) => Tree,
                                         typeMatcher: TypeMatcher,
                                         classStore: ClassStore,
                                         namespace: Option[String]) : Tree = {

    def COPRODUCT(defaultParam: Schema, tp: List[Type]): Tree =  {
      val copTypes = tp :+ typeRef(RootClass.newClass(newTypeName("CNil")))
      val chain: forest.Tree = INFIX_CHAIN(":+:", copTypes.map(t => Ident(t.safeToString)))
      val chainedS = treeToString(chain)
      val copType = typeRef(RootClass.newClass(newTypeName(chainedS)))
      REF("Coproduct") APPLYTYPE copType APPLY treeMatcher(node, defaultParam)
    }

    val includesNull: Boolean = unionSchemas.exists(_.getType == Schema.Type.NULL)

    val nonNullableSchemas: List[Schema] = unionSchemas.filter(_.getType != Schema.Type.NULL)

    def unionsAsOptionalShapelessCoproductStrategy = nonNullableSchemas match {
      case firstSchema :: _ => //Coproduct
        COPRODUCT(firstSchema,
          nonNullableSchemas
            .map(typeMatcher.toScalaType(classStore, namespace, _)))
      case _ => throw new Exception("unrecognized shape for shapeless coproduct")
    }

    def unionsArityStrategy(
      classStore: ClassStore,
      namespace: Option[String],
      typeMatcher: TypeMatcher) =
      nonNullableSchemas match {
        case List(schemaA) => //Option
          treeMatcher(node, schemaA)
        case List(schemaA, schemaB) if typeMatcher.avroScalaTypes.union == OptionEitherShapelessCoproduct => //Either
          LEFT(treeMatcher(node, schemaA))
        case firstSchema :: _ => //Coproduct
          COPRODUCT(firstSchema,
            nonNullableSchemas
              .map(typeMatcher.toScalaType(classStore, namespace, _)))
      }

    def matchedTree(classStore: ClassStore, namespace: Option[String]) = typeMatcher.avroScalaTypes.union match {
      case OptionShapelessCoproduct => unionsArityStrategy(classStore, namespace, typeMatcher)
      case OptionEitherShapelessCoproduct => unionsArityStrategy(classStore, namespace, typeMatcher)
      case OptionalShapelessCoproduct => unionsAsOptionalShapelessCoproductStrategy
    }

    node match {
      case `nullNode` => NONE
      case _ : NullNode => NONE
      case _ if includesNull => SOME(matchedTree(classStore, namespace))
      case _ => matchedTree(classStore, namespace)
    }
  }
}
