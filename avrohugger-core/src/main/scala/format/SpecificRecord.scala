package avrohugger
package format

import specific._

import org.apache.avro.Schema.Type._

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.Schema

import scala.collection.JavaConversions._

object SpecificRecord extends SourceFormat{

  override val toolName = "generate-specific";

  override val toolShortDescription = "Generates Scala code extending SpecificRecordBase.";

  override def fileExt(schema: Schema) = schema.getType match {
    case RECORD => ".scala"
    case ENUM => ".java" // Avro's SpecificData requires enums be Java Enum

    case _ => sys.error("Only RECORD and ENUM can be top-level definitions")
  }

  override def asDefinitionString(
    classStore: ClassStore, 
    namespace: Option[String], 
    schema: Schema): String = {

    schema.getType match {
      case RECORD => SpecificScalaTreehugger.asScalaCodeString(classStore, namespace, schema)
      case ENUM => SpecificJavaTreehugger.asJavaCodeString(classStore, namespace, schema)
      case _ => sys.error("Only RECORD and ENUM can be top-level definitions")
    }
  }


}
