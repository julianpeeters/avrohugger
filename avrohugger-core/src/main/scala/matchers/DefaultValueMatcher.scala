package avrohugger
package matchers

import avrohugger.matchers.custom.CustomDefaultParamMatcher
import avrohugger.matchers.custom.CustomDefaultValueMatcher
import avrohugger.stores.ClassStore
import avrohugger.types._
import treehugger.forest._
import definitions._
import org.apache.avro.Schema
import org.codehaus.jackson.JsonNode
import org.codehaus.jackson.map.ObjectMapper
import org.codehaus.jackson.node.{NullNode, ObjectNode, TextNode}
import treehugger.forest
import treehuggerDSL._

import scala.collection.JavaConverters._

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
            default = LIT(node.getIntValue)) {
            case Date =>
              CustomDefaultValueMatcher.checkCustomDateType(
                node.getLongValue,
                typeMatcher.avroScalaTypes.date)
          }
        case Schema.Type.FLOAT => LIT(node.getDoubleValue.asInstanceOf[Float])
        case Schema.Type.LONG =>
          LogicalType.foldLogicalTypes[Tree](
            schema = schema,
            default = LIT(node.getLongValue)) {
            case TimestampMillis =>
            CustomDefaultValueMatcher.checkCustomTimestampMillisType(
              node.getLongValue,
              typeMatcher.avroScalaTypes.timestampMillis)
          }
        case Schema.Type.DOUBLE => LIT(node.getDoubleValue)
        case Schema.Type.BOOLEAN => LIT(node.getBooleanValue)
        case Schema.Type.STRING =>
          LogicalType.foldLogicalTypes[Tree](
            schema = schema,
            default = LIT(node.getTextValue)) {
            case UUID => REF("java.util.UUID.fromString") APPLY LIT(node.getTextValue)
          }
        case Schema.Type.BYTES =>
          CustomDefaultParamMatcher.checkCustomDecimalType(
            schema = schema,
            value = LIT(node.getDecimalValue.toString),
            defaultValue = Some(REF("Array[Byte]") APPLY node.getTextValue.getBytes.map((e: Byte) => LIT(e))))
        case Schema.Type.ENUM => typeMatcher.avroScalaTypes.enum match {
          case JavaEnum => (REF(schema.getName) DOT node.getTextValue)
          case ScalaEnumeration => (REF(schema.getName) DOT node.getTextValue)
          case ScalaCaseObjectEnum => (REF(schema.getName) DOT node.getTextValue)
          case EnumAsScalaString => LIT(node.getTextValue)
        }
        case Schema.Type.NULL => LIT(null)
        case Schema.Type.UNION => {
          val unionSchemas = schema.getTypes.asScala.toList
          val result = unionDefaultArgsImpl(node, unionSchemas, fromJsonNode, typeMatcher, classStore, namespace)
          result
        }
        case Schema.Type.ARRAY => {
          val collectionType = CustomDefaultParamMatcher.checkCustomArrayType(typeMatcher.avroScalaTypes.array)
          collectionType APPLY(node.getElements.asScala.toSeq.map(e => fromJsonNode(e, schema.getElementType)))
        }
        case Schema.Type.MAP => {
          val kvps = node.getFields.asScala.toList.map(e => LIT(e.getKey) ANY_-> fromJsonNode(e.getValue, schema.getValueType))
          MAKE_MAP(kvps)
        }
        case Schema.Type.RECORD  => {
          val fields  = schema.getFields
          val jsObject = node match {
            case t: TextNode =>
              val mapper = new ObjectMapper();
              mapper.readValue(t.getTextValue, classOf[ObjectNode])
            case o: ObjectNode => o
            case _ => throw new Exception(s"Invalid default value for field: $field, value: $node")
          }

          val fieldValues = fields.asScala.map { f =>
            fromJsonNode(jsObject.get(f.name), f.schema)
          }
          NEW(schema.getName, fieldValues: _*)
        }
        case x => throw new Exception("Can't extract a default field, type not yet supported: " + x)
      }
    }

    fromJsonNode(field.defaultValue, field.schema)
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