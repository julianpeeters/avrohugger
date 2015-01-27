# avrohugger
Schema-to-case-class code generation for working with Avro in Scala. Depends on Avro and Treehugger

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



## Usage (for Scala 2.10 and 2.11)
Get the dependency with:


    "com.julianpeeters" % "avrohugger" %% "0.0.2-SNAPSHOT"

Then get a `Generator` or `SpecificGenerator` and use `fromSchema` or `fromFile`:


    import avrohugger._

    val schemaFile = new File("path/to/schema")
    val generator = new Generator
    generator.fromFile(schemaFile, "optional/path/to/output") // default output path = "target/generated-sources/"


Or download the avrohugger-tools jar for Scala [2.10](https://search.maven.org/remotecontent?filepath=com/julianpeeters/avrohugger-tools_2.11/0.0.1/avrohugger-tools_2.10-0.0.1-assembly.jar) or Scala [2.11](https://search.maven.org/remotecontent?filepath=com/julianpeeters/avrohugger-tools_2.11/0.0.1/avrohugger-tools_2.11-0.0.1-assembly.jar) and use it like the avro-tools jar `Usage: [-string] (schema|protocol|datafile) input... outputdir`:


    // 'generate' generates Scala case class definitions
    java -jar /path/to/avrohugger-tools_2.11-0.0.1-assembly.jar generate schema user.avsc . 


    // TODO:
    // 'generate-specific' generates case class definitions that extend SpecificRecordBase
    // java -jar /path/to/avrohugger-tools_2.11-0.0.1-assembly.jar generate-specific schema user.avsc . 


## Future
* SpecificGenerator to generate case classes that extend SpecificRecordBase to be compatible with the Avro Specific API
* Support more avro types: enum, fixed, bytes, map.

#### Credits
Depends on Avro and Treehugger. Avrohugger-tools based strongly on avro-tools.

#### Fork away, just make sure the tests pass before you send a pull request.

#### Criticism is appreciated.