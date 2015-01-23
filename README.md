# avrohugger
Schema-to-case-class code generation for working with Avro in Scala.

Case classes can be plain (for use with Scalavro and Salat-Avro), // TODO: or they can extend `SpecificRecordBase` (for use with the Avro Specific API - Scalding, Spark, Avro, etc.).

Supports generating case classes with arbitrary fields of the following datatypes: 


* INT -> Int
* LONG -> Long
* FLOAT -> Float
* DOUBLE -> Double
* STRING -> String
* BOOLEAN -> Boolean
* NULL  -> Null
* MAP -> //TODO
* BYTES -> //TODO
* FIXED -> //TODO
* ARRAY -> List
* UNION -> Option
* RECORD -> case class



## Usage
Get the dependency with:


    "com.julianpeeters" % "avrohugger" %% "0.0.1"

Then get a `Generator` or `SpecificGenerator` and use `fromSchema` or `fromFile`:


    import avrohugger._

    val schemaFile = new File("path/to/schema")
    val generator = new Generator
    generator.fromFile(schemaFile, "optional/path/to/output") // default output path = "target/generated-sources/"


Or download the avrohugger-tools jar and use it like the avro-tools jar:


    // 'generate' generates Scala case class definitions
    java -jar /path/to/avrohugger-tools_2.11-0.0.1-assembly.jar generate schema user.avsc . 


    // TODO:
    // 'generate-specific' generates case class definitions that extend SpecificRecordBase
    // java -jar /path/to/avrohugger-tools_2.11-0.0.1-assembly.jar generate-specific schema user.avsc . 


## Future
* SpecificGenerator
* Support protocols
* Support .avro datafiles

#### Fork away, just make sure the tests pass before you send a pull request.

#### Criticism is appreciated.