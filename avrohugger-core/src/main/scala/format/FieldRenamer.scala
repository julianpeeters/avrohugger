package avrohugger
package format

object FieldRenamer {
  // Reserved words from https://www.scala-lang.org/files/archive/spec/2.13/01-lexical-syntax.html#identifiers
  private val RESERVED_WORDS: Set[String] = Set("abstract", "case", "catch", "class", "def", "do", "else", "extends", "final", "finally",
    "for", "forSome", "if", "implicit", "lazy", "macro", "match", "new", "object", "override", "package", "private", "protected", "return",
    "sealed", "super", "this", "throw", "trait", "try", "type", "unit", "val", "var", "while", "with", "yield")

  private def backtick(variable: String): String = s"`$variable`"

  private def isMangled(fieldName: String): Boolean = RESERVED_WORDS.contains(fieldName) || fieldName.endsWith("_")

  def rename(fieldName: String): String = if (isMangled(fieldName)) backtick(fieldName) else fieldName
}
