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
  val TEST_EXPECTED_OUTPUT_DIR =
    new File(TEST_DIR, "output");
  val TEST_EXPECTED_NICKNAME =
    new File(TEST_EXPECTED_OUTPUT_DIR, "Nickname.scala");
  val TEST_EXPECTED_PLAYER =
    new File(TEST_EXPECTED_OUTPUT_DIR, "Player.scala");

  val TEST_EXPECTED_STRING_OUTPUT_DIR =
    new File(TEST_DIR, "output-string/");
  val TEST_EXPECTED_STRING_NICKNAME =
    new File(TEST_EXPECTED_STRING_OUTPUT_DIR,
             "avro/examples/baseball/Nickname.scala");
  val TEST_EXPECTED_STRING_PLAYER =
    new File(TEST_EXPECTED_STRING_OUTPUT_DIR,
             "avro/examples/baseball/Player.scala");

  // where test output goes
  val TEST_OUTPUT_DIR =
    new File("avrohugger-tools/target/compiler/output/");
  val TEST_OUTPUT_PLAYER =
    new File(TEST_OUTPUT_DIR, "avro/examples/baseball/Player.scala");
  val TEST_OUTPUT_NICKNAME =
    new File(TEST_OUTPUT_DIR, "avro/examples/baseball/Nickname.scala");

  val TEST_OUTPUT_STRING_DIR =
    new File("target/compiler/output-string");
  val TEST_OUTPUT_STRING_PLAYER =
    new File(TEST_OUTPUT_STRING_DIR, "avro/examples/baseball/Player.scala");
  val TEST_OUTPUT_STRING_NICKNAME =
    new File(TEST_OUTPUT_STRING_DIR, "avro/examples/baseball/Nickname.scala");
}