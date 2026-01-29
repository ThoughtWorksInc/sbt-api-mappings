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
    (apiMappings in Compile in doc)
      .value(scalaInstance.value.libraryJar)
      .toString == expectedScaladocUrl
  )
  assert(
    (apiMappings in Test in doc)
      .value(scalaInstance.value.libraryJar)
      .toString == expectedScaladocUrl
  )

  val scalacheckJarName = Artifact.artifactName(
    ScalaVersion(scalaVersion.value, scalaBinaryVersion.value),
    scalacheckModuleId,
    Artifact("scalacheck")
  )
  val Some((_, url)) =
    (apiMappings in Test in doc).value.find(_._1.getName == scalacheckJarName)

  val expectedUrl =
    "https://javadoc.io/page/org.scalacheck/scalacheck_2.13/1.14.3/"
  assert(url.toString == expectedUrl)
  assert(
    !(apiMappings in Compile in doc).value
      .exists(_._1.getName == scalacheckJarName)
  )
}

scalaVersion in Global := "2.13.1"

crossScalaVersions := Seq("2.13.1")

libraryDependencies += scalacheckModuleId % Test

sources in Test += baseDirectory.value / "test_renamed" / "Test.scala"
