package avrohugger
package input
package parsers

object ScalaDocParser {

  val scalaDocRegex = """/\*([^*]|[\r\n]|(\*+([^*/]|[\r\n])))*\*+/""".r
  
  def getScalaDocs(compUnits: List[String]): List[List[Option[String]]] = {
    def extractDoc(maybeDoc: String) = scalaDocRegex.findFirstIn(maybeDoc)
    compUnits.map(compilationUnit => {
      compilationUnit.split("class|object").dropRight(1).toList.map(extractDoc)
    })
  }

  def getEntries(scalaDoc: String) = {
    val rawDocContents = scalaDoc.replaceAll("""/\*|\*/""", "")
    val unCommented = rawDocContents.replaceAll("""\* *""", "")
    val docEntries = unCommented.split("@").toList.map(comment => comment.trim)
    docEntries
  }

  // conversion from Option to String/null is for compatibility with Apache Avro
  def docEntries(maybeScalaDoc: Option[String]) = maybeScalaDoc match {
    case Some(scalaDoc) => ScalaDocParser.getEntries(scalaDoc)
    case None => List.empty
  }

  val paramFilter = (docEntry: String) => docEntry.startsWith("param")

  def paramEntries(maybeScalaDoc: Option[String]) = {
    docEntries(maybeScalaDoc).filter(paramFilter)
  }

  def asParamTuple(paramEntry: String) = {
    val doc = paramEntry.replaceAll("param ", "").split(" ", 2)
    (doc(0), doc(1))
  }

  def asParamsMap(paramEntries: List[String]) = {
    paramEntries.map(paramEntry => asParamTuple(paramEntry)).toMap
  }

  def asMap(paramEntries: List[String]): Map[String, String] = {
    paramEntries match {
      case Nil => Map.empty
      case paramEntries => asParamsMap(paramEntries)
    }
  }

  def fieldDocsMap(maybeScalaDoc: Option[String]): Map[String, String] = {
    asMap(paramEntries(maybeScalaDoc))
  }

  // conversion from Option to String/null is for compatibility with Apache Avro
  def getTopLevelDoc(maybeScalaDoc: Option[String]) = {
    docEntries(maybeScalaDoc).filterNot(paramFilter) match {
      case Nil => null
      case doc => doc.mkString("\n")
    }
  }

}
