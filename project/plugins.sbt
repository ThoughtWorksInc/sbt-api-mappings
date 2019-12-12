libraryDependencies += "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value

addSbtPlugin("com.thoughtworks.sbt-best-practice" % "sbt-best-practice" % "7.1.1")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.8")

addSbtPlugin("com.dwijnand" % "sbt-dynver" % "4.0.0")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "2.0.0")

addSbtPlugin("org.lyranthe.sbt" % "partial-unification" % "1.1.2")

addSbtPlugin("com.thoughtworks.example" % "sbt-example" % "6.0.1")
