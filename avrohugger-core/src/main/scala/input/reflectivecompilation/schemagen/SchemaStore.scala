package avrohugger
package input
package reflectivecompilation
package schemagen

import java.util.concurrent.ConcurrentHashMap
import collection.JavaConversions._
 
import org.apache.avro.Schema

class SchemaStore {

  val schemas: scala.collection.concurrent.Map[String, Schema] = scala.collection.convert.Wrappers.JConcurrentMapWrapper(new ConcurrentHashMap[String, Schema]())

  def accept(schema: Schema) {
  	val fullName = schema.getFullName
    val _ = schemas += (fullName -> schema)
  }

}