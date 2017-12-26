package avrohugger
package format
package specific
package trees

import generators.ScalaDocGenerator
import stores.ClassStore
import matchers.TypeMatcher

import treehugger.forest._
import definitions._
import treehuggerDSL._

import org.apache.avro.{ Protocol, Schema }
import org.apache.avro.Schema.Type.{ ENUM, RECORD }

import scala.collection.JavaConverters._

object SpecificTraitTree {

  def toADTRootDef(protocol: Protocol) = {    
    val sealedTraitTree =  TRAITDEF(protocol.getName).withFlags(Flags.SEALED)
    val adtRootTree = {
      val types = protocol.getTypes.asScala.toList
      // filter out enums since they will be written as java and not in the adt
      val nonEnums = types.filterNot(schema => schema.getType == ENUM)
      if (nonEnums.forall(schema => schema.getType == RECORD)) {
        sealedTraitTree
          .withParents("org.apache.avro.specific.SpecificRecordBase")
          .withParents("Product")
          .withParents("Serializable")
      }
      else sealedTraitTree
    } 
    val treeWithScalaDoc = ScalaDocGenerator.docToScalaDoc(
      Right(protocol),
      adtRootTree)
      
    treeWithScalaDoc
  }
  
  def toRPCTraitDef(
    classStore: ClassStore, 
    namespace: Option[String],
    protocol: Protocol,
    typeMatcher: TypeMatcher) = {
      
    // Name
    val name = protocol.getName
        
    // Register classes and types
    val ProtocolClass = RootClass.newClass("org.apache.avro.Protocol")
    val IOExceptionClass = RootClass.newClass("java.io.IOException")
    val CallbackClass = RootClass.newClass("org.apache.avro.ipc.Callback")
    val CharSequenceClass = RootClass.newClass("java.lang.CharSequence")

    // Annotations
    val suppressWarn = ANNOT("""SuppressWarnings(Array("all"))""")
    val avroGenerated = ANNOT("org.apache.avro.specific.AvroGenerated")
    
    // Docs - Note: "throws" is not an ANNOT because '@' gets stripped as String
    val errorMessage = "The async call could not be completed."
    val callbackDefDocString = s"@throws $IOExceptionClass $errorMessage"
    
    // Protocol val
    val protocolVal: Tree =
      VAL("PROTOCOL", ProtocolClass).withFlags(Flags.FINAL) := {
        val companionName = namespace match {
          case Some(ns) => s"$ns.$name"
          case None => name
        }
        REF(companionName) DOT "PROTOCOL"
      }
    
    // Return type - Note: rpc trait requires `CharSequence` for String or Utf8
    def asReturnType(schema: Schema): Type = schema.getName match {
      case "string" => CharSequenceClass
      case _ => typeMatcher.toScalaType(classStore, namespace, schema)
    }
    
    def asRequestParam(message: Protocol#Message): ValDef = {
      val request = message.getRequest.getFields.asScala.headOption match {
        case Some(avroField) => avroField
        case None => sys.error("""Expected a request field, found no fields.""")
      }
      val requestParamName = request.name
      val requestParamTypeName = namespace match {
        case Some(ns) => s"$ns.${request.schema.getName}"
        case None => request.schema.getName
      }
      PARAM(requestParamName, requestParamTypeName).tree
    }
    
    def asCallbackParam(message: Protocol#Message): ValDef = {
      val callbackParamName = "callback"
      val callbackTypeParam = asReturnType(message.getResponse)
      val callbackParamTypeName = CallbackClass TYPE_OF callbackTypeParam
      PARAM(callbackParamName, callbackParamTypeName).tree
    }
    
    // Message defs
    val messageDefs: List[Tree] = protocol.getMessages.asScala.toList.map(kvPair => {
      val defName = kvPair._1
      val message = kvPair._2
      val requestParam = asRequestParam(message)
      val returnType = asReturnType(message.getResponse)
      val defTree = DEF(defName, returnType).withParams(requestParam).tree
      defTree
    })
    
    // Callback defs
    val callbackDefs: List[Tree] = protocol.getMessages.asScala.toList.map(kvPair => {
      val defName = kvPair._1
      val message = kvPair._2
      val requestParam = asRequestParam(message)
      val callbackParam = asCallbackParam(message)
      val params = List(requestParam, callbackParam)
      val defTree = DEF(defName, UnitClass).withParams(params).tree
      defTree.withDoc(callbackDefDocString)
    })
    
    // Nested Tree
    val callbackTraitTree = 
      TRAITDEF("Callback").withAnnots(suppressWarn).withParents(name) := BLOCK(
        protocolVal +: callbackDefs
      )
      
    // Tree
    val traitTree = {
      TRAITDEF(name).withAnnots(suppressWarn, avroGenerated) := BLOCK(
        messageDefs :+ callbackTraitTree
      )
    }
    
    // Return
    val treeWithScalaDoc = ScalaDocGenerator.docToScalaDoc(
      Right(protocol),
      traitTree)
    treeWithScalaDoc
    
  }
  
}
