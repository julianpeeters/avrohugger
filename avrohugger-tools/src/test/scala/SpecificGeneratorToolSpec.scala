

import avrohugger.tool.{Main, Directory, SpecificGeneratorTool}
import org.apache.avro.tool.Tool

import org.specs2._
import mutable._
import specification._
import scala.collection.JavaConversions._
import scala.util.Try

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;



/**
 * Verifies that the GeneratorTool generates Scala source properly
 */
class SpecificGeneratorToolSpec extends mutable.Specification {
  

  // Runs the actual generator tool with the given input args
  private def doCompile(args: Array[String])  = {
    val tool = new SpecificGeneratorTool();
    Try{
      tool.run(null, null, null, Arrays.asList((args:_*)));
    }
  }

  /**
   * Not the best implementation, but does the job. 
   * 
   */
  private def readFile(file: File) = {
  	Try {
      val reader: BufferedReader = new BufferedReader(new FileReader(file));
      val sb: StringBuilder = new StringBuilder();
      var line: String = null;
      var first: Boolean = true;
      line = reader.readLine()
      while (line != null) {
        if (!first) {
          sb.append("\n");
          first = false;
        }
        sb.append(line);
        line = reader.readLine()
      }
      sb.toString();
    }
  }

  "the generated Scala files" should {
/* //TODO in Java an interface is generated in addition to the types, how to represent in Scala? Trait?
    "match the expected single protocol file" in {
      doCompile(Array[String] ("protocol",
        Directory.TEST_INPUT_DIR.toString() + "/mail.avpr",
        Directory.TEST_OUTPUT_DIR.getPath()
      ));
      readFile(Directory.TEST_OUTPUT_MESSAGE) === readFile(Directory.TEST_EXPECTED_MESSAGE)
    }
*/
    "match the expected single datafile file" in {
      doCompile(Array[String] ("datafile",
        Directory.TEST_INPUT_DIR.toString() + "/twitter.avro",
        Directory.TEST_OUTPUT_SPECIFIC_DIR.getPath()
      ));
      readFile(Directory.TEST_OUTPUT_SPECIFIC_TWITTER) === readFile(Directory.TEST_EXPECTED_SPECIFIC_TWITTER)
    }

    "match the expected single schema file" in {
      doCompile(Array[String] ("schema",
        Directory.TEST_INPUT_DIR.toString() + "/nickname.avsc",
        Directory.TEST_OUTPUT_SPECIFIC_DIR.getPath()
      ));
      readFile(Directory.TEST_OUTPUT_SPECIFIC_NICKNAME) === readFile(Directory.TEST_EXPECTED_SPECIFIC_NICKNAME)
    }
   
    "match the expected dependent files" in {
      doCompile(Array[String]("schema",
        Directory.TEST_INPUT_DIR.toString() + "/nickname.avsc",
        Directory.TEST_INPUT_DIR.toString() + "/player.avsc",
        Directory.TEST_OUTPUT_SPECIFIC_DIR.getPath()
      ));
      readFile(Directory.TEST_OUTPUT_SPECIFIC_PLAYER)  === readFile(Directory.TEST_EXPECTED_SPECIFIC_PLAYER)   
    }

    "match the expected file and directory" in {
      doCompile(Array[String]("schema",
        Directory.TEST_INPUT_DIR.toString() + "/nickname.avsc",
        Directory.TEST_INPUT_DIR.toString(),
        Directory.TEST_OUTPUT_SPECIFIC_DIR.getPath()
      ));
      readFile(Directory.TEST_OUTPUT_SPECIFIC_PLAYER)  === readFile(Directory.TEST_EXPECTED_SPECIFIC_PLAYER)
    }

  }
}
