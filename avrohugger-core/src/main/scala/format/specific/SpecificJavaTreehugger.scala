package avrohugger
package format
package specific

import format.abstractions.JavaTreehugger
import stores.ClassStore
import treehugger.forest._
import definitions._
import treehuggerDSL._

import java.io.{BufferedWriter, File, FileNotFoundException, FileWriter, IOException}
import org.apache.avro.{Protocol, Schema}
import org.apache.avro.compiler.specific.SpecificCompiler

import java.nio.file.{FileSystems, Files, Path}

object SpecificJavaTreehugger extends JavaTreehugger {

  def asJavaCodeString(
    classStore: ClassStore,
    namespace: Option[String],
    schema: Schema): String = {

    // Avro's SpecificCompiler only writes files, but we need a string
    // so write the Java file and read
    val tempDir = createTempDir("avrohugger_specific")
    writeJavaTempFile(schema, tempDir)
    val tempPath = tempDir + "/" + schema.getFullName.replace('.','/') + ".java"
    val fileContents = scala.io.Source.fromFile(tempPath)
    val schemaPackage = "package " + schema.getNamespace
    val updatedPackage = namespace match {
      case Some(ns) => "package " + ns
      case None => ""
    }
    val schemaNamespace = """\"namespace\":\"""" +schema.getNamespace+ """\","""
    val updatedNamespace = namespace match {
      case Some(ns) => """\"namespace\":\"""" + ns + """\","""
      case None => ""
    }
    val codeString = fileContents.mkString
      .replace(schemaPackage, updatedPackage)
      .replace(schemaNamespace, updatedNamespace)
    fileContents.close
    deleteTempDirOnExit(tempDir)
    codeString
  }

}


