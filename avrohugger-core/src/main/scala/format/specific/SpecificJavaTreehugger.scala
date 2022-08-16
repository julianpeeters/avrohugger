package avrohugger
package format
package specific

import format.abstractions.JavaTreehugger
import stores.ClassStore
import org.apache.avro.Schema

object SpecificJavaTreehugger extends JavaTreehugger {

  def asJavaCodeString(
    classStore: ClassStore,
    namespace: Option[String],
    schema: Schema): String = {

    // Avro's SpecificCompiler only writes files, but we need a string
    // so write the Java file and read
    val tempDir = createTempDir("avrohugger_specific")
    writeJavaTempFile(schema, tempDir)
    val tempPath = s"$tempDir/${schema.getFullName.replace('.','/')}.java"
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


