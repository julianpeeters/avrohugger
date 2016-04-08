package avrohugger
package tool

import java.io.File

object Directory {

// where test input/expected output comes from
  val TEST_DIR =
    new File(System.getProperty("test.compile.schema.dir", "avrohugger-tools/src/test/compiler"));
  // where test input comes from
  val TEST_INPUT_DIR =
    new File(TEST_DIR, "input");

  // where test expected output comes from
  val TEST_EXPECTED_OUTPUT_BASE_DIR =
    new File(TEST_DIR, "output");
  val TEST_EXPECTED_NICKNAME =
    new File(TEST_EXPECTED_OUTPUT_BASE_DIR, "Nickname.scala");
  val TEST_EXPECTED_PLAYER =
    new File(TEST_EXPECTED_OUTPUT_BASE_DIR, "Player.scala");
  val TEST_EXPECTED_TWITTER =
    new File(TEST_EXPECTED_OUTPUT_BASE_DIR, "com/miguno/avro/twitter_schema.scala");
  val TEST_EXPECTED_MESSAGE =
    new File(TEST_EXPECTED_OUTPUT_BASE_DIR, "example/proto/Message.scala");


  val TEST_EXPECTED_STRING_OUTPUT_BASE_DIR =
    new File(TEST_DIR, "output-string/");
  val TEST_EXPECTED_STRING_NICKNAME =
    new File(TEST_EXPECTED_STRING_OUTPUT_BASE_DIR,
             "avro/examples/baseball/Nickname.scala");
  val TEST_EXPECTED_STRING_PLAYER =
    new File(TEST_EXPECTED_STRING_OUTPUT_BASE_DIR,
             "avro/examples/baseball/Player.scala");
  val TEST_EXPECTED_STRING_TWITTER =
    new File(TEST_EXPECTED_STRING_OUTPUT_BASE_DIR,
             "com/miguno/avro/twitter_schema.scala");
  val TEST_EXPECTED_STRING_MESSAGE =
    new File(TEST_EXPECTED_STRING_OUTPUT_BASE_DIR,
             "example/proto/Message.scala");

  val TEST_EXPECTED_SPECIFIC_OUTPUT_BASE_DIR =
    new File(TEST_DIR, "output-specific/");
  val TEST_EXPECTED_SPECIFIC_NICKNAME =
    new File(TEST_EXPECTED_SPECIFIC_OUTPUT_BASE_DIR,
             "avro/examples/baseball/Nickname.scala");
  val TEST_EXPECTED_SPECIFIC_PLAYER =
    new File(TEST_EXPECTED_SPECIFIC_OUTPUT_BASE_DIR,
             "avro/examples/baseball/Player.scala");
  val TEST_EXPECTED_SPECIFIC_TWITTER =
    new File(TEST_EXPECTED_SPECIFIC_OUTPUT_BASE_DIR,
             "com/miguno/avro/twitter_schema.scala");
  val TEST_EXPECTED_SPECIFIC_MESSAGE =
    new File(TEST_EXPECTED_SPECIFIC_OUTPUT_BASE_DIR,
             "example/proto/Message.scala");

  val TEST_EXPECTED_SCAVRO_OUTPUT_BASE_DIR =
    new File(TEST_DIR, "output-scavro/");
  val TEST_EXPECTED_SCAVRO_NICKNAME =
    new File(TEST_EXPECTED_SCAVRO_OUTPUT_BASE_DIR,
            "avro/examples/baseball/Nickname.scala");
  val TEST_EXPECTED_SCAVRO_PLAYER =
    new File(TEST_EXPECTED_SCAVRO_OUTPUT_BASE_DIR,
            "avro/examples/baseball/Player.scala");
  val TEST_EXPECTED_SCAVRO_TWITTER =
    new File(TEST_EXPECTED_SCAVRO_OUTPUT_BASE_DIR,
            "com/miguno/avro/twitter_schema.scala");
  val TEST_EXPECTED_SCAVRO_MESSAGE =
    new File(TEST_EXPECTED_SCAVRO_OUTPUT_BASE_DIR,
            "example/proto/Message.scala");


  // where test output goes
  val TEST_OUTPUT_BASE_DIR =
    new File("avrohugger-tools/target/compiler/output");
  val TEST_OUTPUT_PLAYER =
    new File(TEST_OUTPUT_BASE_DIR, "avro/examples/baseball/Player.scala");
  val TEST_OUTPUT_NICKNAME =
    new File(TEST_OUTPUT_BASE_DIR, "avro/examples/baseball/Nickname.scala");
  val TEST_OUTPUT_TWITTER =
    new File(TEST_OUTPUT_BASE_DIR, "com/miguno/avro/twitter_schema.scala");
      val TEST_OUTPUT_MESSAGE =
    new File(TEST_OUTPUT_BASE_DIR, "example/proto/Message.scala");

  val TEST_OUTPUT_STRING_BASE_DIR =
    new File("avrohugger-tools/target/compiler/output-string");
  val TEST_OUTPUT_STRING_PLAYER =
    new File(TEST_OUTPUT_STRING_BASE_DIR, "avro/examples/baseball/Player.scala");
  val TEST_OUTPUT_STRING_NICKNAME =
    new File(TEST_OUTPUT_STRING_BASE_DIR, "avro/examples/baseball/Nickname.scala");
  val TEST_OUTPUT_STRING_TWITTER =
    new File(TEST_OUTPUT_STRING_BASE_DIR, "com/miguno/avro/twitter_schema.scala");
  val TEST_OUTPUT_STRING_MESSAGE =
    new File(TEST_OUTPUT_STRING_BASE_DIR, "example/proto/Message.scala");

  val TEST_OUTPUT_SPECIFIC_BASE_DIR =
    new File("avrohugger-tools/target/compiler/output-specific");
  val TEST_OUTPUT_SPECIFIC_PLAYER =
    new File(TEST_OUTPUT_SPECIFIC_BASE_DIR, "avro/examples/baseball/Player.scala");
  val TEST_OUTPUT_SPECIFIC_NICKNAME =
    new File(TEST_OUTPUT_SPECIFIC_BASE_DIR, "avro/examples/baseball/Nickname.scala");
  val TEST_OUTPUT_SPECIFIC_TWITTER =
    new File(TEST_OUTPUT_SPECIFIC_BASE_DIR, "com/miguno/avro/twitter_schema.scala");
  val TEST_OUTPUT_SPECIFIC_MESSAGE =
    new File(TEST_OUTPUT_SPECIFIC_BASE_DIR, "example/proto/Message.scala");

  val TEST_OUTPUT_SCAVRO_BASE_DIR =
    new File("avrohugger-tools/target/compiler/output-scavro");
  val TEST_OUTPUT_SCAVRO_PLAYER =
    new File(TEST_OUTPUT_SCAVRO_BASE_DIR, "avro/examples/baseball/model/Player.scala");
  val TEST_OUTPUT_SCAVRO_NICKNAME =
    new File(TEST_OUTPUT_SCAVRO_BASE_DIR, "avro/examples/baseball/model/Nickname.scala");
  val TEST_OUTPUT_SCAVRO_TWITTER =
    new File(TEST_OUTPUT_SCAVRO_BASE_DIR, "com/miguno/avro/model/twitter_schema.scala");
  val TEST_OUTPUT_SCAVRO_MESSAGE =
    new File(TEST_OUTPUT_SCAVRO_BASE_DIR, "example/proto/model/Message.scala");
}
