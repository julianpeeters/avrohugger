lazy val avroVersion = "1.7.7"

lazy val commonSettings = Seq(
  organization := "com.julianpeeters",
  version := "0.17.0-SNAPSHOT",
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Ywarn-value-discard"),
  scalaVersion := "2.12.4",
  crossScalaVersions := Seq("2.10.6", "2.11.11", scalaVersion.value),
  resolvers += Resolver.typesafeIvyRepo("releases"),
  libraryDependencies += "org.apache.avro" % "avro" % avroVersion,
  libraryDependencies += "org.apache.avro" % "avro-compiler" % avroVersion,
  // for implementing SpecificRecord from standard case class definitions
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
  libraryDependencies := {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, scalaMajor)) if scalaMajor == 10 =>
        libraryDependencies.value ++ Seq (
          "org.scalamacros" %% "quasiquotes" % "2.0.0" cross CrossVersion.binary)
      case _ =>
        libraryDependencies.value ++ Seq()
    }
  },
  libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value,
  libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  libraryDependencies += "org.codehaus.jackson" % "jackson-core-asl" % "1.9.13",
  // for testing
  libraryDependencies += "org.specs2" %% "specs2-core" % "3.8.6" % "test",
  publishMavenStyle := true,
  publishArtifact in Test := false,
  publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
  ),
  pomIncludeRepository := { _ => false },
  licenses := Seq("Apache 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
  homepage := Some(url("https://github.com/julianpeeters/avrohugger")),
  pomExtra := (
    <scm>
      <url>git://github.com/julianpeeters/avrohugger.git</url>
      <connection>scm:git://github.com/julianpeeters/avrohugger.git</connection>
    </scm>
    <developers>
      <developer>
        <id>julianpeeters</id>
        <name>Julian Peeters</name>
        <url>http://github.com/julianpeeters</url>
      </developer>
    </developers>)
)

lazy val avrohugger = (project in file("."))
  .settings(
    commonSettings,
    libraryDependencies += "com.eed3si9n" %% "treehugger" % "0.4.3"
  ).aggregate(`avrohugger-core`, `avrohugger-filesorter`, `avrohugger-tools`)
  

lazy val `avrohugger-core` = (project in file("avrohugger-core"))
  .settings(
    commonSettings,
    libraryDependencies += "com.eed3si9n" %% "treehugger" % "0.4.3"
  )
  
lazy val `avrohugger-filesorter` = (project in file("avrohugger-filesorter"))
  .settings(
    commonSettings,
    libraryDependencies += "io.spray" %% "spray-json" % "1.3.2"
  )
  
lazy val `avrohugger-tools` = (project in file("avrohugger-tools"))
  .settings(
    commonSettings,
    libraryDependencies += "org.apache.avro" % "avro-tools" % "1.7.7",
    artifact in (Compile, assembly) := {
      val art: Artifact = (artifact in (Compile, assembly)).value
      art.withClassifier(Some("assembly"))
    },
    addArtifact(artifact in (Compile, assembly), assembly).settings
  ).dependsOn(`avrohugger-core`)