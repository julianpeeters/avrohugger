import sbt._
import Keys._
import ScriptedPlugin._
import sbtassembly.AssemblyPlugin.autoImport._

object BuildSettings {
  private val avroVersion = "1.7.7"

  val buildSettings = Defaults.defaultSettings ++ scriptedSettings ++ Seq(
    organization := "com.julianpeeters",
    version := "0.12.2-SNAPSHOT",
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Ywarn-value-discard"),
    scalaVersion := "2.11.8",
    crossScalaVersions := Seq("2.10.6", scalaVersion.value),
    resolvers += Resolver.typesafeIvyRepo("releases"),
    libraryDependencies += "org.apache.avro" % "avro" % avroVersion,
    libraryDependencies += "org.apache.avro" % "avro-compiler" % avroVersion,
    // for implementing SpecificRecord from standard case class definitions
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
    libraryDependencies := {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, scalaMajor)) if scalaMajor == 10 =>
          libraryDependencies.value ++ Seq (
            "org.scalamacros" %% "quasiquotes" % "2.0.0" cross CrossVersion.binary
          )
        case Some((2, 11)) =>
          libraryDependencies.value ++ Seq()
      }
    },
    libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value,
    libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    libraryDependencies += "org.codehaus.jackson" % "jackson-core-asl" % "1.9.13",
    // for testing
    libraryDependencies += "org.specs2" %% "specs2" % "2.4" % "test",
    publishMavenStyle := true,
    publishArtifact in Test := false,
    publishTo <<= version { (v: String) =>
      val nexus = "https://oss.sonatype.org/"
      if (v.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
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

}

object MyBuild extends Build {
  import BuildSettings._

  // Configure prompt to show current project
  override lazy val settings = super.settings :+ {
    shellPrompt := { s => Project.extract(s).currentProject.id + " > " }
  }

  lazy val avrohugger: Project = Project(
    "avrohugger",
    file("."),
    settings = buildSettings
  ) aggregate(core, tools)

  lazy val core: Project = Project(
    "avrohugger-core",
    file("avrohugger-core"),
    settings = buildSettings ++ Seq(
      libraryDependencies += "com.eed3si9n" %% "treehugger" % "0.4.1")
  )

  lazy val tools: Project = Project(
    "avrohugger-tools",
    file("avrohugger-tools"),
    settings = buildSettings)
    .settings(artifact in (Compile, assembly) := {
      val art = (artifact in (Compile, assembly)).value
      art.copy(`classifier` = Some("assembly"))}
     )
    .settings(addArtifact(artifact in (Compile, assembly), assembly).settings: _*)
    .settings(
      libraryDependencies += "org.apache.avro" % "avro-tools" % "1.7.7"
     )
    .dependsOn(core)

}
