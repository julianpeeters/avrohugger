package avrohugger
package tool

import format.abstractions.SourceFormat
import format.{Scavro, SpecificRecord, Standard}
import java.util.Arrays
import java.util.Map
import java.util.TreeMap
import java.io.{InputStream, PrintStream}

import org.apache.avro.tool.Tool

import scala.util.{Failure, Success, Try}
import scala.jdk.CollectionConverters._


/** Command-line driver.*/
class Runner(in: InputStream, out: PrintStream, err: PrintStream) {

  var maxLen = 0;

  /**
   * Available tools, initialized in constructor.
   */
  val toolsMap: Map[String, Tool] = new TreeMap[String, Tool]();
  val formats = Array[SourceFormat](Standard, SpecificRecord, Scavro)
  val tools = formats.map(format => new GeneratorTool(format))
  for (tool <- tools.toArray[Tool]) {
    var prev: Tool = toolsMap.put(tool.getName(), tool);
    if (prev != null) {
      throw new AssertionError(
          "Two toolsMap with identical names: " + tool + ", " + prev);
    }
    maxLen = Math.max(tool.getName().length(), maxLen);
  }

  /**
    * Delegates to tool specified on the command-line.
    */
  def run(args: Array[String]): Int = {
    if (args.length != 0) {
      val tool: Tool = toolsMap.get(args(0))
      if (tool != null) {
        val result = Try {
          tool.run(
            in, out, err, Arrays.asList(args: _*).subList(1, args.length))
        }
        result match {
          case Success(0) => 0
          case Success(exitCode) =>
            err.println("Tool " + args(0) + " failed with exit code " + exitCode)
            exitCode
          case Failure(e) =>
            err.println("Tool " + args(0) + " failed: " + e.toString)
            1
        }
      } else {
        err.println("Unknown tool: " + args(0))
        1
      }
    } else {
      err.println("----------------")

      err.println("Available tools:")
      for (k <- toolsMap.asScala.values) {
        err.printf("%" + maxLen + "s  %s\n", k.getName(), k.getShortDescription())
      }

      1
    }
  }
}
