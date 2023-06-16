package avrohugger
package input
package reflectivecompilation

import scala.jdk.CollectionConverters._

object PackageSplitter {

  @deprecated("Reflective compilation will no longer be supported", "avrohugger 1.5.0")
  def getCompilationUnits(code: String): List[String] = {

    def getCompilationUnits(
      lines : List[String], 
      pkgResult: List[String]= List(),
      compUnitResult: List[String]= List() ): List[String] = {

      def getBody(
        code: List[String],
        bodyResult: List[String] = List()): List[String] = {
        code match {
          case head::tail if head.startsWith("package") => {
            val repackagedBody = (pkgResult:::bodyResult).mkString("\n")
            getCompilationUnits(code, List(), compUnitResult :+ repackagedBody)
          }
          case head::tail => getBody(tail, bodyResult:+head)
          case Nil => compUnitResult:+((pkgResult:::bodyResult).mkString("\n"))
        }
      }
      val compilationUnits = lines match {
        case head::tail if head.startsWith("package") => {
          getCompilationUnits(tail, pkgResult:+head, compUnitResult)
        }
        case ls => getBody(ls)
      }
      compilationUnits
    }

    // the parser can only parse packages if their contents are within explicit blocks
    def wrapPackages(code: String): String = {
      // match package definitions that don't already wrap their contents in { }
      val nonWrappedRegEx = "(?!(package .*? \\{))(package ([a-zA-Z_$][a-zA-Z\\d_$]*\\.)*[a-zA-Z_$][a-zA-Z\\d_$]*)".r
      nonWrappedRegEx.findFirstIn(code) match {
        case Some(nonWrappedPackage) => {
          val openPackage = nonWrappedPackage + " {"
          val closePackage = "}"
          val wrappedPackage = {
            nonWrappedRegEx.replaceFirstIn(code, openPackage) + closePackage
          }
          wrapPackages(wrappedPackage)}
        case None => code
      }
    }

    val lines = code.split("\n").toList
    val compilationUnits = getCompilationUnits(lines)
    val formatted = compilationUnits.map(compUnit => wrapPackages(compUnit))
    // reversed so the most nested classes need to be expanded first
    formatted.reverse
  }

}

