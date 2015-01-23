package avrohugger
package tool

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.io.InputStream;

import org.apache.avro.tool.Tool

import scala.util.Try
import scala.collection.JavaConversions._


/** Command-line driver.*/
class Runner {// extends App{

  var maxLen = 0;

  /**
   * Available tools, initialized in constructor.
   */
  val tools = new TreeMap[String, Tool]();
  for (tool <- Array[Tool] {
      new GeneratorTool()//,
     // new SpecificGeneratorTool() 
      }) {
    var prev: Tool = tools.put(tool.getName(), tool);
    if (prev != null) {
      throw new AssertionError(
          "Two tools with identical names: " + tool + ", " + prev);
    }
    maxLen = Math.max(tool.getName().length(), maxLen);
  }

  /**
   * Delegates to tool specified on the command-line.
   */
  def run(args: Array[String]) = {
    if (args.length != 0) {
      val tool: Tool = tools.get(args(0));
      if (tool != null) {
      	Try {
          tool.run(
            System.in, System.out, System.err, Arrays.asList(args: _*).subList(1, args.length));
        }
      }
    }
   
    System.err.println("----------------");

    System.err.println("Available tools:");
    for (k <- tools.values()) {
      System.err.printf("%" + maxLen + "s  %s\n", k.getName(), k.getShortDescription());
    }

    1;
  }
}

