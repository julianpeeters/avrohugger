
import java.io.File

import avrohugger._
import avrohugger.format.SpecificRecord
import org.specs2._

class CommentsSpec extends mutable.Specification {

  "a Generator" should {

    "use the comments from idl files in their Scala counterparts." in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/comments1.avdl")
      val gen = new Generator(SpecificRecord)
      val outDir = gen.defaultOutputDir + "/specific/"
      gen.fileToFile(infile, outDir)

      val sourceRecord = scala.io.Source.fromFile(s"$outDir/com/example/Example.scala").mkString
      sourceRecord ====
        """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |package com.example
          |
          |/** Example adapted from https://github.com/delagoya. This is a comment for the whole protocol */
          |sealed trait Example extends Product with Serializable
          |
          |/**
          | * The comment applies to the `NoSpaces` record, but is not indented to the
          | * level of the record specification.
          | * @param single_line_comment_property This is a single line comment that is indented for readability,
          | *     and is not affected by indentation.
          | * @param multi_line_property This multi-line comment on `mult_line_property` that would be affected by indentation.
          | * 
          | * This is another paragraph
          | * 
          | * 
          | *     This is an indented block and should be shown as
          | *     such.
          | * 
          | * Here is a code block that apparently does not work for avrodoc. E.g. no [GFM](https://help.github.com/articles/github-flavored-markdown) support.
          | * 
          | * ```ruby
          | * # this is a Ruby code block
          | * def method(arg1, arg2=nil)
          | *   puts "hello world!"
          | * end
          | * ```
          | */
          |final case class NoSpaces(var single_line_comment_property: String, var multi_line_property: String) extends org.apache.avro.specific.SpecificRecordBase with Example {
          |  def this() = this("", "")
          |  def get(field$: Int): AnyRef = {
          |    field$ match {
          |      case pos if pos == 0 => {
          |        single_line_comment_property
          |      }.asInstanceOf[AnyRef]
          |      case pos if pos == 1 => {
          |        multi_line_property
          |      }.asInstanceOf[AnyRef]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |  }
          |  def put(field$: Int, value: Any): Unit = {
          |    field$ match {
          |      case pos if pos == 0 => this.single_line_comment_property = {
          |        value.toString
          |      }.asInstanceOf[String]
          |      case pos if pos == 1 => this.multi_line_property = {
          |        value.toString
          |      }.asInstanceOf[String]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |    ()
          |  }
          |  def getSchema: org.apache.avro.Schema = NoSpaces.SCHEMA$
          |}
          |
          |final object NoSpaces {
          |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"NoSpaces\",\"namespace\":\"com.example\",\"doc\":\"The comment applies to the `NoSpaces` record, but is not indented to the\\nlevel of the record specification.\",\"fields\":[{\"name\":\"single_line_comment_property\",\"type\":\"string\",\"doc\":\"This is a single line comment that is indented for readability,\\n    and is not affected by indentation.\"},{\"name\":\"multi_line_property\",\"type\":\"string\",\"doc\":\"This multi-line comment on `mult_line_property` that would be affected by indentation.\\n\\nThis is another paragraph\\n\\n\\n    This is an indented block and should be shown as\\n    such.\\n\\nHere is a code block that apparently does not work for avrodoc. E.g. no [GFM](https://help.github.com/articles/github-flavored-markdown) support.\\n\\n```ruby\\n# this is a Ruby code block\\ndef method(arg1, arg2=nil)\\n  puts \\\"hello world!\\\"\\nend\\n```\"}]}")
          |}
          |""".stripMargin.trim
    }


        "use the comments from idl fields even if the record has none." in {
          val infile = new java.io.File("avrohugger-core/src/test/avro/comments2.avdl")
          val gen = new Generator(SpecificRecord)
          val outDir = gen.defaultOutputDir + "/specific/"
          gen.fileToFile(infile, outDir)

          val sourceRecord = scala.io.Source.fromFile(s"$outDir/com/example/Example2.scala").mkString
          sourceRecord ====
            """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
              |package com.example
              |
              |/** Example adapted from https://github.com/delagoya. This is a comment for the whole protocol */
              |sealed trait Example2 extends Product with Serializable
              |
              |/** @param comment_property This is a single line comment on a field, in a record that has no comment. */
              |final case class NoSpaces2(var comment_property: String) extends org.apache.avro.specific.SpecificRecordBase with Example2 {
              |  def this() = this("")
              |  def get(field$: Int): AnyRef = {
              |    field$ match {
              |      case pos if pos == 0 => {
              |        comment_property
              |      }.asInstanceOf[AnyRef]
              |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
              |    }
              |  }
              |  def put(field$: Int, value: Any): Unit = {
              |    field$ match {
              |      case pos if pos == 0 => this.comment_property = {
              |        value.toString
              |      }.asInstanceOf[String]
              |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
              |    }
              |    ()
              |  }
              |  def getSchema: org.apache.avro.Schema = NoSpaces2.SCHEMA$
              |}
              |
              |final object NoSpaces2 {
              |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"NoSpaces2\",\"namespace\":\"com.example\",\"fields\":[{\"name\":\"comment_property\",\"type\":\"string\",\"doc\":\"This is a single line comment on a field, in a record that has no comment.\"}]}")
              |}
              |""".stripMargin.trim
        }


        "use the comments from idl records even if the record fields have none." in {
          val infile = new java.io.File("avrohugger-core/src/test/avro/comments3.avdl")
          val gen = new Generator(SpecificRecord)
          val outDir = gen.defaultOutputDir + "/specific/"
          gen.fileToFile(infile, outDir)

          val sourceRecord = scala.io.Source.fromFile(s"$outDir/com/example/Example3.scala").mkString
          sourceRecord ====
            """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
              |package com.example
              |
              |/** Example adapted from https://github.com/delagoya. This is a comment for the whole protocol */
              |sealed trait Example3 extends Product with Serializable
              |
              |/**
              | * The comment applies to the `NoSpaces3` record, but is not indented to the
              | * level of the record specification.
              | * @param comment_property 
              | */
              |final case class NoSpaces3(var comment_property: String) extends org.apache.avro.specific.SpecificRecordBase with Example3 {
              |  def this() = this("")
              |  def get(field$: Int): AnyRef = {
              |    field$ match {
              |      case pos if pos == 0 => {
              |        comment_property
              |      }.asInstanceOf[AnyRef]
              |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
              |    }
              |  }
              |  def put(field$: Int, value: Any): Unit = {
              |    field$ match {
              |      case pos if pos == 0 => this.comment_property = {
              |        value.toString
              |      }.asInstanceOf[String]
              |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
              |    }
              |    ()
              |  }
              |  def getSchema: org.apache.avro.Schema = NoSpaces3.SCHEMA$
              |}
              |
              |final object NoSpaces3 {
              |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"NoSpaces3\",\"namespace\":\"com.example\",\"doc\":\"The comment applies to the `NoSpaces3` record, but is not indented to the\\nlevel of the record specification.\",\"fields\":[{\"name\":\"comment_property\",\"type\":\"string\"}]}")
              |}
              |""".stripMargin.trim
        }



  }

}
