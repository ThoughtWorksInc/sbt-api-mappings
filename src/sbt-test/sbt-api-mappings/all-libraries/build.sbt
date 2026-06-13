def assertDownloadableApiDocumentation(url: URL) = {
  java.lang.System.setProperty("http.agent", "Chrome");
  assert(IO.readLinesURL(url).nonEmpty)
}

val check = TaskKey[Unit]("check")

val scalacheckModuleId = "org.scalacheck" %% "scalacheck" % "1.14.3"
check := {
  // The expected URL is browsable but not accessible from JRE 8's java.net.HttpURLConnection
  val expectedScaladocUrl =
    "https://www.scala-lang.org/api/2.13.1/"

  assert(
    (Compile / doc / apiMappings)
      .value(scalaInstance.value.libraryJar)
      .toString == expectedScaladocUrl
  )
  assert(
    (Test / doc / apiMappings)
      .value(scalaInstance.value.libraryJar)
      .toString == expectedScaladocUrl
  )

  val scalacheckJarName = Artifact.artifactName(
    ScalaVersion(scalaVersion.value, scalaBinaryVersion.value),
    scalacheckModuleId,
    Artifact("scalacheck")
  )
  val Some((_, url)) =
    (Test / doc / apiMappings).value.find(_._1.getName == scalacheckJarName)

  val expectedUrl =
    "https://javadoc.io/page/org.scalacheck/scalacheck_2.13/1.14.3/"
  assert(url.toString == expectedUrl)
  assert(
    !(Compile / doc / apiMappings).value
      .exists(_._1.getName == scalacheckJarName)
  )
}

Global / scalaVersion := "2.13.1"

crossScalaVersions := Seq("2.13.1")

libraryDependencies += scalacheckModuleId % Test

Test / sources += baseDirectory.value / "test_renamed" / "Test.scala"
