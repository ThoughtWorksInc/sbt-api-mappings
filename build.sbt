sbtPlugin := true

name := "sbt-api-mappings"

organization := "com.thoughtworks.sbt-api-mappings"

releasePublishArtifactsAction := PgpKeys.publishSigned.value

import ReleaseTransformations._

description := "A Sbt plugin that fills apiMappings for common Scala libraries."

homepage := Some(url(raw"""https://github.com/ThoughtWorksInc/${name.value}"""))

startYear := Some(2015)

licenses := Seq("Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.html"))

scmInfo := Some(
  ScmInfo(url(raw"""https://github.com/ThoughtWorksInc/${name.value}"""),
          raw"""scm:git:https://github.com/ThoughtWorksInc/${name.value}.git""",
          Some(raw"""scm:git:git@github.com:ThoughtWorksInc/${name.value}.git""")))

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

releaseProcess := {
  releaseProcess.value
    .patch(releaseProcess.value.indexOf(pushChanges), Seq[ReleaseStep](releaseStepCommand("sonatypeRelease")), 0)
}

releaseProcess -= runClean

releaseProcess -= runTest

libraryDependencies += "com.thoughtworks.extractor" %% "extractor" % "1.0.4"

scriptedSettings

scriptedLaunchOpts += s"-Dplugin.version=${version.value}"

scriptedBufferLog := false

test := scripted.toTask("").value