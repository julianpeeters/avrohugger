package avrohugger
package stores

import org.apache.avro.Schema

import java.util.concurrent.ConcurrentHashMap
import collection.JavaConverters._

class SchemaStore {

  val schemas: scala.collection.concurrent.Map[String, Schema] = {
    new ConcurrentHashMap[String, Schema]().asScala
  }

  def accept(schema: Schema) = {
  	val fullName = schema.getFullName
    val _ = schemas += (fullName -> schema)
  }

}