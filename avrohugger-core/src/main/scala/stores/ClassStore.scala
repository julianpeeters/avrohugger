package avrohugger
package stores

import org.apache.avro.Schema

import treehugger.forest.Symbol

import java.util.concurrent.ConcurrentHashMap
import scala.jdk.CollectionConverters._

class ClassStore {

  val generatedClasses: scala.collection.concurrent.Map[Schema, Symbol] = {
  	new ConcurrentHashMap[Schema, Symbol]().asScala
  }

  def accept(schema: Schema, caseClassDef: Symbol) = {
    if (!generatedClasses.contains(schema)) {
      val _ = generatedClasses += schema -> caseClassDef
    }
  }
}
