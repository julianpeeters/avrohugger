package avrohugger
package input
package reflectivecompilation
package schemagen

import java.util.concurrent.ConcurrentHashMap
import collection.JavaConversions._
 
import scala.reflect.runtime.universe._
object TypecheckDependencyStore {

  val knownClasses: scala.collection.concurrent.Map[String, Tree] = scala.collection.convert.Wrappers.JConcurrentMapWrapper(new ConcurrentHashMap[String, Tree]())

  def accept(tree: Tree) {
    if (!knownClasses.contains(tree.toString)) {
      val _ = knownClasses += (tree.toString -> tree)
    }
  }

}