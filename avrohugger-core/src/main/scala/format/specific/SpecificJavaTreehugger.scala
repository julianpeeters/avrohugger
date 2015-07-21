package avrohugger
package format
package specific

import treehugger.forest._
import definitions._
import treehuggerDSL._

import java.io.{File, BufferedWriter, FileWriter, FileNotFoundException, IOException}
import java.nio.file.Path

import org.apache.avro.Schema
import org.apache.avro.compiler.specific.SpecificCompiler

import scala.collection.JavaConversions._

object SpecificJavaTreehugger {

  def asJavaCodeString(classStore: ClassStore, namespace: Option[String], schema: Schema) = {

    def writeJavaTempFile(namespace: Option[String], schema: Schema, outDir: String): Unit = {
	    // Uses Avro's SpecificCompiler, which only compiles from files, 
	    // thus we write the schema to a temp file so we can compile a Java enum from it.
	    val tempSchemaFile = File.createTempFile(schema.getName, ".avsc");
	    tempSchemaFile.deleteOnExit();
	    val out = new BufferedWriter(new FileWriter(tempSchemaFile));
	    out.write(schema.toString);
	    out.close();

	    val folderPath = {
	      if (namespace.isDefined) new File(outDir)
	      else new File(outDir) 
	    }
	    try { 
	      SpecificCompiler.compileSchema(tempSchemaFile, folderPath)
	    }     
	    catch {
	      case ex: FileNotFoundException => sys.error("File not found:" + ex)
	      case ex: IOException => sys.error("There was a problem using the file: " + ex)
	    }
	  }

    val javaSymbol = RootClass.newClass(schema.getName)
    classStore.accept(schema, javaSymbol)
    // Avro's SpecificCompiler only writes files, but we need a string
    // so write the Java file and read
    val tempOutDir = "target/"
    writeJavaTempFile(namespace, schema, tempOutDir)
    val tempFilePath = tempOutDir + schema.getFullName.replace('.','/') + ".java"
    val codeString = scala.io.Source.fromFile(tempFilePath).mkString
    new File(tempFilePath).delete // TODO: delete directories as well as files
    codeString
  
  }
}
