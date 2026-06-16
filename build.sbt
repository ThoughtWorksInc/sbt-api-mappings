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

// `.extract` pattern support, reused via `Compat.Extractor`. Only published for
// Scala 2.12; on Scala 3 a PartialFunction is already a valid pattern, so the
// Scala 3 `Compat.Extractor` provides an equivalent extension instead.
libraryDependencies ++= {
  if (scalaBinaryVersion.value == "2.12")
    Seq("com.thoughtworks.extractor" %% "extractor" % "2.1.3")
  else
    Seq.empty
}

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.20" % "test"

scriptedBufferLog := false

scriptedLaunchOpts += s"-Dplugin.version=${version.value}"

(Test / test) := {
  (Test / test).value
  // The all-libraries and jdk scripted fixtures use very old Scala versions
  // (whose sbt 2.x compiler bridges don't build) and assert sbt 1.x-specific
  // behaviour (scaladoc output path, autoAPIMappings URLs). Until those fixtures
  // are modernised, run only keep-api-url under sbt 2.x; sbt 1.x runs them all.
  Def.taskDyn {
    if ((pluginCrossBuild / sbtVersion).value.startsWith("2."))
      scripted.toTask(" sbt-api-mappings/keep-api-url")
    else
      scripted.toTask("")
  }.value
}
