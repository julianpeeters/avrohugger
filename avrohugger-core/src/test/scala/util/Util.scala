package util

object Util {
  
  def readFile(fileName: String): String = {
    
    var count = 0
    val maxTries = 3
    try { // if file is empty, try again, it should be there
      count += 1
      val contents: String = scala.io.Source.fromFile(fileName).mkString
      if (contents.isEmpty && (count < maxTries)) readFile(fileName)
      else contents
    } catch { // if file is not found, try again, it should be there
      case e: java.io.FileNotFoundException => {
        if (count < maxTries) readFile(fileName)
        else sys.error("File to found: " + fileName)
      }
    }

    
  }
  
}