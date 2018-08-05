package avrohugger
package matchers
package custom

object CustomNamespaceMatcher {
  
  // Custom namespaces work for simple types, but seem to fail for records 
  // within unions, see http://apache-avro.679487.n3.nabble.com/Deserialize-with-different-schema-td4032782.html
  def checkCustomNamespace(
    maybeSchemaNamespace: Option[String],
    typeMatcher: TypeMatcher,
    maybeDefaultNamespace: Option[String]) = {
      
    def queryNamespaceMap(schemaNamespace: String): Option[String] = {
      val maybeCustomNamespace: Option[String] =
        maybeSchemaNamespace.flatMap(schemaNamespace =>
          typeMatcher.customNamespaces.get(schemaNamespace))
      maybeCustomNamespace match {
        case Some(customNamespace) => Some(customNamespace)
        case None => Some(schemaNamespace)
      }
    }
    
    maybeDefaultNamespace match {
      case Some(ns) => queryNamespaceMap(ns)
      case None => None
    }
    
  }

}