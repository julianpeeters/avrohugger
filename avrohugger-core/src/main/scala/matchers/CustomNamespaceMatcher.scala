package avrohugger
package matchers

object CustomNamespaceMatcher {
  
  // Custom namespaces work for simple types, but seem to fail for records 
  // within unions, see http://apache-avro.679487.n3.nabble.com/Deserialize-with-different-schema-td4032782.html
  def checkCustomNamespace(
    maybeCustomNamespace: Option[String],
    defaultNamespace: Option[String]) = {
    def queryNamespaceMap(schemaNamespace: String): Option[String] = {
      maybeCustomNamespace match {
        case Some(customNamespace) => Some(customNamespace)
        case None => Some(schemaNamespace)
      }
    }
    defaultNamespace match {
      case Some(ns) => queryNamespaceMap(ns)
      case None => None
    }
  }

}