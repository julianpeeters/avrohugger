package avrohugger
package input
package parsers

import format.abstractions.SourceFormat
import stores.ClassStore
import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.{ AvroTypeException, SchemaParseException }
import org.apache.avro.Schema.Parser
import org.apache.avro.Schema.Type.{ ENUM, FIXED, RECORD, UNION }
import org.apache.avro.file.DataFileReader
import org.apache.avro.generic.{ GenericDatumReader, GenericRecord }
import org.apache.avro.idl.IdlReader

import java.io.File
import java.util.concurrent.ConcurrentHashMap
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.jdk.CollectionConverters._
import scala.util.Try

class FileInputParser {

  val processedFiles: ConcurrentHashMap[String, Future[List[Either[Schema, Protocol]]]] = new ConcurrentHashMap[String, Future[List[Either[Schema, Protocol]]]]()
  val processedSchemas: ConcurrentHashMap[String, Schema] = new ConcurrentHashMap[String, Schema]()

  private def unUnion(schema: Schema) = {
    schema.getType match {
      case UNION => schema.getTypes().asScala.toList
      case RECORD => List(schema)
      case ENUM => List(schema)
      case FIXED => List(schema)
      case _ => sys.error(
        """Neither a record, enum nor a union of either.
          |Nothing to map to a definition.""".trim.stripMargin)
    }
  }

  private def copySchemas(inFile: File, tempParser: Parser, parser: Parser): Unit = {
    val tempTypes = tempParser.getTypes()
    val types = parser.getTypes()
    val tempKeys = tempTypes.keySet().asScala
    val keys = types.keySet().asScala
    val commonElements = tempKeys.intersect(keys)
    val nonEqualElements = commonElements.filter { element =>
      types.get(element) != tempTypes.get(element)
    }
    if (nonEqualElements.nonEmpty) {
      sys.error(s"Can't redefine:  ${nonEqualElements.mkString(",")} in $inFile")
    } else {
      if (commonElements.isEmpty) {
        val _ = parser.addTypes(tempTypes.values)
      } else {
        val missingTypes = tempKeys.diff(keys)
        val _ = parser.addTypes(missingTypes.map { t =>
          t -> tempTypes.get(t)
        }.toMap.asJava.values)
      }
    }
  }

  private def mightBeRecoverable(e: SchemaParseException): Boolean = {
    val msg = e.getMessage
    msg.contains("Undefined name:") || msg.contains("is not a defined name")
  }

  private def mightBeRecoverableType(e: AvroTypeException): Boolean = {
    val msg = e.getMessage
    msg.contains("Undefined schema:")
  }

  private def tryParse(inFile: File, parser: Schema.Parser): List[Schema] = {
    // val tempParser = new Parser()
    val parsed = Try(parser.parse(inFile)).map(schema => {
      copySchemas(inFile: File, parser, parser)
      schema
    }).recoverWith {
        case e: AvroTypeException if mightBeRecoverableType(e) => 
          Try(parser.parse(inFile))
        case e: SchemaParseException if mightBeRecoverable(e) => 
          Try(parser.parse(inFile))
      }
    unUnion(parsed.get) // throw the avro parse exception if Failure
  }

  def getSchemaOrProtocols(
    infile: File,
    format: SourceFormat,
    classStore: ClassStore,
    classLoader: ClassLoader,
    parser: Parser): Future[List[Either[Schema, Protocol]]] = {
    val path = infile.getCanonicalPath
    processedFiles.computeIfAbsent(path, _ => {
      val future: Future[List[Either[Schema, Protocol]]] = infile.getName.split("\\.").last match {
        case "avro" => Future {
          val gdr = new GenericDatumReader[GenericRecord]
          val dfr = new DataFileReader(infile, gdr)
          val schemas = unUnion(dfr.getSchema)
          schemas.map { s =>
            processedSchemas.putIfAbsent(s.getFullName, s)
            Left(s)
          }
        }
        case "avsc" => Future.successful {
          tryParse(infile, parser).map { s =>
            processedSchemas.putIfAbsent(s.getFullName, s)
            Left(s)
          }
        }
        case "avpr" => Future {
          val p = Protocol.parse(infile)
          p.getTypes().forEach(importedSchema => processedSchemas.putIfAbsent(importedSchema.getFullName, importedSchema))
          List(Right(p))
        }
        case "avdl" =>
          Future {
            val thread = Thread.currentThread()
            val originalClassLoader = thread.getContextClassLoader()
            try {
              thread.setContextClassLoader(classLoader)
              val idl = new IdlReader().parse(infile.toPath())
              val maybeProtocol = Option(idl.getProtocol())

              /**
                * IDLs may refer to types imported from another file. When converted
                * to protocols, the imported types that share the IDL's namespace
                * cannot be distinguished from types defined within the IDL, yet
                * should not be generated as subtypes of the IDL's ADT and should
                * instead be generated in its own namespace. So, strip the protocol
                * of all imported types and generate them separately.
                */
              val importedFiles = IdlImportParser.getImportedFiles(infile, classLoader)

              Future.sequence(importedFiles.map { file =>
                // val importParser = new Parser() // else attempts to redefine schemas
                getSchemaOrProtocols(file, format, classStore, classLoader, parser)
              }).map { f =>
                maybeProtocol match {
                  case Some(protocol) =>
                    val localProtocol = stripImports(protocol, processedSchemas)
                    localProtocol.getTypes().forEach(importedSchema => processedSchemas.putIfAbsent(importedSchema.getFullName, importedSchema))
                    (Right(localProtocol) +: f.flatten).reverse
                  case None =>
                    val mainSchemas = unUnion(idl.getMainSchema())
                    mainSchemas.map(importedSchema => processedSchemas.putIfAbsent(importedSchema.getFullName, importedSchema))
                    (mainSchemas.map(Left(_)) ++ f.flatten).reverse
                }
              }
            } finally {
              thread.setContextClassLoader(originalClassLoader)
            }
          }.flatten
        case _ =>
          throw new Exception(
            """File must end in ".avpr" for protocol files,
              |".avsc" for plain text json files, ".avdl" for IDL files, or .avro
              |for binary.""".trim.stripMargin)
      }
      future.andThen { case _ => processedFiles.put(path, Future.successful(Nil)) }
      future
    })
  }

  private def stripImports(protocol: Protocol, imported: ConcurrentHashMap[String, Schema]) = {
    val types = protocol.getTypes().asScala.toList
    val localTypes = types.filterNot(x => imported.containsKey(x.getFullName))
    protocol.setTypes(localTypes.asJava)
    protocol
  }

}