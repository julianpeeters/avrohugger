package avrohugger
package stores

import org.apache.avro.Schema

import treehugger.forest.Symbol

import java.util.concurrent.ConcurrentHashMap

class ClassStore {

  val generatedClasses: ConcurrentHashMap[String, Symbol] =
  	new ConcurrentHashMap[String, Symbol]()

  def accept(schema: Schema, caseClassDef: Symbol): Symbol =
    generatedClasses.putIfAbsent(schema.getFullName, caseClassDef)
}
