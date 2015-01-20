package avrohugger
package tool


import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import java.io.InputStream;

//import org.apache.avro.tool.Tool

import org.apache.avro.tool.Tool
//import org.apache.avro.reflect.InduceSchemaTool;
//import org.apache.avro.specific.SpecificCompiler.SpecificCompilerTool;

import scala.util.Try
import scala.collection.JavaConversions._


/** Command-line driver.*/
class Main {
  /**
   * Available tools, initialized in constructor.
   */
  //final Map[String, Tool] tools;

  var maxLen = 0;

    
  val tools = new TreeMap[String, Tool]();
  for (tool <- Array[Tool] {
      new GeneratorTool()
      }) {
    var prev: Tool = tools.put(tool.getName(), tool);
    if (prev != null) {
      throw new AssertionError(
          "Two tools with identical names: " + tool + ", " + prev);
    }
    maxLen = Math.max(tool.getName().length(), maxLen);
  }
  
/*
  public static void main(String[] args) throws Exception {
    int rc = new Main().run(args);
    System.exit(rc);
  }
*/
  def main(args: Array[String]) = {
    run(args)
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
    System.err.print("Version ");
    StreamPrinter.printStream(this.getClass.getClassLoader().getResourceAsStream("VERSION.txt"));
    System.err.print(" of ");
    StreamPrinter.printStream(this.getClass.getClassLoader().getResourceAsStream("NOTICE.txt"));
    System.err.println("----------------");

    System.err.println("Available tools:");
    for (k <- tools.values()) {
      System.err.printf("%" + maxLen + "s  %s\n", k.getName(), k.getShortDescription());
    }

    1;
  }
}

object StreamPrinter {
 // private static void printStream(InputStream in) throws Exception {
  def printStream(in: InputStream) = {
    val buffer: Array[Byte] = new Array[Byte](1024);
    //for (int i = in.read(buffer); i != -1; i = in.read(buffer))
    var i = in.read(buffer)
    while (i != -1) Try {
      	in.read(buffer)
        System.err.write(buffer, 0, i);
      }
  }
}