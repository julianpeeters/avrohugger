package avrohugger
package format
package scavro

import matchers.{ CustomNamespaceMatcher, TypeMatcher }

import org.apache.avro.{ Schema, Protocol }

object ScavroNamespaceRenamer {
  // By default, Scavro generates Scala classes in packages that are the same
  // as the Java package with `model` appended.
  // TypeMatcher is here because it holds the custom namespace map
  def renameNamespace(
    maybeNamespace: Option[String],
    schemaOrProtocol: Either[Schema, Protocol],
    typeMatcher: TypeMatcher): Option[String] = {

    val scavroModelDefaultPackage: String =
      typeMatcher.customNamespaces
        .get("SCAVRO_DEFAULT_PACKAGE$")
        .getOrElse("model")
        
    val someScavroModelDefaultNamespace = maybeNamespace match {
      case Some(ns) => Some(ns + "." + scavroModelDefaultPackage)
      case None => sys.error("Scavro requires a namespace because Java " +
        "classes cannot be imported from the default package")
    }
    val scavroModelNamespace = {
      val ns = schemaOrProtocol match {
        case Left(schema) => Option(schema.getNamespace)
        case Right(protocol) => Option(protocol.getNamespace)
      }
      ns match {
        case Some(schemaNS) => {
          CustomNamespaceMatcher.checkCustomNamespace(
            ns,
            typeMatcher,
            maybeDefaultNamespace = someScavroModelDefaultNamespace)
        }
        case None => someScavroModelDefaultNamespace
      }
    }

    scavroModelNamespace
  }

}