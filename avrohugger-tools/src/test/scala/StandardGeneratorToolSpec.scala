
import avrohugger.format.Standard

import avrohugger.tool.{Main, Directory, GeneratorTool}
import org.apache.avro.tool.Tool

import org.specs2._
import mutable._
import specification._
import scala.collection.JavaConversions._
import scala.util.Try

/**
 * Verifies that the GeneratorTool generates Scala source properly
 */
class StandardGeneratorToolSpec extends mutable.Specification {


  // Runs the actual generator tool with the given input args
  private def doCompile(args: List[String])  = {
    val tool = new GeneratorTool(Standard)
    Try{
      tool.run(null, null, null, Seq(args:_*))
    }
  }


  "the generated Scala files" should {
    
    "match the expected single protocol file" in {
      doCompile(List[String] ("protocol",
        Directory.TEST_INPUT_DIR.toString() + "/mail.avpr",
        Directory.TEST_OUTPUT_BASE_DIR.getPath()
      ))
      Util.readFile(Directory.TEST_OUTPUT_MESSAGE) === Util.readFile(Directory.TEST_EXPECTED_MESSAGE)
    }

    "match the expected single datafile file" in {
      doCompile(List[String] ("datafile",
        Directory.TEST_INPUT_DIR.toString() + "/twitter.avro",
        Directory.TEST_OUTPUT_BASE_DIR.getPath()
      ))
      Util.readFile(Directory.TEST_OUTPUT_TWITTER) === Util.readFile(Directory.TEST_EXPECTED_TWITTER)
    }

    "match the expected single schema file" in {
      doCompile(List[String] ("schema",
        Directory.TEST_INPUT_DIR.toString() + "/handle.avsc",
        Directory.TEST_OUTPUT_BASE_DIR.getPath()
      ))
      Util.readFile(Directory.TEST_OUTPUT_HANDLE) === Util.readFile(Directory.TEST_EXPECTED_HANDLE)
    }

    "match the expected dependent files" in {
      doCompile(List[String]("schema",
        Directory.TEST_INPUT_DIR.toString() + "/handle.avsc",
        Directory.TEST_INPUT_DIR.toString() + "/pilot.avsc",
        Directory.TEST_OUTPUT_BASE_DIR.getPath()
      ))
      Util.readFile(Directory.TEST_OUTPUT_HANDLE) === Util.readFile(Directory.TEST_EXPECTED_HANDLE)
      Util.readFile(Directory.TEST_OUTPUT_PILOT)  === Util.readFile(Directory.TEST_EXPECTED_PILOT)
    }

    "match the expected file and directory" in {
      doCompile(List[String]("schema",
        Directory.TEST_INPUT_DIR.toString() + "/handle.avsc",
        Directory.TEST_INPUT_DIR.toString(),
        Directory.TEST_OUTPUT_BASE_DIR.getPath()
      ))
      Util.readFile(Directory.TEST_OUTPUT_HANDLE) === Util.readFile(Directory.TEST_EXPECTED_HANDLE)
      Util.readFile(Directory.TEST_OUTPUT_PILOT)  === Util.readFile(Directory.TEST_EXPECTED_PILOT)
    }
/* currently -string makes no difference, all case classes use String
    "match the expected using the -string option" in {
      doCompile(List[String]("-string", "schema",
        Directory.TEST_INPUT_DIR.toString() + "/nickname.avsc",
        Directory.TEST_INPUT_DIR.toString() + "/player.avsc",
        Directory.TEST_INPUT_DIR.toString() + "/twitter_schema.avro",
        Directory.TEST_OUTPUT_STRING_BASE_DIR.getPath()
      ))
      Util.readFile(Directory.TEST_OUTPUT_STRING_PLAYER) === Util.readFile(Directory.TEST_EXPECTED_STRING_PLAYER)
    }
*/
  }
}
