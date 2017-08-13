package avrohugger
package test

import avrohugger.input.DependencyInspector.getReferredNamespace
import org.apache.avro.Schema
import org.specs2._

class NamespaceExtractorSpec extends Specification {

  private val schemaDef = 
    """
     | {
     |  "namespace": "owner.namespace",
     |  "type": "record",
     |  "name": "ExternalRecord",
     |  "fields": [
     |    {"name": "stringField", "type": "string"},
     |    {"name": "intField", "type": "int"},
     |    {"name": "recordNoNamespaceField", "type":
     |      {"type": "record",
     |       "name": "AddressRecord1",
     |       "fields": [ {"name": "streetaddress", "type": "string"} ]
     |      }
     |    },
     |    {"name": "recordWithNamespaceField", "type":
     |      {"type": "record",
     |       "name": "AddressRecord2",
     |       "namespace": "other.namespace",
     |       "fields": [ {"name": "streetaddress", "type": "string"} ]
     |      }
     |    },
     |    {"name": "unionOfrecordWithNamespaceField", "type":
     |      [
     |       "null",
     |       { "type": "record",
     |        "name": "AddressRecord3",
     |        "namespace": "yet.another.namespace",
     |        "fields": [ {"name": "streetaddress", "type": "string"} ]
     |       }
     |     ]
     |    },
     |    {"name": "arrayOfrecordWithNamespaceField", "type": { "type": "array", "items":
     |       { "type": "record",
     |        "name": "AddressRecord4",
     |        "namespace": "and.yet.another.namespace",
     |        "fields": [ {"name": "streetaddress", "type": "string"} ]
     |       }
     |     }
     |    }
     |  ]
     |}
   """.stripMargin

  lazy val ownerSchema = (new Schema.Parser()).parse(schemaDef)

  def is = s2"""
    A NamespaceExtractor should
      Return no namespace for basic types $e1

    "Return the namespace of the non null inner type for union types" $e2

    "Return the namespace for record types" $e3

    "Return the namespace for array types" in $e4

  """
  
  def e1 = {
    getReferredNamespace( ownerSchema.getField("stringField").schema ) === None
  }
  
  def e2 = {
    getReferredNamespace( ownerSchema.getField("unionOfrecordWithNamespaceField").schema ) === Some("yet.another.namespace")
  }
  
  def e3 = {
    getReferredNamespace( ownerSchema.getField("recordWithNamespaceField").schema ) === Some("other.namespace")
  }
  
  def e4 = {
    getReferredNamespace( ownerSchema.getField("arrayOfrecordWithNamespaceField").schema ) === Some("and.yet.another.namespace")
  }

}
