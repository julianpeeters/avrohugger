package avrohugger
package input
package parsers

import format.abstractions.SourceFormat
import stores.ClassStore

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Parser
import org.apache.avro.Schema.Type.{ RECORD, UNION, ENUM }
import org.apache.avro.compiler.idl.Idl
import org.apache.avro.generic.{ GenericDatumReader, GenericRecord }
import org.apache.avro.file.DataFileReader
import org.apache.avro.SchemaParseException
import java.io.File

import scala.collection.JavaConverters._
import scala.util.Try

class FileInputParser {
  
  val schemaParser = new Parser()

  def getSchemaOrProtocols(
    infile: File,
    format: SourceFormat,
    classStore: ClassStore,
    classLoader: ClassLoader,
    parser: Parser = schemaParser): List[Either[Schema, Protocol]] = {
    def unUnion(schema: Schema) = {
      schema.getType match {
        case UNION => schema.getTypes.asScala.toList
        case RECORD => List(schema)
        case ENUM => List(schema)
        case _ => sys.error("""Neither a record, enum nor a union of either. 
          |Nothing to map to a definition.""".trim.stripMargin)
      }
    }

    def copySchemas(tempParser: Parser, parser: Parser): Unit = {
      val tempKeys = tempParser.getTypes.keySet().asScala
      val keys = parser.getTypes.keySet().asScala
      val commonElements = tempKeys.intersect(keys)
      val nonEqualElements = commonElements.filter { element =>
        parser.getTypes.get(element) != tempParser.getTypes.get(element)
      }
      if (nonEqualElements.nonEmpty) {
        sys.error(s"Can't redefine:  ${nonEqualElements.mkString(",")} in $infile")
      } else {
        if (commonElements.isEmpty) {
          val _ = parser.addTypes(tempParser.getTypes)
        } else {
          val missingTypes = tempParser.getTypes.keySet().asScala.diff(parser.getTypes.keySet().asScala)
          val _ = parser.addTypes(missingTypes.map { t =>
            t -> tempParser.getTypes.get(t)
          }.toMap.asJava)
        }
      }
    }
    
    def mightBeRecoverable(e: SchemaParseException): Boolean = {
      val msg = e.getMessage
      msg.contains("Undefined name:") || msg.contains("is not a defined name") 
    }

    def tryParse(inFile: File, parser: Schema.Parser): List[Schema] = {
      val tempParser = new Parser()
      val parsed = Try(tempParser.parse(inFile)).map(schema => {
        copySchemas(tempParser, parser)
        schema
      }).recoverWith { case e: SchemaParseException if mightBeRecoverable(e) => 
        Try(parser.parse(inFile))
      }
      unUnion(parsed.get)// throw the avro parse exception if Failure
    }
    
    val schemaOrProtocols: List[Either[Schema, Protocol]] = {

      infile.getName.split("\\.").last match {
        case "avro" =>
          val gdr = new GenericDatumReader[GenericRecord]
          val dfr = new DataFileReader(infile, gdr)
          val schemas = unUnion(dfr.getSchema)
          schemas.map(Left(_))
        case "avsc" =>
          val schemas = tryParse(infile, parser)
          schemas.map(Left(_))
        case "avpr" =>
          val protocol = Protocol.parse(infile)
          List(Right(protocol))
        case "avdl" =>
          val idlParser = new Idl(infile, classLoader)
          val protocol = idlParser.CompilationUnit()
          /**
           * IDLs may refer to types imported from another file. When converted 
           * to protocols, the imported types that share the IDL's namespace 
           * cannot be distinguished from types defined within the IDL, yet 
           * should not be generated as subtypes of the IDL's ADT and should 
           * instead be generated in its own namespace. So, strip the protocol 
           * of all imported types and generate them separately.
           */
          val importedFiles = IdlImportParser.getImportedFiles(infile, classLoader)
          val importedSchemaOrProtocols = importedFiles.flatMap(file => {
            val importParser = new Parser() // else attempts to redefine schemas
            getSchemaOrProtocols(file, format, classStore, classLoader, importParser)
          }).toList
          def stripImports(
            protocol: Protocol,
            importedSchemaOrProtocols: List[Either[Schema, Protocol]]) = {
            val imported = importedSchemaOrProtocols.flatMap(avroDef => {
              avroDef match {
                case Left(importedSchema) => List(importedSchema)
                case Right(importedProtocol) => importedProtocol.getTypes.asScala
              }
            })
            val types = protocol.getTypes.asScala.toList
            val localTypes = imported.foldLeft(types)((remaining, imported) => {
              remaining.filterNot(remainingType => remainingType == imported)
            })
            protocol.setTypes(localTypes.asJava)
            protocol
          }
          val localProtocol = stripImports(protocol, importedSchemaOrProtocols)
          // reverse to dependent classes are generated first
          (Right(localProtocol) +: importedSchemaOrProtocols).reverse
        case _ =>
          throw new Exception("""File must end in ".avpr" for protocol files, 
            |".avsc" for plain text json files, ".avdl" for IDL files, or .avro 
            |for binary.""".trim.stripMargin)
      }
    }
    
    schemaOrProtocols
  }
}