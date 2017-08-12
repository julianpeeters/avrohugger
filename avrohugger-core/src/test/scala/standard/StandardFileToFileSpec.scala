package avrohugger
package test
package standard

import java.io.File

import avrohugger._
import avrohugger.format.Standard
import org.specs2._

class StandardFileToFileSpec extends mutable.Specification {
      
  // tests specific to fileToX
  "A. correctly generate a simple case class definition from AVRO" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/twitter.avro")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)
    
    val source = util.Util.readFile("target/generated-sources/standard/com/miguno/avro/twitter_schema.scala")
    
    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/com/miguno/avro/twitter_schema.scala")
  }

  // tests specific to fileToFile
  "0. not generate copy of imported classes in the importing package" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    (new File(s"target/generated-sources/standard/example/idl/ExternalDependency.scala")).exists === false
  }
    
  // tests common to fileToX and stringToX
  "1. correctly generate from a protocol with messages" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/mail.avpr")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)
    
    val source = util.Util.readFile(s"$outDir/example/proto/Message.scala")
    
    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/proto/Message.scala")
  }
    
  "2. correctly generate a simple case class definition in a package" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/user.avsc")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)
    
    val source = util.Util.readFile("target/generated-sources/standard/example/User.scala")
    
    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/User.scala")
  }

  "3. correctly generate a simple case class definition in the default package" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/AvroTypeProviderTestNoNamespace.avsc")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)
    
    val source = util.Util.readFile("target/generated-sources/standard/AvroTypeProviderTestNoNamespace.scala")
    
    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/AvroTypeProviderTestNoNamespace.scala")
  }

  "4. correctly generate a nested case class definition from a schema" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/nested.avsc")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source0 = util.Util.readFile("target/generated-sources/standard/example/Level0.scala")
    val source1 = util.Util.readFile("target/generated-sources/standard/example/Level1.scala")
    val source2 = util.Util.readFile("target/generated-sources/standard/example/Level2.scala")

    source0 === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Level0.scala")
    source1 === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Level1.scala")
    source2 === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Level2.scala")
  }

  "5. correctly generate a nested case class from IDL" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/nested.avdl")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)
    
    val source = util.Util.readFile("target/generated-sources/standard/example/idl/NestedProtocol.scala")
    
    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/NestedProtocol.scala")
  }

  "6. correctly generate a recursive case class from IDL" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/recursive.avdl")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/idl/Recursive.scala")
    
    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/Recursive.scala")
  }

  "7. correctly generate enums from schema" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/enums.avsc")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/Suit.scala")
    
    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Suit.scala")
  }

  "8. correctly generate enums from protocol" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/enums.avpr")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/proto/EnumProtocol.scala")
    
    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/proto/EnumProtocol.scala")
  }

  "9. correctly generate enums from IDL" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/enums.avdl")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/idl/EnumProtocol.scala")
  
    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/EnumProtocol.scala")
  }

  "10. correctly generate nested enums" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/enums_nested.avsc")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)
    
    val sourceEnum = util.Util.readFile("target/generated-sources/standard/example/Direction.scala")
    val sourceRecord = util.Util.readFile("target/generated-sources/standard/example/Compass.scala")
    
    sourceEnum === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Direction.scala")
    sourceRecord === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/Compass.scala")
  }

  "11. correctly generate bytes from schema" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/bytes.avsc")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/BinarySc.scala")
    
    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/BinarySc.scala")
  }
  
  "12. correctly generate bytes from protocol" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/bytes.avpr")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/proto/BinaryPr.scala")

    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/proto/BinaryPr.scala")
  }

  "13. correctly generate bytes from IDL" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/bytes.avdl")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val source = util.Util.readFile("target/generated-sources/standard/example/idl/BinaryIdl.scala")
  
    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/BinaryIdl.scala")
  }

  "14. correctly generate records depending on others defined in a different- and same-namespaced AVDL and AVSC" in {
    val importing = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(importing, outDir)

    val sourceADT = util.Util.readFile("target/generated-sources/standard/example/idl/ImportProtocol.scala")
    val sourceDep1 = util.Util.readFile("target/generated-sources/standard/example/idl/Defaults.scala")
    val sourceDep2 = util.Util.readFile("target/generated-sources/standard/other/ns/ExternalDependency.scala")
    val sourceDep3 = util.Util.readFile("target/generated-sources/standard/other/ns/Suit.scala")
    
    sourceADT === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/ImportProtocol.scala")
    sourceDep1 === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/Defaults.scala")
    sourceDep2 === util.Util.readFile("avrohugger-core/src/test/expected/standard/other/ns/ExternalDependency.scala")
    sourceDep3 === util.Util.readFile("avrohugger-core/src/test/expected/standard/other/ns/Suit.scala")
  }

  "15. correctly generate an empty case class definition" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/AvroTypeProviderTestEmptyRecord.avdl")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)
  
    val source = util.Util.readFile("target/generated-sources/standard/test/Calculator.scala")
  
    source === util.Util.readFile("avrohugger-core/src/test/expected/standard/test/Calculator.scala")
  }

  "16. correctly generate default values" in {
    val infile = new java.io.File("avrohugger-core/src/test/avro/defaults.avdl")
    val gen = new Generator(Standard)
    val outDir = gen.defaultOutputDir + "/standard/"
    gen.fileToFile(infile, outDir)

    val adt = util.Util.readFile("target/generated-sources/standard/example/idl/Defaults.scala")
    
    adt === util.Util.readFile("avrohugger-core/src/test/expected/standard/example/idl/Defaults.scala")
  }

  import util.GlobalTests
  for ((test, idx) <- GlobalTests.tests.zipWithIndex) {
    s"${idx + 17}. ${test.description}" in {
      test.toSpec(Standard).checkFileToFile
    }
  }
}