package avrohugger
package stores

import org.apache.avro.Schema

import java.util.concurrent.ConcurrentHashMap
import scala.collection.convert.Wrappers.JConcurrentMapWrapper

class SchemaStore {

  val schemas: scala.collection.concurrent.Map[String, Schema] = {
    JConcurrentMapWrapper(new ConcurrentHashMap[String, Schema]())
  }

  def accept(schema: Schema) = {
  	val fullName = schema.getFullName
    val _ = schemas += (fullName -> schema)
  }

}