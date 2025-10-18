package test

import avrohugger.filesorter.{AvdlFileSorter, AvscFileSorter}

import java.io.File

import org.apache.avro.Schema
import org.apache.avro.compiler.specific.SpecificCompiler
import org.apache.avro.generic.GenericData.StringType
import org.specs2.mutable.Specification

class FileSorterSpec extends Specification {
  val sourceDir = new File(new File("").getAbsolutePath(), "avrohugger-filesorter/src/test/resources/avro")
  val targetDir = new File(sourceDir.getParentFile, "generated")
  val targetRevDir = new File(sourceDir.getParentFile, "generated/rev")

  val fullyQualifiedNames = Seq(
    new File(sourceDir, "a.avsc"),
    new File(sourceDir, "b.avsc"),
    new File(sourceDir, "c.avsc"),
    new File(sourceDir, "d.avsc"),
    new File(sourceDir, "e.avsc"))


  val fixedNames = Seq(
    new File(sourceDir, "amain/fixedRecord.avsc"),
    new File(sourceDir, "imports/fixed.avsc")
  )

  val simpleNames = Seq(
    new File(sourceDir, "_a.avsc"),
    new File(sourceDir, "_b.avsc"),
    new File(sourceDir, "_c.avsc"),
    new File(sourceDir, "_d.avsc"),
    new File(sourceDir, "_e.avsc"))
    
  val fileNames = Seq(
    new File(sourceDir, "a.avdl"),
    new File(sourceDir, "foo/a.avdl"),
    new File(sourceDir, "c.avdl"))

  val expectedOrderFullyQualifiedNames = Seq(
    new File(sourceDir, "c.avsc"),
    new File(sourceDir, "e.avsc"),
    new File(sourceDir, "d.avsc"),
    new File(sourceDir, "b.avsc"),
    new File(sourceDir, "a.avsc"))

  val expectedOrderFixedNames = Seq(
    new File(sourceDir, "imports/fixed.avsc"),
    new File(sourceDir, "amain/fixedRecord.avsc")
  )

  val expectedOrderSimpleNames = Seq(
    new File(sourceDir, "_c.avsc"),
    new File(sourceDir, "_e.avsc"),
    new File(sourceDir, "_d.avsc"),
    new File(sourceDir, "_b.avsc"),
    new File(sourceDir, "_a.avsc"))
  
  val expectdOrderFileNames = Seq(
    new File(sourceDir, "a.avdl"),
    new File(sourceDir, "foo/a.avdl"),
    new File(sourceDir, "c.avdl"))

  val avscSourceFiles = fullyQualifiedNames ++ simpleNames
  val avdlSourceFiles = fileNames

  "Schema files should be sorted with re-used types schemas first, whatever input order" >> {
    AvscFileSorter.sortSchemaFiles(fullyQualifiedNames) must beEqualTo(expectedOrderFullyQualifiedNames)
    AvscFileSorter.sortSchemaFiles(fullyQualifiedNames.reverse) must beEqualTo(expectedOrderFullyQualifiedNames)
    AvscFileSorter.sortSchemaFiles(simpleNames) must beEqualTo(expectedOrderSimpleNames)
    AvscFileSorter.sortSchemaFiles(simpleNames.reverse) must beEqualTo(expectedOrderSimpleNames)
  }

  "Schema files with Fixed type should be sorted" >> {
    AvscFileSorter.sortSchemaFiles(fixedNames) must beEqualTo(expectedOrderFixedNames)
  }
  
  "AVDL files should be sorted correctly for imports" >> {
    AvdlFileSorter.sortSchemaFiles(avdlSourceFiles) must beEqualTo(expectdOrderFileNames)
    AvdlFileSorter.sortSchemaFiles(avdlSourceFiles.reverse) must beEqualTo(expectdOrderFileNames)
  }

}