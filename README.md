# avrohugger

[![Join the chat at https://gitter.im/julianpeeters/avrohugger](https://badges.gitter.im/julianpeeters/avrohugger.svg)](https://gitter.im/julianpeeters/avrohugger?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)


**Schema-to-case-class code generation for working with Avro in Scala.**

* `avrohugger-core`: Generate source code at runtime for evaluation at a later step.
* `avrohugger-filesorter`: Sort schema files for proper compilation order.
* `avrohugger-tools`: Generate source code at the command line with the avrohugger-tools jar.

**Alternative Distributions:**

* **sbt**: `sbt-avrohugger` - Generate source code at compile time with an sbt plugin [found here](https://github.com/julianpeeters/sbt-avrohugger).
* **maven**: `avrohugger-maven-plugin` - Generate source code at compile time with a maven plugin [found here](https://github.com/makubi/avrohugger-maven-plugin).
* **on the web**: `avro2caseclass` - Generate source code from a web app, [found here](https://github.com/julianpeeters/avro2caseclass).

Table of contents
=================

  * [Supported Formats: `Standard`, `SpecificRecord`, `Scavro`](#generates-scala-case-classes-in-various-formats)
  * [Supported Datatypes](#supports-generating-case-classes-with-arbitrary-fields-of-the-following-datatypes)
  * [Protocol support](#protocol-support)
  * [Doc support](#doc-support)
  * [Usage](#usage)
    * [`avrohugger-core`](#avrohugger-core)
      * [Get the dependency](#get-the-dependency-with)
      * [Description](#description)
      * [Example](#example)
      * [Customizable type mapping](#customizable-type-mapping)
      * [Customizable namespace mapping](#customizable-namespace-mapping)
      * [Generate Classes Instead of Case Classes](#generate-classes-instead-of-case-classes)
    * [`avrohugger-filesorter`](#avrohugger-filesorter)
    * [`avrohugger-tools`](#avrohugger-tools)
  * [Warnings](#warnings)
  * [Best Practices](#best-practices)
  * [Testing](#testing)
  * [Credits](#credits)


##### Generates Scala case classes in various formats:

* `Standard` Vanilla case classes (for use with Apache Avro's [`GenericRecord`
API](http://avro.apache.org/docs/1.8.1/gettingstartedjava.html#Serializing+and+deserializing+without+code+generation), etc.)

* `SpecificRecord` Case classes that implement `SpecificRecordBase` and
therefore have mutable `var` fields (for use with the Avro Specific API -
Scalding, Spark, Avro, etc.).

* `Scavro` Case classes with immutable fields, intended to wrap Java generated
Avro classes (for use with the [Scavro](https://github.com/oedura/scavro#scavro-reader-and-writer)
runtime, Java classes provided separately (see [Scavro Plugin](https://github.com/oedura/scavro#scavro-plugin) or [sbt-avro](https://github.com/sbt/sbt-avro))).

##### Supports generating case classes with arbitrary fields of the following datatypes:

|Avro|`Standard`|`SpecificRecord`|`Scavro`|Notes|
|----------|---------|----------|---------|---------|
|INT|Int|Int|Int||
|LONG|Long|Long|Long||
|FLOAT|Float|Float|Float||
|DOUBLE|Double|Double|Double||
|STRING|String|String|String||
|BOOLEAN|Boolean|Boolean|Boolean||
|NULL|Null|Null|Null||
|MAP|Map|Map|Map||
|ENUM|scala.Enumeration<br>Scala case object<br>Java Enum|Java Enum|scala.Enumeration<br>Scala case object<br>Java Enum| See [Customizable Type Mapping](https://github.com/julianpeeters/avrohugger#customizable-type-mapping)|
|BYTES|Array[Byte]|Array[Byte]|Array[Byte]||
|FIXED|//TODO|//TODO|//TODO||
|ARRAY|List<br>Array<br>Vector|List<br>Array<br>Vector|Array<br>List<br>Vector| See [Customizable Type Mapping](https://github.com/julianpeeters/avrohugger#customizable-type-mapping)|
|UNION|Option<br>Either<br>Shapeless Coproduct|Option|Option| See [Customizable Type Mapping](https://github.com/julianpeeters/avrohugger#customizable-type-mapping)|
|RECORD|case class|case class extending `SpecificRecordBase`|case class extending `AvroSerializeable`|



##### Protocol Support:

* `.avdl`, `.avpr`, and json protocol strings are generated as ADTs if they define more than one Scala definition.

* For `SpecificRecord`, if the protocol contains messages then no ADT is generated, and an RPC trait is generated instead.


##### Doc Support:

* `.avdl`: Comments that begin with `/**` are used as the documentation string for the type or field definition that follows the comment.

* `.avsc`, `.avpr`, and `.avro`: Docs in Avro schemas are used to define a case class' ScalaDoc

* `.scala`: ScalaDocs of case class definitions are used to define record and field docs


_Note:_ Currently [Treehugger](http://eed3si9n.com/treehugger/comments.html#Scaladoc+style+comments) appears to generate Javadoc style docs (thus compatible with ScalaDoc style).




## Usage

* **For Scala 2.10, 2.11, and 2.12**
* **Generating Code Compatible with Scala 2.10, 2.11, and 2.12**


#### `avrohugger-core`

##### Get the dependency with:

    "com.julianpeeters" %% "avrohugger-core" % "1.0.0-RC1"


##### Description:

Instantiate a `Generator` with `Standard`, `Scavro`, or `SpecificRecord` source
formats. Then use


`tToFile(input: T, outputDir: String): Unit`


or


`tToStrings(input: T): List[String]`


where `T` can be `File`, `Schema`, or `String`.

##### Example



    import avrohugger.Generator
    import format.SpecificRecord

    val schemaFile = new File("path/to/schema")
    val generator = new Generator(SpecificRecord)
    generator.fileToFile(schemaFile, "optional/path/to/output") // default output path = "target/generated-sources"


where an input `File` can be `.avro`, `.avsc`, `.avpr`, or `.avdl`,


and where an input `String` can be the string representation of an Avro schema,
protocol, IDL, or a set of case classes that you'd like to have implement
`SpecificRecordBase`.


##### Customizable Type Mapping:

To reassign Scala types to Avro types, use the following:
    
    import avrohugger.format.SpecificRecord
    import avrohugger.types.ScalaVector
    
    val myScalaTypes = Some(SpecificRecord.defaultTypes.copy(array = ScalaVector))
    val generator = new Generator(SpecificRecord, avroScalaCustomTypes = myScalaTypes)
    
* `array` can be assigned to `ScalaArray`, `ScalaList`, and `ScalaVector`
* `enum` can be assigned to `JavaEnum`, `ScalaCaseObjectEnum`, and `ScalaEnumeration`
* `union` can be assigned to `OptionEitherShapelessCoproduct` and `OptionShapelessCoproduct`
* `int`, `long`, `float`, `double` can be assigned to `ScalaInt`, `ScalaLong`, `ScalaFloat`, `ScalaDouble`

##### Customizable Namespace Mapping:

Namespaces can be reassigned by instantiating a `Generator` with a custom
namespace map (please see warnings below):


    val generator = new Generator(SpecificRecord, avroScalaCustomNamespace = Map("oldnamespace"->"newnamespace"))


_Scavro_: by default, a "model" package is appended to the namespace to create a
 Scala namespace that does not conflict with Scavro's generated Java. To
 override, either customize each package namespace separately (preempting the
 use of the default package name), or override the package name like so:

    val generator = new Generator(SpecificRecord, avroScalaCustomNamespace = Map("SCAVRO_DEFAULT_PACKAGE$"->"scavro"))


##### Generate Classes Instead of Case Classes

Generate simple classes instead of case classes when fields.size > 22, useful for generating code for Scala 2.10 from large schemas.

    val generator = new Generator(SpecificRecord, restrictedFieldNumber = true)


#### `avrohugger-filesorter`


##### Get the dependency with:

    "com.julianpeeters" %% "avrohugger-filesorter" % "1.0.0-RC1"
    

##### Description:

To ensure dependent schemas are compiled in the proper order (thus avoiding `org.apache.avro.SchemaParseException: Undefined name: "com.example.MyRecord"` parser errors), sort avsc and avdl files with the `sortSchemaFiles` method on `AvscFileSorter` and `AvdlFileSorter`respectively.


##### Example:


    import avrohugger.filesorter.AvscFileSorter
    import java.io.File
    
    val sorted: List[File] = AvscFileSorter.sortSchemaFiles((srcDir ** "*.avsc")


#### `avrohugger-tools`


  Download the avrohugger-tools jar for Scala [2.10](https://search.maven.org/remotecontent?filepath=com/julianpeeters/avrohugger-tools_2.10/1.0.0-RC1/avrohugger-tools_2.10-1.0.0-RC1-assembly.jar), Scala [2.11](https://search.maven.org/remotecontent?filepath=com/julianpeeters/avrohugger-tools_2.11/1.0.0-RC1/avrohugger-tools_2.11-1.0.0-RC1-assembly.jar), or Scala [2.12](https://search.maven.org/remotecontent?filepath=com/julianpeeters/avrohugger-tools_2.12/1.0.0-RC1/avrohugger-tools_2.12-1.0.0-RC1-assembly.jar) (>30MB!) and use it like the avro-tools jar `Usage: [-string] (schema|protocol|datafile) input... outputdir`:


* `generate` generates Scala case class definitions:

`java -jar /path/to/avrohugger-tools_2.12-1.0.0-RC1-assembly.jar generate schema user.avsc . `


* `generate-specific` generates definitions that extend Avro's `SpecificRecordBase`:

`java -jar /path/to/avrohugger-tools_2.12-1.0.0-RC1-assembly.jar generate-specific schema user.avsc . `


* `generate-scavro` generates definitions that extend Scavro's `AvroSerializable`:

`java -jar /path/to/avrohugger-tools_2.12-1.0.0-RC1-assembly.jar generate-scavro schema user.avsc . `


## Warnings

1) If your framework is one that relies on reflection to get the Schema, it
will fail since Scala fields are private. Therefore preempt it by passing in
a Schema to DatumReaders and DatumWriters (as in the Avro example above).

2) For the `SpecificRecord` format, generated case class fields must be
mutable (`var`) in order to be compatible with the SpecificRecord API. _Note:_
If your framework allows `GenericRecord`, [avro4s](https://github.com/sksamuel/avro4s)
provides a type class that converts to and from immutable case classes cleanly
(though seems to fail on maps and case object enums as of v1.4.3).

3) When the input is a case class definition `String`, import statements are
not supported, please use fully qualified type names if using records/classes
from multiple namespaces.

4) By default, a schema's namespace is used as a package name. In the case of
the Scavro output format, the default is the namespace with `model` appended.

5) While Scavro format uses custom namespaces in a way that leaves it
unaffected, most formats fail on schemas with records within unions
(see [avro forum](see http://apache-avro.679487.n3.nabble.com/Deserialize-with-different-schema-td4032782.html)).

6) `SpecificRecord` requires that `enum` be represented as `JavaEnum`


## Best Practices

1) Avoid recursive schemas since they can cause compatibility issues if trying
to flow data into a system that doesn't support them (e.g., Hive).

2) Use namespaces to ensure compatibility when importing into Java/Scala.

3) Use default field values in case of future schema evolution ([further reading](https://github.com/julianpeeters/avrohugger/issues/23)).


## Testing

To test for regressions, please run `sbt:avrohugger> + test`.

To test that generated code can be de/serialized as expected, please run:
1) `sbt:avrohugger> + publishLocal`
2) then clone sbt-avrohugger and update its avrohugger dependency to the locally
published version
3) finally run `sbt:sbt-avrohugger> scripted avrohugger/*`, or, e.g., `scripted avrohugger/GenericSerializationTests`


#### Credits
Depends on [Avro](https://github.com/apache/avro) and [Treehugger](https://github.com/eed3si9n/treehugger). `avrohugger-tools` is based on [avro-tools](http://avro.apache.org/docs/1.7.7/gettingstartedjava.html#Serializing+and+deserializing+with+code+generation).


Contributors:
- [Marius Soutier](https://github.com/mariussoutier)
- [Paul Pearcy](https://github.com/ppearcy)
- [Stefano Galarraga](https://github.com/galarragas)
- [Brian London](https://github.com/BrianLondon)
- [Matt Allen](https://github.com/Matt343)
- [Lars Albertsson](https://github.com/lallea)
- [alancnet](https://github.com/alancnet)
- [C-zito](https://github.com/C-Zito)
- [Eugene Platonov](https://github.com/jozic)
- [Matt Coffin](https://github.com/mcoffin)
- [Tim Chan](https://github.com/timchan-lumoslabs)
- [Jerome Wacongne](https://github.com/ch4mpy)
- [Ryan Koval](http://github.ryankoval.com)
- [Saket](https://github.com/skate056)
- [Jon Morra](https://github.com/jon-morra-zefr)
- [Simonas Gelazevicius](https://github.com/simsasg)
- [Daniel Davis](https://github.com/wabu)
- [Raúl Raja Martínez](https://github.com/raulraja)
- [Paul Snively](https://github.com/PaulAtBanno)
- [Zach Cox](https://github.com/zcox)

##### Criticism is appreciated.

##### Fork away, just make sure the tests pass before sending a pull request.
