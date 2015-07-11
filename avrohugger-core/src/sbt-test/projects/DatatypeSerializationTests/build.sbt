seq( sbtavrohugger.SbtAvrohugger.specificAvroSettings : _*)

organization := "com.julianpeeters"

name := "datatype-serializaton-tests"

version := "0.2"

scalaVersion := "2.11.7"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Ywarn-value-discard")

libraryDependencies += "org.apache.avro" % "avro" % "1.7.7"

libraryDependencies += "org.specs2" %% "specs2" % "2.4" % "test"
