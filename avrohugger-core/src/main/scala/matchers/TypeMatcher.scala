package avrohugger
package matchers

import avrohugger.matchers.custom.{ CustomNamespaceMatcher, CustomTypeMatcher }
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
    schema: Schema,
    useFullName: Boolean = false
  ): Type = {
    var typeMap = Map[String, Type]()

    // May contain nested schemas that will use the same namespace as the
    // top-level schema. Thus, when a field is parsed, the namespace is passed.
    def matchType(schema: Schema): Type = {
      if (typeMap.contains(schema.getFullName)) {
        typeMap(schema.getFullName)
      }
      else {
        val tp: Type = schema.getType match {
          case Schema.Type.ARRAY =>
            val avroElement = schema.getElementType
            val scalaElementType = toScalaType(classStore, namespace, avroElement)
            val collectionType = CustomTypeMatcher.checkCustomArrayType(avroScalaTypes.array)
            collectionType(scalaElementType)
          case Schema.Type.MAP =>
            val keyType = StringClass
            val avroValueType = schema.getValueType
            val scalaValueType = toScalaType(classStore, namespace, avroValueType)
            TYPE_MAP(keyType, scalaValueType)
          case Schema.Type.BOOLEAN => BooleanClass
          case Schema.Type.DOUBLE => CustomTypeMatcher.checkCustomNumberType(avroScalaTypes.double)
          case Schema.Type.FLOAT => CustomTypeMatcher.checkCustomNumberType(avroScalaTypes.float)
          case Schema.Type.LONG =>
            LogicalType.foldLogicalTypes(
              schema = schema,
              default = CustomTypeMatcher.checkCustomNumberType(avroScalaTypes.long)) {
              case TimestampMillis => CustomTypeMatcher.checkCustomTimestampMillisType(avroScalaTypes.timestampMillis)
              case TimestampMicros => CustomTypeMatcher.checkCustomTimestampMicrosType(avroScalaTypes.timestampMicros)
              case LocalTimestampMicros => CustomTypeMatcher.checkCustomLocalTimestampMicrosType(avroScalaTypes.localTimestampMicros)
              case LocalTimestampMillis => CustomTypeMatcher.checkCustomLocalTimestampMillisType(avroScalaTypes.localTimestampMillis)
              case TimeMicros => CustomTypeMatcher.checkCustomTimeMicrosType(avroScalaTypes.timeMicros)
            }
          case Schema.Type.INT =>
            LogicalType.foldLogicalTypes(
              schema = schema,
              default = CustomTypeMatcher.checkCustomNumberType(avroScalaTypes.int)) {
              case Date => CustomTypeMatcher.checkCustomDateType(avroScalaTypes.date)
              case TimeMillis => CustomTypeMatcher.checkCustomTimeMillisType(avroScalaTypes.timeMillis)
            }
          case Schema.Type.NULL => NullClass
          case Schema.Type.STRING =>
            LogicalType.foldLogicalTypes(
              schema = schema,
              default = StringClass) {
              case UUID => RootClass.newClass(nme.createNameType("java.util.UUID"))
            }
          case Schema.Type.FIXED =>
            RootClass.newClass(s"${schema.getNamespace()}.${classStore.generatedClasses(schema)}")
          case Schema.Type.BYTES => CustomTypeMatcher.checkCustomDecimalType(avroScalaTypes.decimal, schema)
          case Schema.Type.RECORD =>
            val maybeNamespace = CustomNamespaceMatcher.checkCustomNamespace(
              Option(schema.getNamespace()),
              this,
              maybeDefaultNamespace = Option(schema.getNamespace())
            )
            maybeNamespace match {
              case Some(ns) => s"${ns}.${schema.getName()}"
              case None => schema.getName()
            }
          case Schema.Type.ENUM => CustomTypeMatcher.checkCustomEnumType(avroScalaTypes.`enum`, classStore, schema, useFullName)
          case Schema.Type.UNION =>
            //unions are represented as shapeless.Coproduct
            val unionSchemas = schema.getTypes().asScala.toList
            unionTypeImpl(unionSchemas, matchType)
        }
        typeMap += (schema.getFullName -> tp)
        tp
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
    * In case of [OptionScala3UnionType] shapeless is replaced with native Scala 3 union types
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
  private[this] def unionTypeImpl(unionSchemas: List[Schema], typeMatcher: (Schema) => Type): Type = {

    def shapelessCoproductType(tp: Type*): forest.Type = {
      val copTypes = tp.toList :+ typeRef(RootClass.newClass(newTypeName("CNil")))
      val chain: forest.Tree = INFIX_CHAIN(":+:", copTypes.map(t => Ident(t.safeToString)))
      val chainedS = treeToString(chain)
      typeRef(RootClass.newClass(newTypeName(chainedS)))
    }

    def scala3NativeType(tp: Type*): forest.Type = {
      val chain = Alternative(tp.toList.map(t => Ident(t.safeToString)))
      val chainedS = treeToString(chain)
      typeRef(RootClass.newClass(newTypeName(chainedS)))
    }

    val includesNull: Boolean = unionSchemas.exists(_.getType == Schema.Type.NULL)

    val nonNullableSchemas: List[Schema] = unionSchemas.filter(_.getType != Schema.Type.NULL)

    def unionsAsShapelessCoproductStrategy =
      shapelessCoproductType(nonNullableSchemas.map(typeMatcher): _*)

    def unionsAsScala3UnionTypeStrategy =
      scala3NativeType(nonNullableSchemas.map(typeMatcher): _*)

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

    def scala3NativeUnions = nonNullableSchemas match {
      case List(schemaA) =>
        typeMatcher(schemaA)
      case _ => unionsAsScala3UnionTypeStrategy
    }

    val matchedType = avroScalaTypes.union match {
      case OptionShapelessCoproduct => unionsAsOptionShapelessCoproductStrategy
      case OptionEitherShapelessCoproduct => unionsArityStrategy
      case OptionalShapelessCoproduct => unionsAsShapelessCoproductStrategy
      case OptionScala3UnionType => scala3NativeUnions
    }

    if (includesNull) optionType(matchedType) else matchedType
  }

}
