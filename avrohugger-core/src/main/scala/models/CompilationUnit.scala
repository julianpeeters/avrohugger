package avrohugger
package models

import java.nio.file.Path

case class CompilationUnit(maybeFilePath: Option[Path], codeString: String)