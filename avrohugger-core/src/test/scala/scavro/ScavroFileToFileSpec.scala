package avrohugger
package test
package scavro

import java.io.File

import avrohugger._
import avrohugger.format.Scavro
import org.specs2._

class ScavroFileToFileSpec extends mutable.Specification {

  "Scavro Generator fileToFiles method" should {
      
    // tests specific to fileToX
    "A. correctly generate a simple case class definition from AVRO" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/twitter.avro")
      val gen = new Generator(Scavro)
      val outDir = gen.defaultOutputDir + "/scavro/"
      gen.fileToFile(infile, outDir)
      
      val source = util.Util.readFile("target/generated-sources/scavro/com/miguno/avro/model/twitter_schema.scala")
      
      source === util.Util.readFile("avrohugger-core/src/test/expected/scavro/com/miguno/avro/model/twitter_schema.scala")
    }
    
    // tests specific to fileToFile
    "0. not generate copy of imported classes in the importing package" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
      val gen = new Generator(Scavro)
      val outDir = gen.defaultOutputDir + "/scavro/"
      gen.fileToFile(infile, outDir)

      (new File(s"target/generated-sources/scavro/example/idl/model/ExternalDependency.scala")).exists === false
    }
    
    // tests common to fileToX and stringToX
    "1. correctly generate from a protocol with messages" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/mail.avpr")
      val gen = new Generator(Scavro)
      val outDir = gen.defaultOutputDir + "/scavro/"
      gen.fileToFile(infile, outDir)
      
      val source = util.Util.readFile(s"$outDir/example/proto/model/Message.scala")
      
      source === util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/proto/model/Message.scala")
    }
    
    "2. correctly generate a simple case class definition in a package" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/user.avsc")
      val gen = new Generator(Scavro)
      val outDir = gen.defaultOutputDir + "/scavro/"
      gen.fileToFile(infile, outDir)
      
      val source = util.Util.readFile("target/generated-sources/scavro/example/model/User.scala")
      
      source === util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/model/User.scala")
    }

    "3. correctly generate a simple case class definition in the default package" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/AvroTypeProviderTestNoNamespace.avsc")
      val gen = new Generator(Scavro)
      val outDir = gen.defaultOutputDir + "/scavro/"
      gen.fileToFile(infile, outDir) must throwA(new java.lang.RuntimeException("Scavro requires a namespace because Java classes cannot be imported from the default package"))
    }

    "4. correctly generate a nested case class definition from a schema" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/nested.avsc")
      val gen = new Generator(Scavro)
      val outDir = gen.defaultOutputDir + "/scavro/"
      gen.fileToFile(infile, outDir)

      val source0 = util.Util.readFile("target/generated-sources/scavro/example/model/Level0.scala")
      val source1 = util.Util.readFile("target/generated-sources/scavro/example/model/Level1.scala")
      val source2 = util.Util.readFile("target/generated-sources/scavro/example/model/Level2.scala")

      source0 === util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/model/Level0.scala")
      source1 === util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/model/Level1.scala")
      source2 === util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/model/Level2.scala")
    }

    "5. correctly generate a nested case class from IDL" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/nested.avdl")
      val gen = new Generator(Scavro)
      val outDir = gen.defaultOutputDir + "/scavro/"
      gen.fileToFile(infile, outDir)
      
      val source = util.Util.readFile("target/generated-sources/scavro/example/idl/model/NestedProtocol.scala")
      
      source === util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/model/NestedProtocol.scala")
    }

    "6. correctly generate a recursive case class from IDL" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/recursive.avdl")
      val gen = new Generator(Scavro)
      val outDir = gen.defaultOutputDir + "/scavro/"
      gen.fileToFile(infile, outDir)

      val source = util.Util.readFile("target/generated-sources/scavro/example/idl/model/Recursive.scala")
      
      source === util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/model/Recursive.scala")
    }

    "7. correctly generate enums from schema" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/enums.avsc")
      val gen = new Generator(Scavro)
      val outDir = gen.defaultOutputDir + "/scavro/"
      gen.fileToFile(infile, outDir)

      val source = util.Util.readFile("target/generated-sources/scavro/example/model/Suit.scala")
      
      source === util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/model/Suit.scala")
    }

    "8. correctly generate enums from protocol" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/enums.avpr")
      val gen = new Generator(Scavro)
      val outDir = gen.defaultOutputDir + "/scavro/"
      gen.fileToFile(infile, outDir)

      val source = util.Util.readFile("target/generated-sources/scavro/example/proto/model/EnumProtocol.scala")
      
      source === util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/proto/model/EnumProtocol.scala")
    }
 
    "9. correctly generate enums from IDL" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/enums.avdl")
      val gen = new Generator(Scavro)
      val outDir = gen.defaultOutputDir + "/scavro/"
      gen.fileToFile(infile, outDir)

      val source = util.Util.readFile("target/generated-sources/scavro/example/idl/model/EnumProtocol.scala")
    
      source === util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/model/EnumProtocol.scala")
    }
    
    "10. correctly generate nested enums" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/enums_nested.avsc")
      val gen = new Generator(Scavro)
      val outDir = gen.defaultOutputDir + "/scavro/"
      gen.fileToFile(infile, outDir)
      
      val sourceEnum = util.Util.readFile("target/generated-sources/scavro/example/model/Direction.scala")
      val sourceRecord = util.Util.readFile("target/generated-sources/scavro/example/model/Compass.scala")
      
      sourceEnum === util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/model/Direction.scala")
      sourceRecord === util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/model/Compass.scala")
    }

    "11. correctly generate bytes from schema" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/bytes.avsc")
      val gen = new Generator(Scavro)
      val outDir = gen.defaultOutputDir + "/scavro/"
      gen.fileToFile(infile, outDir)

      val source = util.Util.readFile("target/generated-sources/scavro/example/model/BinarySc.scala")
      
      source === util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/model/BinarySc.scala")
    }

    "12. correctly generate bytes from protocol" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/bytes.avpr")
      val gen = new Generator(Scavro)
      val outDir = gen.defaultOutputDir + "/scavro/"
      gen.fileToFile(infile, outDir)

      val source = util.Util.readFile("target/generated-sources/scavro/example/proto/model/BinaryPr.scala")

      source === util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/proto/model/BinaryPr.scala")
    }

    "13. correctly generate bytes from IDL" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/bytes.avdl")
      val gen = new Generator(Scavro)
      val outDir = gen.defaultOutputDir + "/scavro/"
      gen.fileToFile(infile, outDir)

      val source = util.Util.readFile("target/generated-sources/scavro/example/idl/model/BinaryIdl.scala")
    
      source === util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/model/BinaryIdl.scala")
    }

    "14. correctly generate records depending on others defined in a different- and same-namespaced AVDL and AVSC" in {
      val importing = new java.io.File("avrohugger-core/src/test/avro/import.avdl")
      val gen = new Generator(Scavro)
      val outDir = gen.defaultOutputDir + "/scavro/"
      gen.fileToFile(importing, outDir)

      val sourceADT = util.Util.readFile("target/generated-sources/scavro/example/idl/model/ImportProtocol.scala")
      val sourceDep1 = util.Util.readFile("target/generated-sources/scavro/example/idl/model/Defaults.scala")
      val sourceDep2 = util.Util.readFile("target/generated-sources/scavro/other/ns/model/ExternalDependency.scala")
      val sourceDep3 = util.Util.readFile("target/generated-sources/scavro/other/ns/model/Suit.scala")
      
      sourceADT === util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/model/ImportProtocol.scala")
      sourceDep1 === util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/model/Defaults.scala")
      sourceDep2 === util.Util.readFile("avrohugger-core/src/test/expected/scavro/other/ns/model/ExternalDependency.scala")
      sourceDep3 === util.Util.readFile("avrohugger-core/src/test/expected/scavro/other/ns/model/Suit.scala")
    }

    "15. correctly generate an empty case class definition" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/AvroTypeProviderTestEmptyRecord.avdl")
      val gen = new Generator(Scavro)
      val outDir = gen.defaultOutputDir + "/scavro/"
      gen.fileToFile(infile, outDir)
    
      val source = util.Util.readFile("target/generated-sources/scavro/test/model/Calculator.scala")
    
      source === util.Util.readFile("avrohugger-core/src/test/expected/scavro/test/model/Calculator.scala")
    }
   
    "16. correctly generate default values" in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/defaults.avdl")
      val gen = new Generator(Scavro)
      val outDir = gen.defaultOutputDir + "/scavro/"
      gen.fileToFile(infile, outDir)

      val adt = util.Util.readFile("target/generated-sources/scavro/example/idl/model/Defaults.scala")
      
      adt === util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/model/Defaults.scala")
    }

  }
}
