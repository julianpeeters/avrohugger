package test

import org.specs2.mutable.Specification

class SpecificEncoderTest extends Specification {

  "A case class " should {
    "serialize and deserialize correctly via encoder and decoder" in {
      val record = AvroTypeProviderTest00(0)
      SpecificTestUtil.verifyEncodeDecode(record)
    }
  }

}
