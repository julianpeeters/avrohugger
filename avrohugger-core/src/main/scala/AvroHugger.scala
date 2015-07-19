package avrohugger
import specific._

import treehugger.forest._
import definitions._
import treehuggerDSL._
import java.nio.file.{Path, Paths, Files, StandardOpenOption}
import org.apache.avro.Schema
import java.io.{File, FileNotFoundException, IOException}
import scala.collection.JavaConversions._
import org.apache.avro.Schema.Type.{ARRAY, RECORD, UNION, ENUM}

object AvroHugger {

  def write(
    classStore: ClassStore, 
    namespace: Option[String], 
    schema: Schema, 
    outDir: String): Unit = {
  
    // register new type
    val classSymbol = RootClass.newClass(schema.getName)
    classStore.accept(schema, classSymbol)

    // generate list of constructor parameters
    val classDef = schema.getType match {
      case RECORD =>
        val params: List[ValDef] = schema.getFields.toList.map { field =>
          val fieldName = field.name
          val fieldType = TypeMatcher.toType(classStore, namespace, field.schema)
          PARAM(fieldName, fieldType): ValDef
        }

        // generate class definition
        CASECLASSDEF(classSymbol).withParams(params).tree

      case ENUM =>
        OBJECTDEF(schema.getName) withParents("Enumeration") := BLOCK(
          TYPEVAR(schema.getName) := REF("Value"),
          VAL(schema.getEnumSymbols.mkString(", ")) := REF("Value")
        )
      case _ => sys.error("Only RECORD and ENUM can be top-level definitions")

    }

    // wrap the class definition in a block with a comment and a package
    val tree = {
      if (namespace.isDefined) BLOCK(classDef).inPackage(namespace.get)
      else classDef
    }.withDoc("MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY")

    writeToFile(namespace, schema, tree, outDir)
  }

  
  // write the definition to file
  private def writeToFile(namespace: Option[String], schema: Schema, tree: Tree, outDir: String): Unit = {
    val folderPath: Path = Paths.get{
      if (namespace.isDefined) outDir + "/" + namespace.get.toString.replace('.','/')
      else outDir
    }
    if (!Files.exists(folderPath)) Files.createDirectories(folderPath)
    val filePath = { 
      Paths.get(folderPath.toString + "/" + schema.getName + ".scala")
    }
    try { // delete old and/or create new
      Files.deleteIfExists(filePath)
      Files.write(filePath, treeToString(tree).getBytes(), StandardOpenOption.CREATE) 
      () 
    }//finally pw.close() 
    catch {
      case ex: FileNotFoundException => sys.error("File not found:" + ex)
      case ex: IOException => sys.error("There was a problem using the file: " + ex)
    }
  }
}
