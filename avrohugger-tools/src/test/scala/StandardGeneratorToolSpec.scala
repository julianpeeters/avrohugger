
import avrohugger.format.{AvroString, Standard}
import avrohugger.tool.{Directory, GeneratorTool, Main}
import org.apache.avro.generic.GenericData.StringType
import org.apache.avro.tool.Tool
import org.specs2._

import scala.collection.JavaConverters._
import scala.util.Try

/**
 * Verifies that the GeneratorTool generates Scala source properly
 */
class StandardGeneratorToolSpec extends mutable.Specification {

  // Runs the actual generator tool with the given input args
  private def doCompile(args: List[String])  = {
    val tool = new GeneratorTool(Standard)
    Try{
      tool.run(null, null, null, Seq(args:_*).asJava)
    }
  }
    
  "match the expected single protocol file" in {
    doCompile(List[String] ("protocol",
      Directory.TEST_INPUT_DIR + "/mail.avpr",
      Directory.TEST_OUTPUT_BASE_DIR
    ))
    Util.readFile(Directory.TEST_OUTPUT_MESSAGE) === Util.readFile(Directory.TEST_EXPECTED_MESSAGE)
  }
  
  "match the expected single datafile file" in {
    doCompile(List[String] ("datafile",
      Directory.TEST_INPUT_DIR + "/twitter.avro",
      Directory.TEST_OUTPUT_BASE_DIR
    ))
    Util.readFile(Directory.TEST_OUTPUT_TWITTER) === Util.readFile(Directory.TEST_EXPECTED_TWITTER)
  }
  
  "match the expected single schema file" in {
    doCompile(List[String] ("schema",
      Directory.TEST_INPUT_DIR + "/handle.avsc",
      Directory.TEST_OUTPUT_BASE_DIR
    ))
    Util.readFile(Directory.TEST_OUTPUT_HANDLE) === Util.readFile(Directory.TEST_EXPECTED_HANDLE)
  }
  
  "match the expected dependent files" in {
    doCompile(List[String]("schema",
      Directory.TEST_INPUT_DIR + "/handle.avsc",
      Directory.TEST_INPUT_DIR + "/pilot.avsc",
      Directory.TEST_OUTPUT_BASE_DIR
    ))
    Util.readFile(Directory.TEST_OUTPUT_HANDLE) === Util.readFile(Directory.TEST_EXPECTED_HANDLE)
    Util.readFile(Directory.TEST_OUTPUT_PILOT)  === Util.readFile(Directory.TEST_EXPECTED_PILOT)
  }
  
  "match the expected file and directory" in {
    doCompile(List[String]("schema",
      Directory.TEST_INPUT_DIR + "/mascot.avsc",
      Directory.TEST_INPUT_DIR,
      Directory.TEST_OUTPUT_BASE_DIR
    ))
    Util.readFile(Directory.TEST_OUTPUT_MASCOT) === Util.readFile(Directory.TEST_EXPECTED_MASCOT)
    Util.readFile(Directory.TEST_OUTPUT_WRESTLER)  === Util.readFile(Directory.TEST_EXPECTED_WRESTLER)
  }
/* currently -string makes no difference, all case classes use String
  "match the expected using the -string option" in {
    doCompile(List[String]("-string", "schema",
      Directory.TEST_INPUT_DIR + "/nickname.avsc",
      Directory.TEST_INPUT_DIR + "/player.avsc",
      Directory.TEST_INPUT_DIR + "/twitter_schema.avro",
      Directory.TEST_OUTPUT_STRING_BASE_DIR
    ))
    Util.readFile(Directory.TEST_OUTPUT_STRING_PLAYER) === Util.readFile(Directory.TEST_EXPECTED_STRING_PLAYER)
  }
*/
}