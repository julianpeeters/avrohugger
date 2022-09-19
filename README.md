# avrohugger


[![Scala CI](https://github.com/julianpeeters/avrohugger/workflows/Scala%20CI/badge.svg)](https://github.com/julianpeeters/avrohugger/actions?query=workflow%3A%22Scala+CI%22)
[![Join the chat at https://gitter.im/julianpeeters/avrohugger](https://badges.gitter.im/julianpeeters/avrohugger.svg)](https://gitter.im/julianpeeters/avrohugger?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.julianpeeters/avrohugger-core_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.julianpeeters/avrohugger-core_2.12)

**Schema-to-case-class code generation for working with Avro in Scala.**

* `avrohugger-core`: Generate source code at runtime for evaluation at a later step.
* `avrohugger-filesorter`: Sort schema files for proper compilation order.
* `avrohugger-tools`: Generate source code at the command line with the avrohugger-tools jar.

**Alternative Distributions:**

* [sbt](https://github.com/julianpeeters/sbt-avrohugger): `sbt-avrohugger` - Generate source code at compile time with an sbt plugin.
* [Maven](https://github.com/makubi/avrohugger-maven-plugin): `avrohugger-maven-plugin` - Generate source code at compile time with a maven plugin.
* [Mill](https://github.com/joan38/mill-avro): `mill-avro` - Generate source code at compile time with a Mill plugin.
* [Gradle](https://github.com/zladovan/gradle-avrohugger-plugin): `gradle-avrohugger-plugin` - Generate source code at compile time with a gradle plugin.
* [On the web](https://github.com/julianpeeters/avro2caseclass): `avro2caseclass` - Generate source code from a web app,.
* [mu-rpc](https://github.com/higherkindness/mu-scala): `mu-scala` - Generate rpc models, messages, clients, and servers.

Table of contents
=================

  * [Supported Formats: `Standard`, `SpecificRecord`, `Scavro`](#generates-scala-case-classes-in-various-formats)
  * [Supported Datatypes](#supports-generating-case-classes-with-arbitrary-fields-of-the-following-datatypes)
  * [Logical Types Support](#logical-types-support)
  * [Protocol Support](#protocol-support)
  * [Doc Support](#doc-support)
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
API](http://avro.apache.org/docs/1.9.1/gettingstartedjava.html#Serializing+and+deserializing+without+code+generation), etc.)

* `SpecificRecord` Case classes that implement `SpecificRecordBase` and
therefore have mutable `var` fields (for use with the Avro Specific API -
Scalding, Spark, Avro, etc.).

* `Scavro` Case classes with immutable fields, intended to wrap Java generated
Avro classes (for use with the [Scavro](https://github.com/oedura/scavro#scavro-reader-and-writer)
runtime, Java classes provided separately (see [Scavro Plugin](https://github.com/oedura/scavro#scavro-plugin) or [sbt-avro](https://github.com/sbt/sbt-avro))).

##### Supports generating case classes with arbitrary fields of the following datatypes:

|Avro|`Standard`|`SpecificRecord`|`Scavro`|Notes|
|----------|---------|----------|---------|---------|
|INT|Int|Int|Int| See [Logical Types: `date`](https://github.com/julianpeeters/avrohugger#logical-types-support)|
|LONG|Long|Long|Long| See [Logical Types: `timestamp-millis`](https://github.com/julianpeeters/avrohugger#logical-types-support)|
|FLOAT|Float|Float|Float||
|DOUBLE|Double|Double|Double||
|STRING|String|String|String||
|BOOLEAN|Boolean|Boolean|Boolean||
|NULL|Null|Null|Null||
|MAP|Map|Map|Map||
|ENUM|scala.Enumeration<br>Scala case object<br>Java Enum<br>EnumAsScalaString|Java Enum<br>EnumAsScalaString|scala.Enumeration<br>Scala case object<br>Java Enum<br>EnumAsScalaString| See [Customizable Type Mapping](https://github.com/julianpeeters/avrohugger#customizable-type-mapping)|
|BYTES|Array[Byte]<br>BigDecimal|Array[Byte]<br>BigDecimal|Array[Byte]|See [Logical Types: `decimal`](https://github.com/julianpeeters/avrohugger#logical-types-support)|
|FIXED|case class<br>case class + schema|case class extending `SpecificFixed`|//TODO|See [Logical Types: `decimal`](https://github.com/julianpeeters/avrohugger#logical-types-support)|
|ARRAY|Seq<br>List<br>Array<br>Vector|Seq<br>List<br>Array<br>Vector|Array<br>Seq<br>List<br>Vector| See [Customizable Type Mapping](https://github.com/julianpeeters/avrohugger#customizable-type-mapping)|
|UNION|Option<br>Either<br>Shapeless Coproduct|Option|Option| See [Customizable Type Mapping](https://github.com/julianpeeters/avrohugger#customizable-type-mapping)|
|RECORD|case class<br>case class + schema|case class extending `SpecificRecordBase`|case class extending `AvroSerializeable`| See [Customizable Type Mapping](https://github.com/julianpeeters/avrohugger#customizable-type-mapping)|
|PROTOCOL|_No Type_<br>Scala ADT|RPC trait<br>Scala ADT|_No Type_<br>Scala ADT| See [Customizable Type Mapping](https://github.com/julianpeeters/avrohugger#customizable-type-mapping)|
|Date|java.time.LocalDate<br>java.sql.Date|java.time.LocalDate<br>java.sql.Date|Not yet supported| See [Customizable Type Mapping](https://github.com/julianpeeters/avrohugger#customizable-type-mapping)|
|TimestampMillis|java.time.Instant<br>java.sql.Timestamp|java.time.Instant<br>java.sql.Timestamp|Not yet supported|  See [Customizable Type Mapping](https://github.com/julianpeeters/avrohugger#customizable-type-mapping)|
|UUID|java.util.UUID|java.util.UUID|Not yet supported|  See [Customizable Type Mapping](https://github.com/julianpeeters/avrohugger#customizable-type-mapping)|
|Decimal|BigDecimal|BigDecimal|Not yet supported|  See [Customizable Type Mapping](https://github.com/julianpeeters/avrohugger#customizable-type-mapping)|

##### Logical Types Support:

_NOTE: Currently logical types are only supported for `Standard` and `SpecificRecord` formats_

* `date`: Annotates Avro `int` schemas to generate `java.time.LocalDate` or `java.sql.Date` (See [Customizable Type Mapping](https://github.com/julianpeeters/avrohugger#customizable-type-mapping)). Examples: [avdl](https://github.com/julianpeeters/sbt-avrohugger/blob/master/src/sbt-test/avrohugger/GenericSerializationTests/src/main/avro/logical.avdl#L9), [avsc](https://github.com/julianpeeters/sbt-avrohugger/blob/master/src/sbt-test/avrohugger/GenericSerializationTests/src/main/avro/logical.avsc#L22-L27).
* `decimal`: Annotates Avro `bytes` and `fixed` schemas to generate `BigDecimal`. Examples: [avdl](https://github.com/julianpeeters/sbt-avrohugger/blob/master/src/sbt-test/avrohugger/GenericSerializationTests/src/main/avro/logical.avdl#L6), [avsc](https://github.com/julianpeeters/sbt-avrohugger/blob/master/src/sbt-test/avrohugger/GenericSerializationTests/src/main/avro/logical.avsc#L6-L14).
* `timestamp-millis`: Annotates Avro `long` schemas to genarate `java.time.Instant` or `java.sql.Timestamp` (See [Customizable Type Mapping](https://github.com/julianpeeters/avrohugger#customizable-type-mapping)). Examples: [avdl](https://github.com/julianpeeters/sbt-avrohugger/blob/master/src/sbt-test/avrohugger/GenericSerializationTests/src/main/avro/logical.avdl#L8), [avsc](https://github.com/julianpeeters/sbt-avrohugger/blob/master/src/sbt-test/avrohugger/GenericSerializationTests/src/main/avro/logical.avsc#L15-L21).
* `uuid`: Annotates Avro `string` schemas (but not idls as of avro 1.8.2) to generate `java.util.UUID` (See [Customizable Type Mapping](https://github.com/julianpeeters/avrohugger#customizable-type-mapping)). Example: [avsc](https://github.com/julianpeeters/sbt-avrohugger/blob/master/src/sbt-test/avrohugger/GenericSerializationTests/src/main/avro/logical.avsc#L29-L35).
* `time-millis`: Annotates Avro `int` schemas to genarate `java.time.LocalTime` or `java.sql.Time`

##### Protocol Support:

* the records defined in `.avdl`, `.avpr`, and json protocol strings can be generated as ADTs if the protocols define more than one Scala definition (_note:_ message definitions are ignored when this setting is used). See [Customizable Type Mapping](https://github.com/julianpeeters/avrohugger#customizable-type-mapping).

* For `SpecificRecord`, if the protocol contains messages then an RPC trait is generated (instead of generating and ADT, or ignoring the message definitions).


##### Doc Support:

* `.avdl`: Comments that begin with `/**` are used as the documentation string for the type or field definition that follows the comment.

* `.avsc`, `.avpr`, and `.avro`: Docs in Avro schemas are used to define a case class' ScalaDoc

* `.scala`: ScalaDocs of case class definitions are used to define record and field docs


_Note:_ Currently [Treehugger](http://eed3si9n.com/treehugger/comments.html#Scaladoc+style+comments) appears to generate Javadoc style docs (thus compatible with ScalaDoc style).




## Usage

* **Library For Scala 2.12, and 2.13**
* **Parses Schemas and IDLs with Avro version 1.11**
* **Generates Code Compatible with Scala 2.12, 2.13**



#### `avrohugger-core`

##### Get the dependency with:

    "com.julianpeeters" %% "avrohugger-core" % "1.2.1"


##### Description:

Instantiate a `Generator` with `Standard`, `Scavro`, or `SpecificRecord` source
formats. Then use


`tToFile(input: T, outputDir: String): Unit`


or


`tToStrings(input: T): List[String]`


where `T` can be `File`, `Schema`, or `String`.

##### Example



    import avrohugger.Generator
    import avrohugger.format.SpecificRecord
    import java.io.File

    val schemaFile = new File("path/to/schema")
    val generator = new Generator(SpecificRecord)
    generator.fileToFile(schemaFile, "optional/path/to/output") // default output path = "target/generated-sources"


where an input `File` can be `.avro`, `.avsc`, `.avpr`, or `.avdl`,


and where an input `String` can be the string representation of an Avro schema,
protocol, IDL, or a set of case classes that you'd like to have implement
`SpecificRecordBase`.


##### Customizable Type Mapping:

To reassign Scala types to Avro types, use the following (e.g. for customizing `Specific`):
    
    import avrohugger.format.SpecificRecord
    import avrohugger.types.ScalaVector
    
    val myScalaTypes = Some(SpecificRecord.defaultTypes.copy(array = ScalaVector))
    val generator = new Generator(SpecificRecord, avroScalaCustomTypes = myScalaTypes)

* `record` can be assigned to `ScalaCaseClass` and `ScalaCaseClassWithSchema`(with schema in a companion object)
* `array` can be assigned to `ScalaSeq`, `ScalaArray`, `ScalaList`, and `ScalaVector`
* `enum` can be assigned to `JavaEnum`, `ScalaCaseObjectEnum`, `EnumAsScalaString`, and `ScalaEnumeration`
* `fixed` can be assigned to , `ScalaCaseClassWrapper` and `ScalaCaseClassWrapperWithSchema`(with schema in a companion object)
* `union` can be assigned to  `OptionShapelessCoproduct`, `OptionEitherShapelessCoproduct`, or `OptionalShapelessCoproduct`
* `int`, `long`, `float`, `double` can be assigned to `ScalaInt`, `ScalaLong`, `ScalaFloat`, `ScalaDouble`
* `protocol` can be assigned to `ScalaADT` and `NoTypeGenerated`
* `decimal` can be assigned to e.g. `ScalaBigDecimal(Some(BigDecimal.RoundingMode.HALF_EVEN))` and `ScalaBigDecimalWithPrecision(None)` (via Shapeless Tagged Types)

##### Customizable Namespace Mapping:

Namespaces can be reassigned by instantiating a `Generator` with a custom
namespace map (please see warnings below):


    val generator = new Generator(SpecificRecord, avroScalaCustomNamespace = Map("oldnamespace"->"newnamespace"))


_Scavro_: by default, a "model" package is appended to the namespace to create a
 Scala namespace that does not conflict with Scavro's generated Java. To
 override, either customize each package namespace separately (preempting the
 use of the default package name), or override the package name like so:

    val generator = new Generator(SpecificRecord, avroScalaCustomNamespace = Map("SCAVRO_DEFAULT_PACKAGE$"->"scavro"))
    
Wildcarding the beginning of a namespace is permitted, place a single asterisk after the prefix that you want to map and any matching schema will have its 
namespace rewritten. Multiple conflicting wildcards are not permitted.

    val generator = new Generator(SpecificRecord, avroScalaCustomNamespace = Map("example.*"->"example.newnamespace"))


#### `avrohugger-filesorter`


##### Get the dependency with:

    "com.julianpeeters" %% "avrohugger-filesorter" % "1.2.1"
    

##### Description:

To ensure dependent schemas are compiled in the proper order (thus avoiding `org.apache.avro.SchemaParseException: Undefined name: "com.example.MyRecord"` parser errors), sort avsc and avdl files with the `sortSchemaFiles` method on `AvscFileSorter` and `AvdlFileSorter`respectively.


##### Example:


    import avrohugger.filesorter.AvscFileSorter
    import java.io.File
    
    val sorted: List[File] = AvscFileSorter.sortSchemaFiles((srcDir ** "*.avsc")


#### `avrohugger-tools`


  Download the avrohugger-tools jar for Scala [2.12](https://search.maven.org/remotecontent?filepath=com/julianpeeters/avrohugger-tools_2.12/1.2.1/avrohugger-tools_2.12-1.2.1-assembly.jar), or Scala [2.13](https://search.maven.org/remotecontent?filepath=com/julianpeeters/avrohugger-tools_2.13/1.2.1/avrohugger-tools_2.13-1.2.1-assembly.jar) (>30MB!) and use it like the avro-tools jar `Usage: [-string] (schema|protocol|datafile) input... outputdir`:


* `generate` generates Scala case class definitions:

`java -jar /path/to/avrohugger-tools_2.12-1.2.1-assembly.jar generate schema user.avsc . `


* `generate-specific` generates definitions that extend Avro's `SpecificRecordBase`:

`java -jar /path/to/avrohugger-tools_2.12-1.2.1-assembly.jar generate-specific schema user.avsc . `


* `generate-scavro` generates definitions that extend Scavro's `AvroSerializable`:

`java -jar /path/to/avrohugger-tools_2.12-1.2.1-assembly.jar generate-scavro schema user.avsc . `


## Warnings

1) If your framework is one that relies on reflection to get the Schema, it
will fail since Scala fields are private. Therefore preempt it by passing in
a Schema to DatumReaders and DatumWriters (e.g. `val sdw = SpecificDatumWriter[MyRecord](schema)`).

2) For the `SpecificRecord` format, generated case class fields must be
mutable (`var`) in order to be compatible with the SpecificRecord API. _Note:_
If your framework allows `GenericRecord`, [avro4s](https://github.com/sksamuel/avro4s)
provides a type class that converts to and from immutable case classes cleanly.

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

| | | |
| :---         |     :---      |          :--- |
| [Marius Soutier](https://github.com/mariussoutier) </br> [Brian London](https://github.com/BrianLondon) </br> [alancnet](https://github.com/alancnet) </br> [Matt Coffin](https://github.com/mcoffin) </br> [Ryan Koval](http://github.ryankoval.com) </br> [Simonas Gelazevicius](https://github.com/simsasg) </br> [Paul Snively](https://github.com/PaulAtBanno) </br> [Marco Stefani](https://github.com/inafets) </br> [Andrew Gustafson](https://github.com/agustafson) </br> [Kostya Golikov](https://github.com/lazyval) </br> [Plínio Pantaleão](https://github.com/plinioj) </br> [Sietse de Kaper](https://github.com/targeter) </br> [Martin Mauch](https://github.com/nightscape) </br> [Konstantin](https://github.com/tyger) </br> [Adam Drakeford](https://github.com/dr4ke616) </br> [Carlos Silva](https://github.com/alchimystic) | [Paul Pearcy](https://github.com/ppearcy) </br> [Matt Allen](https://github.com/Matt343) </br> [C-zito](https://github.com/C-Zito) </br> [Tim Chan](https://github.com/timchan-lumoslabs) </br> [Saket](https://github.com/skate056) </br> [Daniel Davis](https://github.com/wabu) </br> [Zach Cox](https://github.com/zcox) </br> [Diego E. Alonso Blas](https://github.com/diesalbla) </br> [Fede Fernández](https://github.com/fedefernandez) </br> [Rob Landers](https://github.com/withinboredom) </br> [Simon Petty](https://github.com/simonpetty) </br> [Andreas Drobisch](https://github.com/adrobisch) </br> [natefitzgerald](https://github.com/natefitzgerald) </br> [Timo Schmid](https://github.com/timo-schmid) </br> [mcenkar](https://github.com/mcenkar) </br> [Luca Tronchin](https://github.com/ltronky) | [Stefano Galarraga](https://github.com/galarragas) </br> [Lars Albertsson](https://github.com/lallea) </br> [Eugene Platonov](https://github.com/jozic) </br> [Jerome Wacongne](https://github.com/ch4mpy) </br> [Jon Morra](https://github.com/jon-morra-zefr) </br> [Raúl Raja Martínez](https://github.com/raulraja) </br> [Kaur Matas](https://github.com/kmatasflp) </br> [Chris Albright](https://github.com/chrisalbright) </br> [Francisco Díaz](https://github.com/franciscodr) </br> [Bobby Rauchenberg](https://github.com/bobbyrauchenberg) </br> [Leonard Ehrenfried](https://github.com/leonardehrenfried) </br> [François Sarradin](https://github.com/fsarradin) </br> [niqdev](https://github.com/niqdev) </br> [Julien BENOIT](https://github.com/jbenoit2011) </br> [Algimantas Milašius](https://github.com/AlgMi) </br> [Leonard Ehrenfried](https://github.com/leonardehrenfried) |





##### Criticism is appreciated.

##### Fork away, just make sure the tests pass before sending a pull request.
