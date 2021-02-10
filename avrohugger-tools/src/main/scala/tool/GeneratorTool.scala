package avrohugger
package tool

import format.abstractions.SourceFormat
import avrohugger.filesorter.AvscFileSorter
import types.AvroScalaTypes

import org.apache.avro.tool.Tool
import org.apache.avro.generic.GenericData.StringType;
import org.apache.avro.Schema;
import org.apache.avro.compiler.specific.SpecificCompiler;
import org.apache.avro.Protocol;

import java.io.File
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.List;

import scala.jdk.CollectionConverters._
import scala.util.Try

/**
 * A Tool for generating Scala case classes from schemas
 * Adapted from https://github.com/apache/avro/blob/branch-1.7/lang/java/tools/src/main/java/org/apache/avro/tool/SpecificCompilerTool.java
 */
class GeneratorTool(sourceFormat: SourceFormat,
  avroScalaCustomTypes: Option[AvroScalaTypes] = None,
  avroScalaCustomNamespace: Map[String, String] = Map.empty) extends Tool {

  val generator = new Generator(sourceFormat, avroScalaCustomTypes, avroScalaCustomNamespace)

  @Override
  def run(in: InputStream, out: PrintStream, err: PrintStream, args: List[String]): Int = {
    if (args.size() < 3) {
      System.err
          .println("Usage: [-string] (schema|protocol|datafile) input... outputdir");
      System.err
          .println(" input - input files or directories");
      System.err
          .println(" outputdir - directory to write generated scala");
      System.err.println(" -string - use java.lang.String instead of Utf8");
      1;
    }

    var stringType: StringType = StringType.CharSequence;

    var arg = 0;
    if ("-string".equals(args.get(arg))) {
      stringType = StringType.String;
      arg+=1;
    }

    val method: String = args.get(arg);
    var inputs: List[File] = new ArrayList[File]();

    for (i <- (arg + 1) until (args.size() - 1)) {
      Try {
         inputs.add(new File(args.get(i)));
      }
    }

    if ("datafile".equals(method)) {
      for (src: File <- determineInputs(inputs, DATAFILE_FILTER)) {
        generator.fileToFile(src, args.asScala.last)
      }
    } else if ("schema".equals(method)) {
      for (src: File <- AvscFileSorter.sortSchemaFiles(determineInputs(inputs, SCHEMA_FILTER))) {
        generator.fileToFile(src, args.asScala.last)
      }
    }
    else if ("protocol".equals(method)) {
      for (src: File <- determineInputs(inputs, PROTOCOL_FILTER)) {
        generator.fileToFile(src, args.asScala.last)
      }
    }
    else {
      sys.error("Expected \"datafile\", \"schema\" or \"protocol\".");
      1;
    }
    0;
  }

  @Override
  def getName: String = generator.format.toolName;

  @Override
  def getShortDescription: String = generator.format.toolShortDescription;

  /**
   * For a List of files or directories, returns a File[] containing each file
   * passed as well as each file with a matching extension found in the directory.
   *
   * @param inputs List of File objects that are files or directories
   * @param filter File extension filter to match on when fetching files from a directory
   * @return Unique array of files
   */
  private def determineInputs(inputs: List[File], filter: FilenameFilter): Array[File] = {
    val fileSet: Set[File] = new LinkedHashSet[File](); // preserve order and uniqueness
    for (file: File <- inputs.asScala) {
      // if directory, look at contents to see what files match extension
      if (file.isDirectory()) {
        for (f: File <- file.listFiles(filter)) {
          fileSet.add(f);
        }
      }
      // otherwise, just add the file.
      else {
        fileSet.add(file);
      }
    }
    if (fileSet.size() > 0) {
      System.err.println("Input files to compile:");
      for (file: File <- fileSet.asScala) {
        System.err.println("  " + file);
      }
    }
    else {
      System.err.println("No input files found.");
    }
    fileSet.asScala.toArray[File]
  }

  val SCHEMA_FILTER: FileExtensionFilter  =
    new FileExtensionFilter("avsc");
  val PROTOCOL_FILTER: FileExtensionFilter =
    new FileExtensionFilter("avpr");
  val DATAFILE_FILTER: FileExtensionFilter =
    new FileExtensionFilter("avro");

  case class FileExtensionFilter(extension: String) extends FilenameFilter {
    @Override
    def accept(dir: File, name: String) = {
      name.endsWith(this.extension);
    }
  }
}
