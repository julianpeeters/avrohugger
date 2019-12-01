package avrohugger.filesorter

import java.io.File

import scala.annotation.tailrec
import scala.io.Source

/**
  * The order in which avsc files are compiled depends on the underlying file
  * system (under OSX its is alphabetical, under some linux distros it's not).
  * This is an issue when you have a record type that is used in different
  * other types. This ensures that dependent types are compiled in the
  * correct order.
  * Created by Jon Morra on 2/7/17.
  */
object AvdlFileSorter {
  def sortSchemaFiles(filesIterable: Iterable[File]): Seq[File] = {
    val files = filesIterable.toList
    val importsMap = files.map{ file =>
      (file.getCanonicalFile, getImports(file))
    }.toMap.mapValues(f => f.filter(_.exists))

    @tailrec def addFiles(processedFiles: Seq[File], remainingFiles: List[File]): Seq[File] = {
      remainingFiles match {
        case Nil => processedFiles
        case h :: t =>
          val processedFilesSet = processedFiles.toSet
          if (importsMap(h).forall(processedFilesSet.contains))
            addFiles(processedFiles :+ h, t)
          else
            addFiles(processedFiles, t :+ h)
      }
    }
    val result = addFiles(Seq.empty, files)
    result
  }

  // TODO This should be replaced by letting AVRO compile the IDL files directly, but I'm not sure how to do that now.
  private[this] val importPattern = """\s*import\s+idl\s+"([^"]+)"\s*;\s*""".r

  private[this] def getImports(file: File): Vector[File] = {
    val source = Source.fromFile(file)
    try {
      source.getLines().collect{
        case importPattern(currentImport) => new File(file.getParentFile, currentImport).getCanonicalFile
      }.toVector
    }
    finally source.close()
  }
}
