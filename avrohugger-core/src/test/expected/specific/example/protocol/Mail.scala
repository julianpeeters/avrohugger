/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.protocol

@SuppressWarnings(Array("all"))
@org.apache.avro.specific.AvroGenerated
trait Mail {
  def send(message: example.proto.Message): java.lang.CharSequence
  @SuppressWarnings(Array("all"))
  trait Callback extends Mail {
    final val PROTOCOL: org.apache.avro.Protocol = example.proto.Mail.PROTOCOL
    /** @throws java.io.IOException The async call could not be completed. */
    def send(message: example.proto.Message, callback: org.apache.avro.ipc.Callback[java.lang.CharSequence]): Unit
  }
}

object Mail {
  final val PROTOCOL: org.apache.avro.Protocol = org.apache.avro.Protocol.parse("{\"protocol\":\"Mail\",\"namespace\":\"example.proto\",\"types\":[{\"type\":\"record\",\"name\":\"Message\",\"fields\":[{\"name\":\"to\",\"type\":\"string\"},{\"name\":\"from\",\"type\":\"string\"},{\"name\":\"body\",\"type\":\"string\"}]}],\"messages\":{\"send\":{\"request\":[{\"name\":\"message\",\"type\":\"Message\"}],\"response\":\"string\"}}}")
}