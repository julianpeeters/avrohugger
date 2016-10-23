package avrohugger
package stores

import java.util.concurrent.ConcurrentHashMap

import scala.reflect.runtime.universe._

class TypecheckDependencyStore {

  val knownClasses: scala.collection.concurrent.Map[String, Tree] = scala.collection.convert.Wrappers.JConcurrentMapWrapper(new ConcurrentHashMap[String, Tree]())

  def accept(tree: Tree) {
    val _ = knownClasses += (tree.toString -> tree)
  }

}