sbtavrohugger.SbtAvrohugger.avroSettings

organization := "com.julianpeeters"

name := "datatype-avro-serializaton-tests"

version := "0.4-SNAPSHOT"

scalaVersion := "2.11.8"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Ywarn-value-discard")

libraryDependencies += "com.sksamuel.avro4s" %% "avro4s-core" % "1.4.3"

libraryDependencies += "org.apache.avro" % "avro" % "1.7.7"

libraryDependencies += "org.specs2" %% "specs2" % "2.4" % "test"
