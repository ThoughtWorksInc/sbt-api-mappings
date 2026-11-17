enablePlugins(SbtPlugin)

sbtPlugin := true

name := "sbt-api-mappings"

organization := "com.thoughtworks.sbt-api-mappings"

description := "A Sbt plugin that fills apiMappings for common Scala libraries."

homepage := Some(url(raw"""https://github.com/ThoughtWorksInc/${name.value}"""))

startYear := Some(2015)

scalacOptions += "-deprecation"

// Keep the plugin compiler baselines explicit rather than deriving them from CI.
scalaVersion := "2.12.21"
crossScalaVersions := Seq("2.12.21", "3.9.0")
scalacOptions ++= {
  if (scalaBinaryVersion.value == "2.12") Seq("-Xsource:3", "-release:8") else Nil
}
pluginCrossBuild / sbtVersion := {
  scalaBinaryVersion.value match {
    case "2.12" => "1.13.1"
    case _      => "2.1.0"
  }
}

// Compatibility layer that exposes a subset of the sbt 2 API using sbt 1 API,
// so most of the plugin sources can be shared across both sbt versions.
addSbtPlugin("com.github.sbt" % "sbt2-compat" % "0.2.0")

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
  scripted.toTask("").value
}
