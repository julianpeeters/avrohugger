
import avrohugger.format.Scavro
import avrohugger.tool.{Main, Directory, GeneratorTool}
import org.apache.avro.tool.Tool

import org.specs2._
import scala.collection.JavaConverters._
import scala.util.Try

/**
 * Verifies that the GeneratorTool generates Scala source properly
 */
class ScavroGeneratorToolSpec extends mutable.Specification {


  // Runs the actual generator tool with the given input args
  private def doCompile(args: Array[String])  = {
    val tool = new GeneratorTool(Scavro)
    Try{
      tool.run(null, null, null, Seq(args:_*).asJava)
    }
  }
  
  "match the expected single protocol file" in {
    doCompile(Array[String] ("protocol",
      Directory.TEST_INPUT_DIR + "/mail.avpr",
      Directory.TEST_OUTPUT_SCAVRO_BASE_DIR
    ))
    Util.readFile(Directory.TEST_OUTPUT_SCAVRO_MESSAGE) === Util.readFile(Directory.TEST_EXPECTED_SCAVRO_MESSAGE)
  }

  "match the expected single datafile file" in {
    doCompile(Array[String] ("datafile",
      Directory.TEST_INPUT_DIR + "/twitter.avro",
      Directory.TEST_OUTPUT_SCAVRO_BASE_DIR
    ))
    Util.readFile(Directory.TEST_OUTPUT_SCAVRO_TWITTER) === Util.readFile(Directory.TEST_EXPECTED_SCAVRO_TWITTER)
  }

  "match the expected single schema file" in {
    doCompile(Array[String] ("schema",
      Directory.TEST_INPUT_DIR + "/nickname.avsc",
      Directory.TEST_OUTPUT_SCAVRO_BASE_DIR
    ))
    Util.readFile(Directory.TEST_OUTPUT_SCAVRO_NICKNAME) === Util.readFile(Directory.TEST_EXPECTED_SCAVRO_NICKNAME)
  }

  "match the expected dependent files" in {
    doCompile(Array[String]("schema",
      Directory.TEST_INPUT_DIR + "/nickname.avsc",
      Directory.TEST_INPUT_DIR + "/player.avsc",
      Directory.TEST_OUTPUT_SCAVRO_BASE_DIR
    ))
    Util.readFile(Directory.TEST_OUTPUT_SCAVRO_PLAYER)  === Util.readFile(Directory.TEST_EXPECTED_SCAVRO_PLAYER)
  }

  "match the expected file and directory" in {
    doCompile(Array[String]("schema",
      Directory.TEST_INPUT_DIR + "/mascot.avsc",
      Directory.TEST_INPUT_DIR,
      Directory.TEST_OUTPUT_SCAVRO_BASE_DIR
    ))
    Util.readFile(Directory.TEST_OUTPUT_SCAVRO_WRESTLER)  === Util.readFile(Directory.TEST_EXPECTED_SCAVRO_WRESTLER)
  }

}