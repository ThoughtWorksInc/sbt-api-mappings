def assertDownloadableApiDocumentation(url: URL) = {
  import java.net.HttpURLConnection
  val connection = url.openConnection().asInstanceOf[HttpURLConnection]
  try {
    assert(
      (200 to 399).contains(connection.getResponseCode),
      s"Unexpected HTTP response code ${connection.getResponseCode} when fetching $url"
    )
  } finally {
    connection.disconnect()
  }
}

val check = TaskKey[Unit]("check")

val scalacheckModuleId = "org.scalacheck" %% "scalacheck" % "1.14.3"
check := {
  assertDownloadableApiDocumentation(
    (apiMappings in Compile in doc).value(scalaInstance.value.libraryJar)
  )
  assertDownloadableApiDocumentation(
    (apiMappings in Test in doc).value(scalaInstance.value.libraryJar)
  )

  val scalacheckJarName = Artifact.artifactName(
    ScalaVersion(scalaVersion.value, scalaBinaryVersion.value),
    scalacheckModuleId,
    Artifact("scalacheck")
  )
  val Some((_, url)) =
    (apiMappings in Test in doc).value.find(_._1.getName == scalacheckJarName)

  // The expected URL is browsable but not accessible from java.net.HttpURLConnection
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
