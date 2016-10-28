addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.0")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "0.5.0")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.1")

libraryDependencies += "org.scala-sbt" % "scripted-plugin" % sbtVersion.value
