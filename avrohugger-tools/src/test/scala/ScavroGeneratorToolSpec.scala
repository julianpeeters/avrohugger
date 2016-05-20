
import avrohugger.format.Scavro
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
class ScavroGeneratorToolSpec extends mutable.Specification {


  // Runs the actual generator tool with the given input args
  private def doCompile(args: Array[String])  = {
    val tool = new GeneratorTool(Scavro)
    Try{
      tool.run(null, null, null, Seq(args:_*))
    }
  }

  "the generated Scala files" should {
/* //TODO in Java an interface is generated in addition to the types, how to represent in Scala? Trait?
    "match the expected single protocol file" in {
      doCompile(Array[String] ("protocol",
        Directory.TEST_INPUT_DIR.toString() + "/mail.avpr",
        Directory.TEST_OUTPUT_SCALA_DIR.getPath()
      ))
      Util.readFile(Directory.TEST_OUTPUT_MESSAGE) === Util.readFile(Directory.TEST_EXPECTED_MESSAGE)
    }
*/
    "match the expected single datafile file" in {
      doCompile(Array[String] ("datafile",
        Directory.TEST_INPUT_DIR.toString() + "/twitter.avro",
        Directory.TEST_OUTPUT_SCAVRO_BASE_DIR.getPath()
      ))
      Util.readFile(Directory.TEST_OUTPUT_SCAVRO_TWITTER) === Util.readFile(Directory.TEST_EXPECTED_SCAVRO_TWITTER)
    }

    "match the expected single schema file" in {
      doCompile(Array[String] ("schema",
        Directory.TEST_INPUT_DIR.toString() + "/nickname.avsc",
        Directory.TEST_OUTPUT_SCAVRO_BASE_DIR.getPath()
      ))
      Util.readFile(Directory.TEST_OUTPUT_SCAVRO_NICKNAME) === Util.readFile(Directory.TEST_EXPECTED_SCAVRO_NICKNAME)
    }

    "match the expected dependent files" in {
      doCompile(Array[String]("schema",
        Directory.TEST_INPUT_DIR.toString() + "/nickname.avsc",
        Directory.TEST_INPUT_DIR.toString() + "/player.avsc",
        Directory.TEST_OUTPUT_SCAVRO_BASE_DIR.getPath()
      ))
      Util.readFile(Directory.TEST_OUTPUT_SCAVRO_PLAYER)  === Util.readFile(Directory.TEST_EXPECTED_SCAVRO_PLAYER)
    }

    "match the expected file and directory" in {
      doCompile(Array[String]("schema",
        Directory.TEST_INPUT_DIR.toString() + "/nickname.avsc",
        Directory.TEST_INPUT_DIR.toString(),
        Directory.TEST_OUTPUT_SCAVRO_BASE_DIR.getPath()
      ))
      Util.readFile(Directory.TEST_OUTPUT_SCAVRO_PLAYER)  === Util.readFile(Directory.TEST_EXPECTED_SCAVRO_PLAYER)
    }

  }
}
