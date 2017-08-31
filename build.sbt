sbtPlugin := true

scalaVersion := "2.12.3"

crossSbtVersions := Seq("0.13.16", "1.0.1")

name := "sbt-api-mappings"

organization := "com.thoughtworks.sbt-api-mappings"

description := "A Sbt plugin that fills apiMappings for common Scala libraries."

homepage := Some(url(raw"""https://github.com/ThoughtWorksInc/${name.value}"""))

startYear := Some(2015)

scalacOptions += "-deprecation"

libraryDependencies += "com.thoughtworks.extractor" %% "extractor" % "2.1.0"

scriptedBufferLog := false

test := scripted.toTask("").value
