import sbtavrohugger.AvrohuggerSettings.avroScalaCustomNamespace

sbtavrohugger.SbtAvrohugger.specificAvroSettings

(scalaSource in avroConfig) := new java.io.File("myoutputdir")

(avroScalaCustomNamespace in avroConfig) := Map("example"->"overridden")

organization := "com.julianpeeters"

name := "override-settings"

version := "0.1-SNAPSHOT"

