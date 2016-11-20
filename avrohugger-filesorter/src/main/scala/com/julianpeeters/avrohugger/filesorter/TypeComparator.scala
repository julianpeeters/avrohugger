package com.julianpeeters.avrohugger.filesorter

import spray.json._

/**
  * Code adapted from https://github.com/ch4mpy/sbt-avro/blob/master/src/main/scala/com/c4soft/sbtavro/SbtAvro.scala
  * by Jerome Wascongne
  */
object TypeComparator {
  /**
    * This method is passed the full name of a query schema and checks if it
    * appears in any of the current candidate schema's referred types.
    */
  def strContainsType(candName: String, str: String, name: String): Boolean = {
    def isMatch(regex: String): Boolean = {
      regex.r.findFirstIn(str).isDefined
    }
    val types = ReferredTypeFinder.findReferredTypes(str.parseJson)
    val namespace = name.split("\\.") match {
      case x if x.length == 1 => ""
      case x => x.dropRight(1).mkString("\\.")
    }
    val namespaceRegex = "\\\"namespace\\\"\\s*:\\s*\\\"" + namespace + "\\\""
    val isSameNamespace = isMatch(namespaceRegex)
    val simpleTypeName = name.split("\\.").last
    val simpleCandidateName = candName.split("\\.").last

    val withoutSelf: List[String] = types
      .filter(x => x != candName)
      .filter(x => if (isSameNamespace) x != simpleCandidateName else true)

    def isReferred(name:String): Boolean = {
      withoutSelf.contains(name)
    }

    isReferred(name) || (isSameNamespace && isReferred(simpleTypeName))
  }

}
