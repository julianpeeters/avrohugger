addSbtPlugin("com.julianpeeters" % "sbt-avrohugger" % "latest.integration")

resolvers += Resolver.file("Local Ivy Repository", file("~/.ivy2/local/"))(Resolver.ivyStylePatterns)
