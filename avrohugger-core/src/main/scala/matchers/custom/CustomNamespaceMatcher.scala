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
        maybeSchemaNamespace.flatMap(schemaNamespace => {
          typeMatcher.customNamespaces.get(schemaNamespace) match {
            case Some(matchingNs) => Some(matchingNs)
            case None =>
              typeMatcher.customNamespaces.filter(ns => ns._1.contains('*')).filter(wildcardedNs =>
                schemaNamespace.contains(wildcardedNs._1.substring(0, wildcardedNs._1.indexOf('*') - 1))) match {
                case singleNs if singleNs.size == 1 =>
                  Some(singleNs.head._2)
                case multipleNs if multipleNs.size > 1 =>
                  throw new java.lang.RuntimeException("Multiple conflicting custom namespaces supplied")
                case _ => None
              }
          }
        })
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