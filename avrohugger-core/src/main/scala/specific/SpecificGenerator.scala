package avrohugger
package specific

import org.apache.avro.Schema
import java.io.{File, FileNotFoundException, IOException}

class SpecificGenerator extends GeneratorBase{

  override def fromSchema(schema: Schema, outDir: String = defaultOutputDir): Unit = {
    val namespace: Option[String] = parser.getNamespace(schema)
    // schemas that represent top-level Scala definitions such as RECORD and ENUM
    val topLevelSchemas: List[Schema] = schema::(parser.getNestedSchemas(schema))
    // if a field's type is a record, the nested Schema has no namespace, but we need one for each file so we pass one
    topLevelSchemas.reverse.foreach(schema => SpecificAvroHugger.write(classStore, namespace, schema, outDir))
  }

}
