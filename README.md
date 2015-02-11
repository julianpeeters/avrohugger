# avrohugger
Schema-to-case-class code generation for working with Avro in Scala. 

* `avrohugger-core`: Generate source code dynamically at runtime for evaluation at a later step
* `avrohugger-tools`: Generate source code at the command line with the avrohugger-tools jar.
* `[sbt-avrohugger](https://github.com/julianpeeters/sbt-avrohugger)`: Generate source code at compile time with a sbt plugin.

Generates Scala case classes (for use with [Scalavro](https://github.com/GenslerAppsPod/scalavro) and [Salat-Avro](https://github.com/julianpeeters/salat-avro)), or case classes that implement `SpecificRecordBase` (for use with the Avro Specific API - Scalding, Spark, Avro, etc.).

Supports generating case classes with arbitrary fields of the following datatypes: 


* INT -> Int
* LONG -> Long
* FLOAT -> Float
* DOUBLE -> Double
* STRING -> String
* BOOLEAN -> Boolean
* NULL  -> Null
* MAP -> //TODO
* ENUM -> //TODO
* BYTES -> //TODO
* FIXED -> //TODO
* ARRAY -> List
* UNION -> Option
* RECORD -> case class



## Usage (for Scala 2.10 and 2.11)


####`avrohugger-core` 

Get the dependency with:

    "com.julianpeeters" % "avrohugger-core" %% "0.0.2"

Then get a `Generator` or `specific.SpecificGenerator` and use `fromSchema` or `fromFile`:


    import avrohugger._

    val schemaFile = new File("path/to/schema")
    val generator = new Generator
    generator.fromFile(schemaFile, "optional/path/to/output") // default output path = "target/generated-sources/"


####`avrohugger-tools`


Download the avrohugger-tools jar for Scala [2.10](https://search.maven.org/remotecontent?filepath=com/julianpeeters/avrohugger-tools_2.10/0.0.2/avrohugger-tools_2.10-0.0.2-assembly.jar) or Scala [2.11](https://search.maven.org/remotecontent?filepath=com/julianpeeters/avrohugger-tools_2.11/0.0.2/avrohugger-tools_2.11-0.0.2-assembly.jar)(20MB!) and use it like the avro-tools jar `Usage: [-string] (schema|protocol|datafile) input... outputdir`:


    // 'generate' generates Scala case class definitions
    java -jar /path/to/avrohugger-tools_2.11-0.0.2-assembly.jar generate schema user.avsc . 


    // 'generate-specific' generates case class definitions that extend SpecificRecordBase
    java -jar /path/to/avrohugger-tools_2.11-0.0.2-assembly.jar generate-specific schema user.avsc . 


####`sbt-avrohugger`

Also available as an [sbt plugin](https://github.com/julianpeeters/sbt-avrohugger) that adds a `generate` or `generate-specific` task to `compile` (an alternative to [macros](https://github.com/julianpeeters/avro-scala-macro-annotations)).


## Future

* Support more avro types: enum, fixed, bytes, map.


## Testing

The `scripted` task runs all tests. 

As per Doug Cutting's [recommendations](https://github.com/apache/avro/blob/trunk/lang/java/tools/src/test/java/org/apache/avro/tool/TestSpecificCompilerTool.java#L130) in the avro compiler tests, the string-based tests in `test` are augmented by `scripted` tests that generate and compile source that is run in de/serialization tests.


#### Credits
Depends on [Avro](https://github.com/apache/avro) and [Treehugger](https://github.com/eed3si9n/treehugger). `avrohugger-tools` is based on [avro-tools](http://avro.apache.org/docs/1.7.7/gettingstartedjava.html#Serializing+and+deserializing+with+code+generation).


#### Criticism is appreciated. Fork away, just make sure the tests pass before you send a pull request.