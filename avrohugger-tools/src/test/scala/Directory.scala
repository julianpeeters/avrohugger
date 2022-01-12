package avrohugger
package tool

import java.io.File

object Directory {

// where test input/expected output comes from
  val TEST_DIR =
    System.getProperty("test.compile.schema.dir", "avrohugger-tools/src/test/compiler/")
  // where test input comes from
  val TEST_INPUT_DIR =
    TEST_DIR + "input/"

  // where test expected output comes from
  val TEST_EXPECTED_OUTPUT_BASE_DIR =
    TEST_DIR + "output/"
  val TEST_EXPECTED_HANDLE =
    TEST_EXPECTED_OUTPUT_BASE_DIR + "Handle.scala"
  val TEST_EXPECTED_PILOT =
    TEST_EXPECTED_OUTPUT_BASE_DIR + "Pilot.scala"
  val TEST_EXPECTED_MASCOT =
    TEST_EXPECTED_OUTPUT_BASE_DIR + "avro/examples/baseball/Mascot.scala"
  val TEST_EXPECTED_WRESTLER =
    TEST_EXPECTED_OUTPUT_BASE_DIR + "avro/examples/baseball/Wrestler.scala"
  val TEST_EXPECTED_TWITTER =
    TEST_EXPECTED_OUTPUT_BASE_DIR + "com/miguno/avro/twitter_schema.scala"
  val TEST_EXPECTED_MESSAGE =
    TEST_EXPECTED_OUTPUT_BASE_DIR + "example/proto/Message.scala"


  val TEST_EXPECTED_STRING_OUTPUT_BASE_DIR =
    TEST_DIR + "output-string/"
  val TEST_EXPECTED_STRING_NICKNAME =
    TEST_EXPECTED_STRING_OUTPUT_BASE_DIR + "avro/examples/baseball/Nickname.scala"
  val TEST_EXPECTED_STRING_PLAYER =
    TEST_EXPECTED_STRING_OUTPUT_BASE_DIR + "avro/examples/baseball/Player.scala"
  val TEST_EXPECTED_STRING_TWITTER =
    TEST_EXPECTED_STRING_OUTPUT_BASE_DIR + "com/miguno/avro/twitter_schema.scala"
  val TEST_EXPECTED_STRING_MESSAGE =
    TEST_EXPECTED_STRING_OUTPUT_BASE_DIR + "example/proto/Message.scala"

  val TEST_EXPECTED_SPECIFIC_OUTPUT_BASE_DIR =
    TEST_DIR + "output-specific/"
  val TEST_EXPECTED_SPECIFIC_NICKNAME =
    TEST_EXPECTED_SPECIFIC_OUTPUT_BASE_DIR + "avro/examples/baseball/Nickname.scala"
  val TEST_EXPECTED_SPECIFIC_MASCOT =
    TEST_EXPECTED_SPECIFIC_OUTPUT_BASE_DIR + "avro/examples/baseball/Mascot.scala"
  val TEST_EXPECTED_SPECIFIC_PLAYER =
    TEST_EXPECTED_SPECIFIC_OUTPUT_BASE_DIR + "avro/examples/baseball/Player.scala"
  val TEST_EXPECTED_SPECIFIC_PLAYER_213 = TEST_EXPECTED_SPECIFIC_OUTPUT_BASE_DIR + "avro/examples/baseball/Player_2.13.scala"
  val TEST_EXPECTED_SPECIFIC_WRESTLER =
    TEST_EXPECTED_SPECIFIC_OUTPUT_BASE_DIR + "avro/examples/baseball/Wrestler.scala"
  val TEST_EXPECTED_SPECIFIC_WRESTLER_213 = TEST_EXPECTED_SPECIFIC_OUTPUT_BASE_DIR + "avro/examples/baseball/Wrestler_2.13.scala"
  val TEST_EXPECTED_SPECIFIC_TWITTER =
    TEST_EXPECTED_SPECIFIC_OUTPUT_BASE_DIR + "com/miguno/avro/twitter_schema.scala"
  val TEST_EXPECTED_SPECIFIC_MESSAGE =
    TEST_EXPECTED_SPECIFIC_OUTPUT_BASE_DIR + "example/proto/Message.scala"
  val TEST_EXPECTED_SPECIFIC_MAIL =
    TEST_EXPECTED_SPECIFIC_OUTPUT_BASE_DIR + "example/proto/Mail.scala"

  val TEST_EXPECTED_SCAVRO_OUTPUT_BASE_DIR =
    TEST_DIR + "output-scavro/"
  val TEST_EXPECTED_SCAVRO_NICKNAME =
    TEST_EXPECTED_SCAVRO_OUTPUT_BASE_DIR + "avro/examples/baseball/Nickname.scala"
  val TEST_EXPECTED_SCAVRO_MASCOT =
    TEST_EXPECTED_SCAVRO_OUTPUT_BASE_DIR + "avro/examples/baseball/Mascot.scala"
  val TEST_EXPECTED_SCAVRO_PLAYER =
    TEST_EXPECTED_SCAVRO_OUTPUT_BASE_DIR + "avro/examples/baseball/Player.scala"
  val TEST_EXPECTED_SCAVRO_WRESTLER =
    TEST_EXPECTED_SCAVRO_OUTPUT_BASE_DIR + "avro/examples/baseball/Wrestler.scala"
  val TEST_EXPECTED_SCAVRO_TWITTER =
    TEST_EXPECTED_SCAVRO_OUTPUT_BASE_DIR + "com/miguno/avro/twitter_schema.scala"
  val TEST_EXPECTED_SCAVRO_MESSAGE =
    TEST_EXPECTED_SCAVRO_OUTPUT_BASE_DIR + "example/proto/model/Message.scala"


