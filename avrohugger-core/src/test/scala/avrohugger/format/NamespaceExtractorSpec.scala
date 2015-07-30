package avrohugger.format

import avrohugger.format.DependencyInspectionSupport.getFieldReferredNamespace
import org.apache.avro.Schema
import org.specs2.mutable.Specification

class NamespaceExtractorSpec extends Specification {

  private val schemaDef = """
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

  "A NamespaceExtractor" should {

    "Return no namespace for basic types" in {
      getFieldReferredNamespace( ownerSchema.getField("stringField") ) === None
    }

    "Return the namespace of the non null inner type for union types" in {
      getFieldReferredNamespace( ownerSchema.getField("unionOfrecordWithNamespaceField") ) === Some("yet.another.namespace")
    }

    "Return the namespace for record types" in {
      getFieldReferredNamespace( ownerSchema.getField("recordWithNamespaceField") ) === Some("other.namespace")
    }

    "Return the namespace for array types" in {
      getFieldReferredNamespace( ownerSchema.getField("arrayOfrecordWithNamespaceField") ) === Some("and.yet.another.namespace")
    }

  }

}
