package avrohugger
package input

import schemagen._

import collection.JavaConversions._

import scala.reflect._

import scala.reflect.api._
import scala.reflect.runtime.universe._
import scala.reflect.runtime.universe.Flag._
import scala.tools.reflect.ToolBox

import org.apache.avro.Schema

object TreeInputParser {

  def parse(tree: Tree, knownClasses: List[Tree] = List(EmptyTree)): List[Schema] = {

    //e.g. takes `package test.one; package dev.helper;`, returns `test.one.dev.helper`
    def squashNamespace(pkgTrees: Seq[RefTree]): Name = {
      def extractNamespace(pkgTree: RefTree) ={
        // necessary because just using `.name` results in `one.helper`
        val owner = pkgTree.name
        val children = pkgTree.children
        val namespace = children match {
          case List() => s"$owner" //empty list corresponds to a single-level package
          case List(child) => s"$child.$owner"
        }
        namespace
      }
      val packageString = pkgTrees.map(tree => extractNamespace(tree)).mkString(".")
      newTermName(packageString)
    }

    def generateSchema(namespace: Option[Name], classDefTree: Tree): List[Schema] = {
      classDefTree match {
        // case classes get mapped to records
        case t @ q"$mods class $className[..$tparams](..$fields)(...$rest) extends ..$parents { $self => ..$body }" => {
          List(RecordSchemaGenerator.generateSchema(knownClasses, className.toString, namespace, fields)) 
        }
        // objects that extend Enumeration get mapped to enums
        case t @ q"$mods object $objectName extends ..$parents { $self => ..$body }"  => {
          val values = body.collect{ case ValDef(mods, NameTag(name), tpt, tpe) => name }
          List(EnumSchemaGenerator.generateSchema(knownClasses, objectName.toString, namespace, values)) 
        }
        case definition => sys.error(s"""Unsupported definition. Expected case class or object defition, 
          but found $definition with tree: ${showRaw(definition)}""")
      }
    } 
    
    // Reflective compilation can't seem to handle packages, so field types are typechecked
    // against manually provided classes as dependencies. (Object defs alone won't suffice!)
    def asDependencies(topLevelDefs: List[Tree]): List[Tree] = {
      topLevelDefs.reverse.flatMap(topLevelDef => {
        topLevelDef match {
          case t @ q"$mods class $className[..$tparams](..$fields)(...$rest) extends ..$parents { $self => ..$body }" => List(q"case class $className()")
          case t @ q"$mods object $objectName extends ..$parents { $self => ..$body }"  => {
            List(t, q" case class ${objectName.toTypeName}();")
          }
        }
      })
    }
    
    // if multiple classes are found, each is wrapped in the package def and resubmitted
    tree match {
      //TODO: better way than hard-coding for each number of possible enclosing packages?
      //TODO: handle imports a) overlook extraneous imports b) imported record types
      //      need to be fully qualified, generated schemas using the right namespace.
      
      // nine packages: results in quasiquote error: Don't know how to unquote here

      // eight packages
      case compilationUnit @ q"""package $packageName8 {
        package $packageName7 {
        package $packageName6 {
        package $packageName5 {
        package $packageName4 {
        package $packageName3 {
        package $packageName2 {
        package $packageName1 {
        $classDef}}}}}}}}"""  => {
        val packageTrees = Seq(packageName8, packageName7, packageName6, packageName5, packageName4, packageName3, packageName2, packageName1)
        val namespace = Some(squashNamespace(packageTrees)) 
        generateSchema(namespace, classDef)
      }
      // seven packages
      case compilationUnit @ q"""package $packageName7 {
        package $packageName6 {
        package $packageName5 {
        package $packageName4 {
        package $packageName3 {
        package $packageName2 {
        package $packageName1 {
        $classDef}}}}}}}"""  => {
        val packageTrees = Seq(packageName7, packageName6, packageName5, packageName4, packageName3, packageName2, packageName1)
        val namespace = Some(squashNamespace(packageTrees))
        generateSchema(namespace, classDef)
      }
      // six packages
      case compilationUnit @ q"""package $packageName6 {
        package $packageName5 {
        package $packageName4 {
        package $packageName3 {
        package $packageName2 {
        package $packageName1 {
        $classDef}}}}}}"""  => {
        val packageTrees = Seq(packageName6, packageName5, packageName4, packageName3, packageName2, packageName1)
        val namespace = Some(squashNamespace(packageTrees))
        generateSchema(namespace, classDef)
      }
      // five packages
      case compilationUnit @ q"""package $packageName5 {
        package $packageName4 {
        package $packageName3 {
        package $packageName2 {
        package $packageName1 {
        $classDef}}}}}"""  => {
        val packageTrees = Seq(packageName5, packageName4, packageName3, packageName2, packageName1)
        val namespace = Some(squashNamespace(packageTrees))
        generateSchema(namespace, classDef)
      }
      // four packages
      case compilationUnit @ q"""package $packageName4 {
        package $packageName3 {
        package $packageName2 {
        package $packageName1 {
        $classDef}}}}"""  => {
        val packageTrees = Seq(packageName4, packageName3, packageName2, packageName1)
        val namespace = Some(squashNamespace(packageTrees))        
        generateSchema(namespace, classDef)
      }
      // three packages
      case compilationUnit @ q"""package $packageName3 {
        package $packageName2 {
        package $packageName1 {
        $classDef}}}"""  => {
        val packageTrees = Seq(packageName3, packageName2, packageName1)
        val namespace = Some(squashNamespace(packageTrees))        
        generateSchema(namespace, classDef)
      }
      // two packages
      case compilationUnit @ q"""package $packageName2 {
        package $packageName1 {
        $classDef}}"""  => {
        val packageTrees = Seq(packageName2, packageName1)
        val namespace = Some(squashNamespace(packageTrees))
        generateSchema(namespace, classDef)
      }
      // single package
      case compilationUnit @ q"""package $packageName { $classDef }"""  => {
        val packageTrees = Seq(packageName)
        val namespace = Some(squashNamespace(packageTrees))
        generateSchema(namespace, classDef)
      }
      // default package
      case classDef @ q"$mods class $name[..$tparams](..$fields)(...$rest) extends ..$parents { $self => ..$body }"  => {
        val namespace = None
        generateSchema(namespace, classDef)
      }

      //TODO: So much hard-coding of the number of package levels. A better way?
      // multiple classes in the same package get reparsed as indidually packaged classDefs
      
      // Reverse the list of individually packaged compilation units so most nested classes
      // are expanded first. 

      // default package
      case compilationUnit @ q"""..$classDefs"""  => {
        val individuallyPackaged = classDefs.map(cd => q"""$cd""")
        individuallyPackaged.reverse.map(tree => parse(tree, asDependencies(classDefs))).flatten
      }
      // one package
      case compilationUnit @ q"""
        package $packageName { ..$classDefs }"""  => {
        val individuallyPackaged = classDefs.map(cd => q"""
          package $packageName { $cd }""")
        individuallyPackaged.reverse.map(tree => parse(tree, asDependencies(classDefs))).flatten
      }
      // two package
      case compilationUnit @ q"""
        package $packageName2 {
        package $packageName1 { ..$classDefs }}"""  => {
        val individuallyPackaged = classDefs.map(cd => q"""
          package $packageName2 {
          package $packageName1 { $cd }}""")
        individuallyPackaged.reverse.map(tree => parse(tree, asDependencies(classDefs))).flatten
      }
      // three package
      case compilationUnit @ q"""
        package $packageName3 {
        package $packageName2 {
        package $packageName1 { ..$classDefs }}}"""  => {
        val individuallyPackaged = classDefs.map(cd => q"""
          package $packageName3 {
          package $packageName2 {
          package $packageName1 { $cd }}}""")
        individuallyPackaged.reverse.map(tree => parse(tree, asDependencies(classDefs))).flatten
      }
      // four package
      case compilationUnit @ q"""
        package $packageName4 {
        package $packageName3 {
        package $packageName2 {
        package $packageName1 { ..$classDefs }}}}"""  => {
        val individuallyPackaged = classDefs.map(cd => q"""
          package $packageName4 {
          package $packageName3 {
          package $packageName2 {
          package $packageName1 { $cd }}}}""")
        individuallyPackaged.reverse.map(tree => parse(tree, asDependencies(classDefs))).flatten
      }
      // five package
      case compilationUnit @ q"""
        package $packageName5 {
        package $packageName4 {
        package $packageName3 {
        package $packageName2 {
        package $packageName1 { ..$classDefs }}}}}"""  => {
        val individuallyPackaged = classDefs.map(cd => q"""
          package $packageName5 {
          package $packageName4 {
          package $packageName3 {
          package $packageName2 {
          package $packageName1 { $cd }}}}}""")
        individuallyPackaged.reverse.map(tree => parse(tree, asDependencies(classDefs))).flatten
      }

      // six package
      case compilationUnit @ q"""
        package $packageName6 {
        package $packageName5 {
        package $packageName4 {
        package $packageName3 {
        package $packageName2 {
        package $packageName1 { ..$classDefs }}}}}}"""  => {
        val individuallyPackaged = classDefs.map(cd => q"""
          package $packageName6 {
          package $packageName5 {
          package $packageName4 {
          package $packageName3 {
          package $packageName2 {
          package $packageName1 { $cd }}}}}}""")
        individuallyPackaged.reverse.map(tree => parse(tree, asDependencies(classDefs))).flatten
      }

      // seven package
      case compilationUnit @ q"""
        package $packageName7 {
        package $packageName6 {
        package $packageName5 {
        package $packageName4 {
        package $packageName3 {
        package $packageName2 {
        package $packageName1 { ..$classDefs }}}}}}}"""  => {
        val individuallyPackaged = classDefs.map(cd => q"""
          package $packageName7 {
          package $packageName6 {
          package $packageName5 {
          package $packageName4 {
          package $packageName3 {
          package $packageName2 {
          package $packageName1 { $cd }}}}}}}""")
        individuallyPackaged.reverse.map(tree => parse(tree, asDependencies(classDefs))).flatten
      }

      // eight package
      case compilationUnit @ q"""
        package $packageName8 {
        package $packageName7 {
        package $packageName6 {
        package $packageName5 {
        package $packageName4 {
        package $packageName3 {
        package $packageName2 {
        package $packageName1 { ..$classDefs }}}}}}}}"""  => {
        val individuallyPackaged = classDefs.map(cd => q"""
          package $packageName8 {
          package $packageName7 {
          package $packageName6 {
          package $packageName5 {
          package $packageName4 {
          package $packageName3 {
          package $packageName2 {
          package $packageName1 { $cd }}}}}}}}""")
        individuallyPackaged.reverse.map(tree => parse(tree, asDependencies(classDefs))).flatten
      }

      case x => sys.error("Unsupported class definition format. 1) Imports not supported yet or 2) Package declerations cannot be more than eight levels deep.")

    }
    



  } 
}