  // where test output goes
  val TEST_OUTPUT_BASE_DIR =
    "avrohugger-tools/target/compiler/output/"
  val TEST_OUTPUT_PILOT =
    TEST_OUTPUT_BASE_DIR + "avro/examples/flight/Pilot.scala"
  val TEST_OUTPUT_HANDLE =
    TEST_OUTPUT_BASE_DIR + "avro/examples/flight/Handle.scala"
  val TEST_OUTPUT_WRESTLER =
    TEST_OUTPUT_BASE_DIR + "avro/examples/baseball/Wrestler.scala"
  val TEST_OUTPUT_MASCOT =
    TEST_OUTPUT_BASE_DIR + "avro/examples/baseball/Mascot.scala"
  val TEST_OUTPUT_TWITTER =
    TEST_OUTPUT_BASE_DIR + "com/miguno/avro/twitter_schema.scala"
  val TEST_OUTPUT_MESSAGE =
    TEST_OUTPUT_BASE_DIR + "example/proto/Message.scala"

  val TEST_OUTPUT_STRING_BASE_DIR = "avrohugger-tools/target/compiler/output-string/"
  val TEST_OUTPUT_STRING_PLAYER =
    TEST_OUTPUT_STRING_BASE_DIR + "avro/examples/baseball/Player.scala"
  val TEST_OUTPUT_STRING_NICKNAME =
    TEST_OUTPUT_STRING_BASE_DIR + "avro/examples/baseball/Nickname.scala"
  val TEST_OUTPUT_STRING_TWITTER =
    TEST_OUTPUT_STRING_BASE_DIR + "com/miguno/avro/twitter_schema.scala"
  val TEST_OUTPUT_STRING_MESSAGE =
    TEST_OUTPUT_STRING_BASE_DIR + "example/proto/Message.scala"

  val TEST_OUTPUT_SPECIFIC_BASE_DIR =
    "avrohugger-tools/target/compiler/output-specific/"
  val TEST_OUTPUT_SPECIFIC_PLAYER =
    TEST_OUTPUT_SPECIFIC_BASE_DIR + "avro/examples/baseball/Player.scala"
  val TEST_OUTPUT_SPECIFIC_NICKNAME =
    TEST_OUTPUT_SPECIFIC_BASE_DIR + "avro/examples/baseball/Nickname.scala"
  val TEST_OUTPUT_SPECIFIC_WRESTLER =
    TEST_OUTPUT_SPECIFIC_BASE_DIR + "avro/examples/baseball/Wrestler.scala"
  val TEST_OUTPUT_SPECIFIC_MASCOT =
    TEST_OUTPUT_SPECIFIC_BASE_DIR + "avro/examples/baseball/Mascot.scala"
  val TEST_OUTPUT_SPECIFIC_TWITTER =
    TEST_OUTPUT_SPECIFIC_BASE_DIR + "com/miguno/avro/twitter_schema.scala"
  val TEST_OUTPUT_SPECIFIC_MESSAGE =
    TEST_OUTPUT_SPECIFIC_BASE_DIR + "example/proto/Message.scala"
  val TEST_OUTPUT_SPECIFIC_MAIL =
    TEST_OUTPUT_SPECIFIC_BASE_DIR + "example/proto/Mail.scala"

  val TEST_OUTPUT_SCAVRO_BASE_DIR = "avrohugger-tools/target/compiler/output-scavro/"
  val TEST_OUTPUT_SCAVRO_PLAYER =
    TEST_OUTPUT_SCAVRO_BASE_DIR + "avro/examples/baseball/model/Player.scala"
  val TEST_OUTPUT_SCAVRO_NICKNAME =
    TEST_OUTPUT_SCAVRO_BASE_DIR + "avro/examples/baseball/model/Nickname.scala"
  val TEST_OUTPUT_SCAVRO_WRESTLER =
    TEST_OUTPUT_SCAVRO_BASE_DIR + "avro/examples/baseball/model/Wrestler.scala"
  val TEST_OUTPUT_SCAVRO_MASCOT =
    TEST_OUTPUT_SCAVRO_BASE_DIR + "avro/examples/baseball/model/Mascot.scala"
  val TEST_OUTPUT_SCAVRO_TWITTER =
    TEST_OUTPUT_SCAVRO_BASE_DIR + "com/miguno/avro/model/twitter_schema.scala"
  val TEST_OUTPUT_SCAVRO_MESSAGE =
    TEST_OUTPUT_SCAVRO_BASE_DIR + "example/proto/model/Message.scala"

}
