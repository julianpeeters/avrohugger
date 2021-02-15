lazy val avroVersion = "1.9.1"

lazy val commonSettings = Seq(
  organization := "com.julianpeeters",
  version := "1.0.0-RC24",
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Ywarn-value-discard"),
  scalacOptions in Test ++= Seq("-Yrangepos"),
  scalaVersion := "2.13.4",
  crossScalaVersions := Seq("2.12.10", scalaVersion.value),
  resolvers += Resolver.typesafeIvyRepo("releases"),
  libraryDependencies += "org.apache.avro" % "avro" % avroVersion,
  libraryDependencies += "org.apache.avro" % "avro-compiler" % avroVersion,
  libraryDependencies := { CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, scalaMajor)) if scalaMajor < 13 =>
      // for implementing SpecificRecord from standard case class definitions
      libraryDependencies.value ++ Seq(compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full))

    case _ =>
      // Scala 2.13 has it built-in
      libraryDependencies.value
  }},
  libraryDependencies := { CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, scalaMajor)) if scalaMajor < 13 =>
      libraryDependencies.value ++ Seq("org.scala-lang.modules" %% "scala-collection-compat" % "2.4.1")
    case _ =>
      libraryDependencies.value
  }},
  libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value,
  libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  libraryDependencies += "org.codehaus.jackson" % "jackson-core-asl" % "1.9.13",
  // for testing
  libraryDependencies += "org.specs2" %% "specs2-core" % "4.8.0" % "test",
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
  ).aggregate(`avrohugger-core`, `avrohugger-filesorter`, `avrohugger-tools`)


lazy val `avrohugger-core` = (project in file("avrohugger-core"))
  .settings(
    commonSettings,
    libraryDependencies += "com.eed3si9n" %% "treehugger" % "0.4.4"
  )

lazy val `avrohugger-filesorter` = (project in file("avrohugger-filesorter"))
  .settings(
    commonSettings,
    libraryDependencies += "io.spray" %% "spray-json" % "1.3.5"
  )

lazy val `avrohugger-tools` = (project in file("avrohugger-tools"))
  .settings(
    commonSettings,
    libraryDependencies += "org.apache.avro" % "avro-tools" % avroVersion,
    artifact in (Compile, assembly) := {
      val art: Artifact = (artifact in (Compile, assembly)).value
      art.withClassifier(Some("assembly"))
    },
    addArtifact(artifact in (Compile, assembly), assembly).settings
  ).dependsOn(`avrohugger-core`, `avrohugger-filesorter`)
