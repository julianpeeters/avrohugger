package avrohugger

import specific._
import treehugger.forest._
import definitions._
import treehuggerDSL._
import java.nio.file.{Path, Paths, Files, StandardOpenOption}
import org.apache.avro.Schema
import java.io.{File, FileNotFoundException, IOException}
import scala.collection.JavaConversions._


object AvroHugger {

  def defineCaseClass(
    classStore: ClassStore, 
    namespace: Option[String], 
    schema: Schema, 
    outDir: String, 
    extendsSpecific: Boolean): Unit = {
  
    // register new type
    val classSymbol = RootClass.newClass(schema.getName)
    classStore.accept(schema, classSymbol)

    // generate list of constructor parameters
    val params: List[ValDef] = schema.getFields.toList.map { field =>
      val fieldName = field.name
      val fieldType = TypeMatcher.toType(classStore, namespace, field.schema)
      PARAM(fieldName, fieldType): ValDef
    }
 
    // generate class definition 
    val classDef = {

      // plain case class 
      if (extendsSpecific == false) CASECLASSDEF(classSymbol).withParams(params).tree

      // case class that implements SpecificRecord
      else {
        // register avro classes
        val SchemaClass = RootClass.newClass("org.apache.avro.Schema")
        val ParserClass = RootClass.newClass("org.apache.avro.Schema.Parser")

        // extension
        val baseClass = RootClass.newClass("SpecificRecordBase")

        // no-arg constructor
        val defaultParams: List[Tree] = 
          schema.getFields.toList.map(avroField => DefaultParamMatcher.asDefaultParam(avroField))
        val defThis = DEFTHIS := THIS APPLY(defaultParams:_*)

        // vals
        val valSchema = VAL(REF("SCHEMA$")) := (NEW(ParserClass)) APPLY() DOT "parse" APPLY(LIT(schema.toString)) //REF(schema)//Lit(schema.toString)//generateSchema(namespace, schema)

        // methods
        //add an index the the fields
        case class IndexedField(valDef: ValDef, idx: Int)
        val indexedFields = params.zipWithIndex.map(p => IndexedField(p._1, p._2))

        // get
        def asGetCase(field: IndexedField) = {
          def convertToJava(tree: Tree, args: Name) = args //TODO
          val convertedToJava = convertToJava(field.valDef.tpt, field.valDef.name) //recursively checks tree and unboxes appropriately
          CASE (ID("pos"), IF(REF("pos") INT_== LIT(field.idx))) ==> (REF(convertedToJava) AS AnyRefClass)
        }
        val casesGet = indexedFields.map(field => asGetCase(field)) 
        val defGet = DEF("get", AnyRefClass) withParams(PARAM("field", IntClass)) := BLOCK(
          REF("field") MATCH(casesGet:_*)
        )  

        // put  
        def asPutCase(field: IndexedField) = {
          if (!field.valDef.tpt.children.isEmpty) {
sys.error("field.valDef.tpt")




          }
          else {
            CASE (ID("pos"), (IF(REF("pos") INT_== LIT(field.idx)))) ==> { 
              THIS DOT field.valDef.name := {
                if (field.valDef.tpe.toString == "org.apache.avro.util.Utf8") REF("value") TOSTRING
                else REF("value")
              } AS field.valDef.tpe
            } 
          }
        }
        val casesPut = indexedFields.map(field => asPutCase(field))
        val defPut = DEF("put", UnitClass) withParams(PARAM("field", IntClass), PARAM("value", AnyClass)) := BLOCK(
          REF("field") MATCH(casesPut:_*)
        )

        // getSchema
        val defGetSchema = DEF("getSchema", SchemaClass) := REF("SCHEMA$")

        CASECLASSDEF(classSymbol).withParents(baseClass).withParams(params).tree := BLOCK( 
          valSchema,
          defThis,
          defGet,
          defPut,
          defGetSchema
        )
      }
    }

    // wrap the class definition in a block with a comment and a package
    val tree = {
      if (namespace.isDefined) BLOCK(classDef).inPackage(namespace.get)
      else BLOCK(classDef)
    }.withDoc("MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY")

    //return the tree
    writeToFile(namespace, schema, tree, outDir)
  }
  
  // write the definition to file
  private def writeToFile(namespace: Option[String], schema: Schema, tree: Tree, outDir: String): Unit = {
    val folderPath: Path = Paths.get{
      if (namespace.isDefined) outDir + "/" + namespace.get.toString.replace('.','/')
      else outDir
    }
    if (!Files.exists(folderPath)) Files.createDirectories(folderPath)
    try Files.write( Paths.get(folderPath.toString + "/" + schema.getName + ".scala"), treeToString(tree).getBytes(), StandardOpenOption.CREATE) //finally pw.close() // create new, overwrite if exists
    catch {
      case ex: FileNotFoundException => sys.error("File not found:" + ex)
      case ex: IOException => sys.error("There was a problem using the file: " + ex)
    }
  }
}