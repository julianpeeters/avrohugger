package avrohugger
package input
package schemagen

import java.util.{Arrays => JArrays}
import org.apache.avro.Schema.Field
import org.apache.avro.Schema

import scala.collection.JavaConverters._

import scala.reflect.runtime.universe._
import scala.reflect.runtime.currentMirror

object RecordSchemaGenerator  {

  def generateSchema(
  	dependencies: List[Tree],
  	className: String, 
  	namespace: Option[Name], 
  	fields: List[ValDef]): Schema = {

  	// Can't seem to typecheck packaged classes, so splice-in unpackaged versions
	  // and later the FieldSchemaGenerator's type matcher must be passed the field's 
    // namespace explicitly.
    def typeCheck(t: Tree, dependencies: List[Tree]) = {
      Toolbox.toolBox.typeCheck(q"..$dependencies; {type T = $t}") match {
	      case x @ Block(classDefs, Block(List(TypeDef(mods, name, tparams, rhs)), const)) => rhs.tpe
	    }
	  }

	  def toAvroFieldSchema(valDef: ValDef) = {
	  	FieldSchemaGenerator.toAvroField(
        namespace,
        valDef.name, 
        typeCheck(valDef.tpt, dependencies),
        valDef.rhs
      )
	  }

    // conversion from Option to String/null is for compatibility with Apache Avro
    val ns = namespace match {
    	case Some(n) => n.toString
    	case None => null
    }

    val avroFields = fields.map(valDef => toAvroFieldSchema(valDef))

    val avroSchema = Schema.createRecord(className, "Auto-Generated Schema", ns, false)    
    avroSchema.setFields(JArrays.asList(avroFields.toArray:_*))

    SchemaStore.accept(avroSchema)
    avroSchema
  }

}