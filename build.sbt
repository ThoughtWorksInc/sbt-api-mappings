sbtPlugin := true

scalaVersion := "2.12.3"

sbtVersion in pluginCrossBuild := "1.0.0-RC3"

name := "sbt-api-mappings"

organization := "com.thoughtworks.sbt-api-mappings"

description := "A Sbt plugin that fills apiMappings for common Scala libraries."

homepage := Some(url(raw"""https://github.com/ThoughtWorksInc/${name.value}"""))

startYear := Some(2015)

pomExtra :=
  <developers>
    <developer>
      <id>Atry</id>
      <name>杨博</name>
      <timezone>+8</timezone>
      <email>pop.atry@gmail.com</email>
    </developer>
  </developers>

scalacOptions += "-deprecation"

libraryDependencies += "com.thoughtworks.extractor" %% "extractor" % "2.1.0"

scriptedSettings

scriptedBufferLog := false

test := scripted.toTask("").value
