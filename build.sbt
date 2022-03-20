enablePlugins(SbtPlugin)

sbtPlugin := true

name := "sbt-api-mappings"

organization := "com.thoughtworks.sbt-api-mappings"

description := "A Sbt plugin that fills apiMappings for common Scala libraries."

homepage := Some(url(raw"""https://github.com/ThoughtWorksInc/${name.value}"""))

startYear := Some(2015)

scalacOptions += "-deprecation"

libraryDependencies += "com.thoughtworks.extractor" %% "extractor" % "2.1.3"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.4" % "test"

scriptedBufferLog := false

scriptedLaunchOpts += s"-Dplugin.version=${version.value}"

(Test / test) := {
  (Test / test).value
  scripted.toTask("").value
}
