libraryDependencies <+= (sbtVersion  in sbtPlugin) (v=>
  "org.scala-sbt" % "scripted-plugin" % v
)
