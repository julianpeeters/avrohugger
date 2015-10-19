baseAvroCodegenSettings

organization := "com.julianpeeters"

name := "datatype-scavro-serializaton-tests"

version := "0.3-SNAPSHOT"

scalaVersion := "2.10.5"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Ywarn-value-discard")

// scavro plugin settings
scalaCustomTypes := Map("array"->"List")
avroSchemaFiles :=   ( ((resourceDirectory in Compile).value ** "*.avsc").get.toSet ).toSeq
avroDataFiles :=  ( ((resourceDirectory in Compile).value ** "*.avro").get.toSet ).toSeq

libraryDependencies += Defaults.sbtPluginExtra( "com.oysterbooks" % "scavro" % "0.10.0-SNAPSHOT", "0.13", "2.10" )

libraryDependencies += "org.apache.avro" % "avro" % "1.7.7"

libraryDependencies += "org.specs2" %% "specs2" % "2.4" % "test"
