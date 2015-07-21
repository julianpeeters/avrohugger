package avrohugger
package format
import standard._
import treehugger.forest._
import org.apache.avro.Schema
import scala.collection.JavaConversions._

object Standard extends SourceFormat {

  override val toolName = "generate";
  override val toolShortDescription = "Generates Scala code for the given schema.";
  override def fileExt(schema: Schema) = ".scala"

  override def asDefinitionString(
    classStore: ClassStore, 
    namespace: Option[String], 
    schema: Schema): String = {

    val tree = StandardTreehugger.asScalaCodeString(classStore, schema, namespace)
    treeToString(tree)
  }

}
