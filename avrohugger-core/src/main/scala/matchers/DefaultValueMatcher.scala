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
import com.fasterxml.jackson.databind.node.{ NullNode, ObjectNode, TextNode }
import treehugger.forest
import treehuggerDSL._
import scala.util.Try

import scala.jdk.CollectionConverters._

object DefaultValueMatcher {

  val nullNode = new TextNode("null")

  private def fromJsonNode(node: JsonNode,
    classStore: ClassStore,
    namespace: Option[String],
    schema: Schema,
    typeMatcher: TypeMatcher,
    useFullName: Boolean = false): Tree = {
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
          case TimeMillis =>
            CustomDefaultValueMatcher.checkCustomTimeMillisType(
              node.longValue(),
              typeMatcher.avroScalaTypes.timeMillis)
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
      case Schema.Type.ENUM => {
        val refName = if (useFullName) schema.getFullName else schema.getName
        typeMatcher.avroScalaTypes.`enum` match {
          case JavaEnum => (REF(refName) DOT node.textValue())
          case ScalaEnumeration => (REF(refName) DOT node.textValue())
          case ScalaCaseObjectEnum => (REF(refName) DOT node.textValue())
          case EnumAsScalaString => LIT(node.textValue())
        }
      }
      case Schema.Type.NULL => LIT(null)
      case Schema.Type.UNION => {
        val unionSchemas = schema.getTypes().asScala.toList
        val result = unionDefaultArgsImpl(node, unionSchemas, fromJsonNode(_, classStore, namespace, _, typeMatcher, useFullName), typeMatcher, classStore, namespace)
        result
      }
      case Schema.Type.ARRAY => {
        val collectionType = CustomDefaultParamMatcher.checkCustomArrayType(typeMatcher.avroScalaTypes.array)
        collectionType APPLY (node.elements().asScala.toSeq.map(e => fromJsonNode(e, classStore, namespace, schema.getElementType, typeMatcher, useFullName)))
      }
      case Schema.Type.MAP => {
        val kvps = node.fields().asScala.toList.map(e => LIT(e.getKey) ANY_-> fromJsonNode(e.getValue, classStore, namespace, schema.getValueType, typeMatcher, useFullName))
        MAKE_MAP(kvps)
      }
      case Schema.Type.RECORD => {
        val fields = schema.getFields
        val jsObject = node match {
          case t: TextNode =>
            val mapper = new ObjectMapper();
            mapper.readValue(t.textValue(), classOf[ObjectNode])
          case o: ObjectNode => o
          case _ => throw new Exception(s"Invalid default value for schema: $schema, value: $node")
        }

        val fieldValues = fields.asScala.map { f =>
          fromJsonNode(jsObject.get(f.name), classStore, namespace, f.schema, typeMatcher, useFullName)
        }
        NEW(schema.getName, fieldValues: _*)
      }
      case Schema.Type.FIXED => {
        REF(schema.getName()) APPLY (
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
          )
      }
    }
  }

  // This code was stolen from here:
  // https://github.com/julianpeeters/avro-scala-macro-annotations/blob/104fa325a00044ff6d31184fa7ff7b6852e9acd5/macros/src/main/scala/avro/scala/macro/annotations/provider/matchers/FromJsonMatcher.scala
  def getDefaultValue(
    classStore: ClassStore,
    namespace: Option[String],
    field: Schema.Field,
    typeMatcher: TypeMatcher,
    useFullName: Boolean = false): Tree = {
    val defaultValue = org.apache.avro.util.internal.Accessor.defaultValue(field)
    fromJsonNode(defaultValue, classStore, namespace, field.schema(), typeMatcher, useFullName)
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
    namespace: Option[String]): Tree = {
    val nonNullableSchemas: List[Schema] = unionSchemas.filter(_.getType != Schema.Type.NULL)
    node match {
      case `nullNode` => NONE
      case _: NullNode => NONE
      case _ if unionSchemas.exists(_.getType == Schema.Type.NULL) => SOME(matchedTree(node, nonNullableSchemas, classStore, namespace, treeMatcher, typeMatcher))
      case _ => matchedTree(node, nonNullableSchemas, classStore, namespace, treeMatcher, typeMatcher)
    }
  }

  private def COPRODUCT(
    node: JsonNode,
    treeMatcher: (JsonNode, Schema) => Tree, defaultParam: Schema, tp: List[Type]): Tree = {
    val copTypes = tp :+ typeRef(RootClass.newClass(newTypeName("CNil")))
    val chain: forest.Tree = INFIX_CHAIN(":+:", copTypes.map(t => Ident(t.safeToString)))
    val chainedS = treeToString(chain)
    val copType = typeRef(RootClass.newClass(newTypeName(chainedS)))
    REF("shapeless.Coproduct") APPLYTYPE copType APPLY treeMatcher(node, defaultParam)
  }

  private def unionsAsOptionalShapelessCoproductStrategy(
    node: JsonNode,
    nonNullableSchemas: List[Schema],
    namespace: Option[String],
    classStore: ClassStore,
    treeMatcher: (JsonNode, Schema) => Tree,
    typeMatcher: TypeMatcher
  ) = nonNullableSchemas match {
    case firstSchema :: _ => //Coproduct
      COPRODUCT(node, treeMatcher, firstSchema,
        nonNullableSchemas.map(typeMatcher.toScalaType(classStore, namespace, _)))
    case _ => throw new Exception("unrecognized shape for shapeless coproduct")
  }

  private def unionsArityStrategy(
    node: JsonNode,
    nonNullableSchemas: List[Schema],
    classStore: ClassStore,
    namespace: Option[String],
    typeMatcher: TypeMatcher,
    useEither: Boolean,
    treeMatcher: (JsonNode, Schema) => Tree
  ) =
    nonNullableSchemas match {
      case Nil =>
        UNIT
      case List(schemaA) => //Option
        treeMatcher(node, schemaA)
      case List(schemaA, _) if useEither => //Either
        LEFT(treeMatcher(node, schemaA))
      case firstSchema :: _ => //Coproduct
        COPRODUCT(node, treeMatcher, firstSchema,
          nonNullableSchemas.map(typeMatcher.toScalaType(classStore, namespace, _)))
    }

  private def unionsScala3Strategy(
    node: JsonNode,
    nonNullableSchemas: List[Schema],
    treeMatcher: (JsonNode, Schema) => Tree
  ) = nonNullableSchemas match {
    case Nil =>
      UNIT
    case schemaA :: _ =>
      treeMatcher(node, schemaA)
  }

  private def matchedTree(
    node: JsonNode,
    nonNullableSchemas: List[Schema],
    classStore: ClassStore,
    namespace: Option[String],
    treeMatcher: (JsonNode, Schema) => Tree,
    typeMatcher: TypeMatcher
  ) = typeMatcher.avroScalaTypes.union match {
    case OptionShapelessCoproduct | OptionEitherShapelessCoproduct =>
      unionsArityStrategy(
        node,
        nonNullableSchemas,
        classStore,
        namespace,
        typeMatcher,
        typeMatcher.avroScalaTypes.union.useEitherForTwoNonNullTypes,
        treeMatcher
      )
    case OptionalShapelessCoproduct =>
      unionsAsOptionalShapelessCoproductStrategy(node, nonNullableSchemas, namespace, classStore, treeMatcher, typeMatcher)
    case OptionScala3UnionType => unionsScala3Strategy(node, nonNullableSchemas, treeMatcher)
  }

}
