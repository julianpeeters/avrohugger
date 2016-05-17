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

  def getImportedFiles(infile: java.io.File): List[File] = {
    val path = infile.getParent + "/"
    val file = scala.io.Source.fromFile(infile)
    val contents = stripComments(file.getLines.mkString)
    file.close
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
