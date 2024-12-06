package avrohugger
package format
package abstractions

import avrohugger.matchers.TypeMatcher
import avrohugger.models.CompilationUnit
import avrohugger.stores.{ClassStore, SchemaStore}
import avrohugger.types._
import org.apache.avro.Schema.Type.{ENUM, FIXED, RECORD}
import org.apache.avro.{Protocol, Schema}
import treehugger.forest._
import definitions._

import java.io.{FileNotFoundException, IOException}
import java.nio.file.{Files, Path, Paths, StandardOpenOption}
import scala.jdk.CollectionConverters._

/** Parent to all ouput formats
 *
 * _ABSTRACT MEMBERS_: to be implemented by a subclass
 * asCompilationUnits
 * compile
 * defaultTypes
 * getName
 * javaTreehugger
 * scalaTreehugger
 * toolName
 * toolShortDescription
 *
 * _CONCRETE MEMBERS_: implementations to be inherited by a subclass
 * fileExt
 * getFilePath
 * getLocalSubtypes
 * getJavaEnumCompilationUnit
 * getScalaCompilationUnit
 * isEnum
 * registerTypes
 * renameEnum
 * RESERVED_WORDS
 * writeToFile
 */
trait SourceFormat {

  ////////////////////////////// abstract members //////////////////////////////
  def asCompilationUnits(
                          classStore: ClassStore,
                          namespace: Option[String],
                          schemaOrProtocol: Either[Schema, Protocol],
                          schemaStore: SchemaStore,
                          maybeOutDir: Option[String],
                          typeMatcher: TypeMatcher,
                          restrictedFields: Boolean,
                          targetScalaPartialVersion: String): List[CompilationUnit]

  def compile(
               classStore: ClassStore,
               namespace: Option[String],
               schemaOrProtocol: Either[Schema, Protocol],
               outDir: String,
               schemaStore: SchemaStore,
               typeMatcher: TypeMatcher,
               restrictedFields: Boolean,
               targetScalaPartialVersion: String): Unit

  val defaultTypes: AvroScalaTypes

  def getName(
               schemaOrProtocol: Either[Schema, Protocol],
               typeMatcher: TypeMatcher): String

  val javaTreehugger: JavaTreehugger

  val scalaTreehugger: ScalaTreehugger

  val toolName: String

  val toolShortDescription: String

  ///////////////////////////// concrete members ///////////////////////////////
  def fileExt(
               schemaOrProtocol: Either[Schema, Protocol],
               typeMatcher: TypeMatcher): String = {
    val enumType = typeMatcher.avroScalaTypes.`enum`

    def enumExt: String = enumType match {
      case JavaEnum => ".java"
      case ScalaCaseObjectEnum => ".scala"
      case ScalaEnumeration => ".scala"
      case EnumAsScalaString => sys.error("Only RECORD and ENUM can be top-level definitions")
    }

    schemaOrProtocol match {
      case Left(schema) => schema.getType match {
        case RECORD => ".scala"
        case ENUM => enumExt // Avro's SpecificData requires enums be Java Enum
        case FIXED => ".scala"
        case _ => sys.error("Only RECORD and ENUM can be top-level definitions")
      }
      case Right(protocol) => ".scala"
    }

  }

  def getFilePath(
                   namespace: Option[String],
                   schemaOrProtocol: Either[Schema, Protocol],
                   maybeOutDir: Option[String],
                   typeMatcher: TypeMatcher): Option[Path] = {
    maybeOutDir.map {
      outDir =>
        val folderPath: Path = Paths.get {
          if (namespace.isDefined) {
            s"$outDir/${namespace.get.replace('.', '/')}"
          }
          else outDir
        }
        val ext = fileExt(schemaOrProtocol, typeMatcher)
        val fileName = getName(schemaOrProtocol, typeMatcher) + ext
        if (!Files.exists(folderPath)) Files.createDirectories(folderPath)
        Paths.get(s"$folderPath/$fileName")
    }
  }

  def getLocalSubtypes(protocol: Protocol): List[Schema] = {
    val protocolNS = protocol.getNamespace
    val types = protocol.getTypes().asScala.toList
    types.filter(_.getNamespace == protocolNS)
  }

