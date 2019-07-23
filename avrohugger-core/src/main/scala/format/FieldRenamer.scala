package avrohugger
package format

object FieldRenamer {
  val RESERVED_WORDS: Set[String] = Set("abstract", "assert", "boolean", "break", "byte", "case", "catch",
    "char", "class", "const", "continue", "default", "do", "double",
    "else", "enum", "extends", "false", "final", "finally", "float",
    "for", "goto", "if", "implements", "import", "instanceof", "int",
    "interface", "long", "native", "new", "null", "package", "private",
    "protected", "public", "return", "short", "static", "strictfp",
    "super", "switch", "synchronized", "this", "throw", "throws",
    "transient", "true", "try", "void", "volatile", "while")

  def backtick(variable: String): String = s"`$variable`"

  def rename(fieldName: String): String = if(RESERVED_WORDS.contains(fieldName)) backtick(fieldName) else if (fieldName.endsWith("_")) backtick(fieldName) else fieldName
}
