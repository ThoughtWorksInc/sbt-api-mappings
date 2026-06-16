enablePlugins(SbtPlugin)

sbtPlugin := true

name := "sbt-api-mappings"

organization := "com.thoughtworks.sbt-api-mappings"

description := "A Sbt plugin that fills apiMappings for common Scala libraries."

homepage := Some(url(raw"""https://github.com/ThoughtWorksInc/${name.value}"""))

startYear := Some(2015)

scalacOptions += "-deprecation"

val scala212 = "2.12.20"

val scala3 = "3.8.4"

// Cross-build for both sbt 1.x (Scala 2.12) and sbt 2.x (Scala 3).
crossScalaVersions := Seq(scala212, scala3)

scalaVersion := scala212

pluginCrossBuild / sbtVersion := {
  scalaBinaryVersion.value match {
    case "2.12" => "1.12.11" // sbt 1.x
    case _      => "2.0.0" // sbt 2.x
  }
}

// Compatibility layer that exposes a subset of the sbt 2 API using sbt 1 API,
// so most of the plugin sources can be shared across both sbt versions.
addSbtPlugin("com.github.sbt" % "sbt2-compat" % "0.1.0")

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.20" % "test"

scriptedBufferLog := false

scriptedLaunchOpts += s"-Dplugin.version=${version.value}"

(Test / test) := {
  (Test / test).value
  scripted.toTask("").value
}
