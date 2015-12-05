package avrohugger
package input
package parsers

import reflectivecompilation.schemagen._

import collection.JavaConversions._

import scala.reflect._
import scala.reflect.api._
import scala.reflect.runtime.universe._
import scala.reflect.runtime.universe.Flag._
import scala.tools.reflect.ToolBox

import org.apache.avro.Schema

object TreeInputParser {

  def parse(tree: Tree, maybeScalaDocs: List[Option[String]]): List[Schema] = {

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

    def enforceSingleScalaDoc(className: String, maybeScalaDoc: List[Option[String]]) = {
      maybeScalaDoc match {
        case List(doc) => doc
        case x => sys.error("Expected single ScalaDoc, found " + x)
       }
    }

    def generateSchema(namespace: Option[Name], classDefTree: Tree, maybeScalaDocs: List[Option[String]]): List[Schema] = {
      classDefTree match {
        // case classes get mapped to records
        case t @ q"$mods class $className[..$tparams](..$fields)(...$rest) extends ..$parents { $self => ..$body }" => {
          val maybeScalaDoc = enforceSingleScalaDoc(className.toString, maybeScalaDocs)
          List(RecordSchemaGenerator.generateSchema(className.toString, namespace, fields, maybeScalaDoc)) 
        }
        // objects that extend Enumeration get mapped to enums
        case t @ q"$mods object $objectName extends ..$parents { $self => ..$body }"  => {
          val values = body.collect{ case ValDef(mods, NameTag(name), tpt, tpe) => name }
          val maybeScalaDoc = enforceSingleScalaDoc(objectName.toString, maybeScalaDocs)
println(values)
println(maybeScalaDoc)
          List(EnumSchemaGenerator.generateSchema(objectName.toString, namespace, values, maybeScalaDoc)) 
        }
        case definition => sys.error(s"""Unsupported definition. Expected case class or object definition, 
          but found $definition with tree: ${showRaw(definition)}""")
      }
    }

    def resubmit(classDefs: List[Tree], individuallyPackaged: List[Tree], maybeScalaDocs: List[Option[String]]) = {
      storeAsDependencies(classDefs)
      val individuallyPackagedZippedWithmaybeScalaDocs = individuallyPackaged.zip(maybeScalaDocs)
      individuallyPackagedZippedWithmaybeScalaDocs.reverse.map(treeWithDocs => {
        val tree = treeWithDocs._1
        val scalaDoc = List(treeWithDocs._2)
        parse(tree, scalaDoc)
      }).flatten
    }
    
    // Reflective compilation can't seem to handle packages, so field types are typechecked
    // against manually provided classes as dependencies. (Object defs alone won't suffice!)
    def storeAsDependencies(topLevelDefs: List[Tree]) = {
      topLevelDefs.reverse.foreach(topLevelDef => {
        topLevelDef match {
          case t @ q"$mods class $className[..$tparams](..$fields)(...$rest) extends ..$parents { $self => ..$body }" => {
            TypecheckDependencyStore.accept(q"case class $className()")
          }
          case t @ q"$mods object $objectName extends ..$parents { $self => ..$body }"  => {
            TypecheckDependencyStore.accept(t)
            TypecheckDependencyStore.accept(q" case class ${objectName.toTypeName}();")
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
        storeAsDependencies(List(classDef))
        generateSchema(namespace, classDef, maybeScalaDocs)
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
        storeAsDependencies(List(classDef))
        generateSchema(namespace, classDef, maybeScalaDocs)
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
        storeAsDependencies(List(classDef))
        generateSchema(namespace, classDef, maybeScalaDocs)
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
        storeAsDependencies(List(classDef))
        generateSchema(namespace, classDef, maybeScalaDocs)
      }
      // four packages
      case compilationUnit @ q"""package $packageName4 {
        package $packageName3 {
        package $packageName2 {
        package $packageName1 {
        $classDef}}}}"""  => {
        val packageTrees = Seq(packageName4, packageName3, packageName2, packageName1)
        val namespace = Some(squashNamespace(packageTrees))  
        storeAsDependencies(List(classDef))      
        generateSchema(namespace, classDef, maybeScalaDocs)
      }
      // three packages
      case compilationUnit @ q"""package $packageName3 {
        package $packageName2 {
        package $packageName1 {
        $classDef}}}"""  => {
        val packageTrees = Seq(packageName3, packageName2, packageName1)
        val namespace = Some(squashNamespace(packageTrees)) 
        storeAsDependencies(List(classDef))       
        generateSchema(namespace, classDef, maybeScalaDocs)
      }
      // two packages
      case compilationUnit @ q"""package $packageName2 {
        package $packageName1 {
        $classDef}}"""  => {

        val packageTrees = Seq(packageName2, packageName1)
        val namespace = Some(squashNamespace(packageTrees))
        storeAsDependencies(List(classDef))
        generateSchema(namespace, classDef, maybeScalaDocs)
      }
      // single package
      case compilationUnit @ q"""package $packageName { $classDef }"""  => {
        val packageTrees = Seq(packageName)
        val namespace = Some(squashNamespace(packageTrees))
        storeAsDependencies(List(classDef))
        generateSchema(namespace, classDef, maybeScalaDocs)
      }
      // default package
      case classDef @ q"$mods class $name[..$tparams](..$fields)(...$rest) extends ..$parents { $self => ..$body }"  => {
        val namespace = None
        storeAsDependencies(List(classDef))
        generateSchema(namespace, classDef, maybeScalaDocs)
      }



      // match list of classes in a package, wrap them each in the package and resubmit
      // to be matched as a single class def in 0 or more packages.

      //TODO: So much hard-coding of the number of package levels. A better way?
      // multiple classes in the same package get reparsed as indidually packaged classDefs
      
      // Reverse the list of individually packaged compilation units so most nested classes
      // are expanded first. 

      // default package
      case compilationUnit @ q"""..$classDefs"""  => {
        val individuallyPackaged = classDefs.map(cd => q"""$cd""")
        resubmit(classDefs, individuallyPackaged, maybeScalaDocs)
      }
      // one package
      case compilationUnit @ q"""
        package $packageName { ..$classDefs }"""  => {
        val individuallyPackaged = classDefs.map(cd => q"""
          package $packageName { $cd }""")
        resubmit(classDefs, individuallyPackaged, maybeScalaDocs)
      }
      // two package
      case compilationUnit @ q"""
        package $packageName2 {
        package $packageName1 { ..$classDefs }}"""  => {
        val individuallyPackaged = classDefs.map(cd => q"""
          package $packageName2 {
          package $packageName1 { $cd }}""")
        resubmit(classDefs, individuallyPackaged, maybeScalaDocs)
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
        resubmit(classDefs, individuallyPackaged, maybeScalaDocs)
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
        resubmit(classDefs, individuallyPackaged, maybeScalaDocs)
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
        resubmit(classDefs, individuallyPackaged, maybeScalaDocs)
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
        resubmit(classDefs, individuallyPackaged, maybeScalaDocs)
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
        resubmit(classDefs, individuallyPackaged, maybeScalaDocs)
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
        resubmit(classDefs, individuallyPackaged, maybeScalaDocs)
      }
      case EmptyTree=> List()
      case x => sys.error(s"Unsupported class definition format: $x. 1) Imports not supported yet or 2) Package declarations cannot be more than eight levels deep.")

    }
    

  } 
}
