package avrohugger
package matchers

import stores.ClassStore

import treehugger.forest._
import treehuggerDSL._
import definitions._

import org.apache.avro.Schema
import org.apache.avro.Schema.{Type => AvroType}

import java.util.concurrent.ConcurrentHashMap
import scala.collection.convert.Wrappers.JConcurrentMapWrapper
import scala.collection.JavaConverters._

class TypeMatcher {

  // holds user-defined custom type mappings, e.g. ("array"->classOf[Array[_]])
  val customTypeMap: scala.collection.concurrent.Map[String, Class[_]] = {
    JConcurrentMapWrapper(new ConcurrentHashMap[String, Class[_]]())
  }
  
  // holds user-defined custom namespace mappings, 
  // e.g. ("com.example.idl"->"com.example.model")
  val customNamespaceMap: scala.collection.concurrent.Map[String, String] = {
    JConcurrentMapWrapper(new ConcurrentHashMap[String, String]())
  }
  
  // holds user-selected enum style, e.g. ("enum"->"java_enum")
  val customEnumStyleMap: scala.collection.concurrent.Map[String, String] = {
    JConcurrentMapWrapper(new ConcurrentHashMap[String, String]())
  }
  

  // updates the type map to allow for custom avro to scala mappings
  def updateCustomTypeMap(avroToScalaMapEntry: (String, Class[_])) {
    val _ = customTypeMap += avroToScalaMapEntry
  }

  // updates the namespace map to allow for custom avro to scala mappings
  def updateCustomNamespaceMap(customNamespaceMapEntry: (String, String)) {
    val _ = customNamespaceMap += customNamespaceMapEntry
  }
  
  // updates the enum style map map to allow for avro to java or scala mappings
  def updateCustomEnumStyleMap(customEnumStyleMapEntry: (String, String)) {
    val _ = customEnumStyleMap += customEnumStyleMapEntry
  }

  def toScalaType(
    classStore: ClassStore,
    namespace: Option[String],
    schema: Schema): Type = {
    // May contain nested schemas that will use the same namespace as the 
    // top-level schema. Thus, when a field is parsed, the namespace is passed.
    def matchType(schema: Schema): Type = {

      schema.getType match { 
        case Schema.Type.ARRAY    => {
          // default array mapping is currently List, for historical reasons
          val avroElement = schema.getElementType
          val scalaElementType = toScalaType(classStore, namespace, avroElement)
          val collectionType = CustomTypeMatcher.checkCustomArrayType(
            customTypeMap.get("array"),
            TYPE_LIST)
          collectionType(scalaElementType)
        }
        case Schema.Type.MAP      => {
          val keyType = StringClass
          val avroValueType = schema.getValueType
          val scalaValueType = toScalaType(classStore, namespace, avroValueType)
          TYPE_MAP(keyType, scalaValueType)
        }
        case Schema.Type.BOOLEAN  => BooleanClass
        case Schema.Type.DOUBLE   => CustomTypeMatcher.checkCustomNumberType(
          customTypeMap.get("double"), DoubleClass)
        case Schema.Type.FLOAT    => CustomTypeMatcher.checkCustomNumberType(
          customTypeMap.get("float"), FloatClass)
        case Schema.Type.LONG     => CustomTypeMatcher.checkCustomNumberType(
          customTypeMap.get("long"), LongClass)
        case Schema.Type.INT      => CustomTypeMatcher.checkCustomNumberType(
          customTypeMap.get("int"), IntClass)
        case Schema.Type.NULL     => NullClass
        case Schema.Type.STRING   => StringClass
        case Schema.Type.FIXED    => sys.error("FIXED datatype not supported")
        case Schema.Type.BYTES    => TYPE_ARRAY(ByteClass)
        case Schema.Type.RECORD   => classStore.generatedClasses(schema)
        case Schema.Type.ENUM     => classStore.generatedClasses(schema)
        case Schema.Type.UNION    => { 
          val unionSchemas = schema.getTypes.asScala.toList
          // unions are represented as Scala Option[T], and thus unions must be 
          // of two types, one of them NULL
          val isTwoTypes = unionSchemas.length == 2
          val oneTypeIsNull = unionSchemas.exists(_.getType == Schema.Type.NULL)
          if (isTwoTypes && oneTypeIsNull) {
            val maybeSchema = unionSchemas.find(_.getType != Schema.Type.NULL)
            if (maybeSchema.isDefined ) optionType(matchType(maybeSchema.get))
            else sys.error("no avro type found in this union")  
          }
          else sys.error("unions not yet supported beyond nullable fields")
        }
        case x => sys.error( x + " is not supported or not a valid Avro type")
      }
    }
    
    matchType(schema)
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
        case x => sys.error( x +  " is not supported or not a valid Avro type")
      }
    }

    matchType(schema)
  }
}
