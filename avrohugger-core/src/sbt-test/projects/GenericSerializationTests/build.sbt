sbtavrohugger.SbtAvrohugger.avroSettings

organization := "com.julianpeeters"

name := "datatype-avro-serializaton-tests"

version := "0.4-SNAPSHOT"

scalaVersion := "2.10.3"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Ywarn-value-discard")

libraryDependencies += "com.gensler" %% "scalavro" % "0.6.2"

libraryDependencies += "org.specs2" %% "specs2" % "2.4" % "test"
