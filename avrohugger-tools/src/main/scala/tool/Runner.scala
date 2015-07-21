package avrohugger
package tool
import format.{ SourceFormat, Standard, SpecificRecord }

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.io.InputStream;

import org.apache.avro.tool.Tool

import scala.util.Try
import scala.collection.JavaConversions._


/** Command-line driver.*/
class Runner {

  var maxLen = 0;

  /**
   * Available tools, initialized in constructor.
   */
  val toolsMap = new TreeMap[String, Tool]();
  val formats = Array[SourceFormat](Standard, SpecificRecord)
  val tools = formats.map(format => new GeneratorTool(format))
  for (tool <- Array[Tool](tools:_*)) {
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
  def run(args: Array[String]) = {
    if (args.length != 0) {
      val tool: Tool = toolsMap.get(args(0));
      if (tool != null) {
      	Try {
          tool.run(
            System.in, System.out, System.err, Arrays.asList(args: _*).subList(1, args.length));
        }
      }
    }
   
    System.err.println("----------------");

    System.err.println("Available tools:");
    for (k <- toolsMap.values()) {
      System.err.printf("%" + maxLen + "s  %s\n", k.getName(), k.getShortDescription());
    }

    1;
  }
}

