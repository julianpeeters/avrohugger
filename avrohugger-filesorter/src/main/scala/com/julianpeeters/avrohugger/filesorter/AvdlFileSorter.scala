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
  def sortSchemaFiles(
    filesTraversable: Traversable[File],
    classLoader: ClassLoader): Seq[File] = {

    @tailrec def getImportsMap(
      files: List[File],
      classLoader: ClassLoader,
      acc: Map[File, List[File]]): Map[File, List[File]] =
      files match {
        case h::t =>
          val map = files.map{ file => (file.getCanonicalFile, getImports(file, classLoader)) }.toMap
          getImportsMap(
            map.values.toList.flatten.filter(!map.keys.toList.contains(_)),
            classLoader,
            acc ++ map
          )
        case Nil =>
          acc
      }
    
    val importsMap = getImportsMap(filesTraversable.toList, classLoader, Map.empty)


    @tailrec def addFiles(processedFiles: Seq[File], remainingFiles: List[File]): Seq[File] = {
      remainingFiles match {
        case Nil => processedFiles
        case h :: t =>
          val processedFilesSet = processedFiles.toSet
          if (importsMap(h).forall(processedFilesSet.contains))
            addFiles(processedFiles :+ h, t)
          else
            {
              addFiles(processedFiles, t :+ h)
            }
      }
    }
    
    val result = addFiles(Seq.empty, importsMap.keys.toList)
    result
  }

  // for parsing idls to extract imported files (need to avoid commented-out imports)
  private[this] def stripComments(fileContents: String): String = {
    val multiLinePattern = """/\*.*\*/""".r
    val singleLinePattern = """//.*$""".r
    val noSingleLines = singleLinePattern.replaceAllIn(fileContents, "")
    val commentFree = multiLinePattern.replaceAllIn(noSingleLines, "")
    commentFree
  }
  
  // TODO This should be replaced by letting AVRO compile the IDL files directly,
  // and getting the imports from there, but I'm not sure if that's possible.
  private[this] def getImports(infile: File, classLoader: ClassLoader): List[File] = {
    def readFile(file: File): String = {
      var count = 0
      val maxTries = 3
      try {
        count += 1
        val file = scala.io.Source.fromFile(infile)
        val fileContents: String = stripComments(file.mkString)
        file.close
        // if file is empty, try again, it was there when we read idl
        if (fileContents.isEmpty && (count < maxTries)) readFile(infile)
        else fileContents
      } catch {// if file is not found, try again, it was there when we read idl
        case e: java.io.FileNotFoundException => {
          if (count < maxTries) readFile(infile)
          else sys.error("File to found: " + infile)
        }
      }
    }
    val importingPath = infile.getParent + "/"
    val contents = readFile(infile)
    val avdlPattern = """import[ \t]+idl[ \t]+"([^"]*\.avdl)"[ \t]*;""".r
    val avprPattern = """import[ \t]+protocol[ \t]+"([^"]*\.avpr)"[ \t]*;""".r
    val avscPattern = """import[ \t]+schema[ \t]+"([^"]*\.avsc)"[ \t]*;""".r
    val idlMatches = avdlPattern.findAllIn(contents).matchData.toList
    val protocolMatches = avprPattern.findAllIn(contents).matchData.toList
    val schemaMatches = avscPattern.findAllIn(contents).matchData.toList
    val importMatches = idlMatches ::: protocolMatches ::: schemaMatches
    val importedFiles = importMatches.map(m => {
      val name = m.group(1)
      val file = new File(importingPath + name)
      if (file.exists()) file
      else new File(classLoader.getResource(name).getFile)
    })
    importedFiles.map(file => file.getCanonicalFile)
  }
}
