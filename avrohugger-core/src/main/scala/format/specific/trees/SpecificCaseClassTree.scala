package avrohugger
package format
package specific
package trees
import methods._
import org.codehaus.jackson.JsonNode
import org.codehaus.jackson.map.ObjectMapper
import org.codehaus.jackson.node.{NullNode, ObjectNode, TextNode}

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.Schema

import scala.collection.JavaConversions._

object SpecificCaseClassTree {

  val nullNode = new TextNode("null")

  // This code was stolen from here:
  // https://github.com/julianpeeters/avro-scala-macro-annotations/blob/104fa325a00044ff6d31184fa7ff7b6852e9acd5/macros/src/main/scala/avro/scala/macro/annotations/provider/matchers/FromJsonMatcher.scala
  def getDefaultValue(field: Schema.Field): Tree = {

    def fromJsonNode(node: JsonNode, schema: Schema): Tree = {
      schema.getType match {
        case _ if node == null => EmptyTree //not `default=null`, but no default
        case Schema.Type.INT => LIT(node.getIntValue)
        case Schema.Type.FLOAT => LIT(node.getDoubleValue.asInstanceOf[Float])
        case Schema.Type.LONG => LIT(node.getLongValue)
        case Schema.Type.DOUBLE => LIT(node.getDoubleValue)
        case Schema.Type.BOOLEAN => LIT(node.getBooleanValue)
        case Schema.Type.STRING => LIT(node.getTextValue)

        case Schema.Type.ENUM => (REF(schema.getName) DOT node.getTextValue)
        case Schema.Type.NULL => LIT(null)
        case Schema.Type.UNION => {
          val unionSchemas = schema.getTypes.toList
          if (unionSchemas.length == 2 &&
            unionSchemas.exists(schema => schema.getType == Schema.Type.NULL) &&
            unionSchemas.exists(schema => schema.getType != Schema.Type.NULL)) {
            val maybeSchema = unionSchemas.find(schema => schema.getType != Schema.Type.NULL)
            maybeSchema match {
              case Some(unionSchema) => {
                node match {
                  case `nullNode` => NONE
                  case nn: NullNode => NONE
                  case nonNullNode => SOME(fromJsonNode(nonNullNode, unionSchema))
                }
              }
              case None => throw new Exception("no avro type found in this union")
            }
          }
          else throw new Exception("Unsupported union field")
        }
        case Schema.Type.ARRAY => {
          LIST(node.getElements.toList.map(e => fromJsonNode(e, schema.getElementType)))
        }
        case Schema.Type.MAP => {
          val kvps = LIST(node.getFields.toList.map(e => LIT(e.getKey) ANY_-> fromJsonNode(e.getValue, schema.getValueType)))
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

          val fieldValues = fields.map { f =>
            fromJsonNode(jsObject.get(f.name), f.schema)
          }
          NEW(schema.getName, fieldValues: _*)
        }
        case x => throw new Exception("Can't extract a default field, type not yet supported: " + x)
      }
    }

    fromJsonNode(field.defaultValue, field.schema)
  }

	def toCaseClassDef(
    classStore: ClassStore, 
    namespace: Option[String], 
    schema: Schema) = {

    // register new type
    val classSymbol = RootClass.newClass(schema.getName)
    classStore.accept(schema, classSymbol)

    // generate list of constructor parameters
    val params: List[ValDef] = schema.getFields.toList.map { field =>
      val fieldName = field.name
      val fieldType = TypeMatcher.toType(classStore, namespace, field.schema)
      VAR(fieldName, fieldType) := getDefaultValue(field)
    }

    // extension
    val baseClass = RootClass.newClass("org.apache.avro.specific.SpecificRecordBase")

    // no-arg constructor
    val defaultParams: List[Tree] = schema.getFields.toList.map(avroField => DefaultParamMatcher.asDefaultParam(classStore, avroField.schema))
    val defThis = DEFTHIS.withParams(PARAM("")).tree := THIS APPLY(defaultParams:_*)

    // methods - first add an index the the schema's fields
    val indexedFields = schema.getFields.toList.zipWithIndex.map(p => IndexedField(p._1, p._2))
    val defGet = GetGenerator.toDef(indexedFields)
    val defPut = PutGenerator.toDef(classStore, namespace, indexedFields)
    val defGetSchema = GetSchemaGenerator(classSymbol).toDef

    // define the class def with the members previously defined
    CASECLASSDEF(classSymbol).withParams(params).withParents(baseClass) := BLOCK(
      defThis,
      defGet,
      defPut,
      defGetSchema
    )
	}

}
