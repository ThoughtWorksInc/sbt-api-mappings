libraryDependencies += "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value

addSbtPlugin(
  "com.thoughtworks.sbt-best-practice" % "sbt-best-practice" % "8.2.1"
)

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.9.12")

addSbtPlugin("com.dwijnand" % "sbt-dynver" % "4.1.1")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "2.1.1")

addSbtPlugin("org.lyranthe.sbt" % "partial-unification" % "1.1.2")

addSbtPlugin("com.thoughtworks.example" % "sbt-example" % "9.2.0")
