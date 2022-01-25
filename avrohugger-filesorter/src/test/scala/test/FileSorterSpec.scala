package test

import avrohugger.filesorter.{AvdlFileSorter, AvscFileSorter}

import java.io.File

import org.apache.avro.Schema
import org.apache.avro.compiler.specific.SpecificCompiler
import org.apache.avro.generic.GenericData.StringType
import org.specs2.mutable.Specification

class FileSorterSpec extends Specification {
  val sourceDir = new File(getClass.getClassLoader.getResource("avro").toURI)
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

  "It should be possible to compile types depending on others if source files are provided in right order" >> {
    val parser = new Schema.Parser()
    val parserRev = new Schema.Parser()
    val packageDir = new File(targetDir, "com/cavorite")
    val packageRevDir = new File(targetRevDir, "com/cavorite")

    val aJavaFile = new File(packageDir, "A.java")
    val bJavaFile = new File(packageDir, "B.java")
    val cJavaFile = new File(packageDir, "C.java")
    val dJavaFile = new File(packageDir, "D.java")
    val eJavaFile = new File(packageDir, "E.java")

    val _aJavaFile = new File(packageDir, "_A.java")
    val _bJavaFile = new File(packageDir, "_B.java")
    val _cJavaFile = new File(packageDir, "_C.java")
    val _dJavaFile = new File(packageDir, "_D.java")
    val _eJavaFile = new File(packageDir, "_E.java")

    val aRevJavaFile = new File(packageRevDir, "A.java")
    val bRevJavaFile = new File(packageRevDir, "B.java")
    val cRevJavaFile = new File(packageRevDir, "C.java")
    val dRevJavaFile = new File(packageRevDir, "D.java")
    val eRevJavaFile = new File(packageRevDir, "E.java")

    val _aRevJavaFile = new File(packageRevDir, "_A.java")
    val _bRevJavaFile = new File(packageRevDir, "_B.java")
    val _cRevJavaFile = new File(packageRevDir, "_C.java")
    val _dRevJavaFile = new File(packageRevDir, "_D.java")
    val _eRevJavaFile = new File(packageRevDir, "_E.java")

    aJavaFile.delete()
    bJavaFile.delete()
    cJavaFile.delete()
    dJavaFile.delete()
    eJavaFile.delete()

    _aJavaFile.delete()
    _bJavaFile.delete()
    _cJavaFile.delete()
    _dJavaFile.delete()
    _eJavaFile.delete()

    aRevJavaFile.delete()
    bRevJavaFile.delete()
    cRevJavaFile.delete()
    dRevJavaFile.delete()
    eRevJavaFile.delete()

    _aRevJavaFile.delete()
    _bRevJavaFile.delete()
    _cRevJavaFile.delete()
    _dRevJavaFile.delete()
    _eRevJavaFile.delete()

    for (schemaFile <- AvscFileSorter.sortSchemaFiles(avscSourceFiles)) {
      val schemaAvr = parser.parse(schemaFile)
      val compiler = new SpecificCompiler(schemaAvr)
      compiler.setStringType(StringType.CharSequence)
      compiler.compileToDestination(null, targetDir)
    }

    for (schemaFile <- AvscFileSorter.sortSchemaFiles(avscSourceFiles.reverse)) {
      val schemaAvr = parserRev.parse(schemaFile)
      val compiler = new SpecificCompiler(schemaAvr)
      compiler.setStringType(StringType.CharSequence)
      compiler.compileToDestination(null, targetRevDir)
    }

    aJavaFile.isFile must beTrue
    bJavaFile.isFile must beTrue
    cJavaFile.isFile must beTrue
    dJavaFile.isFile must beTrue
    eJavaFile.isFile must beTrue

    _aJavaFile.isFile must beTrue
    _bJavaFile.isFile must beTrue
    _cJavaFile.isFile must beTrue
    _dJavaFile.isFile must beTrue
    _eJavaFile.isFile must beTrue

    aRevJavaFile.isFile must beTrue
    bRevJavaFile.isFile must beTrue
    cRevJavaFile.isFile must beTrue
    dRevJavaFile.isFile must beTrue
    eRevJavaFile.isFile must beTrue

    _aRevJavaFile.isFile must beTrue
    _bRevJavaFile.isFile must beTrue
    _cRevJavaFile.isFile must beTrue
    _dRevJavaFile.isFile must beTrue
    _eRevJavaFile.isFile must beTrue

  }

}