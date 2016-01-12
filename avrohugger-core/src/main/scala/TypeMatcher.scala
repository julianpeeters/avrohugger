package avrohugger

import treehugger.forest._
import treehuggerDSL._
import definitions._

import org.apache.avro.Schema 
import org.apache.avro.Schema.{Type => AvroType} 

import java.util.concurrent.ConcurrentHashMap
import scala.collection.convert.Wrappers.JConcurrentMapWrapper
import scala.collection.JavaConversions._

class TypeMatcher {

  // holds user-defined custom type mappings, e.g. ("array"->classOf[Seq[_]])
  val typeMap: scala.collection.concurrent.Map[String, Class[_]] = {
    JConcurrentMapWrapper(new ConcurrentHashMap[String, Class[_]]())
  }

  // updates the type map to allow for custom avro to scala mappings
  def updateTypeMap(avroToScalaMapEntry: (String, Class[_])) {
    val _ = typeMap += avroToScalaMapEntry
  }

  def checkCustomArrayType(maybeCustomArray: Option[Class[_]], elementType: Type, defaultType: Type => Type) = {
    maybeCustomArray match {
      case Some(c) if c == classOf[Array[_]] => TYPE_ARRAY(elementType)
      case Some(c) if c == classOf[List[_]]  => listType(elementType)
      case Some(c) if c == classOf[Seq[_]]   => TYPE_SEQ(elementType)
      case _             => defaultType(elementType)
    }
  }

  // Scavro allows number types to be remapped.
  def checkCustomNumberType(maybeCustomNumber: Option[Class[_]], defaultClass: Symbol) = {
    maybeCustomNumber match {
      case Some(c) if c == classOf[Double] => DoubleClass
      case Some(c) if c == classOf[Float]  => FloatClass
      case Some(c) if c == classOf[Long]   => LongClass
      case Some(c) if c == classOf[Int]    => IntClass
      case _                               => defaultClass
    }
  }


  // holds user-defined custom namespace mappings, e.g. ("com.example.idl"->"com.example.model")
  val namespaceMap: scala.collection.concurrent.Map[String, String] = {
    JConcurrentMapWrapper(new ConcurrentHashMap[String, String]())
  }


  // updates the namespace map to allow for custom avro to scala mappings
  def updateNamespaceMap(namespaceMapEntry: (String, String)) {
    val _ = namespaceMap += namespaceMapEntry
  }


  def toScalaType(classStore: ClassStore, namespace: Option[String], schema: Schema): Type = {
    // May contain nested schemas that will use the same namespace as the top-level schema. 
    // Thus, when a field is parsed, the namespace is passed in once
    def matchType(schema: Schema): Type = {

      schema.getType match { 
        case Schema.Type.ARRAY    => {
          val elementType = toScalaType(classStore, namespace, schema.getElementType)
          // default array mapping is currently List, but only for historical reasons
          checkCustomArrayType(typeMap.get("array"), elementType, listType)
        }
        case Schema.Type.MAP      => {
          val keyType = StringClass
          val valueType = toScalaType(classStore, namespace, schema.getValueType)
          TYPE_MAP(keyType, valueType)
        }
        case Schema.Type.BOOLEAN  => BooleanClass
        case Schema.Type.DOUBLE   => checkCustomNumberType(typeMap.get("double"), DoubleClass)
        case Schema.Type.FLOAT    => checkCustomNumberType(typeMap.get("float"), FloatClass)
        case Schema.Type.LONG     => checkCustomNumberType(typeMap.get("long"), LongClass)
        case Schema.Type.INT      => checkCustomNumberType(typeMap.get("int"), IntClass)
        case Schema.Type.NULL     => NullClass
        case Schema.Type.STRING   => StringClass
        case Schema.Type.FIXED    => sys.error("the FIXED datatype is not yet supported")
        case Schema.Type.BYTES    => checkCustomArrayType(typeMap.get("bytes"), ByteClass, TYPE_ARRAY)
        case Schema.Type.RECORD   => classStore.generatedClasses(schema)
        case Schema.Type.ENUM     => classStore.generatedClasses(schema)
        case Schema.Type.UNION    => { 
          val unionSchemas = schema.getTypes.toList
          // unions are represented as Scala Option[T], and thus unions must be of two types, one of them NULL
          if (unionSchemas.length == 2 && unionSchemas.exists(schema => schema.getType == Schema.Type.NULL)) {
            val maybeSchema = unionSchemas.find(schema => schema.getType != Schema.Type.NULL)
            if (maybeSchema.isDefined ) optionType(matchType(maybeSchema.get))
            else sys.error("no avro type found in this union")  
          }
          else sys.error("unions not yet supported beyond nullable fields")
        }
        case x => sys.error( x + " is not yet supported or is not a valid Avro type")
      }
    }
    
    matchType(schema)
  }


  //Scavro format requires that Java types be generated for mapping Java classes to Scala

  // in the future, scavro may allow this to be set
  val avroStringType = TYPE_REF("CharSequence") 

  def toJavaType(classStore: ClassStore, namespace: Option[String], schema: Schema): Type = {
    // The schema may contain nested schemas that will use the same namespace as the top-level schema. 
    // Thus, when a field is parsed, the namespace is passed in once
    def matchType(schema: Schema): Type = {

      schema.getType match { 
        case Schema.Type.INT => TYPE_REF("java.lang.Integer")
        case Schema.Type.DOUBLE => TYPE_REF("java.lang.Double")
        case Schema.Type.FLOAT => TYPE_REF("java.lang.Float")
        case Schema.Type.LONG => TYPE_REF("java.lang.Long")
        case Schema.Type.BOOLEAN => TYPE_REF("java.lang.Boolean")
        case Schema.Type.STRING => avroStringType
        case Schema.Type.ARRAY => {
          val elementType = toJavaType(classStore, namespace, schema.getElementType)
          TYPE_REF(REF("java.util.List") APPLYTYPE(elementType))
        }
        case Schema.Type.MAP      => {
          val keyType = avroStringType
          val valueType = toJavaType(classStore, namespace, schema.getValueType)
          TYPE_REF(REF("java.util.Map") APPLYTYPE(keyType, valueType))
        }
        case Schema.Type.NULL     => TYPE_REF("java.lang.Void")
        case Schema.Type.FIXED    => sys.error("the FIXED datatype is not yet supported")
        case Schema.Type.BYTES    => TYPE_REF("[]java.lang.Byte")
        case Schema.Type.RECORD   => TYPE_REF("J" + classStore.generatedClasses(schema))
        case Schema.Type.ENUM     => TYPE_REF("J" + classStore.generatedClasses(schema))
        case Schema.Type.UNION    => { 
          val unionSchemas = schema.getTypes.toList
          // unions are represented as Scala Option[T], and thus unions must be of two types, one of them NULL
          if (unionSchemas.length == 2 && unionSchemas.exists(schema => schema.getType == Schema.Type.NULL)) {
            val maybeSchema = unionSchemas.find(schema => schema.getType != Schema.Type.NULL)
            if (maybeSchema.isDefined ) matchType(maybeSchema.get)
            else sys.error("no avro type found in this union")  
          }
          else sys.error("unions not yet supported beyond nullable fields")
        }
        case x => sys.error( x +  " is not yet supported or is not a valid Avro type")
      }
    }

    matchType(schema)
  }
}
