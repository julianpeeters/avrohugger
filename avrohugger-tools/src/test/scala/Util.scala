
import org.specs2.matcher.{ Expectable, ExpectationsCreation, MatchResult, Matcher }
import org.specs2.matcher.Matchers.typedEqualExpectation

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

  class LineEndingAmbiguousMatcher(left: String) extends Matcher[String] {
    def apply[S <: String](right: Expectable[S]): MatchResult[S] = {
      val leftAsList = if (left.contains("\r\n")) left.split("\r\n") else left.split("\n")
      val rightValue: String = right.value
      val rightAsList = if (rightValue.contains("\r\n")) rightValue.split("\r\n") else rightValue.split("\n")

      val res = leftAsList === rightAsList
      result(res.isSuccess, res.message, res.message, right)
    }
  }

  implicit class LineEndingAmbiguousMatcherString(s: String) extends ExpectationsCreation{
    def ===/(other: String): MatchResult[String] = createExpectable(s).applyMatcher(new LineEndingAmbiguousMatcher(other))
  }
  
}