package avrohugger
package format
package abstractions

import stores.ClassStore
import org.apache.avro.Schema
import org.apache.avro.compiler.specific.SpecificCompiler

import java.io.{BufferedWriter, File, FileNotFoundException, FileWriter, IOException}
import java.nio.file.{FileSystems, Files, Path, Paths}
import java.util.Comparator
import scala.tools.nsc

/** Parent to all ouput format treehuggers
  * Note: Java required for generating Enums (Specific API requires Java enums)
  * _ABSTRACT MEMBERS_: to be implemented by a subclass
  * asJavaCodeString
  */
trait JavaTreehugger {

  def asJavaCodeString(
    classStore: ClassStore,
    namespace: Option[String],
    schema: Schema): String

  def writeJavaTempFile(
    schema: Schema,
    outDir: File): Unit = {
    // Uses Avro's SpecificCompiler, which only compiles from files, thus we
    // write the schema to a temp file so we can compile a Java enum from it.
    val tempSchemaFile = File.createTempFile(outDir + "/" + schema.getName, ".avsc")
    tempSchemaFile.deleteOnExit()
    val out = new BufferedWriter(new FileWriter(tempSchemaFile))
    out.write(schema.toString)
    out.close()

    try {
      SpecificCompiler.compileSchema(tempSchemaFile, outDir)
    }
    catch {
      case ex: FileNotFoundException =>
        sys.error("File not found:" + ex)
      case ex: IOException =>
        sys.error("There was a problem using the file: " + ex)
    }
  }

  protected def createTempDir(prefix: String): File =
    Files.createTempDirectory(
      FileSystems.getDefault.getPath(System.getProperty("java.io.tmpdir")),
      prefix
    ).toFile

  protected def deleteTempDirOnExit(tempDir: File): Unit =
    Files.walk(Paths.get(tempDir.getPath))
      .sorted(Comparator.reverseOrder[Path]())
      .map(_.toFile)
      .forEach(_.deleteOnExit())

}
