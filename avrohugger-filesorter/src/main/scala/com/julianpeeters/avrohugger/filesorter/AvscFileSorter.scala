package avrohugger.filesorter

import TypeComparator.strContainsType

import java.io.File

import scala.collection.mutable
import scala.io.Source

/**
  * The order in which avsc files are compiled depends on the underlying file
  * system (under OSX its is alphabetical, under some linux distros it's not).
  * This is an issue when you have a record type that is used in different
  * other types. This ensures that dependent types are compiled in the
  * correct order. Code adapted from https://github.com/ch4mpy/sbt-avro/blob/master/src/main/scala/com/c4soft/sbtavro/SbtAvro.scala
  * by Jerome Wascongne
  */
object AvscFileSorter {

  def sortSchemaFiles(files: Iterable[File]): Seq[File] = {
    val sortedButReversed = mutable.ArrayBuffer.empty[File]
    def normalizeInput(files: List[File]) = files.sortBy(file => file.getName)
    var pending: Iterable[File] = normalizeInput(files.toList)
    while (pending.nonEmpty) {
      val (used, unused) = usedUnusedSchemas(pending)
      sortedButReversed ++= unused
      pending = used
    }
    sortedButReversed.reverse.toSeq
  }

  def usedUnusedSchemas(files: Iterable[File]): (Iterable[File], Iterable[File]) = {
    val usedUnused = files.map { file =>
      val fullName = extractFullName(file)
      val numUsages = files.count { candidate =>
        val candidateName = extractFullName(candidate)
        strContainsType(candidateName, fileText(candidate), fullName)
      }
      (file, numUsages)
    }.partition(usedUnused => usedUnused._2 > 0)
    (usedUnused._1.map(_._1), usedUnused._2.map(_._1))
  }

  def extractFullName(f: File): String = {
    val txt = fileText(f)
    val namespace = namespaceRegex.findFirstMatchIn(txt)
    val name = nameRegex.findFirstMatchIn(txt)
    val nameGroup = name.get.group(1)
    if (namespace.isEmpty) {
      nameGroup
    } else {
      s"${namespace.get.group(1)}.$nameGroup"
    }
  }

  def fileText(f: File): String = {
    val src = Source.fromFile(f)
    try {
      src.getLines.mkString
    } finally {
      src.close()
    }
  }

  val namespaceRegex = "\\\"namespace\\\"\\s*:\\s*\"([^\\\"]+)\\\"".r
  val nameRegex = "\\\"name\\\"\\s*:\\s*\"([^\\\"]+)\\\"".r
}
