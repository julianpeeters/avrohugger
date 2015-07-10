package avrohugger

import treehugger.forest._
import treehuggerDSL._
import definitions._
import java.util.concurrent.ConcurrentHashMap
import org.apache.avro.Schema

class ClassStore {

  val generatedClasses: scala.collection.concurrent.Map[Schema, Symbol] = {
  	scala.collection.convert.Wrappers.JConcurrentMapWrapper(new ConcurrentHashMap[Schema, Symbol]())
  }

  def accept(schema: Schema, caseClassDef: Symbol) {
    if (!generatedClasses.contains(schema)) {
      val _ = generatedClasses += schema -> caseClassDef
    }
  }
}