  def getJavaEnumCompilationUnit(
                                  classStore: ClassStore,
                                  namespace: Option[String],
                                  schema: Schema,
                                  maybeOutDir: Option[String],
                                  typeMatcher: TypeMatcher): CompilationUnit = {
    val maybeFilePath =
      getFilePath(namespace, Left(schema), maybeOutDir, typeMatcher)
    val codeString = javaTreehugger.asJavaCodeString(
      classStore,
      namespace,
      schema)
    CompilationUnit(maybeFilePath, codeString)
  }

  // Uses treehugger trees so can't handle java enums, therefore Java enums
  // must be generated separately, and Scala enums must NOT be generated within
  // the compilation unit if enum style is set to "java enum".
  def getScalaCompilationUnit(
                               classStore: ClassStore,
                               namespace: Option[String],
                               schemaOrProtocol: Either[Schema, Protocol],
                               typeMatcher: TypeMatcher,
                               schemaStore: SchemaStore,
                               maybeOutDir: Option[String],
                               restrictedFields: Boolean,
                               targetScalaPartialVersion: String): CompilationUnit = {
    val scalaFilePath =
      getFilePath(namespace, schemaOrProtocol, maybeOutDir, typeMatcher)
    val scalaString = scalaTreehugger.asScalaCodeString(
      classStore,
      namespace,
      schemaOrProtocol,
      typeMatcher,
      schemaStore,
      restrictedFields,
      targetScalaPartialVersion)
    CompilationUnit(scalaFilePath, scalaString)
  }

  def isEnum(schema: Schema): Boolean = schema.getType == Schema.Type.ENUM

  def registerTypes(
                     schemaOrProtocol: Either[Schema, Protocol],
                     classStore: ClassStore,
                     typeMatcher: TypeMatcher): Unit = {
    def registerSchema(schema: Schema): Unit = {
      val typeName = typeMatcher.avroScalaTypes.`enum` match {
        case JavaEnum => schema.getName
        case ScalaCaseObjectEnum => schema.getName
        case ScalaEnumeration => renameEnum(schema, "Value")
        case EnumAsScalaString => schema.getName
      }
      val classSymbol = RootClass.newClass(typeName)
      classStore.accept(schema, classSymbol)
    }

    schemaOrProtocol match {
      case Left(schema) => registerSchema(schema)
      case Right(protocol) => protocol.getTypes().asScala.foreach(registerSchema)
    }
  }

  def renameEnum(schema: Schema, selector: String): String = {
    schema.getType match {
      case RECORD => schema.getName
      case ENUM => schema.getName + "." + selector
      case FIXED => schema.getName
      case _ => sys.error("Only RECORD, ENUM or FIXED can be top-level definitions")
    }
  }

  // From: https://github.com/apache/avro/blob/33d495840c896b693b7f37b5ec786ac1acacd3b4/lang/java/avro/src/main/java/org/apache/avro/specific/SpecificData.java#L70
  val RESERVED_WORDS: Set[String] = Set("abstract", "assert", "boolean", "break", "byte", "case", "catch",
    "char", "class", "const", "continue", "default", "do", "double",
    "else", "enum", "extends", "false", "final", "finally", "float",
    "for", "goto", "if", "implements", "import", "instanceof", "int",
    "interface", "long", "native", "new", "null", "package", "private",
    "protected", "public", "return", "short", "static", "strictfp",
    "super", "switch", "synchronized", "this", "throw", "throws",
    "transient", "true", "try", "void", "volatile", "while",
    /* classnames use internally by the avro code generator */
    "Builder")

  def writeToFile(compilationUnit: CompilationUnit): Unit = {
    val path = compilationUnit.maybeFilePath match {
      case Some(filePath) => filePath
      case None => sys.error("Cannot write to file without a file path")
    }
    val contents = compilationUnit.codeString.getBytes()
    try { // delete old and/or create new
      Files.deleteIfExists(path) // delete file if exists
      Files.createDirectories(path.getParent) // create all parent folders
      Files.write(path, contents, StandardOpenOption.CREATE)
      ()
    }
    catch {
      case ex: FileNotFoundException => sys.error("File not found:" + ex)
      case ex: IOException => sys.error("Problem using the file: " + ex)
    }
  }

}
