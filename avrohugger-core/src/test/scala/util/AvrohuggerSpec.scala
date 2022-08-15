package util

import avrohugger._
import avrohugger.format._
import avrohugger.format.abstractions.SourceFormat
import org.specs2.matcher.{Matchers, ShouldExpectable}

import java.io.File
import java.nio.file.{FileSystems, Path}
import scala.io.Source

class AvrohuggerSpec(
  inPath: Path,
  val outputFiles: Seq[Path],
  sourceFormat: SourceFormat
) extends Matchers {
  implicit class PathExtensions(
    path: Path
  ) {
    def ++(next: String) = path.resolve(next)
    def ++(other: Path) = path.resolve(other)
  }

  val sourceFormatName = sourceFormat match {
    case SpecificRecord => "specific"
    case Standard => "standard"
    case Scavro => "scavro"
  }

  val gen = new Generator(sourceFormat)
  val inputPath = {
    val sourceBase = FileSystems.getDefault.getPath("avrohugger-core", "src", "test", "avro")
    (sourceBase ++ inPath)
  }
  val inputFile = inputPath.toFile
  val outDir = gen.defaultOutputDir + s"/$sourceFormatName/"

  private def readFile(f: File): String = {
    val source = Source.fromFile(f)
    try source.mkString finally source.close()
  }

  val expectedBase = FileSystems.getDefault.getPath("avrohugger-core", "src", "test", "expected", sourceFormatName)
  val generatedBase = FileSystems.getDefault.getPath("target", "generated-sources", sourceFormatName)

  private def prefixedFileString(prefixPath: Path, p: Path) = {
    val fullPath = sourceFormat match {
      case Scavro => {
        Option(p.getParent) match {
          case Some(parent) => parent ++ "model" ++ p.getFileName
          case None => FileSystems.getDefault.getPath("model") ++ p
        }
      }
      case _ => p
    }
    readFile((prefixPath ++ fullPath).toFile)
  }

  def generatedString(p: Path) = prefixedFileString(generatedBase, p)
  def expectedString(p: Path) = prefixedFileString(expectedBase, p)

  def checkFileToFile = {
    gen.fileToFile(inputFile, outDir)

    val generated = outputFiles map generatedString
    val expected = outputFiles map expectedString

    ShouldExpectable(generated) shouldEqual expected
  }

  def checkFileToStrings = {
    val generated = gen.fileToStrings(inputFile)
    val expected = outputFiles map expectedString

    ShouldExpectable(generated) shouldEqual expected
  }

  def checkStringToFile = {
    val inputString = readFile(inputFile)
    gen.stringToFile(inputString, outDir)

    val generated = outputFiles map generatedString
    val expected = outputFiles map expectedString

    ShouldExpectable(generated) shouldEqual expected
  }

  def checkStringToStrings = {
    val generated = {
      val inputString = readFile(inputFile)
      gen.stringToStrings(inputString)
    }

    val expected = outputFiles map expectedString

    ShouldExpectable(generated) shouldEqual expected
  }
}
