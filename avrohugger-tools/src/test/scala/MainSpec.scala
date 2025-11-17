import java.io.{ ByteArrayOutputStream, PrintStream }

import avrohugger.tool.{ Directory, Runner }
import org.specs2._

import scala.jdk.CollectionConverters._


class MainSpec extends mutable.Specification {
  "tool descriptions fit in 80 characters" in {
    val r: Runner = new Runner(null, null, null)
    val descResults = r.toolsMap.values().asScala.exists { t =>
      r.maxLen + 2 + t.getShortDescription().length() > 80
    }
    //make sure there is no tool that didn't pass the desc. length test
    descResults === false
  }

  "successful runs yield zero exit code" in {
    val errBytes: ByteArrayOutputStream = new ByteArrayOutputStream()
    val r: Runner = new Runner(null, null, new PrintStream(errBytes))
    val exitCode = r.run(Array("generate", "schema",
      Directory.TEST_INPUT_DIR + "/handle.avsc",
      Directory.TEST_OUTPUT_BASE_DIR
    ))
    exitCode === 0
  }

  "not print anything to stderr" in {
    val errBytes: ByteArrayOutputStream = new ByteArrayOutputStream()
    val r: Runner = new Runner(null, null, new PrintStream(errBytes))
    r.run(Array("generate", "schema",
      Directory.TEST_INPUT_DIR + "/handle.avsc",
      Directory.TEST_OUTPUT_BASE_DIR
    ))
    errBytes.toString() === ""
  }

  "invalid tool names yield non-zero exit code" in {
    val errBytes: ByteArrayOutputStream = new ByteArrayOutputStream()
    val r: Runner = new Runner(null, null, new PrintStream(errBytes))
    val exitCode = r.run(Array("no-such-tool", "schema",
      Directory.TEST_INPUT_DIR + "/handle.avsc",
      Directory.TEST_OUTPUT_BASE_DIR
    ))
    exitCode !== 0
  }

  "print something to stderr" in {
    val errBytes: ByteArrayOutputStream = new ByteArrayOutputStream()
    val r: Runner = new Runner(null, null, new PrintStream(errBytes))
    r.run(Array("no-such-tool", "schema",
      Directory.TEST_INPUT_DIR + "/handle.avsc",
      Directory.TEST_OUTPUT_BASE_DIR
    ))
    errBytes.toString() must contain("Unknown tool: no-such-tool")
  }

  "invalid input yield non-zero exit code" in {
    val errBytes: ByteArrayOutputStream = new ByteArrayOutputStream()
    val r: Runner = new Runner(null, null, new PrintStream(errBytes))
    val exitCode = r.run(Array("generate", "schema",
      Directory.TEST_INPUT_DIR + "/invalid.avsc",
      Directory.TEST_OUTPUT_BASE_DIR
    ))
    exitCode !== 0
  }

  "print something to stderr" in {
    val errBytes: ByteArrayOutputStream = new ByteArrayOutputStream()
    val r: Runner = new Runner(null, null, new PrintStream(errBytes))
    r.run(Array("generate", "schema",
      Directory.TEST_INPUT_DIR + "/invalid.avsc",
      Directory.TEST_OUTPUT_BASE_DIR
    ))
    errBytes.toString() must contain("invalid_type")
  }

}
