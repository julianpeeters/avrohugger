sbtavrohugger.SbtAvrohugger.avroSettings

organization := "com.julianpeeters"

name := "datatype-avro-serializaton-tests"

version := "0.2"

// 2.10 because scalavro is used for a single test, and it's stalled at 2.10
scalaVersion := "2.10.5"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Ywarn-value-discard")

libraryDependencies += "com.gensler" %% "scalavro" % "0.6.2"

libraryDependencies += "org.specs2" %% "specs2" % "2.4" % "test"
