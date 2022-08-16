package avrohugger
package stores

import java.util.concurrent.ConcurrentHashMap

import scala.reflect.runtime.universe._
import scala.jdk.CollectionConverters._

class TypecheckDependencyStore {

  val knownClasses: scala.collection.concurrent.Map[String, Tree] = new ConcurrentHashMap[String, Tree]().asScala

  def accept(tree: Tree) = {
    val _ = knownClasses += (tree.toString -> tree)
  }

}