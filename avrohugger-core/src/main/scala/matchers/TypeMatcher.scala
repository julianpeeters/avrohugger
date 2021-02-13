package avrohugger
package matchers

import avrohugger.matchers.custom.CustomTypeMatcher
import avrohugger.stores.ClassStore
import avrohugger.types._
import treehugger.forest._
import treehuggerDSL._
import definitions._
import org.apache.avro.Schema

import treehugger.forest

import scala.jdk.CollectionConverters._

class TypeMatcher(
  val avroScalaTypes: AvroScalaTypes,
  //e.g. ("com.example.idl"->"com.example.model")
  val customNamespaces: Map[String, String]) {

  def toScalaType(
    classStore: ClassStore,
    namespace: Option[String],
    schema: Schema): Type = {
    // May contain nested schemas that will use the same namespace as the
    // top-level schema. Thus, when a field is parsed, the namespace is passed.
    def matchType(schema: Schema): Type = {

      schema.getType match {
        case Schema.Type.ARRAY    => {
          val avroElement = schema.getElementType
          val scalaElementType = toScalaType(classStore, namespace, avroElement)
          val collectionType = CustomTypeMatcher.checkCustomArrayType(avroScalaTypes.array)
          collectionType(scalaElementType)
        }
        case Schema.Type.MAP      => {
          val keyType = StringClass
          val avroValueType = schema.getValueType
          val scalaValueType = toScalaType(classStore, namespace, avroValueType)
          TYPE_MAP(keyType, scalaValueType)
        }
        case Schema.Type.BOOLEAN  => BooleanClass
        case Schema.Type.DOUBLE   => CustomTypeMatcher.checkCustomNumberType(avroScalaTypes.double)
        case Schema.Type.FLOAT    => CustomTypeMatcher.checkCustomNumberType(avroScalaTypes.float)
        case Schema.Type.LONG     =>
          LogicalType.foldLogicalTypes(
            schema = schema,
            default = CustomTypeMatcher.checkCustomNumberType(avroScalaTypes.long)) {
            case TimestampMillis => CustomTypeMatcher.checkCustomTimestampMillisType(avroScalaTypes.timestampMillis)
          }
        case Schema.Type.INT      =>
          LogicalType.foldLogicalTypes(
            schema = schema,
            default = CustomTypeMatcher.checkCustomNumberType(avroScalaTypes.int)) {
            case Date => CustomTypeMatcher.checkCustomDateType(avroScalaTypes.date)
          }
        case Schema.Type.NULL     => NullClass
        case Schema.Type.STRING   =>
          LogicalType.foldLogicalTypes(
            schema = schema,
            default = StringClass) {
            case UUID => RootClass.newClass(nme.createNameType("java.util.UUID"))
          }
        case Schema.Type.FIXED    => sys.error("FIXED datatype not yet supported")
        case Schema.Type.BYTES    => CustomTypeMatcher.checkCustomDecimalType(avroScalaTypes.decimal, schema)
        case Schema.Type.RECORD   => classStore.generatedClasses(schema)
        case Schema.Type.ENUM     => CustomTypeMatcher.checkCustomEnumType(avroScalaTypes.enum, classStore, schema)
        case Schema.Type.UNION    => {
          //unions are represented as shapeless.Coproduct
          val unionSchemas = schema.getTypes.asScala.toList
          unionTypeImpl(unionSchemas, matchType)
        }
        case x => sys.error( s"$x is not supported or not a valid Avro type")
      }
    }

    matchType(schema)
  }

  /**
    * Handles unions with the following type translations
    *
    * union:null,T => Option[T]
    * union:L,R => Either[L, R]
    * union:A,B,C => A :+: B :+: C :+: CNil
    * union:null,L,R => Option[Either[L, R]]
    * union:null,A,B,C => Option[A :+: B :+: C :+: CNil]
    *
    * If a null is found at any position in the union the entire type is wrapped in Option and null removed from the
    * types. Per the avro spec which is ambiguous about this:
    *
    * https://avro.apache.org/docs/1.8.1/spec.html#Unions
    *
    * (Note that when a default value is specified for a record field whose type is a union, the type of the default
    * value must match the first element of the union. Thus, for unions containing "null", the "null" is usually listed
    * first, since the default value of such unions is typically null.)
    */
  private[this] def unionTypeImpl(unionSchemas: List[Schema], typeMatcher: (Schema) => Type) : Type = {

    def shapelessCoproductType(tp: Type*): forest.Type =  {
      val copTypes = tp.toList :+ typeRef(RootClass.newClass(newTypeName("CNil")))
      val chain: forest.Tree = INFIX_CHAIN(":+:", copTypes.map(t => Ident(t.safeToString)))
      val chainedS = treeToString(chain)
      typeRef(RootClass.newClass(newTypeName(chainedS)))
    }

    val includesNull: Boolean = unionSchemas.exists(_.getType == Schema.Type.NULL)

    val nonNullableSchemas: List[Schema] = unionSchemas.filter(_.getType != Schema.Type.NULL)

    def unionsAsShapelessCoproductStrategy =
      shapelessCoproductType(nonNullableSchemas.map(typeMatcher): _*)

    def unionsAsOptionShapelessCoproductStrategy = nonNullableSchemas match {
      case List(schemaA) =>
        typeMatcher(schemaA)
      case _ =>
        unionsAsShapelessCoproductStrategy
    }

    def unionsArityStrategy = nonNullableSchemas match {
      case List(schemaA) =>
        typeMatcher(schemaA)
      case List(schemaA, schemaB) =>
        eitherType(typeMatcher(schemaA), typeMatcher(schemaB))
      case _ =>
        unionsAsShapelessCoproductStrategy
    }

    val matchedType = avroScalaTypes.union match {
      case OptionShapelessCoproduct => unionsAsOptionShapelessCoproductStrategy
      case OptionEitherShapelessCoproduct => unionsArityStrategy
      case OptionalShapelessCoproduct => unionsAsShapelessCoproductStrategy
    }

    if (includesNull) optionType(matchedType) else matchedType
  }


  //Scavro requires Java types be generated for mapping Java classes to Scala

  // in the future, scavro may allow this to be set
  val avroStringType = TYPE_REF("CharSequence")

  def toJavaType(
    classStore: ClassStore,
    namespace: Option[String],
    schema: Schema): Type = {
    // The schema may contain nested schemas that will use the same namespace
    // as the top-level schema.  Thus, when a field is parsed, the namespace is
    // passed in once
    def matchType(schema: Schema): Type = {
      def javaRename(schema: Schema) = {
        "J" + classStore.generatedClasses(schema)
      }

      schema.getType match {
        case Schema.Type.INT => TYPE_REF("java.lang.Integer")
        case Schema.Type.DOUBLE => TYPE_REF("java.lang.Double")
        case Schema.Type.FLOAT => TYPE_REF("java.lang.Float")
        case Schema.Type.LONG => TYPE_REF("java.lang.Long")
        case Schema.Type.BOOLEAN => TYPE_REF("java.lang.Boolean")
        case Schema.Type.STRING => avroStringType
        case Schema.Type.ARRAY => {
          val avroElement = schema.getElementType
          val elementType = toJavaType(classStore, namespace, avroElement)
          TYPE_REF(REF("java.util.List") APPLYTYPE(elementType))
        }
        case Schema.Type.MAP      => {
          val keyType = avroStringType
          val valueType = toJavaType(classStore, namespace, schema.getValueType)
          TYPE_REF(REF("java.util.Map") APPLYTYPE(keyType, valueType))
        }
        case Schema.Type.NULL     => TYPE_REF("java.lang.Void")
        case Schema.Type.FIXED    => sys.error("FIXED datatype not supported")
        case Schema.Type.BYTES    => TYPE_REF("java.nio.ByteBuffer")
        case Schema.Type.RECORD   => TYPE_REF(javaRename(schema))
        case Schema.Type.ENUM     => TYPE_REF(javaRename(schema))
        case Schema.Type.UNION    => {
          val unionSchemas = schema.getTypes.asScala.toList
          // unions are represented as Scala Option[T], and thus unions must be
          // of two types, one of them NULL
          val isTwoTypes = unionSchemas.length == 2
          val oneTypeIsNull = unionSchemas.exists(_.getType == Schema.Type.NULL)
          if (isTwoTypes && oneTypeIsNull) {
            val maybeSchema = unionSchemas.find(_.getType != Schema.Type.NULL)
            if (maybeSchema.isDefined ) matchType(maybeSchema.get)
            else sys.error("no avro type found in this union")
          }
          else sys.error("unions not yet supported beyond nullable fields")
        }
        case x => sys.error( s"$x is not supported or not a valid Avro type")
      }
    }

    matchType(schema)
  }
}
