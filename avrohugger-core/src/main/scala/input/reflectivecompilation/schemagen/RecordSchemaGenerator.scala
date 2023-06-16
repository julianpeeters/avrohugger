package avrohugger
package input
package reflectivecompilation
package schemagen

import parsers.ScalaDocParser
import stores.{ SchemaStore, TypecheckDependencyStore }

import org.apache.avro.Schema.Field
import org.apache.avro.Schema

import java.util.{ Arrays => JArrays }

import scala.reflect.runtime.universe._
import scala.reflect.runtime.currentMirror
import scala.jdk.CollectionConverters._

object RecordSchemaGenerator  {

  @deprecated("Reflective compilation will no longer be supported", "avrohugger 1.5.0")
  def generateSchema(
    className: String, 
    namespace: Option[Name], 
    fields: List[ValDef],
    maybeScalaDoc: Option[String],
    schemaStore: SchemaStore, 
    typecheckDependencyStore: TypecheckDependencyStore): Schema = {

    // Can't seem to typecheck packaged classes, so splice-in unpackaged versions
    // and later the FieldSchemaGenerator's type matcher must be passed the field's 
    // namespace explicitly.
    def typeCheck(t: Tree) = {
      val dependencies = typecheckDependencyStore.knownClasses.values.toList
      Toolbox.toolBox.typecheck(q"..$dependencies; {type T = $t}") match {
	      case x @ Block(classDefs, Block(List(TypeDef(mods, name, tparams, rhs)), const)) => rhs.tpe
        case _ => t.tpe // if there are no fields, then no dependencies either
      }
    }

    def toAvroFieldSchema(valDef: ValDef) = {
      val (referredNamespace, fieldType) = valDef.tpt match {
        case tq"$ns.$typeName" => (Some(TermName(ns.toString)), tq"$typeName")
        case t => (namespace, t)
      }

      val maybeFieldDoc = ScalaDocParser.fieldDocsMap(maybeScalaDoc).get(valDef.name.toString)

      new FieldSchemaGenerator().toAvroField(
        referredNamespace,
        valDef.name, 
        typeCheck(fieldType),
        valDef.rhs,
        maybeFieldDoc,
        schemaStore
      )
    }

    // conversion from Option to String/null is for compatibility with Apache Avro
    val ns = namespace match {
      case Some(n) => n.toString
      case None => null
    }

    val avroFields = fields.map(valDef => {
      toAvroFieldSchema(valDef)
    })

    // conversion from Option to String/null is for compatibility with Apache Avro
    val recordDoc = ScalaDocParser.getTopLevelDoc(maybeScalaDoc)

    val avroSchema = Schema.createRecord(className, recordDoc, ns, false)    
    avroSchema.setFields(JArrays.asList(avroFields.toArray:_*))
    schemaStore.accept(avroSchema)
    avroSchema
  }

}
