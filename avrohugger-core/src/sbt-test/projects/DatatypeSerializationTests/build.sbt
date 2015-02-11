seq( sbtavrohugger.SbtAvrohugger.specificAvroSettings : _*)

organization := "com.julianpeeters"

name := "datatype-serializaton-tests"

version := "0.1"

scalaVersion := "2.11.5"

libraryDependencies += "org.apache.avro" % "avro" % "1.7.7"

libraryDependencies += "org.specs2" %% "specs2" % "2.4" % "test"
