package avrohugger
package inputformat
package schemagen

import java.util.concurrent.ConcurrentHashMap
import collection.JavaConversions._
 
import org.apache.avro.Schema

object SchemaStore {

  val schemas: scala.collection.concurrent.Map[String, Schema] = scala.collection.convert.Wrappers.JConcurrentMapWrapper(new ConcurrentHashMap[String, Schema]())

  def accept(schema: Schema) {
  	val fullName = schema.getFullName
    if (!schemas.contains(fullName)) {
      val _ = schemas += (fullName -> schema)
    }
  }

}