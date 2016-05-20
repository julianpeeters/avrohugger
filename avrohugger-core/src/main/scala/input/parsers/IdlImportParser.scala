package avrohugger
package input
package parsers

import org.apache.avro.{ Protocol, Schema }

import java.io.File

object IdlImportParser {
  
  def stripComments(fileContents: String): String = {
    val multiLinePattern = """/\*.*\*/""".r
    val singleLinePattern = """//.*$""".r
    val noSingleLines = singleLinePattern.replaceAllIn(fileContents, "")
    val commentFree = multiLinePattern.replaceAllIn(noSingleLines, "")
    commentFree
  }

  def getImportedFiles(infile: File): List[File] = {
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
    val path = infile.getParent + "/"
    val contents = readFile(infile)
    val avdlPattern = """import[ \t]+idl[ \t]+"([^"]*\.avdl)"[ \t]*;""".r
    val avprPattern = """import[ \t]+protocol[ \t]+"([^"]*\.avpr)"[ \t]*;""".r
    val avscPattern = """import[ \t]+schema[ \t]+"([^"]*\.avsc)"[ \t]*;""".r
    val idlMatches = avdlPattern.findAllIn(contents).matchData.toList
    val protocolMatches = avprPattern.findAllIn(contents).matchData.toList
    val schemaMatches = avscPattern.findAllIn(contents).matchData.toList
    val importMatches = idlMatches ::: protocolMatches ::: schemaMatches
    val importedFiles = importMatches.map(m => new File(path + m.group(1)))
    importedFiles
  }
  
}
