import sbt._
import Keys._
import sbtassembly.AssemblyPlugin.autoImport._

object BuildSettings {
  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "com.julianpeeters",
    version := "0.0.1",
    scalacOptions ++= Seq(),
    scalaVersion := "2.11.5",
    libraryDependencies += "org.apache.avro" % "avro" % "1.7.7",
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
      libraryDependencies += "com.eed3si9n" %% "treehugger" % "0.3.0"))

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
      assemblyJarName in assembly := "avrohugger-tools-0.0.1.jar",
      libraryDependencies += "org.apache.avro" % "avro-tools" % "1.7.7"
      // Add your additional libraries here (comma-separated)...)
     )
    .dependsOn(core)
     
}


