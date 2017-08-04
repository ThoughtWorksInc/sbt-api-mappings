def isDownloadableApiDocumentation(url: URL) = {
  import java.net.HttpURLConnection
  val connection = url.openConnection().asInstanceOf[HttpURLConnection]
  try {
    connection.getResponseCode == 200
  } finally {
    connection.disconnect()
  }
}

def assertDownloadableApiDocumentation(url: URL) = {
  assert(isDownloadableApiDocumentation(url), s"Cannot open $url!")
}

val check = TaskKey[Unit]("check")

val scalacheckModuleId = "org.scalacheck" %% "scalacheck" % "1.13.4"
check := {
  assertDownloadableApiDocumentation((apiMappings in Compile in doc).value(scalaInstance.value.libraryJar))
  assertDownloadableApiDocumentation((apiMappings in Test in doc).value(scalaInstance.value.libraryJar))

  val scalacheckJarName = Artifact.artifactName(ScalaVersion(scalaVersion.value, scalaBinaryVersion.value),
                                                scalacheckModuleId,
                                                Artifact("scalacheck"))
  val Some((_, url)) = (apiMappings in Test in doc).value.find(_._1.getName == scalacheckJarName)
  assertDownloadableApiDocumentation(url)
  assert(!(apiMappings in Compile in doc).value.exists(_._1.getName == scalacheckJarName))
}

scalaVersion in Global := "2.12.3"

crossScalaVersions := Seq("2.12.3")

libraryDependencies += scalacheckModuleId % Test

sources in Test += baseDirectory.value / "test_renamed" / "Test.scala"
