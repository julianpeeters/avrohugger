package test

import org.specs2.mutable.Specification

import java.io.IOException
import java.net.InetSocketAddress
import java.lang.reflect.Proxy

import org.apache.avro.specific.SpecificData
import org.apache.avro.ipc.NettyServer
import org.apache.avro.ipc.NettyTransceiver
import org.apache.avro.ipc.Server
import org.apache.avro.ipc.specific.SpecificRequestor
import org.apache.avro.ipc.specific.SpecificResponder

import example.proto.Mail
import example.proto.Message

class SpecificRPCTest extends Specification {
  skipAll // RPC tests fail on Linux (Ubuntu 16.04), solution unknown
  // adapted from https://github.com/phunt/avro-rpc-quickstart
  "A case class " should {
    "serialize and deserialize correctly via rpc" in {
      
      class MailImpl extends Mail {
        // in this simple example just return details of the message
        def send(message: Message): String = {
          System.out.println("Sending message")
          val response: String = message.body
          response.toString
        }
      }
      
      System.out.println("Starting server")
      // usually this would be another app, but for simplicity
      val protocol = Mail.PROTOCOL
      val responder = new SpecificResponder(protocol, new MailImpl())
      val server = new NettyServer(responder, new InetSocketAddress(65111))
      System.out.println("Server started")

      val client = new NettyTransceiver(new InetSocketAddress(65111))

      // client code - attach to the server and send a message
      val requestor = new SpecificRequestor(protocol, client, SpecificData.get)
      val mailProxy: Mail = Proxy.newProxyInstance(
        SpecificData.get.getClassLoader,
        Array(classOf[Mail]),
        requestor).asInstanceOf[Mail]

      val message = new Message("avro_user", "pat", "hello_world")

      System.out.println("Calling proxy.send with message:  " + message.toString)
      System.out.println("Result: " + mailProxy.send(message).toString)
      
      val received: String = mailProxy.send(message).toString
      

      // cleanup
      client.close
      server.close
      System.out.println("Server stopped")
      
      received === message.body
      
    }
  }

}
