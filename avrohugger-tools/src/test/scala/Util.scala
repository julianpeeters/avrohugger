
import org.specs2.matcher.{ Expectable, ExpectationsCreation, Matcher }

object Util {
  
  def readFile(fileName: String, maxTries: Int = 3): String = {
    def readFile0(count: Int): String = {
      try { // if file is empty, try again, it should be there
        val source = scala.io.Source.fromFile(fileName)
        val contents: String = source.mkString
        source.close()
        if (contents.isEmpty && (count < maxTries)) readFile0(count + 1)
        else contents
      } catch { // if file is not found, try again, it should be there
        case e: Throwable =>
          if (count < maxTries) readFile0(count + 1)
          else sys.error("File not found: " + fileName)
      }
    }
    readFile0(0)
  }
  
}