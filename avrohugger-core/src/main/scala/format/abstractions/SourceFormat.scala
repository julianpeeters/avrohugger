package avrohugger
package format
package abstractions

import avrohugger.matchers.TypeMatcher
import avrohugger.models.CompilationUnit
import avrohugger.stores.{ ClassStore, SchemaStore }

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Type.{ ENUM, RECORD }

import java.nio.file.{ Path, Paths, Files, StandardOpenOption }
import java.io.{ File, FileNotFoundException, IOException }

import scala.collection.JavaConversions._

/** Parent to all ouput formats
  *
  * _ABSTRACT MEMBERS_: to be implemented by a subclass
  * asCompilationUnits
  * compile
  * fileExt
  * getName
  * registerTypes
  * scalaTreehugger
  * toolName
  * toolShortDescription
  * typeMatcher
  *
  * _CONCRETE MEMBERS_: implementations to be inherited by a subclass
  * getFilePath
  * getLocalSubtypes
  * getJavaCompilationUnit
  * renameEnum
  * getScalaCompilationUnit
  * writeToFile
  */
trait SourceFormat {

  ////////////////////////////// abstract members //////////////////////////////
  def asCompilationUnits(
    classStore: ClassStore, 
    namespace: Option[String], 
    schemaOrProtocol: Either[Schema, Protocol],
    schemaStore: SchemaStore,
    maybeOutDir: Option[String]): List[CompilationUnit]
    
  def compile(
    classStore: ClassStore, 
    namespace: Option[String], 
    schemaOrProtocol: Either[Schema, Protocol],
    outDir: String,
    schemaStore: SchemaStore): Unit
  
  def fileExt(schemaOrProtocol: Either[Schema, Protocol]): String

  def getName(schemaOrProtocol: Either[Schema, Protocol]): String
  
  def registerTypes(
    schemaOrProtocol: Either[Schema, Protocol],
    classStore: ClassStore): Unit  
      
  val scalaTreehugger: ScalaTreehugger

  val toolName: String

  val toolShortDescription: String
  
  val typeMatcher: TypeMatcher
  
  
  ///////////////////////////// concrete members ///////////////////////////////
  def getFilePath(
    namespace: Option[String],
    schemaOrProtocol: Either[Schema, Protocol],
    maybeOutDir: Option[String]): Option[Path] = {
    maybeOutDir match {
      case Some(outDir) => {
        val folderPath: Path = Paths.get{
          if (namespace.isDefined) {
            s"$outDir/${namespace.get.toString.replace('.','/')}"
          }
          else outDir
        }
        val fileName = getName(schemaOrProtocol) + fileExt(schemaOrProtocol)
        if (!Files.exists(folderPath)) Files.createDirectories(folderPath)
        Some(Paths.get(s"$folderPath/$fileName"))
      }
      case None => None
    }

  }
  
  def getLocalSubtypes(protocol: Protocol): List[Schema] = {
    val protocolNS = protocol.getNamespace
    val types = protocol.getTypes.toList
    def isTopLevelNamespace(schema: Schema) = schema.getNamespace == protocolNS
    types.filter(isTopLevelNamespace)
  }
  
  def getJavaCompilationUnit(
    classStore: ClassStore,
    namespace: Option[String],
    schema: Schema,
    maybeOutDir: Option[String]): CompilationUnit = {
    val maybeFilePath = getFilePath(namespace, Left(schema), maybeOutDir)
    val codeString = format.specific.SpecificJavaTreehugger.asJavaCodeString(
      classStore,
      namespace,
      schema)
    CompilationUnit(maybeFilePath, codeString)
  }
  
  def renameEnum(schema: Schema) = {
    schema.getType match {
      case RECORD => schema.getName
      case ENUM => schema.getName + ".Value"
      case _ => sys.error("Only RECORD and ENUM can be top-level definitions")
    }
  }
  
  def getScalaCompilationUnit(
    classStore: ClassStore,
    namespace: Option[String],
    schemaOrProtocol: Either[Schema, Protocol],
    typeMatcher: TypeMatcher,
    schemaStore: SchemaStore,
    maybeOutDir: Option[String]): CompilationUnit = {
    registerTypes(schemaOrProtocol, classStore)
    val scalaFilePath = getFilePath(namespace, schemaOrProtocol, maybeOutDir)
    val scalaString = scalaTreehugger.asScalaCodeString(
      classStore,
      namespace,
      schemaOrProtocol,
      typeMatcher,
      schemaStore)
    CompilationUnit(scalaFilePath, scalaString)
  }
  
  def writeToFile(compilationUnit: CompilationUnit): Unit = {
    val path = compilationUnit.maybeFilePath match {
      case Some(filePath) => filePath
      case None => sys.error("Cannot write to file without a file path")
    }
    val contents = compilationUnit.codeString.getBytes()
    try { // delete old and/or create new
      Files.deleteIfExists(path)
      Files.write(path, contents, StandardOpenOption.CREATE) 
      () 
    }
    catch {
      case ex: FileNotFoundException => sys.error("File not found:" + ex)
      case ex: IOException => sys.error("Problem using the file: " + ex)
    }
  }
  
}
