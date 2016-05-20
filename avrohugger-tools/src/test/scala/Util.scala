
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

object Util {
  
  /**
   * Not the best implementation, but does the job.
   *
   */
  def readFile(file: File): String = {
    var count = 0
    val maxTries = 3
    try {
      count += 1
      val reader: BufferedReader = new BufferedReader(new FileReader(file))
      val sb: StringBuilder = new StringBuilder()
      var line: String = null
      var first: Boolean = true
      line = reader.readLine()
      while (line != null) {
        if (!first) {
          sb.append("\n")
          first = false
        }
        sb.append(line)
        line = reader.readLine()
      }
      val contents = sb.toString()
      // if file is empty, try again, it should be there
      if (contents.isEmpty && (count < maxTries)) readFile(file)
      else contents
    } catch { // if file is not found, try again, it should be there
      case e: java.io.FileNotFoundException => {
        if (count < maxTries) readFile(file)
        else sys.error("File to found: " + file)
      }
    }
  }
  
  
}