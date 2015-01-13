name := "avrohugger"

version := "0.0.1"

organization := "com.julianpeeters"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq( 
  "org.specs2" %% "specs2" % "2.4" % "test",
  "org.apache.avro" % "avro" % "1.7.7",
  "com.eed3si9n" %% "treehugger" % "0.3.0"
)

