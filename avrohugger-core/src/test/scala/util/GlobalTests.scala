package util

import avrohugger.format.abstractions.SourceFormat

import java.nio.file.{FileSystems, Path}

final case class AvrohuggerTest(
  inPath: Path,
  outputFiles: Seq[Path],
  description: String
) {
  def toSpec(sf: SourceFormat) = new AvrohuggerSpec(inPath, outputFiles, sf)
}

object GlobalTests {
  import scala.language.implicitConversions

  private implicit def stringToPath(s: String): Path = FileSystems.getDefault.getPath(s)
  private implicit def stringSeqToPath(comps: Seq[String]): Path = FileSystems.getDefault.getPath(comps mkString "/")

  val tests = Seq(
    AvrohuggerTest(
      "importedcomplex.avsc",
      Seq(
        Seq("model", "UnionRecord.scala"),
        Seq("model", "v2", "NestedRecord.scala"),
        Seq("test", "ComplexExternalDependency.scala")
      ),
      "correctly generate imports from complex types"
    )
  )
}
